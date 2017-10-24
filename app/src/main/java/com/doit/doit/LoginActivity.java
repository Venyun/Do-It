package com.doit.doit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import db.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail;
    private EditText inputPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this,"b1dc3818dd1e47444d6acc18b812ae9b");

        inputEmail= (EditText) findViewById(R.id.account);
        inputPw= (EditText) findViewById(R.id.passwd);

        Button loginBtn= (Button) findViewById(R.id.logIn);
        Button signupBtn= (Button) findViewById(R.id.signUp);
        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logIn:
                BmobQuery<User> userBmobQuery=new BmobQuery<>();
                userBmobQuery.addWhereEqualTo("userAccount",inputEmail.getText().toString());
                userBmobQuery.setLimit(1);
                userBmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e==null){
                            for (User user:list){
                                if (user.getPassWd().equals(inputPw.getText().toString())){
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });
                break;

            case R.id.signUp:
                Intent intent1=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent1);
                break;
            default:
        }
    }
}
