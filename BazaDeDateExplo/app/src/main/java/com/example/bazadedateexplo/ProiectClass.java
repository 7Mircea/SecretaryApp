package com.example.bazadedateexplo;

public class ProiectClass {
    private int idProiect;
    private String nume;
    private String dataStart;
    private String dataFinal;
    private String descriereScurta;

    ProiectClass(){}

    ProiectClass(int idProiect, String nume, String dataStart, String dataFinal, String descriereScurta) {
        this.idProiect = idProiect;
        this.nume = nume;
        this.dataStart = dataStart;
        this.dataFinal = dataFinal;
        this.descriereScurta = descriereScurta;
    }

    public int getIdProiect() {
        return idProiect;
    }

    public void setIdProiect(int idProiect) {
        this.idProiect = idProiect;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getDataStart() {
        return dataStart;
    }

    public void setDataStart(String dataStart) {
        this.dataStart = dataStart;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDescriereScurta() {
        return descriereScurta;
    }

    public void setDescriereScurta(String descriereScurta) {
        this.descriereScurta = descriereScurta;
    }
}

