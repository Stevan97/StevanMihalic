package com.ftninformatika.stevanmihalic.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Task.ToDoZadatak)
public class Task {

    public static final String ToDoZadatak = "toDoZadatak";
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAZIV = "naziv";
    private static final String FIELD_OPIS = "opis";
    private static final String TIP_PRIORITETA = "tipPrioriteta";
    private static final String VREME_KREIRANJA = "vremeKreiranja";
    private static final String VREME_ZAVRSETKA = "vremeZavrsetka";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_GRUPA = "grupa";

    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV)
    private String naziv;

    @DatabaseField(columnName = FIELD_OPIS)
    private String opis;

    @DatabaseField(columnName = TIP_PRIORITETA)
    private String tipPrioriteta;

    @DatabaseField(columnName = VREME_KREIRANJA)
    private String vremeKreiranja;

    @DatabaseField(columnName = VREME_ZAVRSETKA)
    private String vremeZavrsetka;

    @DatabaseField(columnName = FIELD_STATUS)
    private String status;

    @DatabaseField(columnName = FIELD_GRUPA, foreign = true, foreignAutoRefresh = true)
    private Grupa grupa;

    public Task() {

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

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getTipPrioriteta() {
        return tipPrioriteta;
    }

    public void setTipPrioriteta(String tipPrioriteta) {
        this.tipPrioriteta = tipPrioriteta;
    }

    public String getVremeKreiranja() {
        return vremeKreiranja;
    }

    public void setVremeKreiranja(String vremeKreiranja) {
        this.vremeKreiranja = vremeKreiranja;
    }

    public String getVremeZavrsetka() {
        return vremeZavrsetka;
    }

    public void setVremeZavrsetka(String vremeZavrsetka) {
        this.vremeZavrsetka = vremeZavrsetka;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Grupa getGrupa() {
        return grupa;
    }

    public void setGrupa(Grupa grupa) {
        this.grupa = grupa;
    }

    public String toString() {
        return naziv;
    }

}
