/*******************************************************************************
 * Copyright 2014 IUH.CyberSoft Team (http://cyberso.wordpress.com/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package vn.cybersoft.summerms.controllers;
/**
 * @author Phạm Văn Năm
 */
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import vn.cybersoft.summerms.R;
import vn.cybersoft.summerms.database.DataMonitorHelper;
import vn.cybersoft.summerms.model.App;
import vn.cybersoft.summerms.model.DateTraffic;
import vn.cybersoft.summerms.model.TrafficRecord;
import vn.cybersoft.summerms.model.TrafficSnapshot;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentTotal extends Fragment{
	private TextView tvToday,tvThisMonth,tvRemainingData;
	private LinearLayout chartLayout,chartRemining;
	private Button btSetDataPlan;
	public static String TAG="FragmentTotal";
	private int dayNumber;
	private long dataMonth,dataToday,dataUp,dataDow,max,dataset;
	private TrafficSnapshot latest=null;
	private DataMonitorHelper dataHelper;
	public FragmentTotal() {
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.layout_fragment_daily_monitor, container,
				false);
		
		dataHelper=new DataMonitorHelper(getActivity());
		getData();
		tvToday=(TextView) rootView.findViewById(R.id.tv_data_today);
		tvThisMonth=(TextView) rootView.findViewById(R.id.tv_this_month);
		tvRemainingData=(TextView) rootView.findViewById(R.id.tv_remaining_1);
		chartLayout=(LinearLayout) rootView.findViewById(R.id.chart_view);
		chartRemining=(LinearLayout) rootView.findViewById(R.id.chartRemining);

		btSetDataPlan=(Button) rootView.findViewById(R.id.bt_set);


		dataMonth=0;
		dataToday=0;
		dataUp=0;
		dataDow=0;
		SaveData();
		setDataForTextView();
		long plan=getSharedPreferences();
		Log.d("data", dataMonth+"-"+plan);
		if(plan!=1 && (dataMonth/(1024*1024))<plan){
			tvRemainingData.setText((plan-dataMonth/(1024*1024))+" MB");
			tvRemainingData.setTextColor(Color.GREEN);//Color.parseColor("#3B5DE5")
			DrawPieChart(dataUp/(1024*1024),dataDow/(1024*1024),plan);
		}else{
			tvRemainingData.setText((plan-dataMonth/(1024*1024))+" MB");
			tvRemainingData.setTextColor(Color.RED);
			DrawPieChart(dataUp/(1024*1024),dataDow/(1024*1024),dataUp/(1024*1024)+dataDow/(1024*1024));
		}

		btSetDataPlan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog=new Dialog(getActivity());
				dialog.setContentView(R.layout.dialog_layout);
				final TextView tvData=(TextView) dialog.findViewById(R.id.tvdataset);
				Button btOK=(Button) dialog.findViewById(R.id.bt_confirm);
				Button btCancel=(Button) dialog.findViewById(R.id.bt_cancel);
				dialog.setTitle("Set data plan");

				btOK.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//doing process
						dataset=Integer.parseInt(tvData.getText().toString());
						long data=dataset-dataMonth/(1024*1024);
						tvRemainingData.setText(data+" MB");
						if(dataset!=1 && (dataMonth/(1024*1024))<dataset){
							tvRemainingData.setTextColor(Color.GREEN);
							chartRemining.removeAllViewsInLayout();
							DrawPieChart(dataUp/(1024*1024),dataDow/(1024*1024),dataset);
						}else{
							tvRemainingData.setTextColor(Color.RED);
							chartRemining.removeAllViewsInLayout();
							DrawPieChart(dataUp/(1024*1024),dataDow/(1024*1024),dataUp/(1024*1024)+dataDow/(1024*1024));
						}
						putSharedPreferences(dataset);
						dialog.dismiss();
					}
				});
				btCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		return rootView;
	}
	private void SaveData() {

		new TrafficSnapshot(getActivity());
		Calendar calendar = Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		calendar.set(year, Calendar.getInstance().get(Calendar.MONTH), 2);
		dayNumber=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Log.d("date",dayNumber+"");
		ArrayList<DateTraffic> lsDateTraffic=new ArrayList<DateTraffic>();
		lsDateTraffic=dataHelper.getDAY();
		ArrayList<Double> datas =new ArrayList<Double>();
		Log.d("Datas", lsDateTraffic.size()+"");
		for (int i = 0; i < lsDateTraffic.size(); i++) {
			long data=lsDateTraffic.get(i).getStartdownLoad()+lsDateTraffic.get(i).getStartupLoad();
			data=data/(1024*1024);
			datas.add((double)data);
			if(data>max) max=data;
			//set Data for textview
			dataDow+=lsDateTraffic.get(i).getStartdownLoad();
			dataUp+=lsDateTraffic.get(i).getStartupLoad();
			Log.d("Datas", data+"");
		}
		dataMonth=(dataDow+dataUp);
		dataToday=lsDateTraffic.get(Calendar.getInstance().get(Calendar.DATE)-1).getStartdownLoad()+
				lsDateTraffic.get(Calendar.getInstance().get(Calendar.DATE)-1).getStartupLoad();

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
	@SuppressWarnings("deprecation")
	public void execute(Context context,ArrayList<Double> datas) {
		String[] titles = new String[] { "data" };
		List<Double> values = new ArrayList<Double>();
		int color = Color.GREEN;
		XYMultipleSeriesRenderer renderer = buildBarRenderer(color);
		renderer.setOrientation(Orientation.HORIZONTAL);
		setChartSettings(renderer, "", "", "", 0,
				6, 0,max+50, Color.BLACK, Color.BLUE);
		renderer.setXLabels(0);
		renderer.setYLabels(10);
		for (int i = 1; i <= dayNumber; i++) {

			renderer.addXTextLabel(i, i+"/"+(Calendar.getInstance().get(Calendar.MONTH)+1));
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
		r.setColor(Color.parseColor("#3B5DE5"));
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
	/*
	 * chart Remining
	 * */
	@SuppressWarnings("deprecation")
	private void DrawPieChart(double up,double dow,double plan){
		NumberFormat formatter;
		formatter = new DecimalFormat("#.#");
		double dow1=Double.parseDouble(formatter.format((dow*100)/plan));
		double up1=Double.parseDouble(formatter.format((up*100)/plan));
		double rem1=100-(dow1+up1);
		double[] values = new double[] {rem1,dow1,up1};
		int[] colors = new int[] { Color.GREEN,Color.parseColor("#3B5DE5") , Color.YELLOW};
		DefaultRenderer renderer = buildCategoryRenderer(colors);
		renderer.setZoomButtonsVisible(false);
		renderer.setZoomEnabled(true);
		renderer.setChartTitleTextSize(0);
		renderer.setDisplayValues(true);
		renderer.setZoomEnabled(false);
		renderer.setShowLabels(false);
		renderer.setShowLegend(false);
		SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
		/*r.setGradientEnabled(true);
		r.setGradientStart(0, Color.WHITE);
		r.setGradientStop(0, Color.GREEN);*/
		r.setHighlighted(true);
		GraphicalView graphicalView=  ChartFactory.getPieChartView(
				getActivity(), buildCategoryDataset("Project budget", values), renderer);
		chartRemining.addView(graphicalView,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	// Function Drawer
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(27);
		renderer.setLabelsColor(Color.parseColor("#D84B41"));
		renderer.setLegendTextSize(0);
		renderer.setInScroll(false);
		renderer.setClickEnabled(false);

		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}
	// Function Drawer
	protected CategorySeries buildCategoryDataset(String title, double[] values) {
		CategorySeries series = new CategorySeries(title);
		int k = 0;
		for (double value : values) {
			series.add("Project " + ++k, value);
		}
		return series;
	}
	public void putSharedPreferences(long plan){

		getActivity();
		SharedPreferences pre=getActivity().getSharedPreferences("my_data", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit=pre.edit();
		edit.putLong("plan",plan);
		edit.commit();

	}
	public long getSharedPreferences()
	{
		SharedPreferences pre=getActivity().getSharedPreferences
				("my_data",Context.MODE_PRIVATE);

		if(pre != null){
			Log.d("share", pre.getLong("plan", 1)+"");
			return pre.getLong("plan", 1);
		}
		return 1;
	}
	
	@SuppressWarnings("deprecation")
	private void getData() {
		latest=new TrafficSnapshot(getActivity());
		DateTraffic dateTraffic=new DateTraffic();
		dateTraffic=dataHelper.getDAY((new Date().getDate()-1)+"."+(new Date().getMonth()+1));
		if((new Date().getDate()>1)&&(dateTraffic.getDate() != null)){
				long dataR=latest.getDevice().getRx()-dateTraffic.getStartdownLoad();
				long dataT=latest.getDevice().getTx()-dateTraffic.getStartupLoad();
				if(dataR>-1 && dataT>-1)
					dataHelper.updateDAY(new DateTraffic(new Date().getDate()+"."+(new Date().getMonth()+1),dataT,dataR));
				else{
					dataHelper.updateDAY(new DateTraffic(new Date().getDate()+"."+(new Date().getMonth()+1),latest.getDevice().getTx(),latest.getDevice().getRx()));
				}
				Log.d(TAG, "Update new-"+dataR +"-"+dataT+"-"+dateTraffic.getDate());
		}else{
			Log.d(TAG, "mis");
			dataHelper.deteleAllTable();
			dataHelper=new DataMonitorHelper(getActivity());
			
			Calendar calendar = Calendar.getInstance();
			int year=calendar.get(Calendar.YEAR);
			calendar.set(year, new Date().getMonth(), 2);
			int dayNumber=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			for (int i = 1; i <(new Date().getDate()); i++) {
				if(dataHelper.addDAY(new DateTraffic((i)+"."+(new Date().getMonth()+1),0,0))!=-1){
					Log.d(TAG, "Add Day1-"+latest.getDevice().getRx() +"-"+latest.getDevice().getTx());
				}
			}
			if(dataHelper.addDAY(new DateTraffic(new Date().getDate()+"."+(new Date().getMonth()+1),latest.getDevice().getTx(),latest.getDevice().getRx()))!=-1){
				Log.d(TAG, "Add Day-"+latest.getDevice().getRx() +"-"+latest.getDevice().getTx());
			}
			for (int i = (new Date().getDate())+1; i <=dayNumber; i++) {
				if(dataHelper.addDAY(new DateTraffic((i)+"."+(new Date().getMonth()+1),0,0))!=-1){
					Log.d(TAG, "Add Day2-"+latest.getDevice().getRx() +"-"+latest.getDevice().getTx());
				}
			}
			//app
		}

		HashSet<Integer> intersection=new HashSet<Integer>(latest.getApps().keySet());
		for (Integer uid : intersection) {
			TrafficRecord latest_rec=latest.getApps().get(uid);
			addAppData(uid,latest_rec.getTag(), latest_rec);
		}

	}
	private void addAppData(int uid,CharSequence name, TrafficRecord latest_rec) {
		if (latest_rec.getRx()>-1 || latest_rec.getTx()>-1) {
			App app =new App();
			app.setAppid(uid);
			app.setAppName(name.toString());
			app.setLaststartdownLoad(0);
			app.setLaststartupLoad(0);
			app.setStartdownLoad(latest_rec.getRx());
			app.setStartupLoad(latest_rec.getTx());
			if(!dataHelper.isAppContain(uid)) dataHelper.addApp(app);
		}
	}

}
