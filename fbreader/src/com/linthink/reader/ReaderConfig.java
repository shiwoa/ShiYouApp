package com.linthink.reader;


public class ReaderConfig {
    private static ReaderConfig instance;

    private ReaderConfig() {
    }

    public static ReaderConfig instance() {
        if (instance == null) {
            instance = new ReaderConfig();
        }
        return instance;
    }

    public boolean needDecryption() {
        return false;
    }

    public boolean needShare() {
        return true;
    }

    public boolean textSearchAcitivtySlideInUp() {
        return false;
    }
}
