package ru.spbau.mit.alyokhina;

import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class HasherTest {
    @Test
    public void testCompareGetMdt() throws Exception {
        Hasher hash = new Hasher();
        Md5 hash1 = hash.getMd5(Paths.get("2.txt"));
        Md5 hash2 = hash.getMd5forThreads(Paths.get("2.txt"));
        byte[] bytes1 = hash1.get(), bytes2 = hash2.get();
        assertEquals(bytes1.length, bytes2.length);
        for (int i = 0; i < bytes1.length; i++) {
            assertEquals(bytes1[i], bytes2[i]);
        }

    }

}