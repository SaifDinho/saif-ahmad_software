package com.example.library.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class XmlDataManagerTest {

    private String originalProp;

    @BeforeEach
    void setUp() {
        originalProp = System.getProperty("xml.data.dir");
    }

    @AfterEach
    void tearDown() {
        if (originalProp == null) {
            System.clearProperty("xml.data.dir");
        } else {
            System.setProperty("xml.data.dir", originalProp);
        }
    }

    @Test
    void getDataFilePath_usesSystemProperty_WhenSet() {
        System.setProperty("xml.data.dir", "custom" + File.separator + "xml");
        String path = XmlDataManager.getDataFilePath("users.xml");
        assertTrue(path.startsWith("custom" + File.separator + "xml" + File.separator));
        assertTrue(path.endsWith("users.xml"));
    }

    @Test
    void getDataFilePath_usesDefault_WhenPropertyNotSet() {
        System.clearProperty("xml.data.dir");
        String path = XmlDataManager.getDataFilePath("users.xml");
        assertTrue(path.startsWith("data" + File.separator));
        assertTrue(path.endsWith("users.xml"));
    }

    @Test
    void getDataFilePath_usesDefault_WhenPropertyEmpty() {
        System.setProperty("xml.data.dir", "");
        String path = XmlDataManager.getDataFilePath("users.xml");
        assertTrue(path.startsWith("data" + File.separator));
        assertTrue(path.endsWith("users.xml"));
    }

    @Test
    void getDataFilePath_addsSeparator_WhenPropertyMissingTrailingSeparator() {
        System.setProperty("xml.data.dir", "custom");
        String path = XmlDataManager.getDataFilePath("users.xml");
        assertTrue(path.startsWith("custom" + File.separator));
        assertTrue(path.endsWith(File.separator + "users.xml"));
    }

    @Test
    void getDataFilePath_preservesSeparator_WhenPropertyHasTrailingSeparator() {
        System.setProperty("xml.data.dir", "custom" + File.separator);
        String path = XmlDataManager.getDataFilePath("users.xml");
        assertTrue(path.startsWith("custom" + File.separator));
        assertTrue(path.endsWith(File.separator + "users.xml"));
    }
}
