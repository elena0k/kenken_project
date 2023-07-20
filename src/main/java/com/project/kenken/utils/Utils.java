package com.project.kenken.utils;

import com.project.kenken.componenti.Coordinate;
import com.project.kenken.componenti.Gruppo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static List<Coordinate> generaCoordinate(int dim) {
        List<Coordinate> coordinates = new LinkedList<>();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                coordinates.add(new Coordinate(i, j));
            }
        }
        return coordinates;
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

    public static boolean adiacenti(Coordinate cur, LinkedList<Coordinate> listaCelle) {

        for (Coordinate c : listaCelle) {
            if (adiacenteDx(c, cur) || adiacenteSx(c, cur) || adiacenteUp(c, cur) || adiacenteDown(c, cur))
                return true;
        }
        return false;
    }

    public static boolean adiacenteDown(Coordinate c, Coordinate cur) {
        return c.getColonna() == cur.getColonna() && c.getRiga() + 1 == cur.getRiga();
    }

    public static boolean adiacenteUp(Coordinate c, Coordinate cur) {
        return c.getColonna() == cur.getColonna() && c.getRiga() - 1 == cur.getRiga();
    }

    public static boolean adiacenteSx(Coordinate c, Coordinate cur) {
        return c.getRiga() == cur.getRiga() && c.getColonna() - 1 == cur.getColonna();
    }

    public static boolean adiacenteDx(Coordinate c, Coordinate cur) {
        return c.getRiga() == cur.getRiga() && c.getColonna() + 1 == cur.getColonna();
    }

    public static Coordinate[] listaAdiacenti(Coordinate c, int n) {

        Coordinate[] ret = new Coordinate[4];
        Coordinate top = new Coordinate(c.getRiga() - 1, c.getColonna());
        if (top.getRiga() >= 0)
            ret[0] = top;
        Coordinate down = new Coordinate(c.getRiga() + 1, c.getColonna());
        if (down.getRiga() <= n)
            ret[2] = down;
        Coordinate left = new Coordinate(c.getRiga(), c.getColonna() - 1);
        if (left.getColonna() >= 0)
            ret[1] = left;
        Coordinate right = new Coordinate(c.getRiga(), c.getColonna() + 1);
        if (right.getColonna() <= n)
            ret[3] = right;
        return ret;
    }

    public static Coordinate eleggiIndice(Gruppo gruppo) {
        List<Coordinate> listaCelle = gruppo.getListaCelle();
        int minR = Integer.MAX_VALUE;
        int minC = Integer.MAX_VALUE;
        for (Coordinate c : listaCelle) {
            if (c.getRiga() < minR && c.getColonna() < minC) {
                minR = c.getRiga();
                minC = c.getColonna();
            } else if (c.getRiga() == minR && c.getColonna() < minC)
                minC = c.getColonna();

            else if (c.getColonna() == minC && c.getRiga() < minR)
                minR = c.getRiga();

        }
        return new Coordinate(minR, minC);
    }

    public static boolean verificaGruppo(Gruppo gruppo, int[][] matriceScelte) {
        LinkedList<Coordinate> celle = gruppo.getListaCelle();
        Coordinate coord = gruppo.getListaCelle().get(0);
        int risultato = matriceScelte[coord.getRiga()][coord.getColonna()];
        for (int i = 1; i < celle.size(); i++) {
            int riga = celle.get(i).getRiga();
            int colonna = celle.get(i).getColonna();
            if ("+".equals(gruppo.getOperazione()))
                risultato += matriceScelte[riga][colonna];
            if ("-".equals(gruppo.getOperazione()) || "%".equals(gruppo.getOperazione()))
                risultato = Utils.ordina(matriceScelte[riga][colonna], risultato, gruppo.getOperazione());
            if ("x".equals(gruppo.getOperazione()))
                risultato *= matriceScelte[riga][colonna];
        }
        return risultato == gruppo.getVincolo();
    }

    public static boolean verificaColonna(int val, int j, int riga, int[][] matriceScelte) {
        for (int i = 0; i < matriceScelte.length; i++)
            if (i != riga && matriceScelte[i][j] == val)
                return false;
        return true;
    }

    public static boolean verificaRiga(int val, int i, int colonna, int[][] matriceScelte) {
        for (int j = 0; j < matriceScelte.length; j++)
            if (j != colonna && matriceScelte[i][j] == val)
                return false;
        return true;
    }

    public static boolean isEmpty(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++)
                if (mat[i][j] != 0)
                    return false;
        }
        return true;
    }

    public static boolean isFull(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++)
                if (mat[i][j] == 0)
                    return false;
        }
        return true;
    }


    public static int[][] copiaProfondaMatriceInt(int[][] other) {
        int[][] ret = new int[other.length][other.length];
        for (int i = 0; i < other.length; i++) {
            ret[i] = Arrays.copyOf(other[i], other.length);
        }
        return ret;
    }

    private static void printMatrice(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println("\n");
        }
    }
}
