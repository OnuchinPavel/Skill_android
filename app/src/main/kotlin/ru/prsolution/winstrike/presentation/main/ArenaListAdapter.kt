package ru.prsolution.winstrike.presentation.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dlg_logout.view.title
import kotlinx.android.synthetic.main.item_arena.view.address
import kotlinx.android.synthetic.main.item_arena.view.iv_checked
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.setColor

class ArenaListAdapter(private val itemClick: (Arena, Int) -> Unit) :
        ListAdapter<Arena, ArenaListAdapter.ViewHolder>(ArenaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(getItem(position))

    inner class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_arena)) {

        fun bind(item: Arena) {
            itemView.title.text = item.name
            itemView.address.text = item.metro
            if (layoutPosition == SELECTED_ITEM) {
                with(itemView) {
                    title.setColor(R.color.redd)
                    address.setColor(R.color.redd)
                    iv_checked.setImageResource(R.drawable.ic_checked)
                    iv_checked.visibility = View.VISIBLE
                }
            } else {
                with(itemView) {
                    title.setColor(R.color.black)
                    address.setColor(R.color.black)
                    iv_checked.visibility = View.GONE
                }
            }

            itemView.setOnClickListener { itemClick.invoke(item, layoutPosition) }
        }
    }

    companion object {
        var SELECTED_ITEM: Int? = -1
    }
}

private class ArenaDiffCallback : DiffUtil.ItemCallback<Arena>() {
    override fun areItemsTheSame(oldItem: Arena, newItem: Arena): Boolean =
            oldItem.name == newItem.metro

    override fun areContentsTheSame(oldItem: Arena, newItem: Arena): Boolean =
            oldItem.metro == newItem.metro &&
                    oldItem.name == newItem.name
}
