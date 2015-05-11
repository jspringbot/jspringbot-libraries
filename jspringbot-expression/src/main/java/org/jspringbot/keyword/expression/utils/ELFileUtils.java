package org.jspringbot.keyword.expression.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ELFileUtils {

    public static FileInputStream stream(Object... files) throws IOException {
        File file = file(files);
        return new FileInputStream(file);
    }


    public static FileInputStream streamResource(Object... files) throws IOException {
        File file = resource(files);
        return new FileInputStream(file);
    }

    public static File resource(Object... files) throws IOException {
        File file;
        File dir;
        if(files.length > 1) {
            if(File.class.isInstance(files[0])) {
                dir = (File) files[0];
            } else {
                dir = getResource(String.valueOf(files[0]));
            }

            makeDirs(dir);

            if(File.class.isInstance(files[1])) {
                return new File(dir, ((File) files[1]).getName());
            } else {
                return new File(dir, String.valueOf(files[1]));
            }
        } else if(files.length == 1) {
            if(File.class.isInstance(files[0])) {
                file = (File) files[0];
            } else {
                file = getResource(String.valueOf(files[0]));
            }

            makeDirs(file.getParentFile());
            return file;
        } else {
            throw new IllegalArgumentException("Expected usage: toFile(dir, file), toFile(file)");
        }
    }

    private static void makeDirs(File dir) {
        if(!dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    private static File getResource(String path) throws IOException {
        if(!StringUtils.startsWith(path, "file:") && !StringUtils.startsWith(path, "classpath:")) {
            return file(path);
        }

        ResourceEditor editor = new ResourceEditor();
        editor.setAsText(path);

        return ((Resource) editor.getValue()).getFile();
    }

    public static File file(Object... files) {
        File file;
        File dir;
        if(files.length > 1) {
            if(File.class.isInstance(files[0])) {
                dir = (File) files[0];
            } else {
                dir = new File(String.valueOf(files[0]));
            }

            makeDirs(dir);

            if(File.class.isInstance(files[1])) {
                return new File(dir, ((File) files[1]).getName());
            } else {
                return new File(dir, String.valueOf(files[1]));
            }
        } else if(files.length == 1) {
            if(File.class.isInstance(files[0])) {
                file = (File) files[0];
            } else {
                file = new File(String.valueOf(files[0]));
            }

            makeDirs(file.getParentFile());

            return file;
        } else {
            throw new IllegalArgumentException("Expected usage: toFile(dir, file), toFile(file)");
        }
    }

    public static String name(File file) {
        return file.getName();
    }

    public static void copy(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }


    public static String nameWithoutExtension(File file) {
        return StringUtils.split(file.getName(), ".")[0];
    }

    public static String extension(File file) {
        String name = name(file);
        return name.substring(name.indexOf(".") + 1);
    }

    public static boolean isFile(File file) {
        return file.isFile();
    }

    public static boolean isDirectory(File file) {
        return file.isDirectory();
    }

    public static boolean mkdirs(File file) {
        return file.mkdirs();
    }
}
