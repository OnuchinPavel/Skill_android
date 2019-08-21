package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.usecases.AppUseCase
import ru.prsolution.winstrike.presentation.model.arena.mapToPresentation
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class AppViewModel constructor(val appUseCase: AppUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val messageResponse = SingleLiveEvent<Resource<MessageResponse>>()

    // Check new app version
    fun checkVersion(versionName: String) {
            scope.launch {
                val response = appUseCase.get(versionName)
                messageResponse.postValue(response)
            }

    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}