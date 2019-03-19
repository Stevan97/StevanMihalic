package com.ftninformatika.stevanmihalic.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = Grupa.GRUPA)
public class Grupa {

    public static final String GRUPA = "grupa";
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAZIV = "naziv";
    private static final String FIELD_VREME_KREIRANJA = "vremeKreiranja";
    private static final String FIELD_LISTA_OZNAKA = "listaOznaka";
    private static final String FIELD_toDOZADACI = "toDoZadaci";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV)
    private String naziv;

    @DatabaseField(columnName = FIELD_VREME_KREIRANJA)
    private String vremeKreiranjaGrupe;

    @ForeignCollectionField(columnName = FIELD_LISTA_OZNAKA,eager = true)
    private ForeignCollection<Oznake> oznake;

    @ForeignCollectionField(columnName = FIELD_toDOZADACI, eager = true)
    private ForeignCollection<Task> toDoZadaci;

    public Grupa() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getVremeKreiranjaGrupe() {
        return vremeKreiranjaGrupe;
    }

    public void setVremeKreiranjaGrupe(String vremeKreiranjaGrupe) {
        this.vremeKreiranjaGrupe = vremeKreiranjaGrupe;
    }

    public ForeignCollection<Oznake> getOznake() {
        return oznake;
    }

    public void setOznake(ForeignCollection<Oznake> oznake) {
        this.oznake = oznake;
    }

    public ForeignCollection<Task> getToDoZadaci() {
        return toDoZadaci;
    }

    public void setToDoZadaci(ForeignCollection<Task> toDoZadaci) {
        this.toDoZadaci = toDoZadaci;
    }

    public String toString() {
        return naziv;
    }

}
