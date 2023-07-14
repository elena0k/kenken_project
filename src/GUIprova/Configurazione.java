package GUIprova;

import decoratorCelle.ICella;
import risolutore.Coordinate;
import risolutore.Gruppo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class Configurazione implements State {

    private Gruppo gruppoTmp;

    @Override
    public void selezionaGruppi(GrigliaGUI grigliaGUI) {

        ICella[][] griglia=grigliaGUI.getGrigliaCelle();

        for (int i = 0; i < grigliaGUI.getN(); i++) {
            for (int j = 0; j < grigliaGUI.getN(); j++) {

                griglia[i][j].setEnabled(false);
                griglia[i][j].setMouseAdapter((new MouseAdapter(){
                    public void mouseClicked(MouseEvent e) {

                        System.out.println("dio banana");
                        boolean primoElemento=false;


                        for(int i=0; i< grigliaGUI.getN();i++) {
                            for(int j=0; j<grigliaGUI.getN(); j++) {

                                if(e.getSource()== griglia[i][j].getText()) {
                                    if (e.getButton() == MouseEvent.BUTTON1) {
                                        if(! grigliaGUI.getGruppoInserito()) {
                                            gruppoTmp = new Gruppo();
                                            primoElemento=true;
                                        }
                                        if(!grigliaGUI.getCellaImpostata()[i][j]) {
                                            if (adiacenti(new Coordinate(i, j), gruppoTmp.getListaCelle()) || primoElemento) {
                                                grigliaGUI.setGruppoInserito(true);
                                                griglia[i][j].getText().setBackground(new Color(210, 210, 210));
                                                gruppoTmp.addCella(i, j);
                                                grigliaGUI.getCellaImpostata()[i][j] = true;
                                                System.out.println("impostata cella:<" + i + ":" + j+">");
                                                primoElemento = false;
                                            }
                                            else primoElemento=false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }));
            }
        }

        if(grigliaGUI.isConfigurata())
            grigliaGUI.setState(new Play());
    }

    @Override
    public void scriviNumero(GrigliaGUI griglia) {
        System.out.println("Prima è necessario terminare la configurazione della griglia di gioco");

    }

    @Override
    public void resetGioco(GrigliaGUI griglia) {
        griglia.resetConfigurazione();
        selezionaGruppi(griglia);

    }

    @Override
    public void terminaGioco(GrigliaGUI griglia) {

    }

    private boolean adiacenti(Coordinate cur, LinkedList<Coordinate> listaCelle) {

        for(Coordinate c:listaCelle) {
            if(adiacenteDx(c,cur) || adiacenteSx(c,cur) || adiacenteUp(c,cur) || adiacenteDown(c,cur))
                return true;
        }
        return false;
    }

    private boolean adiacenteDown(Coordinate c, Coordinate cur) {
        return c.getColonna()==cur.getColonna() && c.getRiga()+1==cur.getRiga();
    }

    private boolean adiacenteUp(Coordinate c, Coordinate cur) {
        return c.getColonna()==cur.getColonna() && c.getRiga()-1==cur.getRiga();
    }

    private boolean adiacenteSx(Coordinate c, Coordinate cur) {
        return c.getRiga()==cur.getRiga() && c.getColonna()-1==cur.getColonna();
    }

    private boolean adiacenteDx(Coordinate c, Coordinate cur) {
        return c.getRiga()==cur.getRiga() && c.getColonna()+1==cur.getColonna();
    }


    /*
    @Override
    public void terminaConfigurazione(Finestra kenken) {
        kenken.setState(new Play());
    }

    @Override
    public void resetGioco(Finestra kenken) {
        kenken.setState(new Configurazione());

    }

    @Override
    public void terminaGioco(Finestra kenken) {
        System.out.println("Necessaria terminazione della configurazione.");
    }

     */


}
