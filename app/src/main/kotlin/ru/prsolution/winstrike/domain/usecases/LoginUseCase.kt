package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.repository.LoginRepository
import ru.prsolution.winstrike.presentation.model.login.*

class LoginUseCase constructor(private val loginRepository: LoginRepository) {

    suspend fun get(loginModel: LoginInfo): Resource<AuthResponse>? =
        loginRepository.get(loginModel.mapToDomain())

    suspend fun getUser(newUserModel: NewUserInfo): Resource<AuthResponse>? =
        loginRepository.getUser(newUserModel.mapToDomain())

    suspend fun sendSms(smsInfo: SmsInfo): Resource<MessageResponse>? =
        loginRepository.sendSms(smsInfo.mapToDomain())

    suspend fun confirm(smsCode: String, smsInfo: SmsInfo): Resource<MessageResponse>? =
        loginRepository.confirm(smsCode, smsInfo.mapToDomain())

    suspend fun update(publicId: String, profileInfo: ProfileInfo): Resource<MessageResponse>? =
        loginRepository.updateInfo(publicId, profileInfo.mapToDomain())

    suspend fun changePassword(confirmCode: String, password: Password): Resource<MessageResponse>? =
        loginRepository.changePassword(confirmCode, password.mapToDomain())
}
