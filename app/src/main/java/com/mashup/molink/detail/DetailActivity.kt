package com.mashup.molink.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.mashup.molink.data.Folder
import com.mashup.molink.data.Link
import com.mashup.molink.data.MoLinkDataBase
import com.mashup.molink.dialog.ModifyFolderDialog
import com.mashup.molink.extensions.runOnIoScheduler
import com.mashup.molink.model.LinkAndFolderModel
import com.mashup.molink.utils.Dlog
import com.mashup.molink.utils.ScreenUtil
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.toast

class DetailActivity : AppCompatActivity()
    , NavFolderAdapter.ItemClickListener
    , LinkAndFolderAdapter.ItemClickListener
    , ModifyFolderDialog.DialogClickListener {

    companion object {

        const val KEY_FOLDER_ID = "folder_id"
    }

    private val navFolderAdapter by lazy {
        NavFolderAdapter().apply {
            setItemClickListener(this@DetailActivity)
        }
    }

    private val detailFolderAdapter by lazy {
        DetailFolderAdapter()
    }

    private val linkAndFolderAdapter by lazy {
        LinkAndFolderAdapter().apply {
            setItemClickListener(this@DetailActivity)
        }
    }

    private val folderDao by lazy { MoLinkDataBase.getInstance(this).getFolderDao() }

    private val linkDao by lazy { MoLinkDataBase.getInstance(this).getLinkDao() }

    private val compositeDisposable = CompositeDisposable()

    private var folderId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mashup.molink.R.layout.activity_detail)

        getData()
        initView()
        initButton()
    }

    /**
     * NavFolderAdapter listener
     */
    override fun onItemClick(beforeFolderId: Int, folderId: Int) {
        navFolderAdapter.deleteItem(folderId)
        loadDetailFolderByParent(beforeFolderId)
    }

    override fun onItemClickRoot(folderId: Int) {
        navFolderAdapter.deleteItem(folderId)
        loadRootFolderGoToFirst()
    }

    /**
     * LinkAndFolderAdapter listener
     */
    override fun onItemFolderClick(item: LinkAndFolderModel) {

        folderDao.getFolderById(folderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { currentFolder ->
                navFolderAdapter.addItem(currentFolder)
                loadDetailFolderByParent(currentFolder.id, item.fid)

            }.also {
                compositeDisposable.add(it)
            }
    }

    override fun onItemLinkClick(item: LinkAndFolderModel) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(intent)
    }

    override fun onItemLinkModifyClick(item: LinkAndFolderModel) {
        //수정 화면 제거
    }

    /**
     * dialog
     */
    override fun make(title: String, color: String) {

        val folder = Folder(name = title, color = color, parentId = folderId)

        Single.fromCallable {
                folderDao.insert(folder)
            }.flatMap {
                folderDao.getAllFolders()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val lastFolder = it[it.size - 1]
                linkAndFolderAdapter.addFolder(lastFolder)
            }) {
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    override fun delete(id: Int) {
        runOnIoScheduler {
            folderDao.deleteFolderById(id)
        }.also {
            compositeDisposable.add(it)
        }
    }

    override fun modify(folder: Folder) {
        runOnIoScheduler {
            folderDao.update(folder)
        }.also {
            compositeDisposable.add(it)
        }
    }

    /**
     * Activity
     */
    private fun getData() {

        folderId = intent.getIntExtra(KEY_FOLDER_ID, -1)
        Dlog.d("folderId : $folderId")

        if (folderId < 0) {
            error("folderId must be not null")
        }

        loadRootFolder()
    }

    private fun initView() {

        with(rvActivityDetailNavFolder) {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayout.HORIZONTAL, false)
            adapter = navFolderAdapter
        }

        with(rvActivityDetailViewFolder) {
            layoutManager = SlideLayoutManager(this).apply {
                callback = object : SlideLayoutManager.OnItemSelectedListener {
                    override fun onItemSelected(layoutPosition: Int) {
                        val folder = detailFolderAdapter.getItem(layoutPosition)
                        folder?.let {
                            folderId = it.id
                            loadChildData()
                        }
                    }
                }
            }

            adapter = detailFolderAdapter

            val folderWidth = dip(82)
            val sidePadding: Int = (ScreenUtil.getScreenWidth(this@DetailActivity) - folderWidth) / 2
            setPadding(sidePadding, 0, sidePadding, 0)

            LinearSnapHelper().attachToRecyclerView(this)
        }

        with(rvActivityDetailLink) {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = linkAndFolderAdapter
        }

    }

    private fun initButton() {

        ivActivityDetailBack.setOnClickListener {
            onBackPressed()
        }

        fabActivityDetail.setOnClickListener {
            ModifyFolderDialog(this@DetailActivity, null).apply {
                setDialogClickListener(this@DetailActivity)
            }.show()
        }
    }

    private fun loadRootFolder() {

        folderDao.getAllFolders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { folders ->

                val items = folders.filter { it.parentId == null }

                detailFolderAdapter.updateListItems(items.toMutableList())
                val pos = detailFolderAdapter.getPosition(folderId)
                rvActivityDetailViewFolder.scrollToPosition(pos)

                loadChildData()

            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun loadRootFolderGoToFirst() {

        folderDao.getAllFolders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { folders ->

                val items = folders.filter { it.parentId == null }
                detailFolderAdapter.updateListItems(items.toMutableList())
                this.folderId = items[0].id

                loadChildData()

            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun loadDetailFolderByParent(folderId: Int, focusId: Int) {

        folderDao.getFoldersByParentId(folderId)
            .subscribeOn(Schedulers.io())
            .subscribe({
                Dlog.d("loadDetailFolderByParentWithFocus : $it")
                if (it.isNotEmpty()) {

                    this.folderId = focusId
                    detailFolderAdapter.updateListItems(it.toMutableList())
                    val pos = detailFolderAdapter.getPosition(this.folderId)
                    rvActivityDetailViewFolder.scrollToPosition(pos)

                    loadChildData()
                }

            }) {
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun loadDetailFolderByParent(folderId: Int) {

        folderDao.getFoldersByParentId(folderId)
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.isNotEmpty()) {
                    this.folderId = it[0].id
                    detailFolderAdapter.updateListItems(it.toMutableList())
                    loadChildData()
                }

            }) {
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun loadChildData() {

        val childFolder = folderDao.getFoldersByParentId(folderId)

        val childLink = linkDao.getLinksByFolderId(folderId)

        Single.zip(childFolder, childLink,
            BiFunction<List<Folder>, List<Link>, List<LinkAndFolderModel>>
            { folders, links ->

                val items = arrayListOf<LinkAndFolderModel>()

                for (item in folders) {
                    val temp =
                        LinkAndFolderModel(fid = item.id, title = item.name, color = item.color, viewType = 1)
                    items.add(temp)
                }

                for (item in links) {
                    val temp =
                        LinkAndFolderModel(lid = item.id, title = item.name, url = item.url, viewType = 2)
                    items.add(temp)
                }

                items.sortBy { it.viewType }

                items
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                linkAndFolderAdapter.setItems(it.toMutableList())
            }) {
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

}