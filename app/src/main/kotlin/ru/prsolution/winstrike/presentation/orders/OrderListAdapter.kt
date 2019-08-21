package ru.prsolution.winstrike.presentation.orders

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_paid.view.*
import ru.prsolution.winstrike.presentation.model.orders.Order
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class OrderListAdapter constructor(private val itemClick: (Order) -> Unit) :
    ListAdapter<Order, OrderListAdapter.ViewHolder>(OrderDiffCallback()) {

    val formatDate =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00")
    val dateFormat = SimpleDateFormat("dd MMMM yyyy")
    val timeFormat = SimpleDateFormat("HH:mm")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListAdapter.ViewHolder =
        ViewHolder(parent)


    inner class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(ru.prsolution.winstrike.R.layout.item_paid)) {

        fun bind(item: Order) {

            var date = formatDate.parse(item.startAt)
            val dateStarted = dateFormat.format(date)
            val timeStart = timeFormat.format(date)
             date = formatDate.parse(item.endAt)
            val timeEnd = timeFormat.format(date)


            itemView.club_name.text = "Компьютерный клуб: «${PrefUtils.arenaName}»"
            itemView.date.text = dateStarted
            itemView.time.text = "$timeStart - $timeEnd"
            itemView.compute_name.text = item.place.name
            itemView.code.text = item.accessCode
            itemView.cost.text ="${item.cost} р."

            itemView.setOnClickListener { itemClick.invoke(item) }
        }
    }

}


private class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
        oldItem.accessCode == newItem.accessCode

    override fun areContentsTheSame(old: Order, new: Order): Boolean =
        old.placePid == new.placePid
}