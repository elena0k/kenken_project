package com.project.kenken.risolutore;

import com.project.kenken.componenti.Coordinate;
import com.project.kenken.componenti.Gruppo;
import com.project.kenken.memento.Memento;
import com.project.kenken.memento.Originator;
import com.project.kenken.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KenkenGrid extends Problema<Integer, Integer> implements Originator {

    private int[][] griglia;
    private int dim;
    private int nrSol;
    private ArrayList<int[][]> listaSoluzioni;
    private LinkedList<Gruppo> listaGruppi;


    public KenkenGrid(int dim) {
        this.dim = dim;
        this.griglia = new int[dim][dim];
        nrSol = 0;
        listaSoluzioni = new ArrayList<>();
        listaGruppi = new LinkedList<>();
    }

    public KenkenGrid(int dim, int maxSol) {
        super(maxSol);
        this.dim = dim;
        this.griglia = new int[dim][dim];
        nrSol = 0;
        listaSoluzioni = new ArrayList<>();
        listaGruppi = new LinkedList<>();
    }

    public int getNrSol() {
        return nrSol;
    }

    public ArrayList<int[][]> getListaSoluzioni(){ //TODO fare copia profonda
        return new ArrayList<>(listaSoluzioni);
    }

    public void setListaGruppi(List<Gruppo> gruppi){
        for(int i=0; i<gruppi.size();i++){
            this.listaGruppi.add(new Gruppo(gruppi.get(i)));
        }
    }

    public void addGroup(Gruppo gruppo) {
        this.listaGruppi.add(new Gruppo(gruppo));
    }


    public void printGroups() {
        System.out.println("GRUPPI:  ");
        for (Gruppo g : listaGruppi)
            System.out.println("" + g + "\n");
    }

    public List<Gruppo> getGroups() {
        List<Gruppo> ret = new LinkedList<>();
        for (Gruppo g : listaGruppi)
            ret.add(new Gruppo(g));
        return ret;

    }

    @Override
    protected Integer primoPuntoDiScelta() {
        return 0;
    }

    @Override
    protected Integer prossimoPuntoDiScelta(Integer ps) {
        return ps + 1;
    }

    @Override
    protected Integer ultimoPuntoDiScelta() {
        return (dim * dim) - 1;
    }

    @Override
    protected Integer primaScelta(Integer ps) {
        return 1;
    }

    @Override
    protected Integer prossimaScelta(Integer valore) {
        return valore + 1;
    }

    @Override
    protected Integer ultimaScelta(Integer ps) {
        return dim;
    }

    @Override
    protected boolean assegnabile(Integer scelta, Integer puntoDiScelta) {
        return verificaColonna(puntoDiScelta % dim, scelta)
                && verificaRiga(puntoDiScelta / dim, scelta)
                && verificaGruppo(puntoDiScelta, scelta)
                && griglia[puntoDiScelta / dim][puntoDiScelta % dim] == 0
                && scelta > 0 && scelta <= dim;
    }

    @Override
    protected void assegna(Integer scelta, Integer puntoDiScelta) {
        griglia[puntoDiScelta / dim][puntoDiScelta % dim] = scelta;
    }

    @Override
    protected void deassegna(Integer scelta, Integer puntoDiScelta) {
        griglia[puntoDiScelta / dim][puntoDiScelta % dim] = 0;
    }

    @Override
    protected Integer precedentePuntoDiScelta(Integer puntoDiScelta) {
        return puntoDiScelta - 1;
    }

    @Override
    protected Integer ultimaSceltaAssegnataA(Integer puntoDiScelta) {
        return griglia[puntoDiScelta / dim][puntoDiScelta % dim];
    }

    private int[][] getGriglia() {
        int[][] grigliaCopia = new int[dim][dim];
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                grigliaCopia[i][j] = this.griglia[i][j];
        return grigliaCopia;
    }

    @Override
    protected void scriviSoluzione(int nr_sol) {
        for (int i = 0; i < dim; i++)
            System.out.println(java.util.Arrays.toString(griglia[i]));
        System.out.println();
        listaSoluzioni.add(getGriglia());
        this.nrSol = listaSoluzioni.size();
    }

    public boolean verificaColonna(Integer j, Integer scelta) {
        for (int i = 0; i < this.dim; i++)
            if (griglia[i][j] == scelta)
                return false;
        return true;
    }

    public boolean verificaRiga(Integer i, Integer scelta) {
        for (int j = 0; j < this.dim; j++)
            if (griglia[i][j] == scelta)
                return false;
        return true;
    }

    public boolean verificaGruppo(int puntoDiScelta, Integer scelta) { //private
        boolean ret = true;
        for (Gruppo gruppo : listaGruppi) {
            if (gruppo.contains(new Coordinate(puntoDiScelta / dim, puntoDiScelta % dim))) {
                ret = verificaVincoli(gruppo, scelta);
                break;
            }
        }
        return ret;
    }

    private boolean verificaVincoli(Gruppo gruppo, Integer scelta) {  //private
        LinkedList<Coordinate> celle = gruppo.getListaCelle();
        if (!completo(celle))
            return true;  //il gruppo non è ancora pieno
        int risultato = scelta;
        for (Coordinate c : celle) {
            if (griglia[c.getRiga()][c.getColonna()] != 0) {
                if (gruppo.getOperazione().equals("+"))
                    risultato += griglia[c.getRiga()][c.getColonna()];
                if (gruppo.getOperazione().equals("-") || gruppo.getOperazione().equals("%"))
                    risultato = ordina(griglia[c.getRiga()][c.getColonna()], risultato, gruppo.getOperazione());
                if (gruppo.getOperazione().equals("x"))
                    risultato *= griglia[c.getRiga()][c.getColonna()];
            }
        }
        return risultato == gruppo.getVincolo();
    }

    public static int ordina(int op1, int op2, String operatore) {
        int ret;
        if (op1 < op2) {
            int tmp = op1;
            op1 = op2;
            op2 = tmp;
        }
        if (operatore.equals("%"))
            ret = op1 / op2;
        else
            ret = op1 - op2;
        return ret;
    }

    private boolean completo(LinkedList<Coordinate> celle) {
        int numZeri = 0;
        for (Coordinate c : celle)
            if (griglia[c.getRiga()][c.getColonna()] == 0)
                numZeri++;
        return numZeri == 1;
    }

    @Override
    public Memento getMemento() {
        return new GruppiMemento();
    }

    @Override
    public void setMemento(Memento memento) {
        if (!(memento instanceof GruppiMemento))
            throw new IllegalArgumentException();
        GruppiMemento gruppiMemento = (GruppiMemento) memento;

        if (this != gruppiMemento.getOriginator())
            throw new IllegalArgumentException();

        this.listaGruppi = gruppiMemento.listaGruppi;

    }

    private class GruppiMemento implements Memento {

        LinkedList<Gruppo> listaGruppi;

        KenkenGrid getOriginator() {
            return KenkenGrid.this;
        }

        GruppiMemento() {
            this.listaGruppi = new LinkedList<>();
            for (Gruppo g : KenkenGrid.this.listaGruppi) {
                listaGruppi.add(new Gruppo(g));
            }
        }


    }


    public static void main(String[] args) {

        List<Gruppo> gruppi=Utils.templateGroups();

        KenkenGrid kenken = new KenkenGrid(3, 8);
        kenken.setListaGruppi(gruppi);
        System.out.println("Impostati"+kenken.getGroups());

        kenken.risolvi();
        System.out.println(kenken.getNrSol());
    }


}
