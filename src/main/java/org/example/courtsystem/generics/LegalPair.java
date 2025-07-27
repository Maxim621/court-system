package org.example.courtsystem.generics;

import org.example.courtsystem.annotations.AuthorAnnotation;
import org.example.courtsystem.model.people.Client;
import org.example.courtsystem.model.people.Lawyer;

@AuthorAnnotation(
        author = "GenericsTeam",
        date = "2025-07-27",
        description = "Immutable pair of lawyer and client"
)

public record LegalPair<T extends Lawyer, U extends Client>(T lawyer, U client) {
    public LegalPair {
        if (lawyer == null || client == null) {
            throw new IllegalArgumentException("Neither lawyer nor client can be null");
        }
    }
}

//public class LegalContainer<T, U> {
//    private T item1;
//    private U item2;
//
//    public LegalContainer(T item1, U item2) {
//        this.item1 = item1;
//        this.item2 = item2;
//    }
//
//    public T getItem1() { return item1; }
//    public U getItem2() { return item2; }
//}