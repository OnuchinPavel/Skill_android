package ru.prsolution.winstrike.data.repository

import ru.prsolution.winstrike.data.datasource.AppRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.repository.AppRepository

class AppRepositoryImpl constructor(
        private val remoteDataSource: AppRemoteDataSource
) : AppRepository {


    override suspend fun get(versionName: String): Resource<MessageResponse>? =
            remoteDataSource.get(versionName)
}

