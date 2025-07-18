package org.example.courtsystem.generics;

public class LegalContainer<T, U> {
    private T item1;
    private U item2;

    public LegalContainer(T item1, U item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public T getItem1() { return item1; }
    public U getItem2() { return item2; }
}