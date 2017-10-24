package com.doit.doit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import db.User;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail;
    private EditText inputName;
    private EditText inputPw1;
    private EditText inputPw2;
    private Button backBtn;
    private Button commitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inputEmail= (EditText) findViewById(R.id.email);
        inputName= (EditText) findViewById(R.id.user_name);
        inputPw1= (EditText) findViewById(R.id.passwd1);
        inputPw2= (EditText) findViewById(R.id.passwd2);
        backBtn= (Button) findViewById(R.id.ic_back);
        commitBtn= (Button) findViewById(R.id.commit);
        backBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_back:
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.commit:
                User user=new User();
                if (inputPw1.getText().toString().equals(inputPw2.getText().toString())){
                   // Toast.makeText(SignupActivity.this,"commit?",Toast.LENGTH_SHORT).show();
                    user.setUserName(inputName.getText().toString());
                    user.setUserAccount(inputEmail.getText().toString());
                    user.setPassWd(inputPw1.getText().toString());
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                Toast.makeText(SignupActivity.this, "committed", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SignupActivity.this,"commit failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                Intent intent1=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent1);
                break;
            default:

        }
    }
}
