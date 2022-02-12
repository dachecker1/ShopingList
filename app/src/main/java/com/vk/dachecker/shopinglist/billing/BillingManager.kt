package com.vk.dachecker.shopinglist.billing

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*

class BillingManager(val activity: AppCompatActivity) {
    private var bClient: BillingClient? = null

    init {
        setUpBillingClient()
    }

    private fun setUpBillingClient() {
        bClient = BillingClient.newBuilder(activity)
            .setListener(getPurchaseListener())
            .enablePendingPurchases()
            .build()
    }

    private fun savePref(isPurchase: Boolean) {
        val pref = activity.getSharedPreferences(MAIN_PREF, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(REMOVE_ADS_KEY, isPurchase)
        editor.apply()
    }

    fun startConnection() {
        bClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {

            }

            override fun onBillingSetupFinished(p0: BillingResult) {
                getItem()
            }

        })
    }

    private fun getItem() {
        val skuList = ArrayList<String>()
        skuList.add(REMOVE_AD_ITEM)
        val skuDetails =
            SkuDetailsParams.newBuilder()                  //BillingClient.SkuType.SUBS - подписка
        skuDetails.setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP) //указываем к продукту REMOVE_AD_ITEM тип покупки - просто купить или подписка.
        bClient?.querySkuDetailsAsync(skuDetails.build()) { bResult, list ->
            run {
                if (bResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val bFlowParams = BillingFlowParams
                                .newBuilder()
                                .setSkuDetails(list[0])
                                .build()
                            bClient?.launchBillingFlow(activity, bFlowParams)
                        }
                    }
                }
            }
        }
    }

    //не запустится, пока не высветится диалог и пользователь не нажмет "купить"
    private fun getPurchaseListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener { bResult, list ->
            run {
                if (bResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    list?.get(0)?.let { nonConsumableItem(it) }
                }
            }

        }
    }


    //подтверждаем покупку, которую нельзя употребить. То есть удаляем рекламу раз и навсегда
    private fun nonConsumableItem(purchase: Purchase) {
        //проверяем, купил ли он продукт и подтверждаем это
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            //если покупка не одобрена, то одобряем
            if (!purchase.isAcknowledged) {
                val acParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                bClient?.acknowledgePurchase(acParams) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                        savePref(true)
                        Toast.makeText(activity, "Покупка произведена успешно", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        savePref(false)
                        Toast.makeText(activity, "Покупка не удалась", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }

    fun closeConnection() {
        bClient?.endConnection()
    }

    companion object {
        //идентификатор должен совпадать с Play Console
        const val REMOVE_AD_ITEM = "remove_ad_item_id" //внимательно.
        const val MAIN_PREF = "main pref"
        const val REMOVE_ADS_KEY = "remove ads key"
    }
}