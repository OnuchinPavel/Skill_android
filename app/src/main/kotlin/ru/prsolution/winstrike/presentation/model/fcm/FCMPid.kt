package ru.prsolution.winstrike.presentation.model.fcm

import ru.prsolution.winstrike.domain.models.common.FCMModel

/**
 * Created by oleg on 07/03/2018.
 */

class FCMPid(val token: String)

fun FCMPid.mapToDomain(): FCMModel = FCMModel(
    token = this.token
)
