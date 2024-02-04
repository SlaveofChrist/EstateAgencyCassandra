package main;

import Generation.AccommodationGeneration;
import Generation.IndividualGeneration;
import Generation.TransactionGeneration;

public class Main {
    public static void main(String[] args) {
        // Generate and svae data
        AccommodationGeneration.generateAndSave();
        IndividualGeneration.generateAndSave();
        TransactionGeneration.generateAndSave();
    }
}
