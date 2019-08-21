package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.orders.mapToPresentation
import ru.prsolution.winstrike.domain.usecases.ArenaUseCase
import ru.prsolution.winstrike.presentation.model.login.NewUserInfo
import ru.prsolution.winstrike.presentation.model.orders.Order
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class OrderViewModel constructor(val arenaUseCase: ArenaUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)


    val orders = SingleLiveEvent<Resource<List<Order>>>()


    fun getOrders() {
        scope.launch {
            val response = arenaUseCase.getOrders()
            orders.postValue(response?.mapToPresentation())
        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}