package com.project.kenken.componenti;

import java.io.Serializable;

public class Coordinate implements Serializable {

    private int riga;
    private int colonna;

    public Coordinate(int riga, int colonna) {
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
    public int hashCode() {
        int result = riga;
        result = 31 * result + colonna;
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "riga=" + riga +
                ", colonna=" + colonna +
                '}';
    }
}
