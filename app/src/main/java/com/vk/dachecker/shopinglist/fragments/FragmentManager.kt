package com.vk.dachecker.shopinglist.fragments

import androidx.appcompat.app.AppCompatActivity
import com.vk.dachecker.shopinglist.R

object FragmentManager {
    var currentFrag : BaseFragment? = null
    fun setFragment(newFragment: BaseFragment, activity: AppCompatActivity){
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.placeHolder, newFragment)
        transaction.commit()
        currentFrag = newFragment
    }
}