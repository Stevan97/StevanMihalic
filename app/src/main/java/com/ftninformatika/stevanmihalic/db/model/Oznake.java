package com.ftninformatika.stevanmihalic.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Oznake.OZNAKE)
public class Oznake {

    public static final String OZNAKE = "oznake";
    private static final String FIELD_ID = "id";
    private static final String FIELD_OZNAKA = "oznaka";
    private static final String FIELD_GRUPA = "grupa";


    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_OZNAKA)
    private String oznaka;

    @DatabaseField(columnName = FIELD_GRUPA, foreignAutoRefresh = true, foreign = true)
    private Grupa grupa;

    public Oznake() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public Grupa getGrupa() {
        return grupa;
    }

    public void setGrupa(Grupa grupa) {
        this.grupa = grupa;
    }

    public String toString() {
        return oznaka;
    }

}
