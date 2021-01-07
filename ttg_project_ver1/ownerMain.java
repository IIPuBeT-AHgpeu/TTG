package com.example.ttg_project_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class ownerMain extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    PresenterClass Presenter;
    // Создаём пустой массив
    private final ArrayList<String> waysArray = new ArrayList<>();
    private final ArrayList<String> wayStationsArray = new ArrayList<>();
    private final ArrayList<String> vehiclesArray = new ArrayList<>();
    private String wayString = "";
    ListView vehiclesList;
    ListView wayStationsList;
    Button addNewBtn;
    TextView loginText;
    TextView averTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        Bundle arguments = getIntent().getExtras();
        loginText = (TextView) findViewById(R.id.loginTextViewInOwnerMain);
        loginText.setText(arguments.get("login").toString());

        averTime = (TextView) findViewById(R.id.averageTime);
        wayStationsList = (ListView) findViewById(R.id.waystationsListInOwnerMain);
        vehiclesList = (ListView) findViewById(R.id.unitsListInOwnerMain);

        addNewBtn = (Button) findViewById(R.id.addNewWay);
        addNewBtn.setOnClickListener(this);

        try {
            Presenter = new PresenterClass(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button editVehicle = (Button) findViewById(R.id.editBtnInOwnerMain);
        editVehicle.setOnClickListener(this);
        editVehicle.setEnabled(false);

        ListView waysList = (ListView) findViewById(R.id.waysListInOwnerMain);

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        final ArrayAdapter<String> waysAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, waysArray);
        waysList.setAdapter(waysAdapter);

        ArrayList<String> ways = Presenter.getOwnerWays(loginText.getText().toString());
        for(int i = 0; i<ways.size(); i++)  waysArray.add(0, ways.get(i));

        waysList.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.editBtnInOwnerMain:
            {
                Intent intent = new Intent(this, EditTableVehicle.class);
                intent.putExtra("login", loginText.getText().toString());
                intent.putExtra("way", wayString);
                //Presenter.closeDB();
                startActivity(intent);
                break;
            }
            case R.id.addNewWay:
            {
                Intent intent = new Intent(this, AddWay.class);
                intent.putExtra("login", loginText.getText().toString());
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        averTime.setText( "Среднее время: " + Presenter.getAverageTime(((TextView) view).getText().toString()));
        final ArrayAdapter<String> wayStationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wayStationsArray);
        wayStationsList.setAdapter(wayStationsAdapter);
        final ArrayAdapter<String> vehiclesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehiclesArray);
        vehiclesList.setAdapter(vehiclesAdapter);
        wayStationsArray.clear();
        vehiclesArray.clear();
        ArrayList<String> wayStations = Presenter.getWayStations(((TextView) view).getText().toString());
        for(int i = 0; i<wayStations.size(); i++)
        {
            wayStationsArray.add(0, wayStations.get(i));
        }
        ArrayList<String> vehicles = Presenter.getVehicles(((TextView) view).getText().toString());
        for(int i = 0; i<vehicles.size(); i++)
        {
            vehiclesArray.add(0, vehicles.get(i));
        }
        wayString = ((TextView) view).getText().toString();
        Button editVehicle = (Button) findViewById(R.id.editBtnInOwnerMain);
        editVehicle.setEnabled(true);
    }
}