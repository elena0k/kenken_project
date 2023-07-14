package GUIprova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Finestra extends JFrame
{

    private int n;
    private GrigliaGUI grigliaGUI;

    private JMenuItem jmiApri, jmiSalva, jmiSalvaConNome,
            jmiHelp, jmiEsci, celle3,celle4,celle5,celle6;
    private JPanel pannelloGriglia;
    private JPanel pannelloPulsanti;
    private JButton plsCheck;
    private JButton plsPrev;
    private JButton plsNext;
    private JButton plsStart;
    private JPopupMenu popup;
    boolean hoModificheNonSalvate = false;
    private int indiceSoluzioneAttuale;

    private final int BOLD=3;
    private final int DEFAULT=4;


    class AscoltatoreEventi implements ActionListener
    {
        Finestra f;
        public void actionPerformed(ActionEvent a) {

            if(a.getSource()==celle3) {
                resizeGriglia(3);
            }
            if(a.getSource()==celle4) {
                resizeGriglia(4);
            }
            if(a.getSource()==celle5) {
                resizeGriglia(5);
            }
            if(a.getSource()==celle6) {
                resizeGriglia(6);
            }
        }
    }

    public Finestra(int n)
    {
        setTitle("KenkenGUI ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AscoltatoreEventi actListener = new AscoltatoreEventi();

        this.n=n;
        grigliaGUI= new GrigliaGUI(n);
        pannelloGriglia= grigliaGUI.getPannelloGriglia();

        costruisciPannelloPulsanti(actListener);
        costruisciMenu(actListener);

        add(pannelloGriglia, BorderLayout.CENTER);
        add(pannelloPulsanti, BorderLayout.EAST);
        setSize(480, 400);
        setLocation(0, 0);
    }

    private void costruisciPannelloPulsanti(AscoltatoreEventi actListener) {
        pannelloPulsanti=new JPanel();
        plsStart = new JButton("START");
        plsStart.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 14));
        plsStart.addActionListener(actListener);
        plsStart.setEnabled(false);

        plsPrev = new JButton("PREVIOUS");
        plsPrev.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 14));
        plsPrev.addActionListener(actListener);
        plsPrev.setEnabled(false);

        plsNext = new JButton("NEXT");
        plsNext.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 14));
        plsNext.addActionListener(actListener);
        plsNext.setEnabled(false);

        plsCheck = new JButton("CHECK");
        plsCheck.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 14));
        plsCheck.addActionListener(actListener);
        plsCheck.setEnabled(false);

        pannelloPulsanti.setLayout(new GridLayout(4, 1, 10, 10));
        pannelloPulsanti.setSize(100, 400);
        pannelloPulsanti.add(plsStart);
        pannelloPulsanti.add(plsPrev);
        pannelloPulsanti.add(plsNext);
        pannelloPulsanti.add(plsCheck);
    }

    private void costruisciMenu(ActionListener ascoltatore) {

        JMenu fileMenu = new JMenu("File");

        JMenuBar jmbBarra = new JMenuBar();
        this.setJMenuBar(jmbBarra);
        jmiApri = new JMenuItem("Apri");
        fileMenu.add(jmiApri);
        jmiApri.addActionListener(ascoltatore);

        jmiSalva = new JMenuItem("Salva");
        fileMenu.add(jmiSalva);
        jmiSalva.addActionListener(ascoltatore);

        jmiSalvaConNome = new JMenuItem("Salva con nome");
        fileMenu.add(jmiSalvaConNome);
        jmiSalvaConNome.addActionListener(ascoltatore);

        jmiEsci = new JMenuItem("Esci");
        fileMenu.add(jmiEsci);
        jmiEsci.addActionListener(ascoltatore);

        jmbBarra.add(fileMenu);

        JMenu menuSize = new JMenu("Size");
        celle3 = new JMenuItem("3x3");
        menuSize.add(celle3);
        celle3.addActionListener(ascoltatore);

        celle4 = new JMenuItem("4x4");
        menuSize.add(celle4);
        celle4.addActionListener(ascoltatore);

        celle5 = new JMenuItem("5x5");
        menuSize.add(celle5);
        celle5.addActionListener(ascoltatore);

        celle6 = new JMenuItem("6x6");
        menuSize.add(celle6);
        celle6.addActionListener(ascoltatore);

        jmbBarra.add(menuSize);

        JMenu menuAbout = new JMenu("About");
        jmiHelp = new JMenuItem("Help");
        menuAbout.add(jmiHelp);
        jmiHelp.addActionListener(ascoltatore);

        jmbBarra.add(menuAbout);

    }

    public void resizeGriglia(int size)
    {
        remove(pannelloGriglia);
        grigliaGUI = new GrigliaGUI(size);
        pannelloGriglia=grigliaGUI.getPannelloGriglia();
        this.n=size;
        add(pannelloGriglia, BorderLayout.CENTER);
        pannelloGriglia.updateUI();
    }



}

public class KenkenGUI
{
    private static final int DEFAULT=4;

    public static void main(String[] args)
    {
        Finestra finestra = new Finestra(DEFAULT);
        finestra.setVisible(true);
        finestra.setResizable(false);
    }


}
