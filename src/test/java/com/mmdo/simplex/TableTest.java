package com.mmdo.simplex;

import com.mmdo.simplex.DTO.Fraction;
import com.mmdo.simplex.DTO.Table;
import javafx.util.Pair;
import org.junit.Test;

import java.util.Arrays;

public class TableTest {

    @Test
    public void test() {
        Table table = new Table();
        table.setCb(Arrays.asList(0, null, 0))
                .setBasis(new Integer[]{3, 4, 5})
                .setMain(new Pair<>(1,3))
                .setMatrix(new Fraction[][]{
                        {new Fraction(12), new Fraction(4), new Fraction(30), new Fraction(0)},
                        {new Fraction(2), new Fraction(-1), new Fraction(3), new Fraction(-11)},
                        {new Fraction(-7), new Fraction(1), new Fraction(4), new Fraction(-3)},
                        {new Fraction(1), new Fraction(0), new Fraction(0), new Fraction(0)},
                        {new Fraction(0), new Fraction(1), new Fraction(0), new Fraction(0)},
                        {new Fraction(0), new Fraction(0), new Fraction(1), new Fraction(0)},
                });
        table.print();
    }

}
