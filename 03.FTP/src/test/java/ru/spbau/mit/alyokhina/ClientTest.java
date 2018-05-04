package ru.spbau.mit.alyokhina;

import javafx.util.Pair;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


/** Test for client-server connection */
public class ClientTest {
    /** If a server was created, this flag will be true */
    private static boolean isCreateServer = false;


    @Test
    public void testCreateClientAndServer() {
        try {
            if (!isCreateServer) {
                final Server server = new Server(1408);
                Thread thread = new Thread(server::start);
                thread.start();
                isCreateServer = true;
            }
            Client client = new Client("localhost", 1408);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testListWithOneClient() {
        try {
            if (!isCreateServer) {
                final Server server = new Server(1408);
                Thread thread = new Thread(server::start);
                thread.start();
                isCreateServer = true;
            }
            Client client = new Client("localhost", 1408);
            List<Pair<String, Boolean>> files = client.list("src/resources/testList1");
            List<Pair<String, Boolean>> rightAnswer = new ArrayList<>();
            rightAnswer.add(new Pair<>("dir1", true));
            rightAnswer.add(new Pair<>("dir2", true));
            rightAnswer.add(new Pair<>("file3", false));
            rightAnswer.add(new Pair<>("file1", false));
            rightAnswer.add(new Pair<>("file2", false));
            assertEquals("", files.get(0).getKey() + files.get(1).getKey() + files.get(2).getKey());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void testListWithTwoClients() {
        try {
            if (!isCreateServer) {
                final Server server = new Server(1408);
                Thread thread = new Thread(server::start);
                thread.start();
                isCreateServer = true;
            }
            Client client1 = new Client("localhost", 1408);
            Client client2 = new Client("localhost", 1408);
            List<Pair<String, Boolean>> files1 = client1.list("src/resources/testList1");
            List<Pair<String, Boolean>> files2 = client2.list("src/resources/testList2");
            List<Pair<String, Boolean>> rightAnswer1 = new ArrayList<>();
            rightAnswer1.add(new Pair<>("dir1", true));
            rightAnswer1.add(new Pair<>("dir2", true));
            rightAnswer1.add(new Pair<>("file3", false));
            rightAnswer1.add(new Pair<>("file1", false));
            rightAnswer1.add(new Pair<>("file2", false));
            assertEquals(rightAnswer1.size(), equalLists(rightAnswer1, files1));

            List<Pair<String, Boolean>> rightAnswer2 = new ArrayList<>();
            rightAnswer2.add(new Pair<>("dir1", true));
            rightAnswer2.add(new Pair<>("dir2", true));
            rightAnswer2.add(new Pair<>("dir3", true));
            rightAnswer2.add(new Pair<>("dir4", true));
            rightAnswer2.add(new Pair<>("dir5", true));
            rightAnswer2.add(new Pair<>("file1", false));
            rightAnswer2.add(new Pair<>("file2", false));
            assertEquals(rightAnswer2.size(), equalLists(files2, rightAnswer2));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCreateWithNonexistentDirectory() {
        try {
            if (!isCreateServer) {
                final Server server = new Server(1408);
                Thread thread = new Thread(server::start);
                thread.start();
                isCreateServer = true;
            }
            Client client = new Client("localhost", 1408);
            List<Pair<String, Boolean>> files = client.list("src/resources/testList3");
            assertEquals(0, files.size());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void testGetWithOneClient() {
        try {
            if (!isCreateServer) {
                final Server server = new Server(1408);
                Thread thread = new Thread(server::start);
                thread.start();
                isCreateServer = true;
            }
            Client client = new Client("localhost", 1408);
            File copyFile = client.get("src/resources/testGet1/file1", "src/resources/testGet1/copyFile1");
            File file = new File("src/resources/testGet1/file1");
            assertEquals(true, equalFiles(file, copyFile));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void testGetWithTwoClients() {
        try {
            if (!isCreateServer) {
                final Server server = new Server(1408);
                Thread thread = new Thread(server::start);
                thread.start();
                isCreateServer = true;
            }
            Client client1 = new Client("localhost", 1408);
            File copyFile1 = client1.get("src/resources/testGet1/file1", "src/resources/testGet1/copyFile1");
            File file1 = new File("src/resources/testGet1/file1");
            assertEquals(true, equalFiles(file1, copyFile1));

            Client client2 = new Client("localhost", 1408);
            File copyFile2 = client2.get("src/resources/testGet1/file2", "src/resources/testGet1/copyFile2");
            File file2 = new File("src/resources/testGet1/file2");
            assertEquals(true, equalFiles(file2, copyFile2));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetWithNonexistentFiles() {
        try {
            if (!isCreateServer) {
                final Server server = new Server(1408);
                Thread thread = new Thread(server::start);
                thread.start();
                isCreateServer = true;
            }
            Client client = new Client("localhost", 1408);
            File file = client.get("src/resources/testGet1/file3", "src/resources/testGet1/copyFile3");
            assertEquals(0, file.length());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /** Compare two List<Pair<String, Boolean>> */
    private static int equalLists(List<Pair<String, Boolean>> a, List<Pair<String, Boolean>> b) {
        if (a.size() != b.size()) {
            return -1;
        }
        int i = 0;
        for (Pair<String, Boolean> elem1 : a) {
            boolean flag = false;
            for (Pair<String, Boolean> elem2 : b) {
                if (elem1.getKey().equals(elem2.getKey()) && elem1.getValue() == elem2.getValue()) {
                    flag = true;
                }
            }
            if (!flag) {
                return i;
            }
            i++;
        }
        return a.size();
    }

    /** Compare files */
    private static boolean equalFiles(File file, File copyFile) throws IOException {
        if (file.length() != copyFile.length()) {
            return false;
        }
        byte[] data1 = new byte[(int) file.length()];
        byte[] data2 = new byte[(int) file.length()];
        FileInputStream fisForFile = new FileInputStream(file);
        FileInputStream fisForCopyFile = new FileInputStream(copyFile);
        if (fisForFile.read(data1) != file.length()) {
            throw new IOException("Can't read file");
        }
        if (fisForCopyFile.read(data2) != copyFile.length()) {
            throw new IOException("Can't read file");
        }
        for (int i = 0; i < data1.length; i++) {
            if (data1[i] != data2[i]) {
                return false;
            }
        }
        return true;
    }

}
