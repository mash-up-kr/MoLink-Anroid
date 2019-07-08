package com.mashup.molink.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
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

    private val colors = arrayOf(
        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.purpleish_blue)),
        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.maize)),
        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.lightblue)),
        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.lighter_purple)),
        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.lightish_green)),
        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.pig_pink)))

    private var color = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.purpleish_blue))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.dialog_modify_folder)

        initColor()

        if(folder == null) {

            btnDialogModifyFolderMake.visibility = View.VISIBLE
            btnDialogModifyFolderMake.setOnClickListener {
                val title = etDialogModifyFolder.text.toString()

                if(title.isNullOrEmpty()) {
                    context.toast("폴더 이름을 입력해 주세요")
                } else {
                    listener?.make(title, color)
                    dismiss()
                }
            }

        } else {

            etDialogModifyFolder.setText(folder.title)
            color = folder.color

            btnDialogModifyFolderDelete.visibility = View.VISIBLE
            btnDialogModifyFolderDelete.setOnClickListener {
                listener?.delete(folder.fid)
                dismiss()
            }

            btnDialogModifyFolderModify.visibility = View.VISIBLE
            btnDialogModifyFolderModify.setOnClickListener {
                val title = etDialogModifyFolder.text.toString()

                if(title.isNullOrEmpty()) {
                    context.toast("폴더 이름을 입력해 주세요")
                } else {
                    val modifyFolder = folder.copy(title = etDialogModifyFolder.text.toString(), color = color)
                    listener?.modify(modifyFolder)
                    dismiss()
                }
            }
        }

        setColor()

        btnDialogModifyFolderCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun initColor() {
        ivDialogModifyFolderColor0.setColorFilter(Color.parseColor(colors[0]))
        ivDialogModifyFolderColor1.setColorFilter(Color.parseColor(colors[1]))
        ivDialogModifyFolderColor2.setColorFilter(Color.parseColor(colors[2]))
        ivDialogModifyFolderColor3.setColorFilter(Color.parseColor(colors[3]))
        ivDialogModifyFolderColor4.setColorFilter(Color.parseColor(colors[4]))
        ivDialogModifyFolderColor5.setColorFilter(Color.parseColor(colors[5]))

        ivDialogModifyFolderColor0.setOnClickListener {
            color = colors[0]
            setColor()
        }

        ivDialogModifyFolderColor1.setOnClickListener {
            color = colors[1]
            setColor()
        }

        ivDialogModifyFolderColor2.setOnClickListener {
            color = colors[2]
            setColor()
        }

        ivDialogModifyFolderColor3.setOnClickListener {
            color = colors[3]
            setColor()
        }

        ivDialogModifyFolderColor4.setOnClickListener {
            color = colors[4]
            setColor()
        }

        ivDialogModifyFolderColor5.setOnClickListener {
            color = colors[5]
            setColor()
        }
    }

    private fun setColor() {
        ivDialogModifyFolderColor.setColorFilter(Color.parseColor(color))
    }

    fun setDialogClickListener(listener: DialogClickListener?) {
        this.listener = listener
    }

    interface DialogClickListener {

        fun make(title: String, color: String)

        fun delete(fid: Int)

        fun modify(folder: Folder)
    }
}