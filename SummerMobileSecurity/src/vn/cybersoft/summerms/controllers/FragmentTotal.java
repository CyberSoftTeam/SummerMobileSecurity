package vn.cybersoft.summerms.controllers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;*/












import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.model.DataMonitorHelper;
import vn.cybersoft.summerms.model.DateTraffic;
import vn.cybersoft.summerms.model.TrafficSnapshot;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentTotal extends Fragment{
	private TextView tvToday,tvThisMonth,tvRemaining,tvRemainingData;
	private LinearLayout chartLayout;
	private TrafficSnapshot current=null;
	private int dayNumber;
	private long dataMonth,dataToday,dataUp,dataDow;
	private DataMonitorHelper dataMonitorHelper;
	public FragmentTotal() {
		// TODO Auto-generated constructor stub
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.layout_fragment_daily_monitor, container,
				false);
		dataMonitorHelper=new DataMonitorHelper(getActivity());
		tvToday=(TextView) rootView.findViewById(R.id.tv_data_today);
		tvThisMonth=(TextView) rootView.findViewById(R.id.tv_this_month);
		tvRemaining=(TextView) rootView.findViewById(R.id.tv_remaining);
		tvRemainingData=(TextView) rootView.findViewById(R.id.tv_remaining_1);
		chartLayout=(LinearLayout) rootView.findViewById(R.id.chart_view);
		dataMonth=0;
		dataToday=0;
		dataUp=0;
		dataDow=0;
		getData();
		setDataForTextView();
		return rootView;
	}
	private void getData() {
		
		current=new TrafficSnapshot(getActivity());
		Calendar calendar = Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		calendar.set(year, new Date().getMonth(), 2);
		dayNumber=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Log.d("date",dayNumber+"");
		ArrayList<DateTraffic> lsDateTraffic=new ArrayList<>();
		lsDateTraffic=dataMonitorHelper.getDAY();
		ArrayList<Double> datas =new ArrayList<>();
		Log.d("Datas", lsDateTraffic.size()+"");
		for (int i = 0; i < lsDateTraffic.size(); i++) {
			long data=lsDateTraffic.get(i).getStartdownLoad()+lsDateTraffic.get(i).getStartupLoad();
			data=data/(1024*1024);
			datas.add((double)data);
			
			//set Data for textview
			dataDow+=lsDateTraffic.get(i).getStartdownLoad();
			dataUp+=lsDateTraffic.get(i).getStartupLoad();
			Log.d("Datas", data+"");
		}
		dataMonth=(dataDow+dataUp);
		dataToday=lsDateTraffic.get(new Date().getDate()-1).getStartdownLoad()+
				lsDateTraffic.get(new Date().getDate()-1).getStartupLoad();
		
		execute(getActivity(), datas);
	}
	public void setDataForTextView(){
		String month = "",today="";
		//month
		if((dataMonth/(1024*1024))!=0){
			month=dataMonth/(1024*1024)+" MB";
		}else if((dataMonth/1024)!=0){
			month=dataMonth/1024+" KB";
		}
		//today
		if((dataToday/(1024*1024))!=0){
			today=dataToday/(1024*1024)+" MB";
		}else if((dataToday/1024)!=0){
			today=dataToday/1024+" KB";
		}
		
		tvThisMonth.setText(month);
		tvToday.setText(today);
		
	}
	public void execute(Context context,ArrayList<Double> datas) {
		String[] titles = new String[] { "data" };
		List<Double> values = new ArrayList<Double>();

		int color = Color.GREEN;
		XYMultipleSeriesRenderer renderer = buildBarRenderer(color);
		renderer.setOrientation(Orientation.HORIZONTAL);
		setChartSettings(renderer, "", "", "", 0,
				6, 0,250, Color.BLACK, Color.BLUE);
		renderer.setXLabels(0);
		renderer.setYLabels(10);
		ArrayList<Double> data=new ArrayList<>();
		for (int i = 1; i <= dayNumber; i++) {

			renderer.addXTextLabel(i, i+"/"+(new Date().getMonth()+1));
			//data.add(object)
		}
		
		for (double d : datas) {
			values.add(d);
		}
		GraphicalView graphicalView= ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer,
				Type.DEFAULT);
		chartLayout.addView(graphicalView,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
			String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}
	protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<Double> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			for (int k = 0; k < values.size(); k++) {
				series.add(values.get(k));
			}
			dataset.addSeries(series.toXYSeries());
			
		}
		return dataset;
	}
	protected XYMultipleSeriesRenderer buildBarRenderer(int color) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setChartTitleTextSize(0);
		renderer.setLabelsTextSize(20);
		renderer.setBarWidth(20);
		
		renderer.setShowLegend(false);
		renderer.setShowGridX(true);
		renderer.setGridColor(Color.GRAY);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setMarginsColor(Color.WHITE);

		renderer.setXLabelsColor(Color.RED);
		renderer.setYLabelsPadding(0);
		renderer.setLabelsColor(Color.RED);
		renderer.setZoomButtonsVisible(false);
		renderer.setInScroll(false);
		renderer.setZoomEnabled(false, false);
		renderer.setClickEnabled(false);
		renderer.setPanEnabled(true, false);

		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);
		r.setDisplayChartValues(true);
		NumberFormat formatter;
		formatter = new DecimalFormat("#'MB'");
		r.setChartValuesFormat(formatter);
		r.setChartValuesTextSize(25);
		r.setChartValuesTextAlign(Align.CENTER);
		
		renderer.addSeriesRenderer(r);
		renderer.setDisplayValues(false);
		return renderer;
	}
}
