package com.example.ahmed.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Assist.Debuger;
import Assist.Information;

public class Register extends AppCompatActivity {
    Button register;
    EditText email,password,confirmpassword,name;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register=(Button) findViewById(R.id.register);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        name=(EditText)findViewById(R.id.name);
        confirmpassword=(EditText)findViewById(R.id.confirmpassword);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()<5)
                    Debuger.TOAST(Register.this,"Name Not Valid");
                else if(!password.getText().toString().equals(confirmpassword.getText().toString()))
                    Debuger.TOAST(Register.this,"Paswword Not Correct");
                else if(!validate(email.getText().toString()))
                    Debuger.TOAST(Register.this,"Email Not Valid");
                else {
                    SharedPreferences.Editor editor = getSharedPreferences(Information.SharedPreferences, MODE_PRIVATE).edit();
                    editor.putString(Information.Username,name.getText().toString());
                    editor.putString(Information.Password,password.getText().toString());
                    editor.putString(Information.Email,email.getText().toString());
                    editor.putBoolean(Information.Rememberme,false);
                    editor.commit();
                    Debuger.TOAST(Register.this,"Success");
                    startActivity(new Intent(Register.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
