package com.project.kenken.gestore_GUI;

import com.project.kenken.componenti.Coordinate;
import com.project.kenken.componenti.Gruppo;
import com.project.kenken.risolutore.KenkenGrid;
import com.project.kenken.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrigliaGUITest {

    /*
    @Test
    @DisplayName("Should show wich cells do not respect constraints")
    void shoudVerifyCOnstraints() {
        KenkenGrid kenken= new KenkenGrid(3);
        List<Gruppo> gruppi= Utils.templateGroups();
        kenken.setListaGruppi(gruppi);
        kenken.risolvi();
        int[][] matScelte= {{1,1,0},{2,3,1},{0,1,3}};
        GrigliaGUI griglia=new GrigliaGUI(kenken,matScelte);
        List<Coordinate> celleErrate=griglia.verificaSoluzione();
        List<Coordinate> expectedRet= new LinkedList<>();
        expectedRet.add(new Coordinate(0,0));
        expectedRet.add(new Coordinate(0,1));
        expectedRet.add(new Coordinate(2,1));
        expectedRet.add(new Coordinate(2,2));

        Assertions.assertAll( () -> assertEquals(expectedRet.size(),celleErrate.size()),
                              () -> assertTrue()


    }

     */
}