package com.example.ttg_project_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

public class signInActivity extends AppCompatActivity implements View.OnClickListener {

    PresenterClass Presenter;
    EditText login;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        try {
            Presenter = new PresenterClass(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button registration = (Button) findViewById(R.id.registrationBtnInSignIn);
        Button signIn = (Button) findViewById(R.id.signInBtnInSignIn);
        Button back = (Button) findViewById(R.id.backBtnInSignIn);
        registration.setOnClickListener(this);
        signIn.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.registrationBtnInSignIn:
            {
                Intent intent = new Intent(this, registerActivity.class);
                //Presenter.closeDB();
                startActivity(intent);
                break;
            }
            case R.id.signInBtnInSignIn:
            {
                login = (EditText) findViewById(R.id.loginEditTextInSignIn);
                password = (EditText) findViewById(R.id.passwordEditTextInSignIn);
                //Валидация полей
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
                else {
                    if(!Presenter.checkLogin(login.getText().toString()))
                        login.setError("Пользователя с таким логином не существует!");
                    else {
                        if (!Presenter.checkPassword(login.getText().toString(), password.getText().toString())) {
                            password.setError("Неверный пароль!");
                        } else {
                            int categoryId = Presenter.checkCategoryId(login.getText().toString());
                            Intent intent = new Intent(this, ownerMain.class); //Дефолт на владельце
                            switch (categoryId) {
                                case 1: {
                                    intent = new Intent(this, passangerMain.class);
                                    break;
                                }
                                case 2: {
                                    intent = new Intent(this, driverMain.class);
                                    break;
                                }
                                default:
                                    break;
                            }
                            intent.putExtra("login", login.getText().toString());
                            //Presenter.closeDB();
                            startActivity(intent);
                        }
                    }
                }
                break;
            }
            case R.id.backBtnInSignIn:
            {
                Intent intent = new Intent(this, MainActivity.class);
                //Presenter.closeDB();
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}