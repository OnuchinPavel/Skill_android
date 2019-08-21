package ru.prsolution.winstrike.domain.models.arena

data class SeatMap(
    val coors: Coors?,
    var pid: String?,
    var seatType: String?,
    var name: String?,
    var status: Boolean?
) {
    var id: String
    var dx: Double = 0.0
    var dy: Double = 0.0
    var numberDeltaX: Int? = 0
    var numberDeltaY: Int? = 0
    var angle: Double = 0.0
    var type: SeatType

    init {
        val id = coors?.id
        val x = coors?.x
        val y = coors?.y
        val xn = coors?.xn
        val yn = coors?.yn

        val angle = coors?.angle
        val type = SeatType[seatType]

        this.id = id.toString()
        this.dx = x!!.toDouble()
        this.dy = y!!.toDouble()
        this.numberDeltaX = xn
        this.numberDeltaY = yn
        this.angle = angle!!
        this.type = type!!
    }
}

enum class SeatType(val type: String) {
    FREE("free"),
    HIDDEN("hidden"),
    SELF_BOOKING("self_booking"),
    BOOKING("booking"),
    VIP("vip");

    companion object {
        // Lookup table
        private val lookup = HashMap<String, SeatType>()

        // Populate the lookup table on loading time
        init {
            for (status in SeatType.values()) {
                lookup[status.type] = status
            }
        }

        // This method can be used for reverse lookup purpose
        operator fun get(status: String?): SeatType? {
            return lookup[status]
        }
    }
}
