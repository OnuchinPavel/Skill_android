package ru.prsolution.winstrike.presentation.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fmt_paid.*
import kotlinx.android.synthetic.main.item_city.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.presentation.model.orders.Order
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.visible
import ru.prsolution.winstrike.viewmodel.OrderViewModel
import timber.log.Timber

class OrderFragment : Fragment() {

    private val mVm: OrderViewModel by viewModel()


    private val itemClick: (Order) -> Unit =
        { order ->
            Timber.tag("$$$").d("Selected order: ${order.place.name}")
        }

    private val adapter = OrderListAdapter(itemClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return context?.inflate(R.layout.fmt_paid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        order_rv.adapter = adapter


        if (savedInstanceState == null) {
            mVm.getOrders()
        }
        mVm.orders.observe(this@OrderFragment, Observer {
            if (it.state == ResourceState.SUCCESS){
                it.data?.let {
                    if (!it.isEmpty()) {
                        updateOrders(it)
                    } else {
                        cv_nopay.visible()
                    }
                }
            } else {
                it.message?.let {
                    cv_nopay.visible()
                }
            }
        })

    }


    private fun updateOrders(orders: List<Order>) {
        adapter.submitList(orders)
    }


    private fun onFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("403") ||
                    appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            (appErrorMessage.contains("409")) -> longToast("Не верный код.")
            appErrorMessage.contains("502") -> longToast("Ошибка сервера")
            appErrorMessage.contains("401") -> longToast(getString(R.string.message_error_auth))
            (appErrorMessage.contains("413")) -> longToast("Не верный формат данных")
            appErrorMessage.contains("No Internet Connection!") ->
                longToast("Интернет подключение не доступно!")
        }

    }


}
