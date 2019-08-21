package ru.prsolution.winstrike.datasource.model.payment

/**
 * Created by oleg on 07/03/2018.
 */

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.payment.PaymentModel

class PaymentEntity(
    @field:Json(name = "start_at")
    val startAt: String? = null,
    @field:Json(name = "end_at")
    val end_at: String? = null,
    @field:Json(name = "places")
    val placesPid: List<String>? = null,
    @field:Json(name = "device_id")
    val deviceId: String? = null
)

fun PaymentEntity.mapToDomain(): PaymentModel = PaymentModel(
    start_at = this.startAt,
    end_at = this.end_at,
    places = this.placesPid,
    device_id = this.deviceId

)


