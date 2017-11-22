package com.xrone.julis.compous.view.application.map;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.AMap;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import com.amap.api.maps2d.LocationSource;
import android.widget.TextView;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.HttpUtils;
import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.Utils.TransLatorCallback;
import com.xrone.julis.compous.StringData.AppURL;

import com.xrone.julis.compous.model.TranslateResultModel;
import com.xrone.julis.compous.view.application.map.navigation.WalkRouteCalculateActivity;
import com.xrone.julis.compous.view.application.map.utils.AMapUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.xrone.julis.compous.view.application.map.utils.SensorEventHelper;
import com.xrone.julis.compous.view.application.map.utils.ToastUtil;

/**
 * Created by Julis on 2017/10/13.
 */

public class MapActivity extends Activity implements AMap.OnMarkerClickListener,AMap.OnMapClickListener{
    private MapView mapView;
    private AMap aMap;
    private Circle mCircle;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private LatLng myLocation=null;
    private RadioGroup mRadioGroup;
    private boolean mFirstFix = false;
    private Marker mLocMarker;
    private GeocodeSearch geocodeSearch=null;
    private LatLonPoint latLonPoint=null;
    private Marker regeoMarker;


    MyLocationStyle myLocationStyle;//初始化定位蓝点

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;
    private SensorEventHelper mSensorHelper;
    private String nowCityString=null;

    public Handler handler =   new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                //获取Json数组里面的值，并加入到Information对象里面去
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    double altitude = Double.parseDouble(object.getString("altitude"));
                    double longitude = Double.parseDouble(object.getString("longitude"));
                    String place = (String) object.get("place");
                    String content = (String) object.get("content");
                    LatLng latLng = new LatLng(altitude, longitude);
                    addExpressSignByLongPress(latLng, place, content);
                    System.out.println("altitude:" + altitude + "\nlongitude:" + longitude);
                }
            } catch (JSONException e) {
                Log.e("tttt","执行了11"+e);
                e.printStackTrace();
            }
        }
    };
//    private PoiResult myPoiResult; // poi返回的结果
//    private int currentPage = 0;// 当前页面，从0开始计数
//    private PoiSearch.Query query;// Poi查询条件类
//    private PoiSearch poiSearch;// POI搜索
//    private AutoCompleteTextView searchTextView;// 输入搜索关键字
//    private ProgressDialog progDialog = null;// 搜索时进度条
//    private TextView  searchButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_map_basicmap_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initViews();
        init();
        Log.e("tttt","执行了11");
        HttpUtils.getNewsJSON(AppURL.GET_EXPRESS_PLACES_URL, handler);
        Log.e("tttt","执行了22222");
        initGPS();
    }


    public void initViews(){
        /**
         * 显示英文还是中文地图
         */
        mRadioGroup = (RadioGroup) findViewById(R.id.check_language);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_ch) {
                    aMap.setMapLanguage(AMap.CHINESE);
                } else {
                    aMap.setMapLanguage(AMap.ENGLISH);
                }
            }
        });
//        searchTextView=(AutoCompleteTextView)findViewById(R.id.searchText);
//
//        searchButton=(TextView)findViewById(R.id.btn_search);
//
//
//
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doSearchQuery(searchTextView.getText().toString());
//            }
//        });
//        searchTextView.addTextChangedListener(recomenedTextListener);// 添加文本输入框监听事件
    }
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            geocodeSearch=new GeocodeSearch(getBaseContext());
            aMap.setMapLanguage(AMap.ENGLISH);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));//设置放大比例
            mSensorHelper = new SensorEventHelper(this);
            if (mSensorHelper != null) {
                mSensorHelper.registerSensorListener();
            }
        }
        setUpMap();

    }


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
        aMap.setOnMapLongClickListener(onMapLongClickListener);
        // 绑定 Marker 被点击事件
        aMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
        geocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        aMap.setLocationSource(locationSource);// 设置定位监听
        aMap.setInfoWindowAdapter(infoWindowAdapter);

        //定位结果类型：缓存定位结果 返回一段时间前设备在相同的环境中缓存下来的网络定位结果，节省无必要的设备定位消耗
        aMap.setMapType(AMapLocation.LOCATION_TYPE_FIX_CACHE);
        myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
       // aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        Log.e("onResum","null");
        if (mSensorHelper != null) {
            Log.e("onResum","null");
            mSensorHelper.registerSensorListener();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        locationSource.deactivate();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mFirstFix = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }



//    @Override
//    public void onMapLongClick(LatLng latLng) {
//        System.out.println("长按;"+latLng.latitude+"  sdg"+latLng.latitude);
//        addExpressSignByLongPress(latLng,"","");
//    }



    /**
     * 加入marker点
     */
    public void addExpressSignByLongPress(LatLng latLng, String place, String content) {
        aMap.addMarker(new MarkerOptions().position(latLng).title(place).snippet(content));
    }

    /**
     * 添加点标记（网络）
     * @param latlng
     * @param aMapLocation
     */
    private void addMyLocationMarker(LatLng latlng, AMapLocation aMapLocation) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.app_map_navi_gps_locked);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(aMapLocation.getCity());

        mLocMarker.setSnippet(aMapLocation.getAddress());
     }

    /**
     * 跳转到导航页面
     */
    public void goToNavition(Marker marker) {
        Intent intent = new Intent(getApplicationContext(), WalkRouteCalculateActivity.class);
        intent.putExtra("myLatitude", myLocation.latitude);
        intent.putExtra("myLongitude", myLocation.longitude);
        intent.putExtra("desLatitude", marker.getPosition().latitude);
        intent.putExtra("desLongtitude", marker.getPosition().longitude);
        startActivity(intent);
    }


    AMapLocationListener aMapLocationListener=new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            nowCityString=aMapLocation.getCity();
            if (mListener != null && aMapLocation != null) {
                if (aMapLocation != null
                        && aMapLocation.getErrorCode() == 0) {
                    LatLng location = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    myLocation=location;
                    if (!mFirstFix) {
                        addCircle(location, aMapLocation.getAccuracy());
                        mFirstFix = true;
                        addMyLocationMarker(location, aMapLocation);//添加定位图标
                        mCircle.setCenter(location);
                        mCircle.setRadius(aMapLocation.getAccuracy());
                        mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
                    } else {
                        mLocMarker.setPosition(location);
                    }
                } else {
                    String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                }
            }

        }
    };
    private void addCircle(LatLng latlng, double radius) {

        if(mCircle!=null){
            mCircle.remove();
        }
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);


    }

//    /**
//     * 开始进行poi搜索 进度条
//     */
//    protected void doSearchQuery(String searchString) {
//        showProgressDialog(searchString);// 显示进度框
//        currentPage = 0;
//        query = new PoiSearch.Query(searchString, "", nowCityString);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//        query.setPageSize(15);// 设置每页最多返回多少条poiitem
//        query.setPageNum(currentPage);// 设置查第一页
//        query.setCityLimit(true);
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.setOnPoiSearchListener(onPoiSearchListener);
//        poiSearch.searchPOIAsyn();
//    }
//    /**
//     * 显示进度框
//     */
//    private void showProgressDialog(String word) {
//        if (progDialog == null)
//            progDialog = new ProgressDialog(this);
//        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progDialog.setIndeterminate(false);
//        progDialog.setCancelable(false);
//        progDialog.setMessage("正在搜索:\n" + word);
//        progDialog.show();
//    }
//
//    /**
//     * 隐藏进度框
//     */
//    private void dissmissProgressDialog() {
//        if (progDialog != null) {
//            progDialog.dismiss();
//        }
//    }




    /**
     * 显示弹出框是否开始导航
     */
    public void showAlertDialog(Marker marker){
        AlertDialog isToNavigationAlert = new AlertDialog.Builder(MapActivity.this).create();
        isToNavigationAlert.setTitle(R.string.map_navi_isToNavigation);
        isToNavigationAlert.setMessage(getString(R.string.map_navi_goToNavigation));
        isToNavigationAlert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        isToNavigationAlert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), new myAlertListenr(marker));
        isToNavigationAlert.show();
    }

//
//    TextWatcher recomenedTextListener = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            Log.e("change","文字干部");
//            String newText = s.toString().trim();
//            if (!AMapUtil.IsEmptyOrNullString(newText)) {
//                InputtipsQuery inputquery = new InputtipsQuery(newText, searchTextView.getText().toString());
//                Inputtips inputTips = new Inputtips(MapActivity.this, inputquery);
//                inputTips.setInputtipsListener(inputtipsListener);
//                inputTips.requestInputtipsAsyn();
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };
//    Inputtips.InputtipsListener inputtipsListener =new Inputtips.InputtipsListener() {
//        @Override
//        public void onGetInputtips(List<Tip> list, int i) {
//            if (i == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
//                List<String> listString = new ArrayList<String>();
//                for (int t = 0; t< list.size(); t++) {
//                    listString.add(list.get(t).getName());
//                }
//                ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
//                        getApplicationContext(),
//                        R.layout.map_route_inputs, listString);
//                searchTextView.setAdapter(aAdapter);
//                aAdapter.notifyDataSetChanged();
//            } else {
//                ToastUtil.showerror(MapActivity.this, i);
//            }
//
//        }
//    };
//
//
//    /**
//     * 搜索监听事件
//     */
//    PoiSearch.OnPoiSearchListener onPoiSearchListener =new PoiSearch.OnPoiSearchListener() {
//        @Override
//        public void onPoiSearched(PoiResult poiResult, int i) {
//            dissmissProgressDialog();// 隐藏对话框
//            if (i == AMapException.CODE_AMAP_SUCCESS) {
//                if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
//                    if (poiResult.getQuery().equals(query)) {// 是否是同一条
//                        myPoiResult = poiResult;
//                        // 取得搜索到的poiitems有多少页
//                        List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                        List<SuggestionCity> suggestionCities = poiResult
//                                .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
//
//                        if (poiItems != null && poiItems.size() > 0) {
//                            aMap.clear();// 清理之前的图标
//                            PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                            poiOverlay.removeFromMap();
//                            poiOverlay.addToMap();
//                            poiOverlay.zoomToSpan();
//                        } else if (suggestionCities != null
//                                && suggestionCities.size() > 0) {
//                            Log.e("sugges",suggestionCities.toString());
//                           //showSuggestCity(suggestionCities);
//                        } else {
//                            ToastUtil.show(MapActivity.this,"Sorry!Not Find This Place!");
//                        }
//                    }
//                } else {
//                    ToastUtil.show(MapActivity.this,"Sorry!Not Find This Place!");
//                }
//            } else {
//                ToastUtil.showerror(MapActivity.this, i);
//            }
//        }
//
//        @Override
//        public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//        }
//    };

    // 定义 地理编码事件监听
    OnGeocodeSearchListener onGeocodeSearchListener = new OnGeocodeSearchListener(){
        /**
         * 逆地理编码回调
         */
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            String addressName;
            if (i == AMapException.CODE_AMAP_SUCCESS) {
                if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                        && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                    addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                    transLateText(addressName);


                } else {
                    ToastUtil.show(MapActivity.this, "没结果");
                }
            } else {
                ToastUtil.showerror(getBaseContext(), i);
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };



    /**
     * 定位图标的文本显示
     */
    AMap.InfoWindowAdapter infoWindowAdapter=new AMap.InfoWindowAdapter(){
        @Override
        public View getInfoWindow(final Marker marker) {

            View view = getLayoutInflater().inflate(R.layout.map_marker_info, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(marker.getTitle());

            TextView snippet = (TextView) view.findViewById(R.id.snippet);
            snippet.setText(marker.getSnippet());
            Log.e("TextViewText",marker.getSnippet());

            ImageButton button = (ImageButton) view
                    .findViewById(R.id.start_amap_app);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(marker);
                }
            });
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    };


    /**
     * 位置监听
     */

    LocationSource locationSource =new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(getBaseContext());
                mLocationOption = new AMapLocationClientOption();
                //设置定位监听
                mlocationClient.setLocationListener(aMapLocationListener);
                //设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置是否使用缓存策略, 默认为true 使用缓存策略
                mLocationOption.setLocationCacheEnable(true);
                //设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mlocationClient.startLocation();
            }
        }

        @Override
        public void deactivate() {
            mListener = null;
            if (mlocationClient != null) {
                mlocationClient.stopLocation();
                mlocationClient.onDestroy();
            }
            mlocationClient = null;
        }
    };

    AMap.OnMyLocationChangeListener onMyLocationChangeListener =new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Log.e("wss","位置改变了");
            if (location != null) {

                Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
                Bundle bundle = location.getExtras();
                if (bundle != null) {
                    int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                    String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                    // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                    int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                /*
                errorCode
                errorInfo
                locationType
                */
                    Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
                } else {
                    Log.e("amap", "定位信息， bundle is null ");

                }

            } else {
                Log.e("amap", "定位失败");
            }
        }
    };

    /**
     *  判断GPS模块是否开启，如果没有则开启
     */
    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Positioning needs GPS, please open GPS.");
            dialog.setPositiveButton("OK",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
            dialog.setNeutralButton("Cancel", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            } );
            dialog.show();
        }
    }


    /**
     * 标记点击事件监听
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
       if( marker.isInfoWindowShown()){
           marker.hideInfoWindow();
       }else{
           marker.showInfoWindow();
       }
       Log.e("text", String.valueOf(marker.isInfoWindowShown()));

        return true;
    }

    // 定义 长按 点击事件监听
    OnMapLongClickListener onMapLongClickListener =new OnMapLongClickListener(){
        @Override
        public void onMapLongClick(LatLng latLng) {


        }
    };
    /**
     * 地图点击短时间按监听
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        latLonPoint = new LatLonPoint(latLng.latitude,latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint,100,geocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }

    /**
     * 设置点击确定监听开始导航事件
     */
    class myAlertListenr implements DialogInterface.OnClickListener {
        private Marker marker;
        public myAlertListenr(Marker marker) {
            this.marker = marker;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            goToNavition(marker);
        }
    }







    /**
     * 翻译文本
     * @param playText
     */
    public void transLateText(final String playText) {
        TransLaterUtilts.getData(getBaseContext(),playText,new TransLatorCallback() {
            @Override
            public void setPropety(TranslateResultModel resultModel) {
                regeoMarker.setTitle("目标位置/TargetPosition");
                regeoMarker.setSnippet("\n" + resultModel.getSrc()+"/"+resultModel.getDst());
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                regeoMarker.hideInfoWindow();
                regeoMarker.showInfoWindow();
            }
        });

    }



}
