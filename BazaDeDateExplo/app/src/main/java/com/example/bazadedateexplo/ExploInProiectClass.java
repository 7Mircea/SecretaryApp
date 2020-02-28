package com.example.bazadedateexplo;

public class ExploInProiectClass {
    int id;
    int idProiect;
    int idExplorator;
    int nrOre;

    ExploInProiectClass() {}

    ExploInProiectClass(int id, int idProiect, int idExplorator, int nrOre) {
        this.id = id;
        this.idProiect = idProiect;
        this.idExplorator = idExplorator;
        this.nrOre = nrOre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProiect() {
        return idProiect;
    }

    public void setIdProiect(int idProiect) {
        this.idProiect = idProiect;
    }

    public int getIdExplorator() {
        return idExplorator;
    }

    public void setIdExplorator(int idExplorator) {
        this.idExplorator = idExplorator;
    }

    public int getNrOre() {
        return nrOre;
    }

    public void setNrOre(int nrOre) {
        this.nrOre = nrOre;
    }
}
