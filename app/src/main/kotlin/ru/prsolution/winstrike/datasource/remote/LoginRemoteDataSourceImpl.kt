package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.LoginRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.login.*
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.networking.UserApi

class LoginRemoteDataSourceImpl constructor(
    private val api: UserApi
) : LoginRemoteDataSource, BaseRepository() {

    override suspend fun get(loginModel: LoginEntity): Resource<AuthResponse>? {
        val response = safeApiCall(
            call = { api.getLoginAsync(loginModel).await() },
            errorMessage = "Error Fetching Cities List"
        )
        return response?.mapToDomain()
    }

    override suspend fun getUser(newUserModel: NewUserEntity): Resource<AuthResponse>? {
        val response = safeApiCall(
            call = { api.getUserAsync(newUserModel).await() },
            errorMessage = "Error Fetching Cities List"
        )
        return response?.mapToDomain()
    }

    override suspend fun sendSms(smsEntity: SmsEntity): Resource<MessageResponse>? {
        return safeApiCall(
            call = { api.sendSmsAsync(smsEntity).await() },
            errorMessage = "Error Fetching Cities List"
        )
    }

    override suspend fun confirm(smsCode: String, smsEntity: SmsEntity): Resource<MessageResponse>? {
        return safeApiCall(
            call = { api.confirmUserAsync(smsCode, smsEntity).await() },
            errorMessage = "Error Fetching Cities List"
        )
    }

    override suspend fun updateInfo(publicId: String, profile: ProfileEntity): Resource<MessageResponse>? {
        return safeApiCall(
            call = { api.updateUserAsync(public_id = publicId, profileEntity = profile).await() },
            errorMessage = "Error Fetching Cities List"
        )
    }

    override suspend fun changePassword(
        confirmCode: String,
        passwordEntity: PasswordEntity
    ): Resource<MessageResponse>? {
        return safeApiCall(
            call = { api.changePasswordAsync(confirmCode = confirmCode, passwordEntity = passwordEntity).await() },
            errorMessage = "Error Fetching Cities List"
        )
    }

}
