package com.project.kenken.componenti;

import java.io.Serializable;
import java.util.LinkedList;

public class Gruppo implements Serializable { //blocchi della griglia

    private LinkedList<Coordinate> listaCelle;
    private int vincolo;
    private String operazione;

    public Gruppo() {
        listaCelle = new LinkedList<>();
    }

    public Gruppo(int vincolo, String op, LinkedList<Coordinate> lista) {
        this.vincolo = vincolo;
        this.operazione = op;
        LinkedList<Coordinate> nuovo = new LinkedList<>();

        for (Coordinate c : lista)
            nuovo.add(new Coordinate(c));

        this.listaCelle = nuovo;
    }

    public Gruppo(Gruppo other) {
        this.vincolo = other.vincolo;
        this.operazione = other.operazione;
        LinkedList<Coordinate> nuovo = new LinkedList<>();

        for (Coordinate c : other.listaCelle)
            nuovo.add(new Coordinate(c));

        this.listaCelle = nuovo;
    }

    public LinkedList<Coordinate> getListaCelle() {
        LinkedList<Coordinate> nuovo = new LinkedList<>();
        for (Coordinate c : this.listaCelle)
            nuovo.add(new Coordinate(c));
        return nuovo;

    }

    public void addCella(int x, int y) {
        this.listaCelle.add(new Coordinate(x, y));
    }

    public int getVincolo() {
        return vincolo;
    }

    public String getOperazione() {
        return operazione;
    }

    public void setVincolo(int vincolo) {
        this.vincolo = vincolo;
    }

    public void setOperazione(String operazione) {
        this.operazione = operazione;
    }

    public boolean contains(Coordinate cord) {
        return listaCelle.contains(cord);
    }

    @Override
    public String toString() {
        return "Gruppo{" +
                "listaCelle=" + listaCelle +
                ", vincolo=" + vincolo +
                ", operazione='" + operazione + '\'' +
                '}';
    }
}
