package com.project.kenken.risolutore;

import com.project.kenken.componenti.Coordinate;
import com.project.kenken.componenti.Gruppo;
import com.project.kenken.memento.Memento;
import com.project.kenken.memento.Originator;
import com.project.kenken.utils.TemplateKenken;
import com.project.kenken.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KenkenGrid extends Problema<Integer, Integer> implements Originator {

    private int[][] griglia;
    private int dim;
    private int nrSol=0;
    private ArrayList<int[][]> listaSoluzioni;
    private LinkedList<Gruppo> listaGruppi;


    public KenkenGrid(int dim) {
        this.dim = dim;
        this.griglia = new int[dim][dim];
        listaSoluzioni = new ArrayList<>();
        listaGruppi = new LinkedList<>();
    }

    public KenkenGrid(int dim, int maxSol) {
        super(maxSol);
        this.dim = dim;
        this.griglia = new int[dim][dim];
        listaSoluzioni = new ArrayList<>();
        listaGruppi = new LinkedList<>();
    }

    public KenkenGrid(List<Gruppo> groups,int dim,int maxSol){
        this(dim,maxSol);
        for(int i=0;i<groups.size();i++)
            this.listaGruppi.add(new Gruppo(groups.get(i)));
    }

    public KenkenGrid(KenkenGrid other){
        this.griglia=Utils.copiaProfondaMatriceInt(other.griglia);
        this.dim= other.dim;
        this.nrSol= other.nrSol;
        this.listaGruppi= new LinkedList<>();
        this.listaSoluzioni=new ArrayList<>();
        for(int i=0;i<other.listaGruppi.size();i++)
            this.listaGruppi.add(new Gruppo(other.listaGruppi.get(i)));
        for(int i=0;i<other.listaSoluzioni.size();i++)
            this.listaSoluzioni.add(Utils.copiaProfondaMatriceInt(other.listaSoluzioni.get(i)));




    }

    public int getDim(){return this.dim;}

    public int getNrSol() {return nrSol;}

    public ArrayList<int[][]> getListaSoluzioni(){
        ArrayList<int[][]> ret= new ArrayList<>();
        for(int i=0;i<listaSoluzioni.size();i++)
            ret.add(Utils.copiaProfondaMatriceInt(listaSoluzioni.get(i)));
        return ret;
    }

    private int[][] getGriglia() {
        return Utils.copiaProfondaMatriceInt(this.griglia);
    }

    public void setGroupsList(List<Gruppo> gruppi){
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

    @Override
    protected void scriviSoluzione(int nr_sol) {
        for (int i = 0; i < dim; i++)
            System.out.println(java.util.Arrays.toString(griglia[i]));
        System.out.println();
        listaSoluzioni.add(getGriglia());
        this.nrSol = listaSoluzioni.size();
    }

    private boolean verificaColonna(Integer j, Integer scelta) {
        for (int i = 0; i < this.dim; i++)
            if (griglia[i][j] == scelta)
                return false;
        return true;
    }

    private boolean verificaRiga(Integer i, Integer scelta) {
        for (int j = 0; j < this.dim; j++)
            if (griglia[i][j] == scelta)
                return false;
        return true;
    }

    private boolean verificaGruppo(int puntoDiScelta, Integer scelta) { //private
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
                    risultato = Utils.ordina(griglia[c.getRiga()][c.getColonna()], risultato, gruppo.getOperazione());
                if (gruppo.getOperazione().equals("x"))
                    risultato *= griglia[c.getRiga()][c.getColonna()];
            }
        }
        return risultato == gruppo.getVincolo();
    }

    //se c'è un solo zero il gruppo si completa nel momento in cui inseriamo la scelta
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
}
