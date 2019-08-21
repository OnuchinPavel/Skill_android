package ru.prsolution.winstrike.data.datasource

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.MessageResponse

interface AppRemoteDataSource {

   suspend fun get(versionName: String): Resource<ru.prsolution.winstrike.domain.models.common.MessageResponse>?

}
