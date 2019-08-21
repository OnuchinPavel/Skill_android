package ru.prsolution.winstrike.presentation.cities

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_city_detail.view.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.model.arena.ArenaItem
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class ArenaListAdapter constructor(private val itemClick: (ArenaItem) -> Unit) :
        ListAdapter<ArenaItem, ArenaListAdapter.ViewHolder>(ArenaItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArenaListAdapter.ViewHolder =
            ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_city_detail)) {

        fun bind(item: ArenaItem) {
            itemView.city_tv.text = item.name
            itemView.metro_tv.text = item.trsMetro
            itemView.setOnClickListener { itemClick.invoke(item) }

            when {
               !PrefUtils.arenaPid?.isEmpty()!! && item.publicId?.contains(PrefUtils.arenaPid.toString())!! -> with(itemView) {
                    city_tv.setTextColor(resources.getColor(R.color.white))
                    metro_tv.setTextColor(resources.getColor(R.color.white))
                    card_v.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                    check_box_iv.setImageResource(R.drawable.ic_check_white)
                }
                else -> with(itemView) {
                    city_tv.setTextColor(resources.getColor(R.color.white))
                    metro_tv.setTextColor(resources.getColor(R.color.white))
                    card_v.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                    check_box_iv.setImageResource(R.drawable.ic_check_white)
                }
            }
        }
    }

}

private class ArenaItemDiffCallback : DiffUtil.ItemCallback<ArenaItem>() {
    override fun areItemsTheSame(oldItem: ArenaItem, newItem: ArenaItem): Boolean =
            oldItem.publicId == newItem.publicId

    override fun areContentsTheSame(oldItem: ArenaItem, newItem: ArenaItem): Boolean =
            oldItem.name == newItem.name
}