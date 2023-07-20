package com.project.kenken.risolutore;

import com.project.kenken.componenti.Gruppo;
import com.project.kenken.utils.TemplateKenken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KenkenGridTest {


    @Test
    @DisplayName("Should verify if the only solution is correct")
    void shouldVerifyOneSolution() {
        KenkenGrid kenken = new KenkenGrid(3);
        List<Gruppo> gruppi = TemplateKenken.templateGroupsOneSolution();
        kenken.setGroupsList(gruppi);
        kenken.risolvi();
        List<int[][]> solutions = kenken.getListaSoluzioni();
        int[][] expectedSolution = {{3, 1, 2}, {2, 3, 1}, {1, 2, 3}};

        assertAll(
                () -> assertEquals(1, kenken.getListaSoluzioni().size(), "More than one solution"),
                () -> assertArrayEquals(expectedSolution, solutions.get(0))
        );
    }

    @Test
    @DisplayName("Should verify if there is no solution")
    void shouldVerifyNOSolutions() {
        KenkenGrid kenken = new KenkenGrid(3);
        List<Gruppo> gruppi = TemplateKenken.templateGroupsNOSolution();
        kenken.setGroupsList(gruppi);
        kenken.risolvi();
        List<int[][]> solutions = kenken.getListaSoluzioni();
        assertTrue(kenken.getListaSoluzioni().size() == 0);
    }

    @Test
    @DisplayName("Should verify if the numbers of solution is the one that we expect")
    void shouldVerifyNumberOfSolutions() {
        KenkenGrid kenken = new KenkenGrid(3);
        List<Gruppo> gruppi = TemplateKenken.templateManySolution();
        kenken.setGroupsList(gruppi);
        kenken.risolvi();
        List<int[][]> solutions = kenken.getListaSoluzioni();
        assertTrue(solutions.size() == 12, "Il numero di soluzioni è scorretto");
    }


}