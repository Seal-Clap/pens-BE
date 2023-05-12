package com.example.pens.util;

import org.springframework.http.ContentDisposition;

import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static String buildFileName(Integer groupId, String originalFileName) {
        return "groupFiles/" + String.valueOf(groupId) + "/" + originalFileName;
    }

    public static ContentDisposition createContentDisposition(String fileName) {
        return ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
    }
}
