package com.mashup.molink.ui.detail

import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//https://medium.com/@nbtk123/create-your-own-horizontal-vertical-slider-picker-android-94b6ee32b3ff
class SlideLayoutManager (
    private val recyclerView: RecyclerView
) : LinearLayoutManager(recyclerView.context, LinearLayout.HORIZONTAL, false) {

    private val scaleDownBy = 0.66f
    private val scaleDownDistance = 1.6f

    var callback: OnItemSelectedListener? = null

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        scaleDownView()
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            scaleDownView()
            scrolled
        } else 0
    }

    private fun scaleDownView() {
        val mid = width / 2.0f
        val unitScaleDownDist = scaleDownDistance * mid
        for (i in 0 until childCount) {
            val child = getChildAt(i)!!
            val childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f
            val scale = 1.0f + -1 * scaleDownBy * Math.min(unitScaleDownDist, Math.abs(mid - childMid)) / unitScaleDownDist
            
            child.scaleX = scale
            child.scaleY = scale
            child.alpha = scale
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        // When scroll stops we notify on the selected item
        if (state == RecyclerView.SCROLL_STATE_IDLE) {

            // Find the closest child to the recyclerView center --> this is the selected item.
            val recyclerViewCenterX = getRecyclerViewCenterX()

            var minDistance = recyclerView.width

            var position = -1

            for (i in 0 until recyclerView.childCount) {

                val child = recyclerView.getChildAt(i)

                val childCenterX = getDecoratedLeft(child) + (getDecoratedRight(child) - getDecoratedLeft(child)) / 2

                val childDistanceFromCenter = Math.abs(childCenterX - recyclerViewCenterX)

                if (childDistanceFromCenter < minDistance) {
                    minDistance = childDistanceFromCenter
                    position = recyclerView.getChildLayoutPosition(child)
                }
            }

            // Notify on the selected item
            callback?.onItemSelected(position)
        }
    }

    private fun getRecyclerViewCenterX() : Int {

        return (recyclerView.right - recyclerView.left)/2 + recyclerView.left
    }

    interface OnItemSelectedListener {

        fun onItemSelected(layoutPosition: Int)
    }
}