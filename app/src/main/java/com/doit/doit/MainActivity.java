package com.doit.doit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import db.Place;
import db.Plan;
import util.AMapUtil;
import util.Utils;

public class MainActivity extends AppCompatActivity implements
        AMapLocationListener,GeocodeSearch.OnGeocodeSearchListener, AMap.OnMapClickListener, AMap.OnCameraChangeListener, View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private CloudSearch mCloudSearch;
    private CloudSearch.Query mQuery;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView textView;
    private String[] strMsg;
    private com.amap.api.maps.AMap aMap;
    private MapView mapView;
    private GeocodeSearch geocoderSearch;
    private Marker geoMarker;
    private static LatLonPoint latLonPoint;
    double latitude;
    double longitude;
    private Button yesBtn;
    private Button noBtn;
    private Button startBtn;
    private Button backBtn;
    private String address;
    private String userAccount;
    private TextView timeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        userAccount = sharedPreferences.getString("userAccount", "");

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        //AMap aMap = mapView.getMap();

        timeText= (TextView) findViewById(R.id.plan_text2);
        yesBtn= (Button) findViewById(R.id.yesbtn);
        noBtn= (Button) findViewById(R.id.nobtn);
        startBtn= (Button) findViewById(R.id.start_btn);
        backBtn= (Button) findViewById(R.id.ic_back);
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        Location();
        final Intent intent=new Intent();
        intent.setAction("Utils");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home);

        }
        navView.setCheckedItem(R.id.plan_list);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.plan_list:
                        Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.study_location:
                        intent = new Intent(MainActivity.this, PlaceActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.settings:
                        intent = new Intent(MainActivity.this, PlanActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.log_out:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;


                    default:
                }
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, AddPlanActivity.class);
               startActivity(intent);

            }
        });

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
        String date=dateFormat.format(new java.util.Date());
        final BmobQuery<Plan> timeQuery=new BmobQuery<Plan>();
        timeQuery.setLimit(1);
        timeQuery.addWhereEqualTo("userAccount",userAccount);
        timeQuery.addWhereEqualTo("planDate",date);
        timeQuery.findObjects(new FindListener<Plan>() {
            @Override
            public void done(List<Plan> list, BmobException e) {
                if (e==null){
                    for (Plan plan:list){
                        timeText.setText(plan.getPlanTime());
                    }
                }else {
                    Toast.makeText(MainActivity.this,""+e.getMessage()+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private void initMap(){

        if (aMap == null) {
            aMap = mapView.getMap();

            //用高德默认图标
            geoMarker= aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            //自定义图标
            //geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
             //       .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.punch_dw))));
//            aMap.setMyLocationEnabled(true);
            aMap.setOnMapClickListener(this);
            aMap.setOnCameraChangeListener(this);
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        getAddress(latLonPoint);
    }

    //地图点击事件
    @Override
    public void onMapClick(LatLng latLng) {
//点击地图后清理图层插上图标，在将其移动到中心位置
        aMap.clear();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.position(latLng);
        aMap.addMarker(otMarkerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

    }

    //这是重点方法

    @Override
    public void onCameraChange(CameraPosition arg0) {
// TODO 自动生成的方法存根
        LatLng target = arg0.target;
        latitude=target.latitude;
        longitude=target.longitude;
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener(){

            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

                String formatAddress = result.getRegeocodeAddress().getFormatAddress();
                Log.e("formatAddress", "formatAddress:"+formatAddress);
//                place=formatAddress;
//                tv_local.setText(place);
                Log.e("formatAddress", "rCode:"+rCode);

            }});
        latLonPoint= new LatLonPoint(latitude,longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);

    }


    @Override
    public void onCameraChangeFinish(CameraPosition arg0) {
// TODO 自动生成的方法存根

    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
            //doSearchQuery(city,latitude,longitude);
        }

    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //定位完成
                case Utils.MSG_LOCATION_FINISH:
                    String result = "";
                    try {
                        AMapLocation loc = (AMapLocation) msg.obj;
                        result = Utils.getLocationStr(loc, 1);
                        strMsg = result.split(",");
                        Toast.makeText(MainActivity.this, "定位成功", Toast.LENGTH_LONG).show();
//                        Log.d("Main","nnn");
//                        textView.setText("地址：" + strMsg[0] + "\n" + "经    度：" + strMsg[1] + "\n" + "纬    度：" + strMsg[2]);
//                        Log.d("Main","nnn");
                        latitude = Double.parseDouble(strMsg[2]);
                        longitude=Double.parseDouble(strMsg[1]);
                        latLonPoint= new LatLonPoint(latitude, longitude);
//                        Log.d("Main","nnn");
                        initMap();
//                        Log.d("Main","nnn");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        };

    };



    public void Location() {
        // TODO Auto-generated method stub
        try {
            locationClient = new AMapLocationClient(this);
            //初始化定位参数
            locationOption = new AMapLocationClientOption();
            // 设置定位模式为低功耗模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            //locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 设置定位监听
            locationClient.setLocationListener(this);
            locationOption.setOnceLocation(true);//设置为单次定位
            locationClient.setLocationOption(locationOption);// 设置定位参数
            // 启动定位
            locationClient.startLocation();
            mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {

    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                address=result.getRegeocodeAddress().getFormatAddress();
                Toast.makeText(MainActivity.this,result.getRegeocodeAddress().getFormatAddress()
                        + "附近",Toast.LENGTH_LONG).show();
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                geoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
            } else {

            }
        } else {
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yesbtn:
                getAddress(latLonPoint);
                Place place=new Place();
                place.setUserAccount(userAccount);
                place.setStudyPlace(address);
                place.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this,"添加失败"+e.getMessage()+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.nobtn:
                break;
            case R.id.start_btn:
                break;
            case R.id.ic_back:
                finish();
        }
    }
}
