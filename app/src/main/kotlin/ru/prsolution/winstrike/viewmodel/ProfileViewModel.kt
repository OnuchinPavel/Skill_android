package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.usecases.LoginUseCase
import ru.prsolution.winstrike.presentation.model.login.Password
import ru.prsolution.winstrike.presentation.model.login.ProfileInfo
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext
class ProfileViewModel constructor(val loginUseCase: LoginUseCase) : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)


    val messageResponse = SingleLiveEvent<Resource<MessageResponse>>()


    fun updateUserProfile(publicId: String, profileInfo: ProfileInfo) {
        scope.launch {
            val response = loginUseCase.update(publicId, profileInfo)
            messageResponse.postValue(response)
        }
    }


    fun changePassword(confirmCode: String, password: Password) {
        scope.launch {
            val response = loginUseCase.changePassword(confirmCode, password)
            messageResponse.postValue(response)
        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}