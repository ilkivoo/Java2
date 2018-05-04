package ru.spbau.mit.alyokhina;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/** A server that processes two list requests and receives */
public class Server {
    /** Value for request list */
    public static int LIST_REQUEST = 1;
    /** Value for request get */
    public static int GET_REQUEST = 2;

    /** Socket for connection with this server */
    private ServerSocket serverSocket;

    /**
     * Constructor
     *
     * @param port port of connection
     * @throws IOException if Socket can't be created
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);

    }

    /** Start of the server */
    public void start() {
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                Thread thread = new Thread(() -> {
                    try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                         DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
                        while (!Thread.interrupted()) {
                            int requestType = dataInputStream.readInt();
                            String path = dataInputStream.readUTF();
                            if (requestType == LIST_REQUEST) {
                                list(path, dataOutputStream);
                            } else if (requestType == GET_REQUEST) {
                                get(path, dataOutputStream);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                });
                thread.start();
            } catch (IOException ignored) {
                break;
            }
        }
    }

    /**
     * Write count files, names of files and their types from input directory to dataOutputStream
     *
     * @param path             directory path
     * @param dataOutputStream stream for write result
     * @throws IOException if it is impossible to write in dataOutputStream
     */
    private void list(String path, DataOutputStream dataOutputStream) throws IOException {
        File directory = new File(path);
        File[] files = directory.listFiles();
        dataOutputStream.writeInt(files == null ? 0 : files.length);
        if (files != null) {
            for (File file : files) {
                dataOutputStream.writeUTF(file.getName());
                dataOutputStream.writeBoolean(file.isDirectory());
            }
        }
        dataOutputStream.flush();
    }

    /**
     * Write file contents in dataOutputStream
     * @param path name of file
     * @param dataOutputStream OutputStream for write result
     * @throws IOException
     */
    private void get(String path, DataOutputStream dataOutputStream) throws IOException {
        File file = new File(path);
        int length = (int) file.length();
        if (length != 0) {
            DataInputStream dataInputStreamForRequest = new DataInputStream(new FileInputStream(file));
            byte[] bytes = new byte[length];

            if (dataInputStreamForRequest.read(bytes) == length) {
                dataOutputStream.writeInt(length);
                dataOutputStream.write(bytes);
            } else {
                throw new IOException("Impossible to read all data");
            }
            dataInputStreamForRequest.close();
        } else {
            dataOutputStream.writeInt(length);
        }
    }
}





