package com.mashup.molink.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.mashup.molink.R
import com.mashup.molink.data.Folder
import com.mashup.molink.data.MoLinkDataBase
import com.mashup.molink.detail.DetailActivity
import com.mashup.molink.dialog.ModifyFolderDialog
import com.mashup.molink.extensions.runOnIoScheduler
import com.mashup.molink.utils.Dlog
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MainFolderAdapter.ItemClickListener, ModifyFolderDialog.DialogClickListener {

    companion object {

        const val KEY_INTERESTS = "KEY_INTERESTS"
    }

    private val folderAdapter by lazy {
        MainFolderAdapter().apply {
            setItemClickListener(this@MainActivity)
        }
    }

    private val folderDao by lazy { MoLinkDataBase.getInstance(this).getFolderDao() }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()
        initRecyclerView()
        initButton()

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
     * dialog
     */
    override fun make(title: String, color: String) {
        runOnIoScheduler {
            val folder =  Folder(name = title, color = color, parentId = null)
            folderDao.insert(folder)
            loadData()
        }.also {
            compositeDisposable.add(it)
        }
        /*folderAdapter.addItem(
            Folder(name = title, color = color, parentId = null)
        )*/
    }

    override fun delete(id: Int) {
        runOnIoScheduler {
            folderDao.deleteFolderById(id)
            loadData()
        }.also {
            compositeDisposable.add(it)
        }
        //folderAdapter.deleteItem(id)
    }

    override fun modify(folder: Folder) {
        runOnIoScheduler {
            folderDao.update(folder)
            loadData()
        }.also {
            compositeDisposable.add(it)
        }
        //folderAdapter.modifyItem(folder)
    }

    /**
     * activity
     */
    private fun getData() {

        val colors = arrayOf(
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.purpleish_blue)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.maize)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.lightblue)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.lighter_purple)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.lightish_green)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.pig_pink)))

        val interests = intent?.getSerializableExtra(KEY_INTERESTS)
        Dlog.d("interests : $interests")

        if(interests is ArrayList<*>) {

            runOnIoScheduler {
                folderDao.clearAll()

                for(interest in interests) {
                    Dlog.d("interest : $interest")

                    val random = Random()
                    val num = random.nextInt(5)

                    val folder = Folder(name = interest.toString(), color = colors[num], parentId = null)
                    folderDao.insert(folder)
                }

                loadData()

            }.also {
                compositeDisposable.add(it)
            }
        } else {
            loadData()
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

        folderDao.getAllFolders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {folders ->

                Dlog.e("getAllFolders : $folders")
                val items = folders.filter { it.parentId == null }
                Dlog.e("items : $items")

                folderAdapter.updateListItems(items.toMutableList())
            }.also {
                compositeDisposable.add(it)
            }

    }
}
