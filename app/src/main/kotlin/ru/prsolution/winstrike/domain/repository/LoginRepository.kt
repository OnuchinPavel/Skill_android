package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.*

interface LoginRepository {

    suspend fun get(loginModel: LoginModel): Resource<AuthResponse>?

    suspend fun getUser(newUserModel: NewUserModel): Resource<AuthResponse>?

    suspend fun sendSms(smsModel: SmsModel): Resource<MessageResponse>?

    suspend fun confirm(smsCode: String, smsModel: SmsModel): Resource<MessageResponse>?

    suspend fun updateInfo(publicId: String, profileModel: ProfileModel): Resource<MessageResponse>?

    suspend fun changePassword(confirmCode: String, passwordModel: PasswordModel): Resource<MessageResponse>?

}
