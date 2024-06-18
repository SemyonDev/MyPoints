package com.tapyou.myPointsGraph.repositories

import com.tapyou.myPointsGraph.entities.Point
import com.tapyou.myPointsGraph.networking.ApiService
import com.tapyou.myPointsGraph.repositories.PointsRepository
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PointsRepository {
    override suspend fun getPoints(count: Int): List<Point> {
        return apiService.getPoints(count).points
    }
}