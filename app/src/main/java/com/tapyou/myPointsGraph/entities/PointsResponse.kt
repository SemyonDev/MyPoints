package com.tapyou.myPointsGraph.entities

import com.google.gson.annotations.SerializedName

data class PointsResponse(
    @SerializedName("points")
    val points: List<Point>
)
