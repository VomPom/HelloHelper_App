package com.xrone.julis.compous.view.application.Express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkStep;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.Utils.TransLateresults;
import com.xrone.julis.compous.model.TranslateResultModel;
import com.xrone.julis.compous.view.application.map.utils.AMapUtil;

import java.util.ArrayList;
import java.util.List;


public class WalkRouteDetailActivity extends Activity {
	private WalkPath mWalkPath;
	private TextView mTitle,mTitleWalkRoute;
	private ListView mWalkSegmentList;
	private WalkSegmentListAdapter mWalkSegmentListAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		getIntentData();
		mTitle = (TextView) findViewById(R.id.title_center);
		mTitle.setText("Details of Pedestrian Route");
		mTitleWalkRoute = (TextView) findViewById(R.id.firstline);
		String dur = AMapUtil.getFriendlyTime((int) mWalkPath.getDuration());
		String dis = AMapUtil
				.getFriendlyLength((int) mWalkPath.getDistance());
		mTitleWalkRoute.setText(dur + "(" + dis + ")");
		mWalkSegmentList = (ListView) findViewById(R.id.bus_segment_list);

//		List<WalkStep> steps=mWalkPath.getSteps();
//		for(int i=0;i<steps.size();i++){
//			WalkStep step=steps.get(i);
//			Log.e("获取到的info", String.valueOf(step.getInstruction()));
//			final int finalI = i;
//			Log.e("ttttt", String.valueOf(finalI));
//			TransLaterUtilts.getData(getBaseContext(), step.getInstruction(), new TransLateresults() {
//				@Override
//				public void setPropety(TranslateResultModel resultModel) {
//					mWalkPath.getSteps().get(finalI).setInstruction(resultModel.getDst());
//				}
//			});
//		}


		mWalkSegmentList.setAdapter(mWalkSegmentListAdapter);
		mWalkSegmentListAdapter = new WalkSegmentListAdapter(
				this.getApplicationContext(), mWalkPath.getSteps());



	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		mWalkPath = intent.getParcelableExtra("walk_path");
	}

	public void onBackClick(View view) {
		this.finish();
	}

}