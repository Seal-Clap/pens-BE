package com.example.pens.util;

public class FileUtil {
    public static String buildFileName(Integer groupId, String originalFileName) {
        return String.valueOf(groupId) + "/" + originalFileName;
    }
}
