package com.mmdo.simplex.utils;

import com.mmdo.simplex.DTO.Condition;
import com.mmdo.simplex.DTO.Fraction;
import com.mmdo.simplex.DTO.SAE;
import com.mmdo.simplex.DTO.Table;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mmdo.simplex.DTO.Condition.MAX;
import static com.mmdo.simplex.DTO.Condition.MIN;

public class SimplexUtils {

    public static List<Table> solve(SAE sae) {
        int m = sae.getAmountOfEquations();
        int n = sae.getAmountOfUnknown();
        Condition condition = sae.getCondition();
        List<Integer> c = sae.getFunction();
        Integer[][] A = sae.getAList();
        char[] signs = sae.getSignsList();
        List<Integer> B = sae.getBList();

        List<List<Integer>> P = new ArrayList<>();
        P.add(B);
        for (int j = 0; j < A[0].length; j++) {
            List<Integer> temp = new ArrayList<>();
            for (int i = 0; i < A.length; i++) {
                temp.add(A[i][j]);
            }
            P.add(temp);
        }

        for (int i = 0; i < signs.length; i++) {
            char sign = signs[i];
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < signs.length; j++) {
                temp.add(j == i ? 1 : 0);
            }
            if (sign == '>') {
                temp = changeVectorSign(temp);
            }
            P.add(temp);
            c.add(0);
        }

        for (int i = n + 1; i < P.size(); i++) {
            if (P.get(i).stream().mapToInt(Integer::intValue).sum() == -1) {
                P.add(changeVectorSign(P.get(i)));
                c.add(null);
            }
        }

        // basis calc
        Integer[] basis = new Integer[m];
        for (int i = n + 1; i < P.size(); i++) {
            int index = P.get(i).indexOf(1);
            if (index != -1) {
                basis[index] = basis[index] == null ? i : basis[index];
            }
        }

        // Cb column init
        List<Integer> Cb = new ArrayList<>();
        for (Integer i : basis) {
            Cb.add(c.get(i - 1));
        }

        // index row calc
        List<Integer> indexRow = new ArrayList<>();
        List<Integer> indexRowM = new ArrayList<>();
        c.add(0, 0);

        for (int j = 0; j < P.size(); j++) {
            int sum = 0;
            int sumM = 0;
            for (int i = 0; i < P.get(j).size(); i++) {
                if (Cb.get(i) == null) {
                    sumM += P.get(j).get(i);
                } else {
                    sum += P.get(j).get(i) * Cb.get(i);
                }
            }
            if (c.get(j) != null) {
                indexRow.add(sum - c.get(j));
                indexRowM.add(sumM);
            } else {
                indexRow.add(0);
                indexRowM.add(0);
            }
        }

        if (Cb.contains(null) && condition == MAX) {
            indexRowM = changeVectorSign(indexRowM);
        }

        Fraction[][] matrix = new Fraction[P.size()][];
        for (int i = 0; i < P.size(); i++) {
            List<Fraction> list = P.get(i).stream().map(e -> new Fraction(e, 1)).collect(Collectors.toList());
            Fraction[] temp = new Fraction[Cb.contains(null) ? list.size() + 2 : list.size() + 1];
            temp = list.toArray(temp);
            if (Cb.contains(null)) {
                temp[temp.length - 1] = new Fraction(indexRowM.get(i), 1);
                temp[temp.length - 2] = new Fraction(indexRow.get(i), 1);
            } else {
                temp[temp.length - 1] = new Fraction(indexRow.get(i), 1);
            }
            matrix[i] = temp;
        }
        List<Table> tables = new ArrayList<>();
        tables.add(new Table(matrix, basis, null, Cb));
        boolean error = false;
        while (!checkIfSolved(matrix, condition, c)) {
            Pair<Integer, Integer> main = getMainElement(matrix, condition, c);
            if (main == null) {
                error = true;
                break;
            }
            basis[main.getValue()] = main.getKey();
            boolean isM = Cb.contains(null);
            Cb.set(main.getValue(), c.get(main.getKey()));
            if (isM && !Cb.contains(null)) {
                removeIndexMRow(matrix);
            }
            matrix = GaussJordan(matrix[main.getKey()][main.getValue()], main, matrix);
            tables.get(tables.size() - 1).setMain(main);
            tables.add(new Table(matrix, basis, main, Cb));
        }
        if (!error) {
            tables.get(tables.size() - 1).setMain(null);
        }
        return error ? null : tables;
    }

    private static Fraction[][] GaussJordan(Fraction mainVal, Pair<Integer, Integer> main, Fraction[][] matrix) {
        Fraction[][] result = copyMatrix(matrix);
        for (int j = 0; j < result[0].length; j++) {
            for (int i = 0; i < result.length; i++) {
                if (main.getValue() == j) {
                    result[i][j] = result[i][j].divide(mainVal);
                } else {
                    Fraction firstDob = matrix[i][j].multiply(matrix[main.getKey()][main.getValue()]);
                    Fraction secondDob = matrix[main.getKey()][j].multiply(matrix[i][main.getValue()]);
                    result[i][j] = firstDob.subtract(secondDob).divide(mainVal);
                }
            }
        }
        return result;
    }

    private static Fraction[][] copyMatrix(Fraction[][] matrix) {
        Fraction[][] copy = new Fraction[matrix.length][matrix[0].length];
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                copy[i][j] = matrix[i][j];
            }
        }
        return copy;
    }

    private static Pair<Integer, Integer> getMainElement(Fraction[][] matrix, Condition condition, List<Integer> c) {
        List<Fraction> indexRow = getIndexRow(matrix);
        int maxIndex = getMaxAbs(indexRow, condition, c);
        if (maxIndex != -1) {
            int vectorForReplace = selectVectorForReplace(matrix, maxIndex);
            if (vectorForReplace != -1) {
                return new Pair<>(maxIndex, vectorForReplace);
            }
        }
        return null;
    }

    private static List<Fraction> getIndexRow(Fraction[][] matrix) {
        List<Fraction> indexRow = new ArrayList<>();
        for (int i = 1; i < matrix.length; i++) {
            indexRow.add(matrix[i][matrix[i].length - 1]);
        }
        return indexRow;
    }

    private static void removeIndexMRow(Fraction[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Arrays.copyOf(matrix[i], matrix[i].length - 1);
        }
    }

    private static boolean checkIfSolved(Fraction[][] matrix, Condition condition, List<Integer> c) {
        List<Fraction> indexRow = getIndexRow(matrix);
        List<Fraction> result = new ArrayList<>();
        for (int i = 0; i < indexRow.size() - c.stream().filter(Objects::isNull).count(); i++) {
            result.add(indexRow.get(i));
        }
        switch (condition) {
            case MIN:
                return result.stream().noneMatch(elem -> elem.getNumerator() > 0 && elem.getDenominator() > 0);
            case MAX:
                return result.stream().noneMatch(elem -> elem.getNumerator() < 0 || elem.getDenominator() < 0);
        }
        return false;
    }

    private static List<Integer> changeVectorSign(List<Integer> vector) {
        return vector.stream().map(element -> element = element * -1).collect(Collectors.toList());
    }

    private static int getMaxAbs(List<Fraction> indexRow, Condition condition, List<Integer> c) {
        List<Double> abs = indexRow.stream().map(Fraction::calculate).map(Math::abs).collect(Collectors.toList());
        List<Double> res = new ArrayList<>();
        List<Fraction> f = indexRow.subList(0, (int) (indexRow.size() - c.stream().filter(Objects::isNull).count()));
        if (condition == MAX) {
            res = f.stream().map(Fraction::calculate).filter(e -> e < 0).collect(Collectors.toList());
        } else if (condition == MIN) {
            res = f.stream().map(Fraction::calculate).filter(e -> e > 0).collect(Collectors.toList());
        }
        Double max = res.stream().map(Math::abs).max(Double::compare).orElse(null);
        if (max == null) {
            return -1;
        }
        return abs.indexOf(max) + 1;
    }

    private static int selectVectorForReplace(Fraction[][] matrix, Integer maxIndex) {
        if (Arrays.stream(matrix[maxIndex]).noneMatch(e -> e.calculate() > 0)) {
            return -1;
        }
        List<Fraction> res = new ArrayList<>();
        for (int i = 0; i < matrix[0].length; i++) {
            res.add(matrix[0][i].divide(matrix[maxIndex][i]));
        }
        List<Double> calculated = res.stream().map(Fraction::calculate).collect(Collectors.toList());
        Double min = calculated.stream().filter(elem -> elem > 0).min(Double::compare).orElse(null);
        if (min == null) {
            return -1;
        }
        return calculated.indexOf(min);
    }
}