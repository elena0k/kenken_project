package com.project.kenken.risolutore;

import com.project.kenken.componenti.Gruppo;
import com.project.kenken.utils.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KenkenGridTest {


    @Test
    @DisplayName("Should verify if solutions are correct")
    void shouldVerifySolutions(){
        KenkenGrid kenken= new KenkenGrid(3);
        List<Gruppo> gruppi= Utils.templateGroups();
        kenken.setListaGruppi(gruppi);
        kenken.risolvi();
        int[][] solution=kenken.getListaSoluzioni().get(0);
        int[][] expectedSolution= {{3,1,2},{2,3,1},{1,2,3}};
        for(int i=0; i<3;i++)
            for(int j=0;j<3;j++)
                assertEquals(expectedSolution[i][j],solution[i][j]);
    }
}