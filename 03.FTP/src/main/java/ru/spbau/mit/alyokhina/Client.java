package ru.spbau.mit.alyokhina;

import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** Client, which allows you to execute the requests list and get */
public class Client {
    /** OutputStream from Socket */
    private DataOutputStream dataOutputStream;
    /**InputStream from Socket*/
    private DataInputStream dataInputStream;

    /**
     * Constructor
     *
     * @throws IOException if Socket or Stream can't be created
     */
    public Client(String host, int port) throws IOException {
        Socket clientSocket = new Socket(host, port);
        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

    }


    /**
     * Listing files in the directory on the server
     *
     * @param path directory path
     * @return list of Pair. Fist value - name of file. Second value - type of file ( if file is directory - true, else false)
     * Count files in directory = length of list
     * @throws IOException if we can't read/write in InputStream/OutputStream
     */
    public List<Pair<String, Boolean>> list(String path) throws IOException {
        List<Pair<String, Boolean>> listFiles = new ArrayList<>();
        dataOutputStream.writeInt(Server.LIST_REQUEST);
        dataOutputStream.writeUTF(path);
        dataOutputStream.flush();
        int count = dataInputStream.readInt();
        for (int i = 0; i < count; i++) {
            String fileName = dataInputStream.readUTF();
            Boolean isDirectory = dataInputStream.readBoolean();
            listFiles.add(new Pair<>(fileName, isDirectory));
        }
        return listFiles;
    }

    /**
     * Сopy the file from the server to the file
     *
     * @param path            path of the file from server
     * @param nameFileForSave the name of the file into which the content will be copied
     * @return file into which the content will be copied
     * @throws IOException if we can't read/write in InputStream/OutputStream
     */
    public File get(String path, String nameFileForSave) throws IOException {
        dataOutputStream.writeInt(Server.GET_REQUEST);
        dataOutputStream.writeUTF(path);
        dataOutputStream.flush();
        File fileForSave = new File(nameFileForSave);
        int count = dataInputStream.readInt();
        if (count != 0) {
            byte[] bytes = new byte[count];
            int countReadBytes = dataInputStream.read(bytes);
            if (countReadBytes != count) {
                throw new IOException("Impossible to read all data");
            }
            DataOutputStream dataOutputStreamForSave = new DataOutputStream(new FileOutputStream(fileForSave));
            dataOutputStreamForSave.write(bytes);
        }
        return fileForSave;
    }

}
