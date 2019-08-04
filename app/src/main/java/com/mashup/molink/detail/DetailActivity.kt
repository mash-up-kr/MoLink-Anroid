package com.mashup.molink.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.mashup.molink.data.Injection
import com.mashup.molink.data.model.Folder
import com.mashup.molink.data.model.Link
import com.mashup.molink.detail.adapter.DetailFolderAdapter
import com.mashup.molink.detail.adapter.LinkAndFolderAdapter
import com.mashup.molink.detail.adapter.NavFolderAdapter
import com.mashup.molink.detail.adapter.model.LinkAndFolderModel
import com.mashup.molink.dialog.ModifyFolderDialog
import com.mashup.molink.share.ShareActivity
import com.mashup.molink.utils.Dlog
import com.mashup.molink.utils.ScreenUtil
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.dip
import java.util.concurrent.TimeUnit

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

    private val folderRepository by lazy {
        Injection.provideFolderRepository(this)
    }

    private val linkRepository by lazy {
        Injection.provideLinkRepository(this)
    }

    private val compositeDisposable = CompositeDisposable()

    private var currentFolderId = -1

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
        loadCategoryFolder()
    }

    /**
     * LinkAndFolderAdapter listener
     */
    override fun onItemFolderClick(item: LinkAndFolderModel) {

        folderRepository.getFolderById(currentFolderId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { currentFolder ->
                navFolderAdapter.addItem(currentFolder)
                loadDetailFolderByParent(currentFolder.id, item.fid)
            }.also {
                compositeDisposable.add(it)
            }
    }

    override fun onItemFolderLongClick(item: LinkAndFolderModel) {
        //TODO 애니메이션 효과가 필요합니다.
        val folder = Folder(
            id = item.fid,
            name = item.title!!,
            color = item.color!!,
            parentId = currentFolderId
        )

        ModifyFolderDialog(this@DetailActivity, folder).apply {
            setDialogClickListener(this@DetailActivity)
        }.show()
    }

    override fun onItemLinkClick(item: LinkAndFolderModel) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(intent)
    }

    override fun onItemLinkModifyClick(item: LinkAndFolderModel) {
        //TODO 애니메이션 효과가 필요합니다.
        val intent = Intent(this, ShareActivity::class.java).apply {
            putExtra(ShareActivity.KEY_LINK_ID, item.lid)
        }
        startActivity(intent)
    }

    /**
     * folder dialog
     */
    override fun make(title: String, color: String) {
        folderRepository.insertFolder(title, color, currentFolderId).also {
            compositeDisposable.add(it)
        }
    }

    override fun delete(id: Int) {
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
     * Activity
     */
    private fun getData() {

        currentFolderId = intent.getIntExtra(KEY_FOLDER_ID, -1)
        Dlog.d("currentFolderId : $currentFolderId")

        if (currentFolderId < 0) {
            error("currentFolderId must be not null")
        }

        loadCategoryFolder()
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
                            //2번 이상씩 작돔됨
                            currentFolderId = it.id
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

    private fun loadCategoryFolder() {

        folderRepository.getCategoryFolders()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (detailFolderAdapter.itemCount != 0) {
                    //nav 마지막 아이템을 누른 경우 currentFolderId 초기화
                    currentFolderId = it[0].id
                }

                detailFolderAdapter.updateListItems(it.toMutableList())

                val pos = detailFolderAdapter.getPosition(currentFolderId)
                rvActivityDetailViewFolder.scrollToPosition(pos)

                loadChildData()

            }) {
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun loadDetailFolderByParent(folderId: Int, focusId: Int) {

        folderRepository.getFoldersByParentId(folderId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (it.isNotEmpty()) {

                    detailFolderAdapter.updateListItems(it.toMutableList())

                    currentFolderId = focusId

                    val pos = detailFolderAdapter.getPosition(currentFolderId)
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

        folderRepository.getFoldersByParentId(folderId)
            .subscribe({
                if (it.isNotEmpty()) {

                    currentFolderId = it[0].id
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

        val childFolder = folderRepository.flowableFoldersByParentId(currentFolderId)
        val childLink = linkRepository.flowableLinksByFolderId(currentFolderId)

        Flowable
            .zip(childFolder, childLink,
                BiFunction<List<Folder>, List<Link>, List<LinkAndFolderModel>>
                { folders, links ->

                    val items = arrayListOf<LinkAndFolderModel>()

                    for (item in folders) {
                        val temp =
                            LinkAndFolderModel(
                                fid = item.id,
                                title = item.name,
                                color = item.color,
                                viewType = 1
                            )
                        items.add(temp)
                    }

                    for (item in links) {
                        val temp =
                            LinkAndFolderModel(
                                lid = item.id,
                                title = item.name,
                                url = item.url,
                                viewType = 2
                            )
                        items.add(temp)
                    }

                    items.sortBy { it.viewType }

                    items
                })
            .debounce(250, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Dlog.e("$currentFolderId -> loadChildData")
                linkAndFolderAdapter.setItems(it.toMutableList())
                setChildData(childFolder, childLink)
            }) {
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun setChildData(childFolder: Flowable<List<Folder>>, childLink: Flowable<List<Link>>) {

        compositeDisposable.clear()

        childFolder
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { folders ->

                Dlog.d("folders : $folders")
                linkAndFolderAdapter.setFolders(folders)

            }.also {
                compositeDisposable.add(it)
            }

        childLink
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe { links ->

               Dlog.d("links : $links")
               linkAndFolderAdapter.setLinks(links)

           }.also {
               compositeDisposable.add(it)
           }
    }
}