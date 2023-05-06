package org.example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class WorkWithFile {
    private final String PATH;
    private RandomAccessFile file;

    public WorkWithFile(String path) {
        this.PATH = path;
    }

    public ArrayList<Pair> getLinePositions() throws IOException {
        ArrayList<Pair> res = new ArrayList<>();

        file = new RandomAccessFile(PATH, "r");

        long start = 0;
        long end = file.length();
        long ptr = 0;
        String name;

        file.seek(start);
        while (file.getFilePointer() < end) {
            name = file.readLine().split(",")[1];
            res.add(new Pair(name, ptr));
            ptr = file.getFilePointer();
        }
        file.close();
        return res;
    }


    public String readLineInPosition(long posNum) throws IOException {
        file = new RandomAccessFile(PATH, "r");
        file.seek(posNum);
        String res = file.readLine();
        file.close();
        return res;
    }

}
