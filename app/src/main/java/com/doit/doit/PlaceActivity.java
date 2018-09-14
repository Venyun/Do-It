package com.doit.doit;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import db.Place;

public class PlaceActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SideslipListView mSideslipListView;
    private String userAccount;
    private String objectId="";

    /**
     * 初始化数据
     */
    private ArrayList<String> mDataList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PlaceActivity.this);
        userAccount = sharedPreferences.getString("userAccount", "");

        BmobQuery<Place> placeBmobQuery=new BmobQuery<Place>();
        placeBmobQuery.addWhereEqualTo("userAccount",userAccount);
        placeBmobQuery.setLimit(50);
        placeBmobQuery.findObjects(new FindListener<Place>() {
            @Override
            public void done(List<Place> list, BmobException e) {
                if (e==null){
                    for (Place place:list){
                        mDataList.add(place.getStudyPlace());
                    }
                }else{
                    Toast.makeText(PlaceActivity.this,"query failed"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PlaceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSideslipListView = (SideslipListView) findViewById(R.id.sideslipListView);
                        mSideslipListView.setAdapter(new CustomAdapter());//设置适配器
                    }
                });
            }
        }).start();

        //设置item点击事件
//        mSideslipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                if (mSideslipListView.isAllowItemClick()) {
//                    Log.i(TAG, mDataList.get(position) + "被点击了");
//                    Toast.makeText(PlaceActivity.this, mDataList.get(position) + "被点击了",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        //设置item长按事件
//        mSideslipListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
//                if (mSideslipListView.isAllowItemClick()) {
//                    Log.i(TAG, mDataList.get(position) + "被长按了");
//                    Toast.makeText(PlaceActivity.this, mDataList.get(position) + "被长按了",
//                            Toast.LENGTH_SHORT).show();
//                    return true;//返回true表示本次事件被消耗了，若返回
//                }
//                return false;
//            }
//        });
    }

    /**
     * 自定义ListView适配器
     */
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(PlaceActivity.this, R.layout.place_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
                viewHolder.txtv_delete = (TextView) convertView.findViewById(R.id.txtv_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(mDataList.get(position));
            final int pos = position;
            viewHolder.txtv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(PlaceActivity.this, mDataList.get(pos) + "被删除了",
//                            Toast.LENGTH_SHORT).show();

                    BmobQuery<Place> placeQuery=new BmobQuery<Place>();
                    placeQuery.addWhereEqualTo("userAccount",userAccount);
                    placeQuery.addWhereEqualTo("studyPlace",mDataList.toArray()[pos].toString());
                    placeQuery.setLimit(1);
                    placeQuery.findObjects(new FindListener<Place>() {
                        @Override
                        public void done(List<Place> list, BmobException e) {
                            if (e==null){
                                for (Place place:list){
                                    objectId =place.getObjectId();
                                    place.setObjectId(objectId);
                                    place.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e==null){
                                                mDataList.remove(pos);
                                                notifyDataSetChanged();
                                                mSideslipListView.turnNormal();
                                            }else {
                                                Toast.makeText(PlaceActivity.this,"query failed"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else {
                                Toast.makeText(PlaceActivity.this,"query failed"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    Place place=new Place();
//                    place.setObjectId(objectId);
//                    place.delete(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e==null){
//                                mDataList.remove(pos);
//                                notifyDataSetChanged();
//                                mSideslipListView.turnNormal();
//                            }else {
//                                Toast.makeText(PlaceActivity.this,"query failed"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        public TextView textView;
        public TextView txtv_delete;
    }
}
