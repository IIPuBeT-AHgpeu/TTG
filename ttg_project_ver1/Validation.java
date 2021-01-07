package com.example.ttg_project_ver1;

public class Validation {

    static public boolean checkPassword(String password)
    {
        return password.matches("[a-zA-Z0-9]{6,20}");//
    }

    static public boolean checkLogin(String login)
    {
        return login.matches("[a-zA-Z0-9]{3,26}");
    }

    static public boolean checkFIO(String str)
    {
        return str.matches("[A-ZА-Я]{1}[а-яa-zА-ЯA-Z-]{1,25}");
    }

    static public boolean checkSeries(String str)
    {
        return str.matches("\\d{4,4}");
    }

    static public boolean checkNumber(String str)
    {
        return str.matches("\\d{6,6}");
    }

    static public boolean checkDate(String str)
    {
        return str.matches("\\d{4,4}\\.\\d{2,2}\\.\\d{2,2}");
    }

    static public boolean checkPhone(String str)
    {
        return str.matches("\\d{11,11}");
    }
    static public boolean checkBrigade(String str)
    {
        return str.matches("\\d+");
    }
    static public boolean checkNumeric(String str) {
        return str.matches("\\d{1,4}");
    }
    static public boolean checkVehicleNumber(String str)
    {
        return str.matches("[А-Я]{1}\\d{3}[А-Я]{2}\\d{3}[a-z]{3}");
    }
}
