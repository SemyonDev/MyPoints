package com.tapyou.myPointsGraph.networking

import com.tapyou.myPointsGraph.entities.Point
import com.tapyou.myPointsGraph.entities.PointsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("api/test/points")
    suspend fun getPoints(@Query("count") count: Int): PointsResponse
}