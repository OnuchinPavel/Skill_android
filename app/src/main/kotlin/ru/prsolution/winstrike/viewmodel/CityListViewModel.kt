package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.usecases.CityUseCase
import ru.prsolution.winstrike.presentation.model.arena.CityItem
import ru.prsolution.winstrike.presentation.model.arena.mapToPresentation
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class CityListViewModel constructor(val cityUseCase: CityUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val cityList = SingleLiveEvent<Resource<List<CityItem>>>()

    fun fetchCities() {
        scope.launch {
            val cities = cityUseCase.get(refresh = true)
            cityList.postValue(cities?.mapToPresentation())
        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}