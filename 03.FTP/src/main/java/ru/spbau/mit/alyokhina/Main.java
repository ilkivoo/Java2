package ru.spbau.mit.alyokhina;


import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/** Console UI (list files on server and download files) */
public class Main {
    public static void main(String[] args) {
        int type;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("1 - запустить сервер");
            System.out.println("2 - запустить клиента");
            System.out.println("3 - выйти");
            type = in.nextInt();
            if (type == 1) {
                System.out.println("Введите порт");
                int port = in.nextInt();
                try {
                    final Server server = new Server(port);
                    Thread thread = new Thread(server::start);
                    thread.start();
                    System.out.println("Сервер создан");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (type == 2) {
                try {
                    System.out.println("Введите порт");
                    int port = in.nextInt();
                    Client client = new Client("localhost", port);
                    int typeRequest = 0;
                    do {
                        System.out.println("1 - list");
                        System.out.println("2 - get");
                        System.out.println("3 - назад");
                        typeRequest = in.nextInt();
                        if (typeRequest == 1) {
                            System.out.println("Введите путь к директории");
                            String path = in.next();
                            List<Pair<String, Boolean>> files = client.list(path);
                            for (Pair<String, Boolean> file : files) {
                                System.out.print(file.getKey());
                                if (file.getValue()) {
                                    System.out.println(" is directory");
                                } else {
                                    System.out.println(" is file");
                                }
                            }
                        }
                        if (typeRequest == 2) {
                            System.out.println("Введите путь к файлу");
                            String path = in.next();
                            System.out.println("Введите путь для сохранения");
                            String fileName = in.next();
                            File file = client.get(path, fileName);
                            System.out.print("Размер файла = ");
                            System.out.println(file.length());
                        }
                    } while (typeRequest != 3);


                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } while (type != 3);
        System.exit(0);
    }
}

