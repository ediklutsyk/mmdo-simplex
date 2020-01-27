package com.mmdo.simplex.DTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
    private Fraction[][] matrix;
    private Integer[] basis;
    private Pair<Integer, Integer> main;
    private List<Integer> Cb;

    public Table() {
    }

    public Table(Fraction[][] matrix, Integer[] basis, Pair<Integer, Integer> main, List<Integer> cb) {
        this.matrix = matrix;
        this.basis = basis.clone();
        this.main = main;
        this.Cb = new ArrayList<>(cb);
    }

    public Fraction[][] getMatrix() {
        return matrix;
    }

    public Table setMatrix(Fraction[][] matrix) {
        this.matrix = matrix;
        return this;
    }

    public Integer[] getBasis() {
        return basis;
    }

    public Table setBasis(Integer[] basis) {
        this.basis = basis;
        return this;
    }

    public Pair<Integer, Integer> getMain() {
        return main;
    }

    public Table setMain(Pair<Integer, Integer> main) {
        this.main = main;
        return this;
    }

    public List<Integer> getCb() {
        return Cb;
    }

    public Table setCb(List<Integer> cb) {
        Cb = cb;
        return this;
    }

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    public void print() {
        StringBuilder header = new StringBuilder(String.format("|%1$3s|%2$10s|%3$4s|", center("i",3), center("Базис",10), center("Сб", 4)));
        for (int i = 0; i < matrix.length; i++) {
            header.append(String.format("%10s|", center("P"+i,10)));
        }
        System.out.println(header);
        System.out.println(new String(new char[header.length()]).replace('\0', '-'));
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length + 3; i++) {
                if (i == 0) {
                    System.out.print(String.format("|%3s|", center(j + 1, 3)));
                } else if (i == 1) {
                    System.out.print(String.format("%10s|", j < basis.length ? center("P" + basis[j], 10) : ""));
                } else if (i == 2) {
                    System.out.print(String.format("%4s|", j < this.Cb.size() ? center(this.Cb.get(j) == null ? "M" : this.Cb.get(j), 4) : ""));
                } else {
                    if (main != null && i - 3 == main.getKey() && j == main.getValue()) {
                        System.out.print(ANSI_RED + String.format("%10s", center(matrix[i - 3][j], 10)) + ANSI_RESET + "|");
                    } else {
                        System.out.print(String.format("%10s|", center(matrix[i - 3][j], 10)));
                    }
                }
            }
            System.out.println();
            System.out.println(new String(new char[header.length()]).replace('\0', '-'));
        }
        System.out.println();
        System.out.println();
    }


    private static String center(Object obj, int size) {
        String s = obj.toString();
        if (s == null || size <= s.length()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(' ');
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(' ');
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Table{" +
                "matrix=" + Arrays.toString(matrix) +
                ", basis=" + Arrays.toString(basis) +
                ", main=" + main +
                ", Cb=" + Cb +
                '}';
    }
}
