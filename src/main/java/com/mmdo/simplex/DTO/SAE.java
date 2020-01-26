package com.mmdo.simplex.DTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SAE {
    private int amountOfUnknown;
    private int amountOfEquations;
    private Condition condition;
    private List<Integer> function;
    private List<Equation> equations;

    public SAE setAmountOfUnknown(int amountOfUnknown) {
        this.amountOfUnknown = amountOfUnknown;
        return this;
    }

    public int getAmountOfUnknown() {
        return amountOfUnknown;
    }

    public SAE setAmountOfEquations(int amountOfEquations) {
        this.amountOfEquations = amountOfEquations;
        return this;
    }

    public int getAmountOfEquations() {
        return amountOfEquations;
    }

    public SAE setCondition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public Condition getCondition() {
        return condition;
    }

    public SAE setFunction(List<Integer> function) {
        this.function = function;
        return this;
    }

    public List<Integer> getFunction() {
        return function;
    }

    public SAE setEquations(List<Equation> equations) {
        this.equations = equations;
        return this;
    }

    public List<Equation> getEquations() {
        return equations;
    }

    public List<Integer> getBList() {
        return equations.stream().map(Equation::getB).collect(Collectors.toList());
    }

    public char[] getSignsList() {
        return equations.stream()
                .map(Equation::getSign)
                .map(String::valueOf)
                .collect(Collectors.joining()).toCharArray();
    }

    public Integer[][] getAList() {
        return equations.stream()
                .map(a -> a.getA().toArray(new Integer[0]))
                .toArray(Integer[][]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SAE)) return false;
        SAE sae = (SAE) o;
        return getAmountOfUnknown() == sae.getAmountOfUnknown() &&
                getAmountOfEquations() == sae.getAmountOfEquations() &&
                getCondition() == sae.getCondition() &&
                Objects.equals(getFunction(), sae.getFunction()) &&
                Objects.equals(getEquations(), sae.getEquations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmountOfUnknown(), getAmountOfEquations(), getCondition(), getFunction(), getEquations());
    }

    @Override
    public String toString() {
        return "SAE{" +
                "amountOfUnknown=" + amountOfUnknown +
                ", amountOfEquations=" + amountOfEquations +
                ", condition=" + condition +
                ", function=" + function +
                ", equations=" + equations +
                '}';
    }
}
