package ru.prsolution.winstrike.domain.models.arena

class ArenaMap(schema: ArenaSchema?) {

    var name: String? = schema?.name
    var labels: MutableList<Label> = mutableListOf()
    var seats: MutableList<SeatMap> = mutableListOf()
    var arenaSchema: ArenaSchemaName =
        ArenaSchemaName.WINSTRIKE

    init {
        when {
            this.name?.contains("Winstrike ArenaStorage 1")!! -> arenaSchema =
                ArenaSchemaName.WINSTRIKE
            this.name?.contains("Schema 2")!! -> arenaSchema =
                ArenaSchemaName.CORNER
            this.name?.contains("Серпухов")!! -> arenaSchema =
                ArenaSchemaName.SERPUCHOV
        }
        val seats = schema?.seats
        if (seats != null) {
            for (seat in seats) {

                val computer = seat.computer
                val coors = seat.coors
                var type = if ((coors?.type) == 1 && seat.status != "hidden") "vip" else seat.status
                if (type == "vip") {
                    if (seat.status == "booking" || seat.status == "self_booking") {
                        type = seat.status
                    }
                }

                this.seats.add(
                    SeatMap(
                        coors, seat.publicId, type, computer?.name,
                        computer?.active
                    )
                )
            }
        }

        val labels = schema?.labels
        if (labels != null) {
            for (label in labels) {
                val text = label.text
                val dx = label.x
                val dy = label.y

                this.labels.add(Label(text, dx, dy))
            }
        }
    }
}

class Label(
    val text: String? = null,
    val x: Int? = null,
    val y: Int? = null
)

enum class ArenaSchemaName {
    WINSTRIKE, CORNER, SERPUCHOV
}
