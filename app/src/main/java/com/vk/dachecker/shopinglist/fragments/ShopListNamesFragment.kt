package com.vk.dachecker.shopinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vk.dachecker.shopinglist.activities.MainApp
import com.vk.dachecker.shopinglist.activities.ShopListActivity
import com.vk.dachecker.shopinglist.databinding.FragmentShopListNamesBinding
import com.vk.dachecker.shopinglist.db.MainViewModel
import com.vk.dachecker.shopinglist.db.ShopListNameAdapter
import com.vk.dachecker.shopinglist.dialogs.DeleteDialog
import com.vk.dachecker.shopinglist.dialogs.NewListDialog
import com.vk.dachecker.shopinglist.entities.ShopListNameItem
import com.vk.dachecker.shopinglist.utils.TimeManager
import kotlinx.coroutines.InternalCoroutinesApi

class ShopListNamesFragment : BaseFragment() , ShopListNameAdapter.Listener{
    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter : ShopListNameAdapter

    //во фрагменте инициализируем mainViewModel следующим образом. Не забыть передать контекст, т.к.
    //мы не в активити, а во фрагменте находимся.
    @InternalCoroutinesApi
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelfactory((context?.applicationContext as MainApp).database)
    }
    @InternalCoroutinesApi
    override fun onClickNew() {
        //запускаем слушатель записи данных
        NewListDialog.showDialog(activity as AppCompatActivity, object  : NewListDialog.Listener{
            override fun onClick(name: String) {
                val shopListName = ShopListNameItem(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }
        }, "")
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)


        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rcView.adapter = adapter
    }

    //метод, который следит за изменениями
    @InternalCoroutinesApi
    private fun observer(){
        mainViewModel.allShopListNamesItem.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }

    @InternalCoroutinesApi
    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
               mainViewModel.deleteShopList(id, true)
            }

        })
    }

    @InternalCoroutinesApi
    override fun editItem(shopListNameItem: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object  : NewListDialog.Listener{
            override fun onClick(name: String) {
                mainViewModel.updateListName(shopListNameItem.copy(name = name))
            }
        }, shopListNameItem.name)
    }

    override fun onClickItem(shopListNameItem: ShopListNameItem) {
        val i = Intent(activity, ShopListActivity::class.java).apply {
            putExtra(ShopListActivity.SHOP_LIST_NAME, shopListNameItem)
        }
        startActivity(i)
    }
}