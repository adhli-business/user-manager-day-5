package com.example.user_manager.utils

import android.content.Context
import android.graphics.Color
import com.example.user_manager.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

object ChartUtils {
    fun PieChart.setupDefaultSettings(context: Context) {
        description.isEnabled = false
        setUsePercentValues(true)
        setExtraOffsets(5f, 10f, 5f, 5f)
        dragDecelerationFrictionCoef = 0.95f
        isDrawHoleEnabled = true
        setHoleColor(Color.WHITE)
        setTransparentCircleColor(Color.WHITE)
        setTransparentCircleAlpha(110)
        holeRadius = 58f
        transparentCircleRadius = 61f
        setDrawCenterText(true)
        rotationAngle = 0f
        isRotationEnabled = true
        animateY(1400)
        legend.isEnabled = true
    }

    fun BarChart.setupDefaultSettings() {
        description.isEnabled = false
        setDrawGridBackground(false)
        setDrawBarShadow(false)
        setDrawValueAboveBar(true)
        setPinchZoom(false)
        setScaleEnabled(false)

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(false)
        }

        axisLeft.apply {
            setDrawGridLines(true)
            spaceTop = 35f
            axisMinimum = 0f
        }

        axisRight.isEnabled = false
        legend.isEnabled = true
        animateY(1400)
    }

    fun createPieDataSet(entries: List<PieEntry>, label: String): PieDataSet {
        return PieDataSet(entries, label).apply {
            colors = Constants.CHART_COLORS.toList()
            valueFormatter = PercentFormatter()
            valueTextSize = 14f
            valueTextColor = Color.WHITE
        }
    }

    fun createBarDataSet(entries: List<BarEntry>, label: String): BarDataSet {
        return BarDataSet(entries, label).apply {
            colors = Constants.CHART_COLORS.toList()
            valueTextSize = 12f
            valueTextColor = Color.BLACK
        }
    }

    fun createPercentageFormatter(): ValueFormatter {
        return object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.1f%%", value * 100)
            }
        }
    }
}
