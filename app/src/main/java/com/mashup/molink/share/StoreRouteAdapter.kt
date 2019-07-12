package com.mashup.molink.share

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import android.widget.AdapterView
import com.bumptech.glide.Glide.init
import com.mashup.molink.utils.Dlog


class StoreRouteAdapter(var context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var folderRecyclerList: ArrayList<String>? = ArrayList() // 리사이클러 아이템 리스트
    var dataAdapter: ArrayList<ArrayAdapter<String>> = ArrayList() // 스피너 리스트

    init {
        //folderRecyclerList = folders
        folderRecyclerList = arrayListOf("test")
        dataAdapter.add(ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayOf("백엔드", "안드", "프론트")))
    }

    fun addFolderRecyclerList(){
        folderRecyclerList!!.add("test")
    }

    fun removeRecentIndexOfFolderRecyclerList(){
        //최근 리사이클러 뷰만 삭제
        folderRecyclerList!!.removeAt(folderRecyclerList!!.size-1)
    }

    fun removeAfterIndexOfFolderRecyclerList(index : Int){

        //position 이후의 모든 리사이클러 뷰와 스피너 데이터 삭제
        val deleteSize = folderRecyclerList!!.size - index - 1
        Dlog.e(deleteSize.toString())
        for(i in 1 .. deleteSize){
            Dlog.e(i.toString())
            folderRecyclerList!!.removeAt(folderRecyclerList!!.size-1)
            dataAdapter!!.removeAt(dataAdapter!!.size-1)
        }

    }

    //test data
    fun addDataAdapterList(item: String) {

        // TODO : item = 선택한 폴더의 id 또는 이름..
        when(item){
            "백엔드" -> dataAdapter!!.add(ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayOf("노드","스프링")))
            "노드" ->  dataAdapter!!.add(ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayOf("자바스크립트","node자료")))
            "안드" -> dataAdapter!!.add(ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayOf("자바","코틀린")))
            "프론트" -> dataAdapter!!.add(ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayOf("리액트","앵귤러", "뷰")))
            else -> {
                //리사이클러 뷰 추가 안함
                removeRecentIndexOfFolderRecyclerList()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(com.mashup.molink.R.layout.item_route,parent,false)

        return StoreRouteViewHolder(view, parent.context, this@StoreRouteAdapter)
    }

    class StoreRouteViewHolder(view: View?, context: Context, adapter:StoreRouteAdapter) : RecyclerView.ViewHolder(view!!) {

        var spinner : Spinner? = null

        init {
            spinner = view!!.findViewById(com.mashup.molink.R.id.spinnerRoute)

            spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    // On selecting a spinner item
                    val item = parent.getItemAtPosition(position).toString()

                    Dlog.e(adapterPosition.toString())

                    Dlog.e(item)
                    //초기 값 일때 뒤에 폴더가 보이지 않음

                    if(position.toString() == "0" && (adapter.itemCount - 1 == adapterPosition)){
                        Dlog.e("여기 들어옴")
                        return
                    }

                    //adapter.notifyDataSetChanged() // 다 스피너아이템 선택한거까지 초기화됨
                    //마지막 리사이클러뷰만 변경된 경우
                    if(adapterPosition == adapter.itemCount - 1){
                        Dlog.e("마지막 리사이클러뷰 변경")
                        //add recycler item
                        adapter.addFolderRecyclerList()

                        //add spinner data _ for test
                        Dlog.e(item)
                        adapter.addDataAdapterList(item) //나중엔 폴더 id 넘기기

                        adapter.notifyItemChanged(adapter.itemCount-1)
                    } else {
                        //이전 리사이클러뷰가 변경된 경우
                        adapter.removeAfterIndexOfFolderRecyclerList(adapterPosition)
                        Dlog.d("adapterPosition : $adapterPosition , count : ${adapter.itemCount}")
                        adapter.notifyItemRangeChanged(adapterPosition + 1, adapter.itemCount-1)

                        adapter.addFolderRecyclerList()
                        adapter.addDataAdapterList(item)
                        adapter.notifyItemChanged(adapter.itemCount-1)
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }


    }

    override fun getItemCount(): Int {
        return folderRecyclerList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var view = holder as StoreRouteViewHolder

        view.spinner!!.adapter = dataAdapter!![position]

    }


}