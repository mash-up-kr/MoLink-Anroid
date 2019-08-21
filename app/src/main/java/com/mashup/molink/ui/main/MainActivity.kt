package com.mashup.molink.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mashup.molink.R
import com.mashup.molink.data.Injection
import com.mashup.molink.data.model.Folder
import com.mashup.molink.ui.detail.DetailActivity
import com.mashup.molink.ui.dialog.ModifyFolderDialog
import com.mashup.molink.ui.main.adapter.MainFolderAdapter
import com.mashup.molink.utils.Dlog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import java.util.*

class MainActivity : AppCompatActivity(), MainFolderAdapter.ItemClickListener, ModifyFolderDialog.DialogClickListener {

    companion object {

        const val KEY_INTERESTS = "KEY_INTERESTS"
    }

    private val folderAdapter by lazy {
        MainFolderAdapter().apply {
            setItemClickListener(this@MainActivity)
        }
    }

    private val folderRepository by lazy {
        Injection.provideFolderRepository(this)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()
        initRecyclerView()
        initButton()
        loadCategory()

    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    /**
     * adapter
     */
    override fun onItemClick(folder: Folder) {
        startActivity(
            Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtra(DetailActivity.KEY_FOLDER_ID, folder.id)
            }
        )
    }

    override fun onItemClickModify(folder: Folder) {
        ModifyFolderDialog(this@MainActivity, folder).apply {
            setDialogClickListener(this@MainActivity)
        }.show()
    }

    /**
     * folder dialog
     */
    override fun make(title: String, color: String) {
        folderRepository.insertFolder(title, color).also {
            compositeDisposable.add(it)
        }
    }

    override fun delete(id: Int) {
        if (folderAdapter.itemCount <= 1) {
            longToast("적어도 1개의 폴더가 있어야 합니다 :)")
            return
        }

        folderRepository.deleteFolderById(id).also {
            compositeDisposable.add(it)
        }
    }

    override fun modify(folder: Folder) {
        folderRepository.updateFolder(folder).also {
            compositeDisposable.add(it)
        }
    }

    /**
     * activity
     */
    private fun getData() {

        val interests = intent?.getSerializableExtra(KEY_INTERESTS)

        if (interests is ArrayList<*>) {

            val temp = arrayListOf<String>()

            for(interest in interests) {
                temp.add(interest.toString())
                Dlog.d("interest : $interest")
            }

            folderRepository.insertCategoryFolders(temp)
                .also { compositeDisposable.add(it) }
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

    private fun loadCategory() {

        folderRepository.flowableCategoryFolders()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                folderAdapter.updateListItems(it.toMutableList())
            }) {
                Dlog.e(it.message)
            }
            .also {
                compositeDisposable.add(it)
            }
    }

    private fun showProgress() {
        pbActivityMain.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        pbActivityMain.visibility = View.GONE
    }
}
