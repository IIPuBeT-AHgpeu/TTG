package com.example.ttg_project_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

public class passangerMain extends AppCompatActivity implements View.OnClickListener{

    private PresenterClass Presenter;
    private EditText wayNumber;
    private ListView tripsList;
    private ListView wayStationsList;
    private TextView loginText;
    private TextView avTime;
    private final ArrayList<String> tripsArray = new ArrayList<>();
    private final ArrayList<String> wayStationsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_main);

        Bundle arguments = getIntent().getExtras();
        loginText = (TextView) findViewById(R.id.loginTextViewInPassangerMain);
        loginText.setText(arguments.get("login").toString());

        try {
            Presenter = new PresenterClass(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button enterBtn = (Button) findViewById(R.id.enterWayBtnInPassengerMain);
        enterBtn.setOnClickListener(this);

        avTime = (TextView) findViewById(R.id.averageTimeInPassanger);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.enterWayBtnInPassengerMain:
            {
                wayNumber = (EditText) findViewById(R.id.enterWayEditTextInPasssangerMain);
                tripsList = (ListView) findViewById(R.id.tripsListInPassengerMain);
                wayStationsList = (ListView) findViewById(R.id.waystationsListInPassangerMain);
                //Заполнить листы данными (вызвать 3 метода и заполнить)
                if(!Presenter.checkWay(wayNumber.getText().toString()))
                {
                    wayNumber.setError("Такого маршрута не существует!");
                }
                else
                {
                    avTime.setText("Среднее время: " + Presenter.getAverageTime(wayNumber.getText().toString()));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tripsArray);
                    tripsList.setAdapter(adapter);
                    ArrayAdapter<String> adapterStations = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wayStationsArray);
                    wayStationsList.setAdapter(adapterStations);
                    tripsArray.clear();
                    ArrayList<String> distinctStr = Presenter.getFiveLastTrips(wayNumber.getText().toString());
                    for(int i = distinctStr.size()-1; i>=0; i--)
                    {
                        tripsArray.add(0, distinctStr.get(i));
                    }
                    wayStationsArray.clear();
                    distinctStr.clear(); distinctStr = Presenter.getWayStations(wayNumber.getText().toString());
                    for (int i = 0; i<distinctStr.size(); i++)
                    {
                        wayStationsArray.add(0, distinctStr.get(i));
                    }
                }

                break;
            }
            default:
                break;
        }
    }
}