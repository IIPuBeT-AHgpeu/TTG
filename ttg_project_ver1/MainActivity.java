package com.example.ttg_project_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button register = (Button) findViewById(R.id.toRegistrationBtn);
        register.setOnClickListener(this);
        Button continueBtn = (Button) findViewById(R.id.chooseEnterBtn);
        continueBtn.setOnClickListener(this);
        TextView test = (TextView) findViewById(R.id.wantRegisterTextView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toRegistrationBtn: {
                Intent intent = new Intent(this, registerActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.chooseEnterBtn:
            {
                RadioButton looker = (RadioButton) findViewById(R.id.lookerRadioBtn);
                if(!looker.isChecked()) {
                    Intent intent = new Intent(this, signInActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(this, passangerMain.class);
                    intent.putExtra("login", "User");
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }
    }
}