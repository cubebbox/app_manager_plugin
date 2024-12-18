package com.letoai.app_manager_plugin;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件处理工具
 */
public class FileUtils {


    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    //特殊文件夹目录，跳过扫描
    private static String[] ignoreDisNames = new String[]{"Android", "DCIM", ""};


    public static File[] getFileList(String dirPath) {
        System.out.println("调用");
        if (dirPath == null) {//不传的话默认根目录
            dirPath = Environment.getExternalStorageDirectory() + "/";
        }
        System.out.println(dirPath);
        File fileDir = new File(dirPath);

        final File[] files = fileDir.listFiles();

        ArrayList<File> fileList = new ArrayList<>(Arrays.asList(files));


        //过滤特殊文件夹
        for (int i = 0; i < fileList.size(); i++) {
            for (int j = 0; j < ignoreDisNames.length; j++) {
                if (ignoreDisNames[j].equals(fileList.get(i).getName())) {
                    fileList.remove(i--);
                    break;
                }
            }
        }

//        long total = 0;
//        for (int i = 0; i < files.length; i++) {
//            total += files[i].length();
////            System.out.println(files[i].getAbsolutePath() + "   " + files[i].length());
//            if (files[i].getName().contains("暴走漫画")) {
//                System.out.println(files[i].getAbsolutePath());
//            }
//        }

//        File file = new File("/storage/emulated/0/Download/wm_crop_pic.jpg");
//        if (file.exists()) {
//            boolean flag = file.delete();
//            System.out.println(flag);
//        } else {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        for (int i = 0; i < fileList.size(); i++) {
            String path = fileList.get(i).getAbsolutePath();
            String total = "0B";
//            try {
//                total = getAutoFileOrFilesSize(fileList.get(i).getAbsolutePath());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            System.out.println(path + "   " + total);
        }

        return files;
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
//            Log.e("获取文件大小", "文件不存在!");
        }
//        System.out.println(file.getAbsolutePath() + "   " + size);
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
//        System.out.println(f.getAbsoluteFile());
        long size = 0;
        File[] flist = f.listFiles();
        if (flist == null || flist.length > 10) {
            return 0;
        }
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

}
