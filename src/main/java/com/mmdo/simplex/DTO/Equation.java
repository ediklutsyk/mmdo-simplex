package com.mmdo.simplex.DTO;

import java.util.List;
import java.util.Objects;

public class Equation {
    private List<Integer> A;
    private Integer B;
    private char sign;

    public List<Integer> getA() {
        return A;
    }

    public Equation setA(List<Integer> a) {
        A = a;
        return this;
    }

    public Integer getB() {
        return B;
    }

    public Equation setB(Integer b) {
        B = b;
        return this;
    }

    public char getSign() {
        return sign;
    }

    public Equation setSign(char sign) {
        this.sign = sign;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equation)) return false;
        Equation equation = (Equation) o;
        return getSign() == equation.getSign() &&
                Objects.equals(getA(), equation.getA()) &&
                Objects.equals(getB(), equation.getB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getA(), getB(), getSign());
    }

    @Override
    public String toString() {
        return "Equation{" +
                "A=" + A +
                ", B=" + B +
                ", sign=" + sign +
                '}';
    }
}
