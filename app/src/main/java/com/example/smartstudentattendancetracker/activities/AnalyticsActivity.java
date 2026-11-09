package com.example.smartstudentattendancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    private TextView txtAvg, txtHigh, txtLow;
    private LineChart lineChartTrend;
    private PieChart pieChartClass;
    private SQLiteHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        database = new SQLiteHelper(this);
        initViews();
        calculateMetrics();
        setupTrendChart();
        setupClassPieChart();
    }

    private void initViews() {
        txtAvg = findViewById(R.id.txtAvgMetric);
        txtHigh = findViewById(R.id.txtHighMetric);
        txtLow = findViewById(R.id.txtLowMetric);
        lineChartTrend = findViewById(R.id.lineChartTrend);
        pieChartClass = findViewById(R.id.pieChartClass);

        View toolbar = findViewById(R.id.toolbarAnalytics);
        if (toolbar != null) {
            toolbar.setOnClickListener(v -> finish());
        }
    }

    private void calculateMetrics() {
        int totalSum = 0;
        int count = 0;
        int highest = 0;
        int lowest = 100;

        Cursor c = database.getReadableDatabase().rawQuery("SELECT email FROM users WHERE role='STUDENT'", null);
        if (c != null && c.moveToFirst()) {
            do {
                int p = database.getOverallPercentage(c.getString(0));
                totalSum += p;
                if (p > highest) highest = p;
                if (p < lowest) lowest = p;
                count++;
            } while (c.moveToNext());
            c.close();
        }

        if (count > 0) {
            txtAvg.setText((totalSum / count) + "%");
            txtHigh.setText(highest + "%");
            txtLow.setText((lowest == 100 && totalSum == 0) ? "0%" : lowest + "%");
        } else {
            txtAvg.setText("0%");
            txtHigh.setText("0%");
            txtLow.setText("0%");
        }
    }

    private void setupTrendChart() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 55));
        entries.add(new Entry(2, 62));
        entries.add(new Entry(3, 70));
        entries.add(new Entry(4, 75));
        entries.add(new Entry(5, 82));

        LineDataSet set = new LineDataSet(entries, "Attendance Trend");
        set.setColor(getResources().getColor(R.color.primary_blue, getTheme()));
        set.setCircleColor(getResources().getColor(R.color.primary_blue, getTheme()));
        set.setLineWidth(3f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawFilled(true);
        set.setFillColor(getResources().getColor(R.color.soft_blue, getTheme()));
        set.setDrawValues(false);

        LineData data = new LineData(set);
        lineChartTrend.setData(data);
        lineChartTrend.getDescription().setEnabled(false);
        lineChartTrend.getLegend().setEnabled(false);
        
        XAxis xAxis = lineChartTrend.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        
        lineChartTrend.getAxisRight().setEnabled(false);
        lineChartTrend.animateX(1000);
        lineChartTrend.invalidate();
    }

    private void setupClassPieChart() {
        List<String> classes = database.getAllClasses();
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (String cls : classes) {
            if (!cls.equals("General Section")) {
                entries.add(new PieEntry(85, cls)); 
            }
        }

        if (entries.isEmpty()) {
            pieChartClass.setNoDataText("Add classes to view distribution");
            pieChartClass.invalidate();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(4f);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        pieChartClass.setData(data);
        pieChartClass.getDescription().setEnabled(false);
        pieChartClass.setHoleRadius(65f);
        pieChartClass.setCenterText("By Class");
        pieChartClass.animateY(1000);
        pieChartClass.invalidate();
    }
}