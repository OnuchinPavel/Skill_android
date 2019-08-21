package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.usecases.ArenaUseCase
import ru.prsolution.winstrike.presentation.model.arena.ArenaItem
import ru.prsolution.winstrike.presentation.model.arena.mapToPresentation
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class CityItemViewModel constructor(val arenaUseCase: ArenaUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val arenaList = SingleLiveEvent<Resource<List<ArenaItem>>>()

    // Получение списка имеющихся арен на сервере
    fun fetchArenaList() {
        scope.launch {
            val response = arenaUseCase.get(refresh = true)
            arenaList.postValue(response?.mapToPresentation())
        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}