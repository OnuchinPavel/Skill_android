package ru.prsolution.winstrike.presentation.utils.custom

import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.RecyclerView

class BottomDecoratorHelper(private val bottomOffset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val dataSize = state.itemCount
        val position = parent.getChildPosition(view)
        if (dataSize > 0 && position == dataSize - 1) {
            outRect.set(0, 0, 0, bottomOffset)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
