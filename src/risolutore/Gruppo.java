package risolutore;

import java.util.LinkedList;
import java.util.List;

public class Gruppo {

    //TODO faccio un builder nel caso si oglia implementare il gruppo in modo diverso
    private LinkedList<Coordinate> listaCelle;
    private int vincolo;
    private String operazione;

    public Gruppo(){
        listaCelle= new LinkedList<>();
    }

    public Gruppo(int vincolo, String op, LinkedList<Coordinate> lista) {
        this.vincolo=vincolo;
        this.operazione=op;
        listaCelle=new LinkedList<>(List.copyOf(lista));
    }

    public LinkedList<Coordinate> getListaCelle() {
        return new LinkedList<>(List.copyOf(listaCelle));
    }

    public void addCella(int x, int y)
    {
        this.listaCelle.add(new Coordinate(x,y));
    }

    public int getVincolo() {
        return vincolo;
    }

    public String getOperazione() {
        return operazione;
    }

    public boolean contains(Coordinate cord)
    {
        return listaCelle.contains(cord);
    }

    public void setVincolo(int vincolo) {
        this.vincolo = vincolo;
    }

    public void setOperazione(String operazione) {
        this.operazione = operazione;
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
