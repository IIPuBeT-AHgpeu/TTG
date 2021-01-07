package com.example.ttg_project_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class EditTableVehicle extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    PresenterClass Presenter;
    private final ArrayList<String> vehiclesArray = new ArrayList<>();
    EditText vehicleNumber;
    EditText vehicleBrand;
    EditText vehicleModel;
    EditText vehicleRelease;
    EditText vehicleCapacity;
    TextView wayText;
    ListView vehiclesList;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table_vehicle);

        Bundle arguments = getIntent().getExtras();
        loginText = (TextView) findViewById(R.id.loginTextViewInOwnerEdit);
        loginText.setText(arguments.get("login").toString());
        wayText = (TextView) findViewById(R.id.wayTextViewInOwnerEdit);
        wayText.setText(arguments.get("way").toString());

        try {
            Presenter = new PresenterClass(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button addBtn = (Button) findViewById(R.id.addBtnInOwnerEdit);
        addBtn.setOnClickListener(this);
        Button editBtn = (Button) findViewById(R.id.editBtnInOwnerEdit);
        editBtn.setOnClickListener(this);
        Button deleteBtn = (Button) findViewById(R.id.deleteBtnInOwnerEdit);
        deleteBtn.setOnClickListener(this);
        setButtonsEnable(true);
        Button backBtn = (Button) findViewById(R.id.backInOwnerEdit);
        backBtn.setOnClickListener(this);

        vehiclesList = (ListView) findViewById(R.id.vehiclesListInOwnerEdit);
        vehiclesList.setOnItemClickListener(this);

        final ArrayAdapter<String> vegiclesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehiclesArray);
        vehiclesList.setAdapter(vegiclesAdapter);
        ArrayList<String> ways = Presenter.getVehicles(wayText.getText().toString());
        for(int i = 0; i<ways.size(); i++)  vehiclesArray.add(0, ways.get(i));

        vehicleNumber = (EditText) findViewById(R.id.numberEditTextInOwnerEdit);
        vehicleBrand = (EditText) findViewById(R.id.brandEditTextInOwnerEdit);
        vehicleModel = (EditText) findViewById(R.id.modelEditTextInOwnerEdit);
        vehicleRelease = (EditText) findViewById(R.id.releaseEditTextInOwnerEdit);
        vehicleCapacity = (EditText) findViewById(R.id.capacityEditTextInOwnerEdit);
        wayText = (TextView) findViewById(R.id.wayTextViewInOwnerEdit);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.addBtnInOwnerEdit:
            {
                //Добавить авто
                if(!Validation.checkVehicleNumber(vehicleNumber.getText().toString()))
                {
                    vehicleNumber.setError("Введите номер в формате А000АА134rus!");
                }
                else
                {
                    Presenter.addVehicle(vehicleNumber.getText().toString(), vehicleBrand.getText().toString(), vehicleModel.getText().toString(),
                            vehicleRelease.getText().toString(), vehicleCapacity.getText().toString(), Integer.parseInt(wayText.getText().toString()));
                    final ArrayAdapter<String> vehiclesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehiclesArray);
                    vehiclesList.setAdapter(vehiclesAdapter);
                    vehiclesArray.clear();
                    ArrayList<String> ways = Presenter.getVehicles(wayText.getText().toString());
                    for(int i = 0; i<ways.size(); i++)  vehiclesArray.add(0, ways.get(i));
                }
                break;
            }
            case R.id.editBtnInOwnerEdit:
            {
                Presenter.editVehicle(vehicleNumber.getText().toString(), vehicleBrand.getText().toString(), vehicleModel.getText().toString(),
                        vehicleRelease.getText().toString(), Integer.parseInt(vehicleCapacity.getText().toString()));
                final ArrayAdapter<String> vehiclesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehiclesArray);
                vehiclesList.setAdapter(vehiclesAdapter);
                vehiclesArray.clear();
                ArrayList<String> ways = Presenter.getVehicles(wayText.getText().toString());
                for(int i = 0; i<ways.size(); i++)  vehiclesArray.add(0, ways.get(i));
                setButtonsEnable(true);
                vehicleNumber.setEnabled(true);
                setEditFieldsNull();
                break;
            }
            case R.id.deleteBtnInOwnerEdit:
            {
                Presenter.deleteVehicle(vehicleNumber.getText().toString());
                final ArrayAdapter<String> vehiclesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehiclesArray);
                vehiclesList.setAdapter(vehiclesAdapter);
                vehiclesArray.clear();
                ArrayList<String> ways = Presenter.getVehicles(wayText.getText().toString());
                for(int i = 0; i<ways.size(); i++)  vehiclesArray.add(0, ways.get(i));
                setButtonsEnable(true);
                vehicleNumber.setEnabled(true);
                break;
            }
            case R.id.backInOwnerEdit:
            {
                TextView loginText = (TextView) findViewById(R.id.loginTextViewInOwnerEdit);
                Intent intent = new Intent(this, ownerMain.class);
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
        setButtonsEnable(false);
        vehicleNumber.setEnabled(false);
        String[] buf1 = ((TextView) view).getText().toString().split("\n");
        String number = buf1[0];
        String strToSplit = Presenter.getVehicleFields(number);
        String[] splt = strToSplit.split("#");
        vehicleNumber.setText(splt[0]);
        vehicleBrand.setText(splt[1]);
        vehicleModel.setText(splt[2]);
        vehicleRelease.setText(splt[3]);
        vehicleCapacity.setText(splt[4]);
    }

    public void setButtonsEnable(boolean enable)
    {
        Button addBtn = (Button) findViewById(R.id.addBtnInOwnerEdit);
        Button editBtn = (Button) findViewById(R.id.editBtnInOwnerEdit);
        Button deleteBtn = (Button) findViewById(R.id.deleteBtnInOwnerEdit);
        editBtn.setEnabled(!enable);
        deleteBtn.setEnabled(!enable);
        addBtn.setEnabled(enable);
    }

    public void setEditFieldsNull()
    {
        vehicleNumber.setText("");
        vehicleBrand.setText("");
        vehicleModel.setText("");
        vehicleRelease.setText("");
        vehicleCapacity.setText("");
    }
}