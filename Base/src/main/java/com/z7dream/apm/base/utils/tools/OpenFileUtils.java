package com.z7dream.apm.base.utils.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import com.z7dream.apm.base.Appli;
import com.z7dream.apm.base.utils.cache.CacheManager;
import com.z7dream.apm.base.utils.cache.SPreference;
import com.z7dream.apm.base.utils.explorer.FileType;
import com.z7dream.apm.base.utils.file.FileProvider7;
import com.z7dream.apm.base.utils.listener.Callback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;

/**
 * Created by ziyouxingdong on 2017/5/17.
 */

public class OpenFileUtils {
    public static void openFile(String filePath) {
        Intent intent = getFileIntent(filePath);
        if (intent != null)
            Appli.Companion.getContext().startActivity(intent);
        else EToast.get().showToast(Appli.Companion.getContext(), "没有可以打开文件的软件！");
    }

    /**
     * @param filePath
     * @return
     */
    private static Intent getFileIntent(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        // XXX Sigi edited
        /* 取得扩展名 */
        String end = FileUtils.getExtensionName(filePath).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        if (FileType.isAudio(end)) {
            return getAudioFileIntent(filePath);
        } else if (FileType.isVideo(end)) {
            return getVideoFileIntent(filePath);
        } else if (FileType.isPic(end) || end.equals("gif")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt") || (end.equals("pptx") || (end.endsWith(".zip")) || (end.endsWith(".rar")))) {
            if (WPSUtils.isHasWPS()) {
                return getWPSFileIntent(filePath);
            } else {
                return getAllIntent(filePath);
            }
        } else if (end.equals("xls") || (end.equals("xlsx"))) {
            if (WPSUtils.isHasWPS()) {
                return getWPSFileIntent(filePath);
            } else {
                return getAllIntent(filePath);
            }
        } else if (end.equals("doc") || (end.equals("docx"))) {
            if (WPSUtils.isHasWPS()) {
                return getWPSFileIntent(filePath);
            } else {
                return getAllIntent(filePath);
            }
        } else if (end.equals("pdf")) {
            if (WPSUtils.isHasWPS()) {
                return getWPSFileIntent(filePath);
            } else {
                return getAllIntent(filePath);
            }
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            if (WPSUtils.isHasWPS()) {
                return getWPSFileIntent(filePath);
            }
            return getTextFileIntent(filePath, false);
        } else {
            return getAllIntent(filePath);
        }
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        //        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    // Android获取一个WPS的intent
    public static Intent getWPSFileIntent(String param) {
        IntentFilter localIntentFilter1 = new IntentFilter("cn.wps.moffice.file.close");
        localIntentFilter1.addAction("cn.wps.moffice.file.save");
        Appli.Companion.getContext().registerReceiver(mCloseReceiver, localIntentFilter1);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("OpenMode", "ReadOnly");
        bundle.putBoolean("SendCloseBroad", true);
        bundle.putBoolean("SendSaveBroad", true);
        bundle.putString("SavePath", getWpsDir());
        bundle.putString("ThirdPackage", Appli.Companion.getContext().getPackageName());
        bundle.putBoolean("ClearBuffer", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setData(uri);
        intent.putExtras(bundle);
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }


    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (paramBoolean) {
            uri = Uri.parse(param);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        }
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static BroadcastReceiver mCloseReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent paramIntent) {
            Bundle localBundle = paramIntent.getExtras();
            String closefile = localBundle.getString("CloseFile");

            String newFilePath = getWpsDir() + closefile.substring(closefile.lastIndexOf("/") + 1, closefile.length());
            copyFile(new File(closefile), new File(newFilePath));
        }
    };

    public static String getWpsDir() {
        return CacheManager.getEsCompanyPath(CacheManager.WORD, SPreference.getDefaultCompanyId());
    }

    public static void copyFile(File sourceFile, File targetFile) {
        try {
            FileInputStream input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);

            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
        } catch (Exception e) {
        }
    }

    public static void openFile(String filePath, Callback<String> callback) {
        String finishExc = FileUtils.getExtensionName(filePath);
        int type = FileType.createFileType(finishExc);
        switch (type) {
            case FileType.TXT:
            case FileType.EXCEL:
            case FileType.PPT:
            case FileType.WORD:
            case FileType.PDF:
                WPSUtils.openWpsFile(Appli.Companion.getContext(), filePath);
                break;
            case FileType.AUDIO:
                callback.event(filePath);
                break;
            default:
                openFile(filePath);
                break;
        }
    }

//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//    Uri uri = FileProvider7.getUriForFile(Appli.Companion.getContext(), new File(param));
}
