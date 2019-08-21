package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse

interface AppRepository {

   suspend fun get(versionName: String): Resource<MessageResponse>?
}
