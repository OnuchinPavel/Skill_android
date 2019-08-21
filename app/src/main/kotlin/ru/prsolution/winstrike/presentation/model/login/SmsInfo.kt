package ru.prsolution.winstrike.presentation.model.login

import ru.prsolution.winstrike.domain.models.login.SmsModel

/**
 * Created by oleg on 07/03/2018.
 */

class SmsInfo(
    val phone: String?
)

fun SmsInfo.mapToDomain(): SmsModel = SmsModel(
    phone = this.phone
)
