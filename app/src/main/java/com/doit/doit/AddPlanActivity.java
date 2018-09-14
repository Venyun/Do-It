package com.doit.doit;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import db.Plan;

public class AddPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(AddPlanActivity.this);
        final String userAccount=sharedPreferences.getString("userAccount","");
        final EditText inputTitle= (EditText) findViewById(R.id.add_plan_title);
        final EditText inputContent= (EditText) findViewById(R.id.add_plan_content);
        final DatePicker planDate= (DatePicker) findViewById(R.id.plan_date);
        final EditText inputTime= (EditText) findViewById(R.id.plan_time);
        Button commitPlanBtn= (Button) findViewById(R.id.commit_plan);
        commitPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plan plan=new Plan();
                if (!inputTitle.getText().toString().equals("")){
                    plan.setUserAccount(userAccount);
                    plan.setPlanTitle(inputTitle.getText().toString());
                    plan.setPlanContent(inputContent.getText().toString());
                    plan.setPlanDate(planDate.getYear()+"-"+(planDate.getMonth()+1)+"-"+planDate.getDayOfMonth());
                    plan.setPlanTime(inputTime.getText().toString());
                    plan.setIfFinished(false);
                    plan.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null) {
                                Toast.makeText(AddPlanActivity.this, "Add successful!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(AddPlanActivity.this,"Add failed!"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(AddPlanActivity.this,"The title is empty!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
