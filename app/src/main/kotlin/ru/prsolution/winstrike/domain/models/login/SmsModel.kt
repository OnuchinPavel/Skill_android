package ru.prsolution.winstrike.domain.models.login

import ru.prsolution.winstrike.datasource.model.login.LoginEntity
import ru.prsolution.winstrike.datasource.model.login.SmsEntity

/**
 * Created by oleg on 07/03/2018.
 */

class SmsModel(
    val phone: String?
)


fun SmsModel.mapToDataSource(): SmsEntity = SmsEntity(
    phone = this.phone
)
