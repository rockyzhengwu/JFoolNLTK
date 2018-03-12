package me.midday.utils;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class PathKit {

    private static String ROOT_PATH = "";

    public static void setRootPath(String rootPath) {
        ROOT_PATH = rootPath;
    }

    public static String getConfPath() {
        return getRootPath() + "/conf/";
    }

    public static String getRootPath() {
        if (ROOT_PATH != null && ROOT_PATH.length() > 0) {
            return ROOT_PATH;
        } else {
            String path;
            if (PathKit.class.getResource("/") != null) {
                URL url = PathKit.class.getResource("/");
                String tPath = url.getPath().replace("file:", "");
                try {
                    path = URLDecoder.decode(tPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    //e.printStackTrace();
                    path = tPath;
                }
                path = new File(path).getParent();
            } else {
                path = System.getProperty("java.home");
            }
            return path;
        }
    }

    public static String getConfFile(String file) {
        return getConfPath() + file;
    }

    public static String getStaticPath() {
        return getRootPath() + "/static/";
    }

    public static String getTempPath() {
        String str = getRootPath() + "/temp/";
        new File(str).mkdirs();
        return str;
    }
}
