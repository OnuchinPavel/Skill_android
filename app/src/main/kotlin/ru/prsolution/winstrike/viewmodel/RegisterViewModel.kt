package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.usecases.LoginUseCase
import ru.prsolution.winstrike.presentation.model.login.LoginInfo
import ru.prsolution.winstrike.presentation.model.login.NewUserInfo
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class RegisterViewModel constructor(val loginUseCase: LoginUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)


    val authResponse = SingleLiveEvent<Resource<AuthResponse>>()


    fun getUser(newUserModel: NewUserInfo) {
        scope.launch {
            val response = loginUseCase.getUser(newUserModel)
            authResponse.postValue(response)
        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}