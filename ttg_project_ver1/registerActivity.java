package com.example.ttg_project_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.sql.SQLException;

public class registerActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    PresenterClass Presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            Presenter = new PresenterClass(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button backBtn = (Button) findViewById(R.id.backBtnInRegister);
        backBtn.setOnClickListener(this);
        Button registerBtn = (Button) findViewById(R.id.registerBtnInRegister);
        registerBtn.setOnClickListener(this);
        RadioGroup group = (RadioGroup) findViewById(R.id.chooseCategoryGroupBtnInRegister);
        group.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backBtnInRegister:
            {
                Intent intent = new Intent(this, MainActivity.class);
                //Presenter.closeDB();
                startActivity(intent);
                break;
            }
            case R.id.registerBtnInRegister:
            {
                EditText login = (EditText) findViewById(R.id.loginEditText);
                EditText password = (EditText) findViewById(R.id.passwordEditText);
                EditText surname = (EditText) findViewById(R.id.surnameEditText);
                EditText firstname = (EditText) findViewById(R.id.firstNameEditText);
                EditText secondname = (EditText) findViewById(R.id.secondNameEditText);
                CheckBox haveSecondName = (CheckBox) findViewById(R.id.secondnameCheckBoxInRegister);
                RadioButton driver = (RadioButton) findViewById(R.id.driverBtnInRegister);
                RadioButton passenger = (RadioButton) findViewById(R.id.passangerBtnInRegister);
                RadioButton owner = (RadioButton) findViewById(R.id.ownerBtnInRegister);
                EditText series = (EditText) findViewById(R.id.seriesEditTextInRegistr);
                EditText number = (EditText) findViewById(R.id.numberEditTextInRegistr);
                EditText birthday = (EditText) findViewById(R.id.birthdayEditTextInRegistr);
                EditText address = (EditText) findViewById(R.id.addressEditTextInRegistr);
                EditText phonenumber = (EditText) findViewById(R.id.phoneEditTextInRegistr);
                EditText brigade = (EditText) findViewById(R.id.brigadeEditTextInRegistr);
                //Валидация
                if(!Validation.checkLogin(login.getText().toString()))
                {
                    login.setError("Логин должен содержать только буквы латинского алфавита или цифры," +
                            " также длина логина должна быть в пределах от 3 до 26 включительно!");
                }
                else if(!Validation.checkPassword(password.getText().toString()))
                {
                    password.setError("Пароль должен содержать только буквы латинского алфавита или цифры, также длина пароля должна быть в пределах" +
                            " от 6 до 20 символов включительно!");
                }
                else if(!Validation.checkFIO(surname.getText().toString()))
                {
                    surname.setError("В наборе фамилии можно использовать только буквы или знак \"-\", любая фамилия начинается с заглавной буквы!");
                }
                else if(!Validation.checkFIO(firstname.getText().toString()))
                {
                    firstname.setError("В наборе имени можно использовать только буквы или знак \"-\", любое имя начинается с заглавной буквы!");
                }
                else if(!Validation.checkFIO(secondname.getText().toString()) && !haveSecondName.isChecked())
                {
                    secondname.setError("В наборе отчества можно использовать только буквы или знак \"-\", любое отчество начинается с заглавной буквы!");
                }
                else if(!Validation.checkSeries(series.getText().toString()) && driver.isChecked())
                {
                    series.setError("Введите 4 цифры!");
                }
                else if(!Validation.checkNumber(number.getText().toString()) && driver.isChecked())
                {
                    number.setError("Введите 6 цифр!");
                }
                else if(!Validation.checkDate(birthday.getText().toString()) && driver.isChecked())
                {
                    birthday.setError("Введите дату в формате yyyy.mm.dd!");
                }
                else if(!Validation.checkPhone(phonenumber.getText().toString()) && driver.isChecked())
                {
                    phonenumber.setError("Введите номер телефона (11 цифр)!");
                }
                else if(!Validation.checkBrigade(brigade.getText().toString()) && driver.isChecked())
                {
                    brigade.setError("Введите номер бригады!");
                }
                               else
                {
                    //Проверка на существующий логин
                    if(Presenter.checkLogin(login.getText().toString()))
                    {
                        login.setError("Такой логин уже существует!");
                    }
                    else
                    {

                        if(passenger.isChecked())
                        {
                            if(haveSecondName.isChecked()) Presenter.addUser(login.getText().toString(), password.getText().toString(),
                                                                            surname.getText().toString(), firstname.getText().toString(), 1);
                            else Presenter.addUser(login.getText().toString(), password.getText().toString(), surname.getText().toString(),
                                                    firstname.getText().toString(), 1, secondname.getText().toString());
                        }
                        else if(driver.isChecked())
                        {
                            if(haveSecondName.isChecked()) Presenter.addUser(login.getText().toString(), password.getText().toString(),
                                    surname.getText().toString(), firstname.getText().toString(), 2, "", series.getText().toString(),
                                    number.getText().toString(), birthday.getText().toString(), address.getText().toString(),
                                    phonenumber.getText().toString(), brigade.getText().toString());
                            else Presenter.addUser(login.getText().toString(), password.getText().toString(),
                                    surname.getText().toString(), firstname.getText().toString(), 2, secondname.getText().toString(),
                                    series.getText().toString(), number.getText().toString(), birthday.getText().toString(), address.getText().toString(),
                                    phonenumber.getText().toString(), brigade.getText().toString());
                        }
                        else
                        {
                            if(haveSecondName.isChecked()) Presenter.addUser(login.getText().toString(), password.getText().toString(),
                                    surname.getText().toString(), firstname.getText().toString(), 3);
                            else Presenter.addUser(login.getText().toString(), password.getText().toString(), surname.getText().toString(),
                                    firstname.getText().toString(), 3, secondname.getText().toString());
                        }

                        Intent intent = new Intent(this, signInActivity.class);
                        //Presenter.closeDB();
                        startActivity(intent);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if(checkedId == R.id.passangerBtnInRegister || checkedId == R.id.ownerBtnInRegister)
        {
            setDriverFieldsEnable(false);
        }
        else
        {
            setDriverFieldsEnable(true);
        }
    }

    public void setDriverFieldsEnable(boolean b)
    {
        EditText series = (EditText) findViewById(R.id.seriesEditTextInRegistr);
        EditText number = (EditText) findViewById(R.id.numberEditTextInRegistr);
        EditText birthday = (EditText) findViewById(R.id.birthdayEditTextInRegistr);
        EditText address = (EditText) findViewById(R.id.addressEditTextInRegistr);
        EditText phone = (EditText) findViewById(R.id.phoneEditTextInRegistr);
        EditText brigade = (EditText) findViewById(R.id.brigadeEditTextInRegistr);
        series.setEnabled(b);
        number.setEnabled(b);
        birthday.setEnabled(b);
        address.setEnabled(b);
        phone.setEnabled(b);
        brigade.setEnabled(b);
    }
}