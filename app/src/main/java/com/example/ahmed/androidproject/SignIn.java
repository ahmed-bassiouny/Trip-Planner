package com.example.ahmed.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import Assist.Debuger;
import Assist.Information;

public class SignIn extends AppCompatActivity {
    TextView newuser;
    Button login;
    EditText name,password;
    CheckBox rememberme;
    SharedPreferences prefs ;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        prefs = getSharedPreferences(Information.SharedPreferences, MODE_PRIVATE);
        edit = getSharedPreferences(Information.SharedPreferences, MODE_PRIVATE).edit();
        newuser=(TextView)findViewById(R.id.newuser);
        login=(Button)findViewById(R.id.login);
        name=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password);
        rememberme=(CheckBox)findViewById(R.id.rememberme);
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,Register.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getString(Information.Username, "NULL").equals(name.getText().toString()) &&
                        prefs.getString(Information.Password, "NULL").equals(password.getText().toString()))
                {
                    edit.putBoolean(Information.Rememberme,rememberme.isChecked());
                    edit.commit();
                    startActivity(new Intent(SignIn.this, MainActivity.class));
                    finish();
                }
                else{
                    Debuger.TOAST(SignIn.this,"Username or Password not correct");
                }
            }
        });

    }

}
