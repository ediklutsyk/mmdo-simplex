package com.mmdo.simplex.DTO;

import java.util.Objects;

public class Pair<A,B> {
    private A key;
    private B value;

    public Pair(A key, B value) {
        this.key = key;
        this.value = value;
    }

    public static <A,B> Pair<A,B> of(A key, B value) {
        return new Pair<>(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) &&
                Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    public A getKey() {
        return key;
    }

    public void setKey(A key) {
        this.key = key;
    }

    public B getValue() {
        return value;
    }

    public void setValue(B value) {
        this.value = value;
    }
}