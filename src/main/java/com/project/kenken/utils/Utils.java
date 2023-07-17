package com.project.kenken.utils;

import com.project.kenken.componenti.Coordinate;
import com.project.kenken.componenti.Gruppo;

import java.util.LinkedList;
import java.util.List;

public  class Utils {

    public static List<Coordinate> generaCoordinate(int dim){
        List<Coordinate> coordinates= new LinkedList<>();
        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
                coordinates.add(new Coordinate(i,j));
            }
        }
        return coordinates;
    }

    public static List<Gruppo> templateGroups(){
        List<Coordinate> allCoord = Utils.generaCoordinate(3);

        LinkedList<Coordinate> l1 = new LinkedList<>();
        LinkedList<Coordinate> l2 = new LinkedList<>();
        LinkedList<Coordinate> l3 = new LinkedList<>();
        LinkedList<Coordinate> l4 = new LinkedList<>();
        LinkedList<Coordinate> l5 = new LinkedList<>();

        l1.add(allCoord.get(0)); l1.add(allCoord.get(1));
        l2.add(allCoord.get(2)); l2.add(allCoord.get(5));
        l3.add(allCoord.get(3)); l3.add(allCoord.get(4)); l3.add(allCoord.get(6));
        l4.add(allCoord.get(7)); l4.add(allCoord.get(8));

        LinkedList<Gruppo> gruppi = new LinkedList<>();

        Gruppo g1 = new Gruppo(3, "%", l1);
        Gruppo g2 = new Gruppo(2, "%", l2);
        Gruppo g3 = new Gruppo(6, "x", l3);
        Gruppo g4 = new Gruppo(1, "-", l4);

        gruppi.add(g1); gruppi.add(g2);
        gruppi.add(g3); gruppi.add(g4);

        return gruppi;
    }

    public static void main(String[] args){

        System.out.println(templateGroups());
    }


}
