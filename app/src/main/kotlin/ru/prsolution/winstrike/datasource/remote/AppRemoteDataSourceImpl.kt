package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.AppRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.networking.AppApi

class AppRemoteDataSourceImpl constructor(
    private val api: AppApi
) : AppRemoteDataSource,  BaseRepository() {

    override suspend fun get(versionName: String): Resource<MessageResponse>? {
        val messageResponse = safeApiCall(
                call = { api.getVersion(versionName).await() },
                errorMessage = "Error Fetching Cities List"
        )

        return messageResponse
    }

}
