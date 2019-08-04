package com.mashup.molink.share

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mashup.molink.data.Injection
import com.mashup.molink.data.model.Folder
import com.mashup.molink.data.model.Link
import com.mashup.molink.data.repository.LinkRepository
import com.mashup.molink.utils.Dlog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_share.*
import org.jetbrains.anko.toast
import org.jsoup.Jsoup


class ShareActivity : AppCompatActivity() {

    /**
     * Make by Black JIn
     */
    private val linkRepository by lazy { Injection.provideLinkRepository(this) }

    private val folderRepository by lazy { Injection.provideFolderRepository(this) }

    companion object {

        const val KEY_LINK_ID = "key_link_id"

    }

    private var linkId = -1


    private val selectAdapter = SelectFolderAdapter()

    private val compositeDisposable = CompositeDisposable()

    private lateinit var selectedFolder: Folder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mashup.molink.R.layout.activity_share)

        tvSharedUrl.movementMethod = ScrollingMovementMethod()

        intent.getIntExtra(KEY_LINK_ID, -1).let {
            if(it > 0) {
                linkId = it
                initModifyMode()
            } else {
                getLinkUrl()
            }
        }

        initRecyclerView()
        loadFolders()

        var hashTagsArray = arrayListOf<String>()

        //링크 저장 버튼 클릭시
        btnLinkSave.setOnClickListener {

            /*var intent = Intent(this@ShareActivity, MainActivity::class.java)
            intent.putExtra("sharedUrl", tvSharedUrl.text)
            intent.putExtra("sharedTitle", etSharedTitle.text)
            intent.putExtra("hashTags",hashTagsArray)
            setResult(Activity.RESULT_OK, intent)

            finish()*/
            saveLink()

        }

        // x 버튼 클릭 시
        btnShareActivityCancel.setOnClickListener {
            finish()
        }

        //제목 수정
        /*etSharedTitle.focusable = View.NOT_FOCUSABLE
        etSharedTitle.isClickable = false

        etSharedTitle.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if(event!!.action == MotionEvent.ACTION_UP) {
                    if(event.rawX >= (etSharedTitle.right - etSharedTitle.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        etSharedTitle.focusable = View.FOCUSABLE
                        etSharedTitle.isFocusableInTouchMode = true

                        return true
                    }
                }
                return false
            }
        })*/


        //해쉬태그 추가
        /*
        //recycler view
        recyclerviewHashTags.adapter = HashTagAdapter(hashTagsArray)
        recyclerviewHashTags.layoutManager = GridLayoutManager(this, 3) //예쁘게 나중에

        edAddHashTag.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if(event!!.action == MotionEvent.ACTION_UP) {
                    if(event.rawX >= (edAddHashTag.right - edAddHashTag.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        var hashtagText = edAddHashTag.text.toString()
                        Dlog.e(hashtagText)
                        if(hashtagText != ""){
                            hashTagsArray.add(hashtagText)
                            recyclerviewHashTags.adapter!!.notifyItemInserted(recyclerviewHashTags.adapter!!.itemCount)
                        }

                        return true
                    }
                }
                return false
            }
        })
        */

        //저장 경로 설정
        //recyclerviewStoreRoute.adapter = StoreRouteAdapter(this)
        //recyclerviewStoreRoute.layoutManager = GridLayoutManager(this, 3)


    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun getLinkUrl() {

        val intent = intent
        val action = intent.action
        val type = intent.type

        var sharedText: String?
        var url: String?

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)  // 가져온 인텐트의 텍스트 정보
                url = Util().extractUrlFromText(sharedText)
                Dlog.d(url)

                tvSharedUrl.text = url

                getMetaDataFromUrl(url)

            }
        }
    }

    private fun getMetaDataFromUrl(url: String?) {

        var async = object : AsyncTask<Void, Void, org.jsoup.nodes.Document>() {
            override fun doInBackground(vararg params: Void?): org.jsoup.nodes.Document {
                val doc = Jsoup.connect(url).get()
                Dlog.d(doc.toString())
                return doc
            }

            override fun onPostExecute(result: org.jsoup.nodes.Document?) {
                super.onPostExecute(result)

                if (result != null) {

                    //TODO 일부 링크 에서는 header 에 원하는 정보가 없습니다.
                    try {
                        val title = result.select("meta[property=og:title]").first().attr("content")
                        val description = result.select("meta[property=og:description]")[0].attr("content")
                        val imageUrl = result.select("meta[property=og:image]")[0].attr("content")

                        Dlog.e("title : $title")
                        Dlog.e("description : $description")
                        Dlog.e("imageUrl : $imageUrl")

                        etSharedTitle.setText(title)
                    } catch (e: Exception) {
                        Dlog.e(e.message)
                    }

                } else {
                    toast("링크 생성에 실패 했습니다.")
                }
            }

        }

        async.execute()

        /*
                var thread = Thread{
                    run {
                            //핸들러 쓰면 죽고 핸들러 안쓰면 안죽는 이유 ㅠㅠ
                            val doc = Jsoup.connect(url).get()
                            Dlog.d(doc.toString())

                            val title = doc.select("meta[property=og:title]").first().attr("content")
                            val description = doc.select("meta[property=og:description]")[0].attr("content")
                            val imageUrl = doc.select("meta[property=og:image]")[0].attr("content")

                            Dlog.e("title : $title")
                            Dlog.e("description : $description")
                            Dlog.e("imageUrl : $imageUrl")
                    }
                }
                thread.start()
         */

    }

    /**
     *  Make by Black JIn
     */
    private fun initRecyclerView() {

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL)

        with(recyclerviewStoreRoute) {
            layoutManager = staggeredGridLayoutManager
            adapter = selectAdapter
        }

        selectAdapter.setItemClickListener(object : SelectFolderAdapter.ItemClickListener {

            override fun onItemClick(folder: Folder) {

                selectedFolder = folder
                tvStoreRouteFolder.text = folder.name
            }

        })
    }

    private fun loadFolders() {

        folderRepository
            .getAllFolders()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                selectedFolder = it[0]
                tvStoreRouteFolder.text = selectedFolder.name

                selectAdapter.setItem(it.toMutableList())

            }) {
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun initModifyMode() {

        linkRepository.getLinkById(linkId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({link ->

                tvSharedUrl.text = link.url
                etSharedTitle.setText(link.name)

                 folderRepository.getFolderById(link.folderId!!).subscribe({folder ->
                     selectedFolder = folder
                     tvStoreRouteFolder.text = folder.name
                }){
                     Dlog.e(it.message)
                 }

            }){
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }

        tvLinkSaveTitle.text = "Link Modify"

        btnLinkSave.visibility = View.GONE
        btnLinkModify.visibility = View.VISIBLE
        btnLinkDelete.visibility = View.VISIBLE

        btnLinkModify.setOnClickListener {
            modifyLink()
        }
        btnLinkDelete.setOnClickListener {
            deleteLink()
        }
    }

    private fun saveLink() {

        val url = tvSharedUrl.text.toString()
        val name = etSharedTitle.text.toString()

        if (name.isEmpty()) {
            toast("링크 제목을 입력해 주세요.")
        } else {

            linkRepository.insertLink(name, url, selectedFolder.id) {
                toast("${selectedFolder.name}에 링크 저장 성공!")
                finish()
            }.also {
                compositeDisposable.add(it)
            }

        }
    }


    private fun modifyLink() {

        val url = tvSharedUrl.text.toString()
        val name = etSharedTitle.text.toString()

        val link = Link(linkId, name, url, selectedFolder.id)

        linkRepository.updateLink(link) {
            toast("${selectedFolder.name}에 링크 수정")
            finish()
        }.also {
            compositeDisposable.add(it)
        }
    }

    private fun deleteLink() {

        linkRepository.deleteLinkById(linkId) {
            toast("삭제 완료")
            finish()
        }.also {
            compositeDisposable.add(it)
        }
    }
}