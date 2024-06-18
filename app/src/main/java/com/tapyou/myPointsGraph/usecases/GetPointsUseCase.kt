package com.tapyou.myPointsGraph.usecases

import com.tapyou.myPointsGraph.entities.Point
import com.tapyou.myPointsGraph.repositories.PointsRepository
import javax.inject.Inject

class GetPointsUseCase @Inject constructor(
    private val repository: PointsRepository
) {
    suspend operator fun invoke(count: Int): List<Point> {
        return repository.getPoints(count)
    }
}