package ru.spbau.mit.alyokhina;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Path path = Paths.get(args[0]);
        Hasher hasher = new Hasher();
        long startTime1 = System.currentTimeMillis();
        Md5 hash1 = hasher.getMd5(path);
        long endTime1 = System.currentTimeMillis();
        long startTime2 = System.currentTimeMillis();
        Md5 hash2 = hasher.getMd5forThreads(path);
        long endTime2 = System.currentTimeMillis();
        System.out.println( endTime1 - startTime1);
        System.out.println(endTime2 - startTime2);

    }
}
