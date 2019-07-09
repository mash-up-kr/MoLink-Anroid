package com.mashup.molink.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.mashup.molink.R
import com.mashup.molink.detail.DetailActivity
import com.mashup.molink.dialog.ModifyFolderDialog
import com.mashup.molink.model.Folder
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainFolderAdapter.ItemClickListener, ModifyFolderDialog.DialogClickListener {

    companion object {

        const val KEY_INTERESTS = "interests"
    }

    private val folderAdapter by lazy {
        MainFolderAdapter().apply {
            setItemClickListener(this@MainActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO Interest 에서 받아온 정보로 폴더 생성

        getData()
        initRecyclerView()
        initButton()
        loadData()

    }

    override fun onItemClick(folder: Folder) {
        startActivity(
            Intent(this@MainActivity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.KEY_FOLDER, folder)
                }
        )
    }

    override fun onItemClickModify(folder: Folder) {
        ModifyFolderDialog(this@MainActivity, folder).apply {
            setDialogClickListener(this@MainActivity)
        }.show()
    }

    override fun make(title: String, color: String) {
        folderAdapter.addItem(
            Folder(10, title, color)
        )
    }

    override fun delete(fid: Int) {
        //TODO 폴더를 모두 지워버리면?
        folderAdapter.deleteItem(fid)
    }

    override fun modify(folder: Folder) {
        folderAdapter.modifyItem(folder)
    }

    private fun getData() {

        val interests = intent?.getStringArrayExtra(KEY_INTERESTS)
        Dlog.d("interests : $interests")

        if(interests == null) {
            //TODO error("interests must be not null")
        }
    }

    private fun initRecyclerView() {

        with(rvActivityMain) {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = folderAdapter
        }
    }

    private fun initButton() {
        fabActivityMain.setOnClickListener {
            ModifyFolderDialog(this@MainActivity, null).apply {
                setDialogClickListener(this@MainActivity)
            }.show()
        }
    }

    private fun loadData() {

        val items = mutableListOf(
            Folder(1, "Developer", "#47c7f5"),
            Folder(2, "Design", "#87db77"),
            Folder(3, "MashUp", "#f7c34d"),
            Folder(4, "Home", "#e26f8e")
        )

        folderAdapter.setItems(items)
    }
}
