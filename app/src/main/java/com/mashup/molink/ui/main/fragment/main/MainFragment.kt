package com.mashup.molink.ui.main.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mashup.molink.R
import com.mashup.molink.data.Injection
import com.mashup.molink.data.model.Folder
import com.mashup.molink.ui.detail.DetailActivity
import com.mashup.molink.ui.dialog.ModifyFolderDialog
import com.mashup.molink.utils.Dlog
import com.mashup.molink.utils.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.longToast
import java.io.Serializable
import java.util.*

class MainFragment : Fragment(), MainFolderAdapter.ItemClickListener, ModifyFolderDialog.DialogClickListener {

    companion object {

        const val KEY_INTERESTS = "KEY_INTERESTS"

        fun newInstance(interests: Serializable?) = MainFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_INTERESTS, interests)
            }
        }
    }

    private val folderAdapter by lazy {
        MainFolderAdapter().apply {
            setItemClickListener(this@MainFragment)
        }
    }

    private val folderRepository by lazy {
        Injection.provideFolderRepository(context!!)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getData()
        initProfile()
        initRecyclerView()
        initButton()
        loadCategory()
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    /**
     * adapter
     */
    override fun onItemClick(folder: Folder) {
        startActivity(
            Intent(context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.KEY_FOLDER_ID, folder.id)
            }
        )
    }

    override fun onItemClickModify(folder: Folder) {
        ModifyFolderDialog(context!!, folder).apply {
            setDialogClickListener(this@MainFragment)
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
            activity?.longToast("적어도 1개의 폴더가 있어야 합니다 :)")
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
     * fragment
     */
    private fun getData() {

        val interests = arguments?.getSerializable(KEY_INTERESTS)

        if (interests is ArrayList<*>) {

            val temp = arrayListOf<String>()

            for (interest in interests) {
                temp.add(interest.toString())
                Dlog.d("interest : $interest")
            }

            folderRepository.insertCategoryFolders(temp)
                .also { compositeDisposable.add(it) }
        }
    }

    private fun initProfile() {

        tvFragmentMainUserkName.text = PrefUtil.get(PrefUtil.PREF_USER_NAME, "")
        tvFragmentMainInterest.text = "#메쉬업 #모링 #스터디 #배포"

        tvFragmentMainLink.text = "42개"
        tvFragmentMainFollow.text = "12개"
    }

    private fun initRecyclerView() {

        with(rvFragmentMain) {
            layoutManager = GridLayoutManager(context, 3)
            adapter = folderAdapter
        }
    }

    private fun initButton() {

        fabFragmentMain.setOnClickListener {
            ModifyFolderDialog(context!!, null).apply {
                setDialogClickListener(this@MainFragment)
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
        pbFragmentMain.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        pbFragmentMain.visibility = View.GONE
    }
}
