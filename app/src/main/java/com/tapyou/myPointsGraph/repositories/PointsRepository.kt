package com.tapyou.myPointsGraph.repositories

import com.tapyou.myPointsGraph.entities.Point

interface PointsRepository {
    suspend fun getPoints(count: Int): List<Point>
}