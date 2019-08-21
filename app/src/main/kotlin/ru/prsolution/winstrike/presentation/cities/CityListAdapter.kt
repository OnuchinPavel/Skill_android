package ru.prsolution.winstrike.presentation.cities

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_city.view.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.model.arena.CityItem
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class CityListAdapter constructor(private val itemClick: (CityItem) -> Unit) :
        ListAdapter<CityItem, CityListAdapter.ViewHolder>(CityItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListAdapter.ViewHolder =
            ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_city)) {

        fun bind(item: CityItem) {
            itemView.name_tv.text = item.name

            when {
                !PrefUtils.cityPid?.isEmpty()!! &&  item.id.contains(PrefUtils.cityPid.toString()) -> with(itemView) {
                    name_tv.setTextColor(resources.getColor(R.color.white))
                    checkbox_iv.setImageResource(R.drawable.ic_check_white)
                }
                else -> with(itemView) {
                    card_view.setCardBackgroundColor(resources.getColor(R.color.white))
                    name_tv.setTextColor(resources.getColor(R.color.white))
                    checkbox_iv.setImageResource(R.drawable.ic_check_white)
                }
            }


            itemView.setOnClickListener { itemClick.invoke(item) }
        }
    }

}


private class CityItemDiffCallback : DiffUtil.ItemCallback<CityItem>() {
    override fun areItemsTheSame(oldItem: CityItem, newItem: CityItem): Boolean =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CityItem, newItem: CityItem): Boolean =
            oldItem.name == newItem.name
}