package com.cc.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.amap.api.location.*;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.cc.android.net.Api;
import com.cc.android.net.NetUtils;
import com.cc.android.R;
import com.cc.android.entity.RspOk;
import com.cc.android.widget.AlertMessage;
import com.cc.android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * 高精度定位模式功能演示
 *
 * @创建时间： 2015年11月24日 下午5:22:42
 * @项目名称： AMapLocationDemo2.x
 * @author hongming.wang
 * @文件名称: Hight_Accuracy_Activity.java
 * @类型名称: Hight_Accuracy_Activity
 */
public class Location_Activity extends CheckPermissionsActivity implements OnClickListener{
	private Button btLocation;
	private Button btMessage;
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	private MapView mMapView;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		setTitle(R.string.title_location);
		AMapLocationClient.setApiKey("4f45c9ad5d21c7550fce15f2b0015752");

		mMapView = (MapView) findViewById(R.id.map);
		//在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
		mMapView.onCreate(savedInstanceState);
		mContext = this;

		//初始化地图控制器对象
		AMap aMap = null;
		if (aMap == null) {
			aMap = mMapView.getMap();
			MyLocationStyle myLocationStyle;
			myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
			myLocationStyle.interval(2000);
			aMap.setMyLocationStyle(myLocationStyle);
			aMap.setMyLocationEnabled(true);
			aMap.moveCamera(CameraUpdateFactory.zoomBy(6));
			aMap.showIndoorMap(true);
		}
		initView();
		
		//初始化定位
		initLocation();
	}
	
	//初始化控件
	private void initView(){
		btLocation = (Button) findViewById(R.id.bt_location);
		btMessage = (Button) findViewById(R.id.bt_message);
		btLocation.setOnClickListener(this);
		btMessage.setOnClickListener(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyLocation();
		mMapView.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bt_location) {
			if (btLocation.getText().equals(
					getResources().getString(R.string.startLocation))) {
				btLocation.setText(getResources().getString(
						R.string.stopLocation));
				startLocation();
			} else {
				btLocation.setText(getResources().getString(
						R.string.startLocation));
				stopLocation();
			}
		}else if(v.getId() == R.id.bt_message){
            AlertMessage.dialogShow(this,"a","assssssssssssssssssss");
        }
	}
	
	/**
	 * 初始化定位
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void initLocation(){
		//初始化client
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = getDefaultOption();
		//设置定位参数
		locationClient.setLocationOption(locationOption);
		// 设置定位监听
		locationClient.setLocationListener(locationListener);
	}
	
	/**
	 * 默认的定位参数
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private AMapLocationClientOption getDefaultOption(){
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
		mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
		mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
		mOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
		mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
		mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
		mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
		AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
		mOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
		mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
		mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
		mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
		return mOption;
	}
	
	/**
	 * 定位监听
	 */
	AMapLocationListener locationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation location) {
			if (null != location && location.getErrorCode() == 0) {
				final Map<String, String> params = new HashMap<>();
				params.put("UserId","1");
				params.put("Longitude",String.valueOf(location.getLongitude()));
				params.put("Latitude",String.valueOf(location.getLatitude()));
				params.put("Province",String.valueOf(location.getProvince()));
				params.put("City",String.valueOf(location.getCity()));
				params.put("Area",String.valueOf(location.getDistrict()));
				params.put("Address",String.valueOf( location.getAddress()));
				params.put("Speed",String.valueOf(location.getSpeed()));
                params.put("ag",String.valueOf(location.getBearing()));
				Api.savePosition(mContext, params, new NetUtils.NetCallBack<RspOk>() {
					@Override
					public void success(RspOk rspData) {
						System.out.println(rspData);
					}
					@Override
					public void failed(String msg) {
						System.out.println(msg);
					}
				});
			} else {
                Toast.show(self,null == location?"当前定位失败，请联系管理员":location.getErrorInfo());
			}
		}
	};


	/**
	 * 获取GPS状态的字符串
	 * @param statusCode GPS状态码
	 * @return
	 */
	private String getGPSStatusString(int statusCode){
		String str = "";
		switch (statusCode){
			case AMapLocationQualityReport.GPS_STATUS_OK:
				str = "GPS状态正常";
				break;
			case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
				str = "手机中没有GPS Provider，无法进行GPS定位";
				break;
			case AMapLocationQualityReport.GPS_STATUS_OFF:
				str = "GPS关闭，建议开启GPS，提高定位质量";
				break;
			case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
				str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
				break;
			case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
				str = "没有GPS定位权限，建议开启gps定位权限";
				break;
		}
		return str;
	}

	/**
	 * 开始定位
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void startLocation(){
		//根据控件的选择，重新设置定位参数
		//resetOption();
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
	}
	
	/**
	 * 停止定位
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void stopLocation(){
		// 停止定位
		locationClient.stopLocation();
	}
	
	/**
	 * 销毁定位
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void destroyLocation(){
		if (null != locationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}
}
