package ru.spbau.mit.alyokhina;

public class Md5 {
    private byte[] bytes;
    public Md5(byte[] bytes) {
        this.bytes = bytes;
    }
    public byte[] get() {
        return bytes;
    }
}