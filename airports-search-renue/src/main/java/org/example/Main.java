package org.example;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        WorkWithFile worker = new WorkWithFile("src/main/resources/airports.csv");
        BST tree = new BST();
        ArrayList<Pair> pairs = worker.getLinePositions();
        int counter;

        for (Pair p : pairs) {
            tree.add(p);
        }
        Scanner in = new Scanner(System.in);

        while (true) {
            counter = 0;
            System.out.println("Вы можете ввести фильтры: ");
            String filters = in.nextLine();
            if (filters.equals("!quit")) {break;}
            FiltersParser fp = new FiltersParser(filters);

            System.out.println("Введите название аэропорта: ");
            String name = in.nextLine();
            if (name.equals("!quit")) {break;}

            long time = System.currentTimeMillis();
            ArrayList<Pair> matchedPairs = tree.search(name.toLowerCase());
            for (Pair p : matchedPairs) {
                String c = worker.readLineInPosition(p.pointer);
                if (fp.matchFilters(c)) {
                    counter++;
                    System.out.printf("%s[%s]\n", p.name, c);
                }
            }
            System.out.printf("Колличество найденных строк: %d \n", counter);
            System.out.printf("Время, затраченное на поиск: %d \n", System.currentTimeMillis() - time);
        }

    }
}