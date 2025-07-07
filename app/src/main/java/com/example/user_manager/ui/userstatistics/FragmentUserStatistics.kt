package com.example.user_manager.ui.userstatistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.user_manager.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch

class FragmentUserStatistics : Fragment() {
    private val viewModel: UserStatisticsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.loadStatistics()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.statistics.collect { stats ->
                updateStatisticsUI(stats)
            }
        }
    }

    private fun updateStatisticsUI(stats: UserStatistics) {
        view?.apply {
            findViewById<TextView>(R.id.tvTotalUsers).text = stats.totalUsers.toString()
            findViewById<TextView>(R.id.tvMaleUsers).text = stats.maleUsers.toString()
            findViewById<TextView>(R.id.tvFemaleUsers).text = stats.femaleUsers.toString()
            findViewById<TextView>(R.id.tvAverageAge).text = String.format("%.1f", stats.averageAge)

            setupGenderChart(stats)
        }
    }

    private fun setupGenderChart(stats: UserStatistics) {
        val pieChart = view?.findViewById<PieChart>(R.id.pieChartGender)

        val entries = arrayListOf(
            PieEntry(stats.maleUsers.toFloat(), "Male"),
            PieEntry(stats.femaleUsers.toFloat(), "Female")
        )

        val dataSet = PieDataSet(entries, "Gender Distribution")
        dataSet.colors = listOf(Color.BLUE, Color.MAGENTA)

        val pieData = PieData(dataSet)
        pieChart?.data = pieData
        pieChart?.invalidate()
    }
}