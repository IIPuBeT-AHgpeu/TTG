package com.example.ttg_project_ver1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class PresenterClass {

    static private DBclass DBHelper;
    private SQLiteDatabase ttg_db;
    private Cursor queryReturn;

    public PresenterClass(Context context) throws SQLException {
        DBHelper = new DBclass(context);
        DBHelper.create_db();
        ttg_db = DBHelper.open();
    }

    public void addStationTime(String id, String vehicleNumber, String time, String date)
    {
        ContentValues cv = new ContentValues();
        cv.put("station_id", id);
        cv.put("vehicle_number", vehicleNumber);
        cv.put("time", time);
        String[] splt = date.split("\\.");
        String day = splt[0] + "-" + splt[1] + "-" + splt[2];
        cv.put("date", day);
        ttg_db.insert("station_time", null, cv);
        cv.clear();
    }

    public boolean checkLogin(String login) //Проверка на существующий логин
    {
        queryReturn = ttg_db.rawQuery("SELECT login FROM users", null);
        while (queryReturn.moveToNext())
        {
            if(queryReturn.getString(0).equals(login))
            {
                queryReturn.close();
                return true;
            }

        }
        queryReturn.close();
        return false;
    }

    public boolean checkPassword(String login, String password) //Проверка пароля
    {
        queryReturn = ttg_db.rawQuery("SELECT login, password FROM users", null);
        while (queryReturn.moveToNext())
        {
            if(queryReturn.getString(0).equals(login) && queryReturn.getString(1).equals(password))
            {
                queryReturn.close();
                return true;
            }

        }
        queryReturn.close();
        return false;
    }

    public void addUser(String login, String password, String surname, String firstname, int categoryId)
    {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAMES_USERS[0], login);
        cv.put(DBHelper.COLUMN_NAMES_USERS[1], password);
        cv.put(DBHelper.COLUMN_NAMES_USERS[2], categoryId);
        cv.put(DBHelper.COLUMN_NAMES_USERS[3], surname + " " + firstname);
        ttg_db.insert("users", null, cv);
        cv.clear();
        if(categoryId == 3){
            cv.put("fio", surname + " " + firstname);
            ttg_db.insert("owner", null, cv);
        }
    }

    public void addUser(String login, String password, String surname, String firstname, int categoryId, String secondname)
    {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAMES_USERS[0], login);
        cv.put(DBHelper.COLUMN_NAMES_USERS[1], password);
        cv.put(DBHelper.COLUMN_NAMES_USERS[2], categoryId);
        cv.put(DBHelper.COLUMN_NAMES_USERS[3], surname + " " + firstname + " " + secondname);
        ttg_db.insert("users", null, cv);
        cv.clear();
        if(categoryId == 3){
            cv.put("fio", surname + " " + firstname + " " + secondname);
            ttg_db.insert("owner", null, cv);
        }
    }

    public boolean addUser(String login, String password, String surname, String firstname, int categoryId, String secondname,
                            String series, String number, String birthday, String address, String phone, String brigade)
    {

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAMES_USERS[0], login);
        cv.put(DBHelper.COLUMN_NAMES_USERS[1], password);
        cv.put(DBHelper.COLUMN_NAMES_USERS[2], categoryId);
        cv.put(DBHelper.COLUMN_NAMES_USERS[3], surname + " " + firstname + " " + secondname);
        ttg_db.insert("users", null, cv);
        cv.clear();
        if(categoryId == 3){
            cv.put("fio", surname + " " + firstname + " " + secondname);
            ttg_db.insert("owner", null, cv);
        }
        else if(categoryId == 2)
        {
            cv.put("fio", surname + " " + firstname + " " + secondname);
            cv.put("phonenumber", phone);
            cv.put("persNum", series+number);
            cv.put("brigade_number", brigade);
            ttg_db.insert("driver", null, cv);
            cv.clear();
            cv.put("persNum", series+number);
            cv.put("birthday", birthday);
            cv.put("address", address);
            ttg_db.insert("passport", null, cv);
        }
        return true;
    }

    public boolean checkWay(String way)
    {
        queryReturn = ttg_db.rawQuery("SELECT way_number FROM way", null);
        while (queryReturn.moveToNext())
        {
            if(queryReturn.getString(0).equals(way))
            {
                queryReturn.close();
                return true;
            }
        }
        queryReturn.close();
        return false;
    }

    public ArrayList<String> getFiveLastTrips(String wayNumber)
    {
        ArrayList<String> arr = new ArrayList<>();
        String buffer = "";
        queryReturn = ttg_db.rawQuery( "SELECT station_time.vehicle_number, station_time.time, station_time.date, station.name, station.place FROM station_time JOIN station ON station_time.station_id=station.station_id JOIN vehicle ON station_time.vehicle_number=vehicle.number WHERE vehicle.way_number='" + wayNumber + "' AND station_time.date=CURRENT_DATE ORDER BY station_time.time DESC LIMIT 5;", null);
        while(queryReturn.moveToNext())
        {
            buffer = "Название: " + queryReturn.getString(3) + "\nМесто: " + queryReturn.getString(4) + "\nВремя: " + queryReturn.getString(1) + "\nДата: " + queryReturn.getString(2) + "\nАвто: " + queryReturn.getString(0);
            arr.add(buffer);
        }
        queryReturn.close();
        return arr;
    }

    public ArrayList<String> getWayStations(String way)
    {
        ArrayList<String> arr = new ArrayList<>();
        String buffer = "";
        queryReturn = ttg_db.rawQuery("SELECT name, place FROM way\n" +
                "JOIN way_station ON way.way_number=way_station.way_number\n" +
                "JOIN station ON way_station.station_id=station.station_id\n" +
                "WHERE way_station.way_number="+way, null);
        while(queryReturn.moveToNext())
        {
            buffer = "Название: " + queryReturn.getString(0) + " \nМестоположение: " + queryReturn.getString(1);
            arr.add(buffer);
        }
        queryReturn.close();
        return arr;
    }

    public ArrayList<String> getWayStationsOnLogin(String login)
    {
        queryReturn = ttg_db.rawQuery("SELECT way_number FROM vehicle JOIN driver ON driver.driver_id=vehicle.driver_id JOIN users ON driver.fio=users.fio "+
                "WHERE users.login='" + login + "';", null);
        queryReturn.moveToFirst();
        String way = queryReturn.getString(0);
        queryReturn.close();
        ArrayList<String> arr = new ArrayList<>();
        String buffer = "";
        queryReturn = ttg_db.rawQuery("SELECT station.station_id, station.name, station.place FROM way\n" +
                "JOIN way_station ON way.way_number=way_station.way_number\n" +
                "JOIN station ON way_station.station_id=station.station_id\n" +
                "WHERE way_station.way_number="+way, null);
        while(queryReturn.moveToNext())
        {
            buffer = queryReturn.getString(0) + "#Название: " + queryReturn.getString(1) + " \nМестоположение: " + queryReturn.getString(2);
            arr.add(buffer);
        }
        queryReturn.close();
        return arr;
    }

    public boolean driverHaveVehicle(String login)
    {
        queryReturn = ttg_db.rawQuery("SELECT fio, login FROM users WHERE login='" + login + "';", null);
        queryReturn.moveToFirst();
        String fio = queryReturn.getString(0);
        queryReturn.close();
        queryReturn = ttg_db.rawQuery("SELECT count(*) FROM driver JOIN vehicle ON driver.driver_id=vehicle.driver_id WHERE fio ='" + fio + "';", null);
        queryReturn.moveToFirst();
        if (queryReturn.getInt(0) > 0)
        {
            queryReturn.close();
            return true;
        }
        else
        {
            queryReturn.close();
            return false;
        }
    }

    public int checkCategoryId(String login)
    {
        queryReturn = ttg_db.rawQuery("SELECT categoryId FROM users WHERE login = '"+login+"';", null);
        queryReturn.moveToNext();
        int buf = queryReturn.getInt(0);
        queryReturn.close();
        return buf;
    }

    public String getVehicleNumber(String login)
    {
        queryReturn = ttg_db.rawQuery("SELECT fio FROM users WHERE login='" + login + "';", null);
        queryReturn.moveToFirst();
        String fio = queryReturn.getString(0);
        queryReturn.close();
        queryReturn = ttg_db.rawQuery("SELECT number FROM driver JOIN vehicle ON driver.driver_id=vehicle.driver_id WHERE fio ='" + fio + "';", null);
        queryReturn.moveToFirst();
        String number = queryReturn.getString(0);
        queryReturn.close();
        return number;
    }

    public String getVehicle(String vehicleNumber)
    {
        queryReturn = ttg_db.rawQuery("SELECT * FROM vehicle WHERE number='" + vehicleNumber + "';", null);
        queryReturn.moveToFirst();
        String strForReturn = "Номер: " + queryReturn.getString(0) + "\nБренд: " + queryReturn.getString(1) + "\nМодель: " +
                queryReturn.getString(2) + "\nДата выпуска: " + queryReturn.getString(3) + "\nВместимость: " +
                queryReturn.getString(4) +"\nСтатус: " + queryReturn.getString(5);
        queryReturn.close();
        return strForReturn;
    }

    public String getVehicleFields(String vehicleNumber)
    {
        String buffer = "";
        queryReturn = ttg_db.rawQuery("SELECT * FROM vehicle WHERE number='" + vehicleNumber + "';", null);
        queryReturn.moveToFirst();

        buffer = queryReturn.getString(0) + "#" + queryReturn.getString(1) + "#" +
                    queryReturn.getString(2) + "#" + queryReturn.getString(3) + "#" +
                    queryReturn.getString(4);

        queryReturn.close();
        return buffer;
    }

    public void setVehicleStatus(String vehicleNumber, String status)
    {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        ttg_db.update("vehicle", cv, "number = '" + vehicleNumber + "'", null);
        cv.clear();
    }

    public boolean checkVehicleNumber(String number)
    {
        queryReturn = ttg_db.rawQuery("SELECT number FROM vehicle;", null);
        while (queryReturn.moveToNext())
        {
            if(queryReturn.getString(0).equals(number))
            {
                queryReturn.close();
                return true;
            }
        }
        queryReturn.close();
        return false;
    }

    public void makeDriverVehicleCommunication(String login, String number)
    {
        queryReturn = ttg_db.rawQuery("SELECT driver_id FROM users JOIN driver ON users.fio=driver.fio WHERE login ='"+login+"';", null);
        queryReturn.moveToNext();
        int id = queryReturn.getInt(0);
        queryReturn.close();
        ContentValues cv = new ContentValues();
        cv.put("driver_id", id);
        ttg_db.update("vehicle", cv, "number='"+number+"'", null);
        cv.clear();
    }

    public void addTrip(String date, String startTime, String finishTime, String vehicleNumber)
    {
        queryReturn = ttg_db.rawQuery("SELECT way_number FROM vehicle WHERE number='" + vehicleNumber + "';", null);
        queryReturn.moveToFirst();
        int wayNumber = queryReturn.getInt(0);
        queryReturn.close();
        String[] splt = date.split("\\.");
        String day = splt[0] + "-" + splt[1] + "-" + splt[2];
        ContentValues cv = new ContentValues();
        cv.put("trip_date", day);
        cv.put("start_time", startTime);
        cv.put("finish_time", finishTime);
        cv.put("way_number", wayNumber);
        cv.put("vehicle_number", vehicleNumber);
        ttg_db.insert("trip", null, cv);
        cv.clear();
    }

    public ArrayList<String> getOwnerWays(String login)
    {
        ArrayList<String> strForReturn = new ArrayList<>();
        queryReturn = ttg_db.rawQuery("SELECT fio FROM users WHERE login='" + login + "';", null);
        queryReturn.moveToFirst();
        String fio = queryReturn.getString(0);
        queryReturn.close();
        queryReturn = ttg_db.rawQuery("SELECT way.way_number FROM way JOIN owner ON way.owner_id=owner.owner_id WHERE owner.fio='" + fio + "';", null);
        while (queryReturn.moveToNext())
        {
                strForReturn.add(0, queryReturn.getString(0));
        }
        queryReturn.close();
        return strForReturn;
    }

    public ArrayList<String> getVehicles(String way)
    {
        ArrayList<String> arr = new ArrayList<>();
        queryReturn = ttg_db.rawQuery("SELECT DISTINCT vehicle.number, vehicle.brand, vehicle.model, vehicle.release_date, vehicle.capacity, vehicle.status FROM vehicle " +
                "WHERE way_number='" + way + "';", null);
        String strForReturn;
        while(queryReturn.moveToNext())
        {
            strForReturn = queryReturn.getString(0) + "\nБренд: " + queryReturn.getString(1) + "\nМодель: " +
                    queryReturn.getString(2) + "\nДата выпуска: " + queryReturn.getString(3) + "\nВместимость: " +
                    queryReturn.getString(4) +"\nСтатус: " + queryReturn.getString(5) +
                    "\nКоличество рейсов: " + getCountOfTrips(queryReturn.getString(0));
            arr.add(0, strForReturn);

        }
        queryReturn.close();
        return arr;
    }

    public void editVehicle(String number, String brand, String model, String release, int capacity)
    {
        ContentValues cv = new ContentValues();
        cv.put("brand", brand);
        cv.put("model", model);
        cv.put("release_date", release);
        cv.put("capacity", capacity);
        ttg_db.update("vehicle", cv, "number='" + number + "'", null);
        cv.clear();
    }

    public void deleteVehicle(String number)
    {
        ttg_db.delete("vehicle", "number='" + number + "'", null);
    }

    public void addVehicle(String number, String brand, String model, String release, String capacity, int wayNumber)
    {
        ContentValues cv = new ContentValues();
        cv.put("number", number);
        cv.put("brand", brand);
        cv.put("model", model);
        cv.put("release_date", release);
        cv.put("capacity", capacity);
        cv.put("driver_id", NULL);
        cv.put("way_number", wayNumber);
        ttg_db.insert("vehicle", null, cv);
        cv.clear();
    }

    public void closeDB()
    {
        ttg_db.close();
    }

    public boolean numberIsBusy(String number)
    {
        queryReturn = ttg_db.rawQuery("SELECT count(*) FROM vehicle WHERE driver_id=NULL AND number='" + number + "';", null);
        queryReturn.moveToFirst();
        if(queryReturn.getInt(0) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public ArrayList<String> getAllWayStations()
    {
        ArrayList<String> arr = new ArrayList<>();
        String buffer;
        queryReturn = ttg_db.rawQuery("SELECT * FROM station", null);
        while(queryReturn.moveToNext())
        {
            buffer = queryReturn.getString(0)+"#"+"Название: " + queryReturn.getString(1) + "\nМесто: " + queryReturn.getString(2);
            arr.add(buffer);
        }
        queryReturn.close();
        return arr;
    }

    public void addNewWay(String login, ArrayList<String> id, String wayNumber)
    {
        int ownerId = -1;
        queryReturn = ttg_db.rawQuery("SELECT owner_id FROM owner JOIN users ON users.fio=owner.fio WHERE login='"+login+"';", null);
        queryReturn.moveToFirst();
        ownerId = queryReturn.getInt(0);
        queryReturn.close();
        ContentValues cv = new ContentValues();
        cv.put("way_number", wayNumber);
        cv.put("owner_id", ownerId);
        ttg_db.insert("way", null, cv);
        cv.clear();
        for(int i = 0; i<id.size(); i++)
        {
            cv.put("way_number", wayNumber);
            cv.put("station_id", id.get(i));
            ttg_db.insert("way_station", null, cv);
            cv.clear();
        }
    }

    public String getAverageTime(String wayNumber)
    {
        String buffer;
        queryReturn = ttg_db.rawQuery("SELECT AVG((strftime('%s', finish_time) - strftime('%s',start_time))/60) FROM way \n" +
                "JOIN trip ON way.way_number=trip.way_number \n" +
                "WHERE way.way_number='" + wayNumber + "' AND strftime('%Y%m', trip.trip_date)=strftime('%Y%m', 'now');", null);
        queryReturn.moveToFirst();
        if(queryReturn.isNull(0))
        {
            buffer = "--";
        }
        else
        {
            buffer = queryReturn.getString(0);
        }
        queryReturn.close();
        return buffer;
    }

    public String getCountOfTrips(String vehicleNumber)
    {
        Cursor crs = ttg_db.rawQuery("SELECT count(*) FROM trip WHERE trip.vehicle_number='" + vehicleNumber + "';", null);
        crs.moveToFirst();
        String buffer = crs.getString(0);
        crs.close();
        return buffer;
    }
}
