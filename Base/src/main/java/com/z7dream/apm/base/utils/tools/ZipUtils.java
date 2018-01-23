package com.z7dream.apm.base.utils.tools;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

/**
 * 压缩解压
 * Created by xiaoyu.zhang on 2016/6/30.
 */

public class ZipUtils {
    private static final String TAG = ZipUtils.class.getName();
    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    /**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile     生成的压缩文件
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.close();
    }

    /**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile     生成的压缩文件
     * @param comment     压缩文件的注释
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment)
            throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.setComment(comment);
        zipout.close();
    }

    /**
     * 解压缩一个文件
     *
     * @param zipFile    压缩文件
     * @param folderPath 解压缩的目标目录
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static void upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "utf-8");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFF_SIZE];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        }
    }

    /**
     * 解压文件名包含传入文字的文件
     *
     * @param zipFile      压缩文件
     * @param folderPath   目标文件夹
     * @param nameContains 传入的文件匹配名
     * @throws ZipException 压缩格式有误时抛出
     * @throws IOException  IO错误时抛出
     */
    public static ArrayList<File> upZipSelectedFile(File zipFile, String folderPath,
                                                    String nameContains) throws ZipException, IOException {
        ArrayList<File> fileList = new ArrayList<File>();

        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdir();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            if (entry.getName().contains(nameContains)) {
                InputStream in = zf.getInputStream(entry);
                String str = folderPath + File.separator + entry.getName();
                str = new String(str.getBytes("8859_1"), "utf-8");
                // str.getBytes("utf-8"),"8859_1" 输出
                // str.getBytes("8859_1"),"utf-8" 输入
                File desFile = new File(str);
                if (!desFile.exists()) {
                    File fileParentDir = desFile.getParentFile();
                    if (!fileParentDir.exists()) {
                        fileParentDir.mkdirs();
                    }
                    desFile.createNewFile();
                }
                OutputStream out = new FileOutputStream(desFile);
                byte buffer[] = new byte[BUFF_SIZE];
                int realLength;
                while ((realLength = in.read(buffer)) > 0) {
                    out.write(buffer, 0, realLength);
                }
                in.close();
                out.close();
                fileList.add(desFile);
            }
        }
        return fileList;
    }

    /**
     * 获得压缩文件内文件列表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件内文件名称
     * @throws ZipException 压缩文件格式有误时抛出
     * @throws IOException  当解压缩过程出错时抛出
     */
    public static ArrayList<String> getEntriesNames(File zipFile) throws IOException {
        ArrayList<String> entryNames = new ArrayList<String>();
        Enumeration<?> entries = getEntriesEnumeration(zipFile);
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            entryNames.add(new String(getEntryName(entry).getBytes("utf-8"), "8859_1"));
        }
        return entryNames;
    }

    /**
     * 获得压缩文件内压缩文件对象以取得其属性
     *
     * @param zipFile 压缩文件
     * @return 返回一个压缩文件列表
     * @throws ZipException 压缩文件格式有误时抛出
     * @throws IOException  IO操作有误时抛出
     */
    public static Enumeration<?> getEntriesEnumeration(File zipFile) throws
            IOException {
        ZipFile zf = new ZipFile(zipFile);
        return zf.entries();
    }

    /**
     * 取得压缩文件对象的注释
     *
     * @param entry 压缩文件对象
     * @return 压缩文件对象的注释
     * @throws UnsupportedEncodingException
     */
    public static String getEntryComment(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getComment().getBytes("utf-8"), "8859_1");
    }

    /**
     * 取得压缩文件对象的名称
     *
     * @param entry 压缩文件对象
     * @return 压缩文件对象的名称
     * @throws UnsupportedEncodingException
     */
    public static String getEntryName(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getName().getBytes("utf-8"), "8859_1");
    }

    /**
     * 压缩文件
     *
     * @param resFile  需要压缩的文件（夹）
     * @param zipout   压缩的目的文件
     * @param rootpath 压缩的文件路径
     * @throws FileNotFoundException 找不到文件时抛出
     * @throws IOException           当压缩过程出错时抛出
     */
    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath)
            throws IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
                + resFile.getName();
        rootpath = new String(rootpath.getBytes("8859_1"), "utf-8");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath);
            }
        } else {
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
                    BUFF_SIZE);
            zipout.putNextEntry(new ZipEntry(rootpath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
    }

    /**
     * 解压assets的zip压缩文件到指定目录
     *
     * @param context         上下文对象
     * @param assetName       压缩文件名
     * @param outputDirectory 输出目录
     * @param isReWrite       是否覆盖
     * @throws IOException
     */
    public static void unZipFromAsset(Context context, String assetName, String outputDirectory, boolean isReWrite) throws IOException {
        // 打开压缩文件
        InputStream inputStream = context.getAssets().open(assetName);
        unZip(inputStream, outputDirectory, isReWrite);
    }

    /**
     * @param inputStream
     * @param outputDirectory
     * @param isReWrite
     * @throws IOException
     */
    public static void unZip(InputStream inputStream, String outputDirectory, boolean isReWrite) throws IOException {
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    public static byte[] decompress(byte[] array) throws UnsupportedEncodingException {
        if (array == null || array.length == 0) {
            return null;
        }
        byte[] buffer = new byte[1024];

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(array.length);
            ByteArrayInputStream in = new ByteArrayInputStream(array);
            GZIPInputStream gzip = new GZIPInputStream(in); // Magic Number Exception occures here
            int len;
            while ((len = gzip.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            gzip.close();
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String unGZIP(byte[] bytes) {
        if (!isCompressed(bytes)) {
            return "";
        }
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Source source = Okio.source(in);
        GzipSource gzipSource = new GzipSource(source);
        BufferedSource bufferedSource = Okio.buffer(gzipSource);
        String content = "";
        try {
            content = bufferedSource.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedSource.close();
                gzipSource.close();
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return content;
    }

    public static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

    /**
     * 解压缩一个文件
     *
     * @param zipFile    压缩文件
     * @param folderPath 解压缩的目标目录
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static void unZipFile(File zipFile, String folderPath) throws ZipException,
            IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            // 创建目标目录
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            if (entry.isDirectory()) {
                String tmpStr = folderPath + File.separator + entry.getName();
                tmpStr = new String(tmpStr.getBytes("8859_1"), "GB2312");
                File folder = new File(tmpStr);
                folder.mkdirs();
            } else {
                InputStream is = zf.getInputStream(entry);
                String str = folderPath + File.separator + entry.getName();
                // 转换编码，避免中文时乱码
                str = new String(str.getBytes("8859_1"), "GB2312");
                File desFile = new File(str);
                if (!desFile.exists()) {
                    // 创建目标文件
                    desFile.createNewFile();
                }
                OutputStream os = new FileOutputStream(desFile);
                byte[] buffer = new byte[1024];
                int realLength;
                while ((realLength = is.read(buffer)) > 0) {
                    os.write(buffer, 0, realLength);
                    os.flush();
                }
                is.close();
                os.close();
            }
        }
        zf.close();

    }


}
