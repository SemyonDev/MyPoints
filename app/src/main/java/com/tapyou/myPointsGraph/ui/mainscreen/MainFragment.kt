package com.tapyou.myPointsGraph.ui.mainscreen

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.tapyou.myPointsGraph.R
import com.tapyou.myPointsGraph.ui.base.BaseFragment
import com.tapyou.myPointsGraph.databinding.FragmentMainBinding
import com.tapyou.myPointsGraph.entities.Point
import com.tapyou.myPointsGraph.ui.mainscreen.adapters.PointsAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {
    private val viewModel: PointsViewModel by viewModels()

    private val adapter = PointsAdapter()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            saveChart()
        } else {
            binding.textInfo.text = getString(R.string.permission_denied_to_save_chart)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(layoutInflater, container, false)


    override fun init() {
        initPointsRecyclerView()
        initButtonsActions()
        initChart()
        viewModel.errorMessage.observe(this) { message ->
            adapter.submitList(listOf())
            updateChart(listOf())
            binding.textInfo.text = message
        }
    }

    private fun initPointsRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter

        viewModel.points.observe(this) { points ->
            adapter.submitList(listOf())
            updateChart(points)
            adapter.submitList(points)
            binding.textInfo.text = getString(R.string.number_counts, points.size)
        }
    }

    private fun initButtonsActions() {
        binding.buttonGo.setOnClickListener {
            val count = binding.editPointsCount.text.toString().toIntOrNull()
            if (count != null) {
                viewModel.fetchPoints(count)
            } else {
                binding.textInfo.text = getString(R.string.enter_right_number)
            }
        }

        binding.buttonSaveChart.setOnClickListener {
            context?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            it,
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        saveChart()
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            it,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            1
                        )
                    } else {
                        saveChart()
                    }
                }

            }
        }
    }

    private fun initChart() {
        binding.lineChart.apply {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.isEnabled = false
            axisLeft.setDrawLabels(true)
            axisLeft.setLabelCount(6, true)
            axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            axisLeft.spaceTop = 15f
            description.isEnabled = false
            setBackgroundColor(Color.WHITE)
            setTouchEnabled(true)
            setDrawGridBackground(false)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setExtraLeftOffset(15f)
            setExtraRightOffset(15f)
        }
    }

    private fun updateChart(points: List<Point>) {
        if (points.isNotEmpty()) {
            val entries = points.map { Entry(it.x, it.y) }
            val sortedEntries = entries.sortedBy { it.x }
            val dataSet = LineDataSet(sortedEntries, getString(R.string.points)).apply {
                color = Color.BLUE
                lineWidth = 2f
                setCircleColor(Color.BLUE)
                circleHoleRadius = 4f
                setDrawValues(true)
                valueTextColor = Color.RED
                valueTextSize = 12f
                valueFormatter = object : ValueFormatter() {
                    override fun getPointLabel(entry: Entry?): String {
                        return "x:${entry?.x}"
                    }
                }
            }
            val lineData = LineData(dataSet)
            binding.lineChart.data = lineData
            binding.lineChart.invalidate()
        } else {
            binding.lineChart.data = LineData()
            binding.lineChart.invalidate()
        }
    }

    private fun saveChart() {
        val filePath = viewModel.saveChart(binding.lineChart)
        binding.textInfo.text = getString(R.string.save_graph, filePath)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    saveChart()
                } else {
                    binding.textInfo.text = getString(R.string.permission_denied_to_save_chart)
                }
                return
            }
        }
    }
}