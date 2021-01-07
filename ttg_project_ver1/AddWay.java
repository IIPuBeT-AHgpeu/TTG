package com.example.ttg_project_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class AddWay extends AppCompatActivity implements View.OnClickListener {

    private PresenterClass Presenter;
    private final ArrayList<String> wayStationsArray = new ArrayList<>();
    private final ArrayList<String> id = new ArrayList<>();
    EditText wayText;
    ListView wayStationsList;
    TextView loginText;
    Button backBtn;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_way);

        Bundle arguments = getIntent().getExtras();
        loginText = (TextView) findViewById(R.id.loginTextViewInAddWay);
        loginText.setText(arguments.get("login").toString());

        wayText = (EditText) findViewById(R.id.wayEditTextInAddWay);
        wayStationsList = (ListView) findViewById(R.id.wayStationListInAddWay);
        backBtn = (Button) findViewById(R.id.backBtnInAddWay);
        addBtn = (Button) findViewById(R.id.addBtnInAddWay);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);

        try {
            Presenter = new PresenterClass(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ArrayAdapter<String> wayStationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, wayStationsArray);
        wayStationsList.setAdapter(wayStationsAdapter);
        ArrayList<String> buf = Presenter.getAllWayStations();
        String[] str = {};
        for (int i = 0; i<buf.size(); i++)
        {
            str = buf.get(i).split("#");
            id.add(0, str[0]);
            wayStationsArray.add(0, str[1]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backBtnInAddWay:
            {
                Intent intent = new Intent(this, ownerMain.class);
                intent.putExtra("login", loginText.getText().toString());
                startActivity(intent);
                break;
            }
            case R.id.addBtnInAddWay:
            {
                if(!Validation.checkNumeric(wayText.getText().toString()))
                {
                    wayText.setError("Введите номер маршрута (от 1 до 4 цифр)!");
                }
                else
                {
                    if(Presenter.checkWay(wayText.getText().toString())) wayText.setError("Такой номер уже существует!");
                    else
                    {
                        SparseBooleanArray chosen = wayStationsList.getCheckedItemPositions();
                        ArrayList<String> arr = new ArrayList<>();
                        for (int i = 0; i < chosen.size(); i++) {
                            if(chosen.valueAt(i)){
                                arr.add(0, id.get(chosen.keyAt(i)));
                            } }
                        Presenter.addNewWay(loginText.getText().toString(), arr, wayText.getText().toString());
                    }
                }
                break;
            }
            default:break;
        }
    }
}