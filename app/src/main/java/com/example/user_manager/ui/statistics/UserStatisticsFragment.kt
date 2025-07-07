package com.example.user_manager.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.user_manager.R
import com.example.user_manager.databinding.FragmentUserStatisticsBinding
import com.example.user_manager.ui.base.BaseFragment
import com.example.user_manager.ui.base.BaseViewModel
import com.example.user_manager.ui.base.ViewModelFactory
import com.example.user_manager.utils.ChartUtils.setupDefaultSettings
import com.example.user_manager.utils.ChartUtils.createBarDataSet
import com.example.user_manager.utils.ChartUtils.createPieDataSet
import com.example.user_manager.utils.hide
import com.example.user_manager.utils.show
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieEntry

class UserStatisticsFragment : BaseFragment() {
    private var _binding: FragmentUserStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserStatisticsViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharts()
        setupObservers()
        setupRefresh()
    }

    private fun setupCharts() {
        binding.chartGender.apply {
            setupDefaultSettings(requireContext())
            centerText = getString(R.string.stats_gender_distribution)
        }

        binding.chartAge.setupDefaultSettings()
    }

    private fun setupRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshStatistics()
        }
    }

    private fun setupObservers() {
        viewModel.statistics.observe(viewLifecycleOwner) { stats ->
            updateGenderChart(stats.genderDistribution)
            updateAgeChart(stats.ageDistribution)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateGenderChart(genderData: Map<String, Float>) {
        val entries = genderData.map { (gender, percentage) ->
            PieEntry(percentage, gender.capitalize())
        }

        val dataSet = createPieDataSet(entries, getString(R.string.stats_gender_distribution))
        binding.chartGender.apply {
            data = PieData(dataSet)
            invalidate()
        }
    }

    private fun updateAgeChart(ageData: Map<String, Float>) {
        val entries = ageData.entries.mapIndexed { index, (range, percentage) ->
            BarEntry(index.toFloat(), percentage)
        }

        val dataSet = createBarDataSet(entries, getString(R.string.stats_age_distribution))
        binding.chartAge.apply {
            xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return ageData.keys.elementAtOrNull(value.toInt()) ?: ""
                }
            }
            data = com.github.mikephil.charting.data.BarData(dataSet).apply {
                barWidth = 0.9f
            }
            invalidate()
        }
    }

    override fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.chartGender.hide()
            binding.chartAge.hide()
        } else {
            binding.chartGender.show()
            binding.chartAge.show()
        }
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
