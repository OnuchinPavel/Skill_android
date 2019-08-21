package ru.prsolution.winstrike.datasource.model.fcm

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.common.FCMModel

/**
 * Created by oleg on 07/03/2018.
 */

class FCMEntity(
    @field:Json(name = "fcm_pid")
    val token: String)


fun FCMEntity.mapToDomain(): FCMModel = FCMModel(
    token = this.token
)