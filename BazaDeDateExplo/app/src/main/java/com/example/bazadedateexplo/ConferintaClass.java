package com.example.bazadedateexplo;

public class ConferintaClass {
    int idConferinta;
    String nume;
    int idDirector;

    ConferintaClass() {}

    ConferintaClass(int idConferinta, String nume, int idDirector) {
        this.idConferinta = idConferinta;
        this.nume = nume;
        this.idDirector = idDirector;
    }

    public int getIdConferinta() {
        return idConferinta;
    }

    public void setIdConferinta(int idConferinta) {
        this.idConferinta = idConferinta;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getIdDirector() {
        return idDirector;
    }

    public void setIdDirector(int idDirector) {
        this.idDirector = idDirector;
    }
}
