package ru.prsolution.winstrike.domain.models.common

import ru.prsolution.winstrike.datasource.model.fcm.FCMEntity
import ru.prsolution.winstrike.presentation.model.fcm.FCMPid

/**
 * Created by oleg on 07/03/2018.
 */

class FCMModel(val token: String)

fun FCMModel.mapToDataSource(): FCMEntity = FCMEntity(
    token = this.token
)

fun FCMModel.mapToPresentation(): FCMPid = FCMPid(
    token = this.token
)
