package com.losg.library.utils;

import java.io.File;

/**
 * Created by losg on 2016/6/12.
 */
public class ClearCache {

    //删除文件夹下的所有文件或文件夹
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                deleteDirectory(file.getAbsolutePath());
            } else {
                file.delete();
            }
        }
    }

    private static void deleteDirectory(String dir) {
        File file = new File(dir);
        if (file.exists() && file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                if (childFile.isFile()) {
                    childFile.delete();
                } else if (childFile.isDirectory()) {
                    deleteDirectory(childFile.getAbsolutePath());
                }
            }
            file.delete();
        }
    }
}
