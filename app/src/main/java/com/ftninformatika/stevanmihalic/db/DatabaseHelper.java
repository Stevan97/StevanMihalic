package com.ftninformatika.stevanmihalic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ftninformatika.stevanmihalic.db.model.Grupa;
import com.ftninformatika.stevanmihalic.db.model.Task;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "stevan.mihalic";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private Dao<Grupa, Integer> getmGrupa = null;
    private Dao<Task, Integer> getmToDoZadatak = null;

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Grupa.class);
            TableUtils.createTable(connectionSource, Task.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Task.class, true);
            TableUtils.dropTable(connectionSource, Grupa.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Grupa, Integer> getGrupa() throws SQLException {
        if (getmGrupa == null) {
            getmGrupa = getDao(Grupa.class);
        }
        return getmGrupa;
    }

    public Dao<Task, Integer> getToDoZadatak() throws SQLException {
        if (getmToDoZadatak == null) {
            getmToDoZadatak = getDao(Task.class);
        }
        return getmToDoZadatak;
    }

    @Override
    public void close() {
        getmToDoZadatak = null;
        getmGrupa = null;
        super.close();
    }
}
