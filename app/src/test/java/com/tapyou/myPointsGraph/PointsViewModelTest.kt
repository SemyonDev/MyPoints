package com.tapyou.myPointsGraph

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tapyou.myPointsGraph.entities.Point
import com.tapyou.myPointsGraph.entities.PointsResponse
import com.tapyou.myPointsGraph.repositories.PointsRepository
import com.tapyou.myPointsGraph.ui.helpers.ChartSaver
import com.tapyou.myPointsGraph.ui.mainscreen.PointsViewModel
import com.tapyou.myPointsGraph.usecases.GetPointsUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class PointsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var pointsRepository: PointsRepository

    @Mock
    private lateinit var getPointsUseCase: GetPointsUseCase

    @Mock
    private lateinit var chatSaver: ChartSaver

    @Mock
    private lateinit var pointsObserver: Observer<List<Point>>

    @Mock
    private lateinit var errorObserver: Observer<String>

    private lateinit var viewModel: PointsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = PointsViewModel(getPointsUseCase, chatSaver)
        viewModel.points.observeForever(pointsObserver)
        viewModel.errorMessage.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetchPoints should return expected points`() = runBlocking {
        val actualPoints = listOf(Point(1.0f, 2.0f), Point(3.0f, 4.0f))
        `when`(pointsRepository.getPoints(2)).thenReturn(PointsResponse(actualPoints).points)
        viewModel.fetchPoints(2)
        val expectedPoints = listOf(Point(1.0f, 2.0f), Point(3.0f, 4.0f))
        val resultResponse = pointsRepository.getPoints(2)
        assertEquals(expectedPoints, resultResponse)

    }

    @Test
    fun `fetchPoints should return empty points`() = runBlocking {
        val actualPoints = listOf<Point>()
        `when`(pointsRepository.getPoints(2)).thenReturn(PointsResponse(actualPoints).points)
        viewModel.fetchPoints(2)
        val resultResponse = pointsRepository.getPoints(2)
        assertEquals(listOf<Point>(), resultResponse)
    }

    @Test
    fun `fetchPoints should failure`() = runBlocking {
        val errorMessage = "problem with get points"
        `when`(getPointsUseCase(2)).thenThrow(RuntimeException(errorMessage))
        viewModel.fetchPoints(2)
        val actualMessage = viewModel.errorMessage.value.toString()

        assertEquals(actualMessage, "Error: ${errorMessage}")
    }
}
