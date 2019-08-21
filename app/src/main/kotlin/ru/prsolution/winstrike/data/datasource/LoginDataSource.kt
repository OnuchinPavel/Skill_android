package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.login.*
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.*

interface LoginCacheDataSource {

    fun get(): Single<UserModel>

    fun set(user: UserModel?): Single<UserModel>

}

interface LoginRemoteDataSource {

    suspend fun get(loginModel: LoginEntity): Resource<AuthResponse>?

    suspend fun getUser(newUserModel: NewUserEntity): Resource<AuthResponse>?

    suspend fun sendSms(smsEntity: SmsEntity): Resource<MessageResponse>?

    suspend fun confirm(smsCode: String, smsEntity: SmsEntity): Resource<MessageResponse>?

    suspend fun updateInfo(publicId: String, profile: ProfileEntity): Resource<MessageResponse>?

    suspend fun changePassword(confirmCode: String, passwordEntity: PasswordEntity): Resource<MessageResponse>?

}
