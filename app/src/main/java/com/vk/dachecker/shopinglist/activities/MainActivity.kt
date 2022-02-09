package com.vk.dachecker.shopinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vk.dachecker.shopinglist.R
import com.vk.dachecker.shopinglist.databinding.ActivityMainBinding
import com.vk.dachecker.shopinglist.dialogs.NewListDialog
import com.vk.dachecker.shopinglist.fragments.FragmentManager
import com.vk.dachecker.shopinglist.fragments.NoteFragment
import com.vk.dachecker.shopinglist.fragments.ShopListNamesFragment
import com.vk.dachecker.shopinglist.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener{
    private lateinit var binding : ActivityMainBinding
    private var iAd: InterstitialAd? = null
    private var currentMenuItemId = R.id.shop_list
    private var adShowCounter = 0
    private var adShowCounterMax = 3



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        loadInterAd()
        setBottomNavListener()
    }

    private fun showInterAd(adListener: AdListener){
        if(iAd != null && adShowCounter>adShowCounterMax){
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() { //пользователь закрыл объявление
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()

                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) { //если произошла ошибка, то подгружаем новую рекламу и пускаем его туда, куда он хотел идти.
                    iAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() { //запускается, когда объявление полностью было показано
                    iAd = null
                    loadInterAd()
                }
            }
            adShowCounter = 0
            iAd?.show(this)
        } else { //в блоке else Даем ему зайти туда, куда он хотел, если не загрузилась реклама (нет интернета)
            adShowCounter++
            adListener.onFinish()
        }
    }

    private fun loadInterAd(){
        val request = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id),request,
            object : InterstitialAdLoadCallback(){
                override fun onAdLoaded(ad: InterstitialAd) {
                    iAd = ad
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    iAd = null
                }
        })
    }

    private fun setBottomNavListener(){
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        }
                    })
                }
                R.id.notes -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            currentMenuItemId = R.id.notes
                            FragmentManager.setFragment(NoteFragment.newInstance(), this@MainActivity)
                        }
                    })
                }
                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()

                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
    }

    override fun onClick(name: String) {
        Log.d("MyTag", "Name $name")
    }

    interface AdListener{
        fun onFinish()
    }
}