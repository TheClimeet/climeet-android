package com.climus.climeet.data.model.request

data class UpdateGymRouteVersionRequest(
    val timePoint: String,
    val existingData: ExistingData,
    val newData: NewData
) {
    data class ExistingData(
        val difficulty: List<String>,
        val layout: List<Int>,
        val sector: List<Int>,
        val route: List<Int>
    )

    data class NewData(
        val difficulty: List<DifficultyRequestItem>,
        val layout: List<LayoutRequestItem>,
        val sector: List<SectorRequestItem>,
        val route: List<RouteRequestItem>
    ) {
        data class DifficultyRequestItem(
            val gymDifficultyName: String,
            val climeetDifficultyName: String
        )

        data class LayoutRequestItem(
            val floor: Int,
            val imgUrl: String
        )

        data class SectorRequestItem(
            val name: String,
            val floor: Int,
            val imgUrl: String
        )

        data class RouteRequestItem(
            val sectorName: String,
            val gymDifficultyName: String,
            val holdColor: String,
            val imgUrl: String
        )
    }
}
