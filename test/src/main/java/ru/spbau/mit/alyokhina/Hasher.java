package ru.spbau.mit.alyokhina;

import java.io.*;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiConsumer;


public class Hasher {

    public Md5 getMd5(Path path) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("MD5");
        update(messageDigest, path.toFile(), new BiConsumer<MessageDigest, File>() {
            @Override
            public void accept(MessageDigest message, File file) {
                try {
                    update(message, file, this);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        return new Md5(messageDigest.digest());
    }


    private static void update(MessageDigest message, File file, BiConsumer<MessageDigest, File> action) throws IOException {
        if (file.isFile()) {
            try (FileInputStream stream = new FileInputStream(file)) {
                int sizeBuffer = 2048;
                byte[] buffer = new byte[sizeBuffer];
                try (BufferedInputStream bufferedStream = new BufferedInputStream(stream)) {
                    while (true) {
                        int bytesRead = bufferedStream.read(buffer, 0, sizeBuffer);
                        if (bytesRead == -1) {
                            break;
                        }
                        message.update(buffer, 0, bytesRead);
                    }
                }
            }
            return;
        }
        File[] files = file.listFiles();
        message.update(file.getName().getBytes());
        assert files != null;
        for (File file1 : files) {
            action.accept(message, file1);
        }
    }


    public Md5 getMd5forThreads(Path path) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new Task(path.toFile()));
    }


    public class Task extends RecursiveTask<Md5> {
        MessageDigest message;
        File file;

        private Task(File file) {
            try {
                this.message = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }
            this.file = file;
        }

        @Override
        protected Md5 compute() {
            final List<ForkJoinTask<Md5>> taskList = new ArrayList<>();

            try {
                update(message, file, (messageDigest, file) -> {
                            Task task = new Task(file);
                            task.fork();
                            taskList.add(task);
                        }
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (ForkJoinTask<Md5> task : taskList) {
                message.update(task.join().get());
            }

            return new Md5(message.digest());
        }
    }


}


