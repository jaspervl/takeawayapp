package com.jvl.assignment.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import com.jvl.assignment.R


class IconButton : androidx.appcompat.widget.AppCompatImageButton, Checkable {
    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) :
            super(context, attrs, defStyle)

    override fun isChecked() = isSelected

    override fun setChecked(checked: Boolean) {
        isSelected = checked
        onCheckedChangeListener?.onCheckedChanged(this, checked)
        refreshDrawableState();
    }

    override fun toggle() {
        isChecked = !isChecked
        refreshDrawableState();
    }

    override fun performClick(): Boolean{
        toggle()
        return super.performClick()
    }

    private val checkedState = intArrayOf(android.R.attr.state_checked)

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        return super.onCreateDrawableState(extraSpace + 1).apply {
            if (isChecked) View.mergeDrawableStates(this, checkedState)
        }
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(
            buttonView: IconButton,
            isChecked: Boolean
        )
    }
}