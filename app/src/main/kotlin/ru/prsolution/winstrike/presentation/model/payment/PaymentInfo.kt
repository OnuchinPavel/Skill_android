package ru.prsolution.winstrike.presentation.model.payment

import ru.prsolution.winstrike.domain.models.payment.PaymentModel

/**
 * Created by oleg on 07/03/2018.
 */


class PaymentInfo(
    val startAt: String?,
    val endAt: String?,
    val placesPid: List<String>?,
    val device_id: String?
) {

    override fun toString(): String {
        return "start_at: " + startAt + "end_at: " + endAt + " Places: " + placesPid!!.toString() + "device_id" + device_id
    }

}

fun PaymentInfo.mapToDomain(): PaymentModel = PaymentModel(
    start_at = this.startAt,
    end_at = this.endAt,
    places = this.placesPid,
    device_id = this.device_id
)
