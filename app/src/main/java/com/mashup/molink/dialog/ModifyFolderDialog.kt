package com.mashup.molink.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.mashup.molink.R
import com.mashup.molink.model.Folder
import kotlinx.android.synthetic.main.dialog_modify_folder.*
import org.jetbrains.anko.toast

/**
 * folder
 * null     -> 폴더 생성
 * not null -> 폴더 수정
 */
class ModifyFolderDialog(context: Context,
    private val folder: Folder?) : Dialog(context) {

    private var listener: DialogClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.dialog_modify_folder)

        if(folder == null) {

            btnDialogModifyFolderMake.visibility = View.VISIBLE
            btnDialogModifyFolderMake.setOnClickListener {
                listener?.make()
                dismiss()
            }

        } else {

            btnDialogModifyFolderDelete.visibility = View.VISIBLE
            btnDialogModifyFolderDelete.setOnClickListener {
                listener?.delete(folder.fid)
                dismiss()
            }

            btnDialogModifyFolderModify.visibility = View.VISIBLE
            btnDialogModifyFolderModify.setOnClickListener {
                val modifyFolder = folder.copy(title = "modify")
                listener?.modify(modifyFolder)
                dismiss()
            }
        }

        btnDialogModifyFolderCancel.setOnClickListener {
            dismiss()
        }

    }

    fun setDialogClickListener(listener: DialogClickListener?) {
        this.listener = listener
    }

    interface DialogClickListener {

        fun make()

        fun delete(fid: Int)

        fun modify(folder: Folder)
    }
}