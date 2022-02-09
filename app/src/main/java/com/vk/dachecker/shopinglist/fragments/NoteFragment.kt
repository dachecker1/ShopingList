package com.vk.dachecker.shopinglist.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vk.dachecker.shopinglist.activities.MainApp
import com.vk.dachecker.shopinglist.activities.NewNoteActivity
import com.vk.dachecker.shopinglist.databinding.FragmentNoteBinding
import com.vk.dachecker.shopinglist.db.MainViewModel
import com.vk.dachecker.shopinglist.db.NoteAdapter
import com.vk.dachecker.shopinglist.entities.NoteItem
import kotlinx.coroutines.InternalCoroutinesApi

class NoteFragment : BaseFragment(), NoteAdapter.Listener {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NoteAdapter
    private lateinit var defPref: SharedPreferences

    @InternalCoroutinesApi
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelfactory((context?.applicationContext as MainApp).database)
    }
    override fun onClickNew() {
        //запускаем слушатель записи данных
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        onEditResult()

        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyTag", "запустился onViewCreated NoteFragment. Запускается initRcView и observer")
        initRcView()
        observer()
    }

        //запускаем активити с возвратом результата. инициализируем метод сразу в onCreate
    @InternalCoroutinesApi
    private fun onEditResult(){
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val editState = it.data?.getStringExtra(EDIT_STATE_KEY)
                if (editState == "update"){
                    mainViewModel.updateNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                } else {
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                }
                Log.d("MyTag", "title : ${it.data?.getSerializableExtra(NEW_NOTE_KEY).toString()}")
            }
        }
    }
    @InternalCoroutinesApi
    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun initRcView() = with(binding){
        defPref = PreferenceManager.getDefaultSharedPreferences(activity)
        rcViewNote.layoutManager = getLayoutManager()
        adapter = NoteAdapter(this@NoteFragment, defPref)
        rcViewNote.adapter = adapter
    }

    private fun getLayoutManager():RecyclerView.LayoutManager{
        return if(defPref.getString("note_style_key", "Linear") == "Linear"){
            LinearLayoutManager(activity)
        } else {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    @InternalCoroutinesApi
    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }

    override fun onClickItem(note: NoteItem) {
        val intent = Intent(activity, NewNoteActivity::class.java).apply {
            putExtra(NEW_NOTE_KEY, note)
        }
        editLauncher.launch(intent)
    }

    companion object {
        const val NEW_NOTE_KEY = "new_note_key"
        const val EDIT_STATE_KEY = "edit_state_key"

        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}