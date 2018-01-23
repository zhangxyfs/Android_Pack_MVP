package com.z7dream.apm.base.utils.explorer.collection;


import com.z7dream.apm.base.utils.explorer.FileType;
import com.z7dream.apm.base.utils.tools.FileUtils;

import java.io.File;

/**
 * Created by Z7Dream on 2017/5/25 15:29.
 * Email:zhangxyfs@126.com
 */

public class Collect {
    public static File maxFileByTime(File[] files) {
        if (files.length == 0) return null;
        File file = files[0];
        for (int i = 0; i < files.length; i++) {
            if (file.lastModified() < files[i].lastModified()) {
                file = files[i];
            }
        }
        return file;
    }

    public static File maxFileByTime(File[] files, int fileType) {
        if (files.length == 0) return null;
        File file = files[0];
        for (int i = 0; i < files.length; i++) {
            int type = FileType.createFileType(FileUtils.getExtensionName(files[i].getPath()));
            if (file.lastModified() < files[i].lastModified() && type == fileType) {
                file = files[i];
            }
        }
        return file;
    }

    /**
     * 获取某个类型的文件数量
     *
     * @param files    文件列表
     * @param fileType 文件类型 {@link com.z7dream.apm.base.utils.explorer.FileType}
     * @return
     */
    public static int fileNumByExc(File[] files, int fileType) {
        if (files.length == 0) return 0;
        int count = 0;
        for (int i = 0; i < files.length; i++) {
            int type = FileType.createFileType(FileUtils.getExtensionName(files[i].getPath()));
            if (fileType == type) {
                count++;
            }
        }
        return count;
    }
}
