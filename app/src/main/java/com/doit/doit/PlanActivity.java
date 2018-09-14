package com.doit.doit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import db.Plan;

public class PlanActivity extends AppCompatActivity {

    private List<Plan> unfinishedPlanList=new ArrayList<>();
    private List<Plan> finishedPlanList=new ArrayList<>();

    private void initPlan() {
        final BmobQuery<Plan> planQuery=new BmobQuery<Plan>();
        SharedPreferences planSharesPref= PreferenceManager.getDefaultSharedPreferences(PlanActivity.this);
        String userAccount=planSharesPref.getString("userAccount","");
        planQuery.addWhereEqualTo("userAccount",userAccount);
        planQuery.setLimit(10);
        planQuery.findObjects(new FindListener<Plan>() {
            @Override
            public void done(List<Plan> list, BmobException e) {
                if (e==null){
                    for (Plan plan:list){
                        if (!plan.getIfFinished()){
                            unfinishedPlanList.add(plan);
                            Log.d("PlanActivity",plan.getPlanTitle());
                        }else {
                            finishedPlanList.add(plan);
                            Log.d("PlanActivity",plan.getPlanTitle());
                        }
                    }
                }else{
                    Toast.makeText(PlanActivity.this,"Find failed!"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        initPlan();   //初始化计划列表

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                PlanActivity.this.runOnUiThread(new Runnable() {      //切换成主线程
                    @Override
                    public void run() {
                        RecyclerView unfinishedView= (RecyclerView) findViewById(R.id.unfinished_view);
                        RecyclerView finishedView= (RecyclerView) findViewById(R.id.finished_view);
                        LinearLayoutManager layoutManager1=new LinearLayoutManager(getBaseContext());
                        unfinishedView.setLayoutManager(layoutManager1);
                        PlanAdapter unfinishedPlanAdapter=new PlanAdapter(unfinishedPlanList);
                        unfinishedPlanAdapter.notifyDataSetChanged();
                        unfinishedView.setAdapter(unfinishedPlanAdapter);
                        LinearLayoutManager layoutManager2=new LinearLayoutManager(getBaseContext());
                        finishedView.setLayoutManager(layoutManager2);
                        PlanAdapter finishedPlanAdapter=new PlanAdapter(finishedPlanList);
                        finishedView.setAdapter(finishedPlanAdapter);
                        finishedPlanAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
//        Toast.makeText(PlanActivity.this,"size"+unfinishedPlanList.size(),Toast.LENGTH_SHORT).show();
    }
}
