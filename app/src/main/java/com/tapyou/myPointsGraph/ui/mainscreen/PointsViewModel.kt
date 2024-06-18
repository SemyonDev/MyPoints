package com.tapyou.myPointsGraph.ui.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.LineChart
import com.tapyou.myPointsGraph.entities.Point
import com.tapyou.myPointsGraph.ui.helpers.ChartSaver
import com.tapyou.myPointsGraph.usecases.GetPointsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointsViewModel @Inject constructor(
    private val getPointsUseCase: GetPointsUseCase,
    private val chartSaver: ChartSaver
) : ViewModel() {

    private val _points = MutableLiveData<List<Point>>()
    val points: LiveData<List<Point>> = _points

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchPoints(count: Int) {
        viewModelScope.launch {
            try {
                val pointsList = getPointsUseCase(count)
                _points.postValue(pointsList)
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }

    fun saveChart(lineChart: LineChart): String {
        return chartSaver.saveChart(lineChart).toString()
    }
}
