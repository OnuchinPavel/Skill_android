package ru.prsolution.winstrike.data.repository

import ru.prsolution.winstrike.data.datasource.LoginCacheDataSource
import ru.prsolution.winstrike.data.datasource.LoginRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.*
import ru.prsolution.winstrike.domain.repository.LoginRepository

class LoginRepositoryImpl constructor(
    private val cacheDataSource: LoginCacheDataSource,
    private val remoteDataSource: LoginRemoteDataSource
) : LoginRepository {

    override suspend fun get(loginModel: LoginModel): Resource<AuthResponse>? =
        remoteDataSource.get(loginModel.mapToDataSource())

    override suspend fun getUser(newUserModel: NewUserModel): Resource<AuthResponse>? =
        remoteDataSource.getUser(newUserModel.mapToDataSource())

    override suspend fun sendSms(smsModel: SmsModel): Resource<MessageResponse>? =
        remoteDataSource.sendSms(smsModel.mapToDataSource())

    override suspend fun confirm(smsCode: String, smsModel: SmsModel): Resource<MessageResponse>? =
        remoteDataSource.confirm(smsCode, smsModel.mapToDataSource())

    override suspend fun updateInfo(publicId: String, profileModel: ProfileModel): Resource<MessageResponse>? =
        remoteDataSource.updateInfo(publicId, profileModel.mapToDataSource())

    override suspend fun changePassword(confirmCode: String, passwordModel: PasswordModel): Resource<MessageResponse>? =
        remoteDataSource.changePassword(confirmCode, passwordModel.mapToDataSource())
}

