package ru.spbau.mit.alyokhina;

import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Test for client-server connection
 */
public class ClientTest {
    /**
     * If a server was created, this flag will be true
     */
    private static boolean isCreateServer = false;
    private static File dirTestList1;
    private static File dirTestList2;
    private static File dirTestGet;
    private static File file1ForTestGet;
    private static File file2ForTestGet;
    private static File copyFile1ForTestGet;
    private static File copyFile2ForTestGet;

    @ClassRule
    public static TemporaryFolder folderForTest = new TemporaryFolder();

    @BeforeClass
    public static void createFiles() throws IOException {
        dirTestList1 = folderForTest.newFolder("testList1");
        folderForTest.newFolder("testList1", "dir1");
        folderForTest.newFolder("testList1", "dir2");
        folderForTest.newFile("testList1/file1");
        folderForTest.newFile("testList1/file2");
        folderForTest.newFile("testList1/file3");

        dirTestList2 = folderForTest.newFolder("testList2");
        folderForTest.newFolder("testList2", "dir1");
        folderForTest.newFolder("testList2", "dir2");
        folderForTest.newFolder("testList2", "dir3");
        folderForTest.newFolder("testList2", "dir4");
        folderForTest.newFolder("testList2", "dir5");
        folderForTest.newFile("testList2/file1");
        folderForTest.newFile("testList2/file2");

        dirTestGet = folderForTest.newFolder("testGet1");
        file1ForTestGet = folderForTest.newFile("testGet1/file1");
        file2ForTestGet = folderForTest.newFile("testGet1/file2");

        copyFile1ForTestGet = folderForTest.newFile("testGet1/copyFile1");
        copyFile2ForTestGet = folderForTest.newFile("testGet1/copyFile2");

        FileOutputStream fout = new FileOutputStream(file1ForTestGet);
        String str = "Я вас любил: любовь еще, быть может,\n" +
                "В душе моей угасла не совсем;\n" +
                "Но пусть она вас больше не тревожит;\n" +
                "Я не хочу печалить вас ничем.\n" +
                "Я вас любил безмолвно, безнадежно,\n" +
                "То робостью, то ревностью томим;\n" +
                "Я вас любил так искренно, так нежно,\n" +
                "Как дай вам бог любимой быть другим.";
        fout.write(str.getBytes());

        fout = new FileOutputStream(file2ForTestGet);
        str = "Я выжила… Отчаянно, ознобно,\n" +
                "Легко. Светает. Снег сошёл на нет.\n" +
                "Не слышен плач, не ослепляет свет.\n" +
                "Глаза пусты, глаза беззлобны.\n" +
                "\n" +
                "Недостающий воздух – чушь, пустяк,\n" +
                "Совпал с полночным и привычным зноем.\n" +
                "А если будет что-нибудь не так…\n" +
                "Ты мне поможешь? Нас пока что двое?";
        fout.write(str.getBytes());
    }

    @Test
    public void testCreateClientAndServer() {
        try {
            if (!isCreateServer) {
                final Server server = new Server(1408);
                Thread thread = new Thread(server::start);
                thread.start();
                isCreateServer = true;
            }
            new Client("localhost", 1408);

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
            List<Pair<String, Boolean>> files = client.list(dirTestList1.getAbsolutePath());
            List<Pair<String, Boolean>> rightAnswer = new ArrayList<>();
            rightAnswer.add(new Pair<>("dir1", true));
            rightAnswer.add(new Pair<>("dir2", true));
            rightAnswer.add(new Pair<>("file3", false));
            rightAnswer.add(new Pair<>("file1", false));
            rightAnswer.add(new Pair<>("file2", false));
            assertEquals(rightAnswer.size(), equalLists(rightAnswer, files));
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
            List<Pair<String, Boolean>> files1 = client1.list(dirTestList1.getAbsolutePath());
            List<Pair<String, Boolean>> files2 = client2.list(dirTestList2.getAbsolutePath());
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
            List<Pair<String, Boolean>> files = client.list(dirTestList1.getAbsolutePath() + "/test3");
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
            client.get(file1ForTestGet.getAbsolutePath(), copyFile1ForTestGet.getAbsolutePath());
            assertEquals(true, equalFiles(file1ForTestGet, copyFile1ForTestGet));

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
            client1.get(file1ForTestGet.getAbsolutePath(), copyFile1ForTestGet.getAbsolutePath());
            assertEquals(true, equalFiles(file1ForTestGet, copyFile1ForTestGet));

            Client client2 = new Client("localhost", 1408);
            client2.get(file2ForTestGet.getAbsolutePath(), copyFile2ForTestGet.getAbsolutePath());
            assertEquals(true, equalFiles(file2ForTestGet, copyFile2ForTestGet));

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
            File file = client.get(dirTestGet.getAbsolutePath() + "/file3", dirTestGet.getAbsolutePath() + "/copyFile3");
            assertEquals(0, file.length());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Compare two List<Pair<String, Boolean>>
     */
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

    /**
     * Compare files
     */
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