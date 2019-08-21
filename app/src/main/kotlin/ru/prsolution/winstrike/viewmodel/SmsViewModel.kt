package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.usecases.LoginUseCase
import ru.prsolution.winstrike.presentation.model.login.SmsInfo
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class SmsViewModel constructor(val loginUseCase: LoginUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)


    val messageResponse = SingleLiveEvent<Resource<MessageResponse>>()


    // Отправка код подтверждения (повторно, если пользователь не получил первый код сразу после регистрации)
    fun send(smsInfo: SmsInfo) {
        scope.launch {
            val response = loginUseCase.sendSms(smsInfo)
            messageResponse.postValue(response)
        }
    }

    // Подтверждение пользователя кодом из СМС
    fun confirm(smsCode: String, smsInfo: SmsInfo) {
        scope.launch {
            val response = loginUseCase.confirm(smsCode, smsInfo)
            messageResponse.postValue(response)
        }
    }



    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}