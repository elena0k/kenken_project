package risolutore;

import componenti.Coordinate;
import componenti.Gruppo;
import memento.Memento;
import memento.Originator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KenkenGrid extends Problema<Integer,Integer> implements Originator {

    private int[][] griglia;
    private int dim;
    private int nrSol;
    private ArrayList<int[][]> listaSoluzioni;
    private LinkedList<Gruppo> listaGruppi;


    public KenkenGrid(int dim)
    {
        this.dim=dim;
        this.griglia= new int[dim][dim];

        nrSol=0;
        listaSoluzioni=new ArrayList<>();
        listaGruppi= new LinkedList<>();

    }

    public void addGroup(Gruppo gruppo)
    {
        this.listaGruppi.add(gruppo);
    }

    public void printGroups()
    {
        for(Gruppo g:listaGruppi)
            System.out.println("GRUPPI"+g+"\n");
    }
    @Override
    protected Integer primoPuntoDiScelta() {
        return 0;
    }

    @Override
    protected Integer prossimoPuntoDiScelta(Integer ps) {
        return ps+1;
    }

    @Override
    protected Integer ultimoPuntoDiScelta() {
        return (dim*dim)-1;
    }

    @Override
    protected Integer primaScelta(Integer ps) {
        return 1;
    }

    @Override
    protected Integer prossimaScelta(Integer valore) {
        return valore+1;
    }

    @Override
    protected Integer ultimaScelta(Integer ps) {
        return dim;
    }

    @Override
    protected boolean assegnabile(Integer scelta, Integer puntoDiScelta) {
        return verificaColonna(puntoDiScelta%dim,scelta)
                &&verificaRiga(puntoDiScelta/dim,scelta)
                &&verificaGruppo(puntoDiScelta,scelta)
                &&griglia[puntoDiScelta/dim][puntoDiScelta%dim]==0
                &&scelta>0&&scelta<=dim;
    }

    @Override
    protected void assegna(Integer scelta, Integer puntoDiScelta) {
        griglia[puntoDiScelta/dim][puntoDiScelta%dim]=scelta;
    }

    @Override
    protected void deassegna(Integer scelta, Integer puntoDiScelta) {
        griglia[puntoDiScelta/dim][puntoDiScelta%dim]=0;
    }

    @Override
    protected Integer precedentePuntoDiScelta(Integer puntoDiScelta) {
        return puntoDiScelta-1;
    }

    @Override
    protected Integer ultimaSceltaAssegnataA(Integer puntoDiScelta) {
        return griglia[puntoDiScelta/dim][puntoDiScelta%dim];
    }

    private int[][] getGriglia()
    {
        int[][]grigliaCopia=new int[dim][dim];
        for(int i=0;i<dim;i++)
            for(int j=0;j<dim;j++)
                grigliaCopia[i][j]=this.griglia[i][j];
        return grigliaCopia;
    }

    @Override
    protected void scriviSoluzione(int nr_sol) {
        for(int i=0;i<dim;i++)
            System.out.println(java.util.Arrays.toString(griglia[i]));
        System.out.println();
        listaSoluzioni.add(getGriglia());
    }

    private boolean verificaColonna(Integer j,Integer scelta)
    {
        for(int i=0;i<this.dim;i++)
            if(griglia[i][j]==scelta)
                return false;
        return true;
    }

    private boolean verificaRiga(Integer i, Integer scelta)
    {
        for(int j=0;j<this.dim;j++)
            if(griglia[i][j]==scelta)
                return false;
        return true;
    }

    private boolean verificaGruppo(int puntoDiScelta, Integer scelta) { //private
        boolean ret=true;
        for(Gruppo gruppo: listaGruppi)
        {
            if(gruppo.contains(new Coordinate(puntoDiScelta/dim, puntoDiScelta%dim))) {
                ret = verificaVincoli(gruppo,scelta);
                break;
            }
        }
        return ret;
    }

    private boolean verificaVincoli(Gruppo gruppo, Integer scelta) {  //private
        LinkedList<Coordinate> celle=gruppo.getListaCelle();
        if(!completo(celle))
            return true;  //il gruppo non è ancora pieno
        int risultato=scelta;
        for(Coordinate c:celle)
        {
            if(griglia[c.getRiga()][c.getColonna()]!=0) {
                if (gruppo.getOperazione().equals("+"))
                    risultato += griglia[c.getRiga()][c.getColonna()];
                if (gruppo.getOperazione().equals("-") || gruppo.getOperazione().equals("%"))
                    risultato = ordina(griglia[c.getRiga()][c.getColonna()],risultato,gruppo.getOperazione());
                if (gruppo.getOperazione().equals("x"))
                    risultato *= griglia[c.getRiga()][c.getColonna()];
            }
        }
        return risultato==gruppo.getVincolo();
    }

    private int ordina(int op1, int op2,String operatore) {
        int ret;
        if(op1<op2)
        {
            int tmp=op1;
            op1=op2;
            op2=tmp;
        }
        if(operatore.equals("%"))
            ret=op1/op2;
        else
            ret=op1-op2;
        return ret;
    }

    private boolean completo(LinkedList<Coordinate> celle) {
        int numZeri=0;
        for(Coordinate c: celle)
            if(griglia[c.getRiga()][c.getColonna()]==0)
                numZeri++;
        return numZeri==1;
    }

    @Override
    public Memento getMemento() {
        return new GruppiMemento();
    }

    @Override
    public void setMemento(Memento memento) {
        if(!(memento instanceof GruppiMemento))
            throw new IllegalArgumentException();
        GruppiMemento gruppiMemento= (GruppiMemento) memento;

        if(this!=gruppiMemento.getOriginator())
            throw new IllegalArgumentException();

        this.listaGruppi=gruppiMemento.listaGruppi;

    }

    private class GruppiMemento implements Memento{

        LinkedList<Gruppo> listaGruppi;

        KenkenGrid getOriginator() {
            return KenkenGrid.this;
        }

        GruppiMemento() {
            this.listaGruppi=new LinkedList<>();
            for(Gruppo g: KenkenGrid.this.listaGruppi) {
                listaGruppi.add(new Gruppo(g));
            }
        }



    }



    public static void main(String[] args)
    {

        Coordinate c0= new Coordinate(0,0);
        Coordinate c1= new Coordinate(0,1);
        Coordinate c2= new Coordinate(0,2);
        Coordinate c3= new Coordinate(1,0);
        Coordinate c4= new Coordinate(1,1);
        Coordinate c5= new Coordinate(1,2);
        Coordinate c6= new Coordinate(2,0);
        Coordinate c7= new Coordinate(2,1);
        Coordinate c8= new Coordinate(2,2);
        LinkedList<Coordinate> l1= new LinkedList<>();
        LinkedList<Coordinate> l2= new LinkedList<>();
        LinkedList<Coordinate> l3= new LinkedList<>();
        LinkedList<Coordinate> l4= new LinkedList<>();
        LinkedList<Coordinate> l5= new LinkedList<>();
        l1.add(c1);l1.add(c0);
        l2.add(c2); l2.add(c5);
        l3.add(c3); l3.add(c4); l3.add(c6);
        l4.add(c7); l4.add(c8);

        Gruppo g1= new Gruppo(3,"%",l1);
        Gruppo g2= new Gruppo(2,"%",l2);
        Gruppo g3= new Gruppo(6,"x",l3);
        Gruppo g4= new Gruppo(1,"-",l4);

        LinkedList<Gruppo> gruppi=new LinkedList<>();
        gruppi.add(g1); gruppi.add(g2); gruppi.add(g3); gruppi.add(g4);

        KenkenGrid kenken=new KenkenGrid(3);
        kenken.risolvi();
    }


}
