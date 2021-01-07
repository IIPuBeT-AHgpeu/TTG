package com.example.ttg_project_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class driverMain extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {

    PresenterClass Presenter;
    private String start = "";
    EditText vehicleNumber;
    ToggleButton timeManagerBtn;
    ToggleButton changeStatusBtn;
    Button vehicleNumberBtn;
    TextView showInfo;
    TextView loginText;
    ListView stationList;
    private final ArrayList<String> stationArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        Bundle arguments = getIntent().getExtras();
        loginText = (TextView) findViewById(R.id.loginTextViewInDriverMain);
        loginText.setText(arguments.get("login").toString());

        try {
            Presenter = new PresenterClass(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        timeManagerBtn = (ToggleButton) findViewById(R.id.toggleButtonStartStopInDriverMain);
        timeManagerBtn.setOnCheckedChangeListener(this);
        changeStatusBtn = (ToggleButton) findViewById(R.id.vehicleStatusToggleInDriverMain);
        changeStatusBtn.setOnCheckedChangeListener(this);

        showInfo = (TextView) findViewById(R.id.showInformation);
        vehicleNumber = (EditText) findViewById(R.id.vehicleNumberEditTextInDriverMain);
        vehicleNumberBtn = (Button) findViewById(R.id.setVehicleBtnInDriverMain);
        vehicleNumberBtn.setOnClickListener(this);
        stationList = (ListView) findViewById(R.id.stationsList);
        stationList.setOnItemClickListener(this);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stationArray);
        stationList.setAdapter(adapter);
        ArrayList<String> arr = Presenter.getWayStationsOnLogin(loginText.getText().toString());
        for(int i = 0; i<arr.size(); i++)
        {
            stationArray.add(0, arr.get(i));
        }

        if(Presenter.driverHaveVehicle(loginText.getText().toString()))
        {
            vehicleNumber.setText(Presenter.getVehicleNumber(loginText.getText().toString()));
            showInfo.setText(Presenter.getVehicle(vehicleNumber.getText().toString()));
            vehicleNumber.setEnabled(false);
            vehicleNumberBtn.setEnabled(false);
        }
        else
        {
            changeStatusBtn.setEnabled(false);
            timeManagerBtn.setEnabled(false);
        }

    }

    // Создаём пустой массив
    private final ArrayList<String> tripsArray = new ArrayList<>();

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.setVehicleBtnInDriverMain:
            {
                vehicleNumber = (EditText) findViewById(R.id.vehicleNumberEditTextInDriverMain);
                if(!Validation.checkVehicleNumber(vehicleNumber.getText().toString()))
                {
                    vehicleNumber.setError("Введите номер в формате А000АА001rus!");
                }
                else {

                    if (Presenter.checkVehicleNumber(vehicleNumber.getText().toString())) {
                        if(!Presenter.numberIsBusy(vehicleNumber.getText().toString()))
                        {
                            vehicleNumber.setError("Номер занят!");
                        }
                        else
                        {
                            Presenter.makeDriverVehicleCommunication(loginText.getText().toString(), vehicleNumber.getText().toString());
                            showInfo.setText(Presenter.getVehicle(vehicleNumber.getText().toString()));
                            vehicleNumber.setEnabled(false);
                            vehicleNumberBtn.setEnabled(false);
                            changeStatusBtn.setEnabled(true);
                            timeManagerBtn.setEnabled(true);
                        }
                    } else {
                        vehicleNumber.setError("ТС с таким номером в системе не найдена!");
                    }

                }
                break;
            }
            default:break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.toggleButtonStartStopInDriverMain:
            {
                ListView tripsList = (ListView) findViewById(R.id.tripsListInDriverMain);
                // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tripsArray);
                tripsList.setAdapter(adapter);
                Date time = new Date();
                String stringToList = String.valueOf(time.getHours()) + ":" + String.valueOf(time.getMinutes()) + ":" + String.valueOf(time.getSeconds());

                if(isChecked)
                {
                    start = stringToList;
                    tripsArray.add(0, "Start: " + start);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    tripsArray.add(0, "Finish: " + stringToList);
                    adapter.notifyDataSetChanged();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                    Presenter.addTrip(dateFormat.format(time), start, stringToList, vehicleNumber.getText().toString());
                    //Возможно передавать Номер авто
                }
                break;
            }
            case R.id.vehicleStatusToggleInDriverMain:
            {
                if(isChecked)
                {
                    Presenter.setVehicleStatus(vehicleNumber.getText().toString(), "Требуется ремонт");
                    timeManagerBtn.setEnabled(false);
                }
                else
                {
                    Presenter.setVehicleStatus(vehicleNumber.getText().toString(), "Эксплуатируется");
                    timeManagerBtn.setEnabled(true);
                }
                showInfo.setText(Presenter.getVehicle(vehicleNumber.getText().toString()));
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stationArray);
        stationList.setAdapter(adapter);

        //запись в бд
        String[] buf = stationArray.get(position).split("#");
        Date time = new Date();
        String stringToList = String.valueOf(time.getHours()) + ":" + String.valueOf(time.getMinutes()) + ":" + String.valueOf(time.getSeconds());
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        Presenter.addStationTime(buf[0], vehicleNumber.getText().toString(), stringToList, dateFormat.format(time));

        //удаление строки
        stationArray.remove(position);

        //проверка на 0
        if(stationArray.size() == 0)
        {
            ArrayList<String> arr = Presenter.getWayStationsOnLogin(loginText.getText().toString());
            for(int i = 0; i<arr.size(); i++)
            {
                stationArray.add(0, arr.get(i));
            }
        }
    }
}