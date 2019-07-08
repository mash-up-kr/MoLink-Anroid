package com.mashup.molink.detail

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.mashup.molink.R
import com.mashup.molink.model.Folder
import com.mashup.molink.model.ItemLink
import com.mashup.molink.utils.Dlog
import com.mashup.molink.utils.ScreenUtil
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.toast

class DetailActivity : AppCompatActivity(), NavFolderAdapter.ItemClickListener {

    companion object {

        const val KEY_FOLDER = "folder"
    }

    private val navFolderAdapter by lazy {
        NavFolderAdapter().apply {
            setItemClickListener(this@DetailActivity)
        }
    }

    private val detailFolderAdapter by lazy {
        DetailFolderAdapter()
    }

    private val linkAdapter by lazy {
        LinkAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        getData()
        initView()
        initButton()
        loadData()

    }

    override fun onItemClick(folder: Folder) {
        navFolderAdapter.deleteItem(folder.fid)
    }

    private fun getData() {

        val folder = intent?.getParcelableExtra<Folder>(KEY_FOLDER)
        Dlog.d("folder : $folder")

        if(folder == null) {
            error("folder must be not null")
        }
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
                        Dlog.d("layoutPosition : $layoutPosition")
                    }
                }
            }

            adapter = detailFolderAdapter

            val folderWidth = dip(82)
            val sidePadding: Int = (ScreenUtil.getScreenWidth(this@DetailActivity) - folderWidth) / 2
            setPadding(sidePadding,0, sidePadding,0)

            LinearSnapHelper().attachToRecyclerView(this)
        }


        with(rvActivityDetailLink) {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = linkAdapter
        }

    }

    private fun initButton() {

        ivActivityDetailBack.setOnClickListener {
            onBackPressed()
        }

        fabActivityDetail.setOnClickListener {
            //TODO 폴더 생성
            toast("작업중")
        }
    }

    private fun loadData() {

        // nav folder
        val items = mutableListOf(
            Folder(1, "Developer", "#47c7f5"),
            Folder(2, "Design", "#87db77"),
            Folder(3, "MashUp", "#f7c34d"),
            Folder(4, "Home", "#e26f8e")
        )

        items.forEach {
            navFolderAdapter.addItem(it)
        }

        // detail folder
        val item2 = mutableListOf(
            Folder(1, "1", "#47c7f5"),
            Folder(2, "2", "#87db77"),
            Folder(3, "3", "#f7c34d"),
            Folder(4, "4", "#e26f8e"),
            Folder(5, "5", "#47c7f5"),
            Folder(6, "6", "#87db77"),
            Folder(7, "7", "#f7c34d"),
            Folder(8, "8", "#e26f8e"),
            Folder(9, "9", "#47c7f5"),
            Folder(10, "10", "#87db77"),
            Folder(11, "11", "#f7c34d"),
            Folder(12, "12", "#e26f8e")

        )

        detailFolderAdapter.setItems(item2)

        // link
        val item3 = mutableListOf(
            ItemLink(1, 1, "립취향", "#47c7f5", "www.mashup.com").apply { viewType = 1 },
            ItemLink(1, 1, "친구추천", "#87db77", "www.mashup.com").apply { viewType = 1 },
            ItemLink(1, 1, "2019 MLBB", "#f7c34d", "www.mashup.com").apply { viewType = 2 },
            ItemLink(1, 1, "민감성 피부추천 폼클렌징", "#87db77", "www.mashup.com").apply { viewType = 2 },
            ItemLink(1, 1, "메쉬업", "#f7c34d", "www.mashup.com").apply { viewType = 2 },
            ItemLink(1, 1, "해커톤", "#e26f8e", "www.mashup.com").apply { viewType = 2 }
        )

        linkAdapter.setItems(item3)
    }
}