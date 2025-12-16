package com.example.library.util;

public class XmlDataManager {

    public static String getDataFilePath(String fileName) {
        String baseDir = System.getProperty("xml.data.dir");
        if (baseDir == null || baseDir.isEmpty()) {
            baseDir = "data" + java.io.File.separator;
        }
        if (!baseDir.endsWith(java.io.File.separator)) {
            baseDir = baseDir + java.io.File.separator;
        }
        return baseDir + fileName;
    }
}
