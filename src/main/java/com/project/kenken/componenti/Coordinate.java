package com.project.kenken.componenti;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Coordinate implements Serializable {


    @JsonProperty("riga")
    private int riga;

    @JsonProperty("colonna")
    private int colonna;

    public Coordinate(@JsonProperty("riga") int riga, @JsonProperty("colonna") int colonna) {
        this.riga = riga;
        this.colonna = colonna;
    }

    public Coordinate(Coordinate other) {
        this.riga = other.riga;
        this.colonna = other.colonna;
    }

    public int getRiga() {
        return riga;
    }

    public int getColonna() {
        return colonna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        if (riga != that.riga) return false;
        return colonna == that.colonna;
    }

    @Override
    public String toString() {
        return "{" +
                "riga=" + riga +
                ", colonna=" + colonna +
                '}';
    }
}
