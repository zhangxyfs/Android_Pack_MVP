package com.z7dream.apm.base.utils.explorer;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.z7dream.apm.base.Appli;
import com.z7dream.apm.base.utils.cache.CacheManager;
import com.z7dream.apm.base.utils.explorer.collator.OrderingConstants;
import com.z7dream.apm.base.utils.listener.Callback;
import com.z7dream.apm.base.utils.rx.RxSchedulersHelper;
import com.z7dream.apm.base.utils.tools.FileUtils;
import com.z7dream.apm.base.utils.tools.Utils;
import com.z7dream.apm.base.utils.tools.WPSUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Flowable.create;

/**
 * Created by Z7Dream on 2017/2/3 17:42.
 * Email:zhangxyfs@126.com
 */

public class MagicExplorer {
    private static final String VOLUME_NAME = "external";
    private static final String TAG = MagicExplorer.class.getName();

    public static final String WPS_PATH = CacheManager.getSaveFilePath() + File.separator + "/Android/Data/cn.wps.moffice_eng/.cache/KingsoftOffice/.history/attach_mapping_v1.json";
    public static final String QQ_PIC_PATH = CacheManager.getSaveFilePath() + File.separator + "tencent" + File.separator + "QQ_Images";
    public static final String QQ_FILE_PATH = CacheManager.getSaveFilePath() + File.separator + "tencent" + File.separator + "QQfile_recv";
    public static final String WX_PIC_PATH = CacheManager.getSaveFilePath() + File.separator + "tencent" + File.separator + "MicroMsg" + File.separator + "WeiXin";
    public static final String WX_FILE_PATH = CacheManager.getSaveFilePath() + File.separator + "tencent" + File.separator + "MicroMsg" + File.separator + "Download";
    public static final String SYS_CAMERA_PATH = CacheManager.getSaveFilePath() + File.separator + "DCIM" + File.separator + "Camera";
    public static final String SCREENSHOTS_PATH = CacheManager.getSaveFilePath() + File.separator + "Pictures";
    public static final String HW_SCREEN_SAVER_PATH = CacheManager.getSaveFilePath() + File.separator + "MagazineUnlock";
    public static final String ES_PATH = CacheManager.getCachePath(Appli.Companion.getContext(), CacheManager.ES);
    public static final String PIC_EBPHOTO_PATH = CacheManager.getCachePath(Appli.Companion.getContext(), CacheManager.EB_PHOTO);

    /**
     * 获取各种类型的文件数量
     *
     * @param callback
     */
    public static void getAllCount(Callback<long[]> callback) {
        long[] newNumbers = new long[9];
        Observable.create((ObservableOnSubscribe<Long>) e -> {
            MagicExplorer.getAllPicCount(e::onNext);
        }).flatMap(num -> {
            newNumbers[0] = num;
            return Observable.create((ObservableOnSubscribe<Long>) e -> {
                MagicExplorer.getAllVoiceCount(e::onNext);
            });
        }).flatMap(num -> {
            newNumbers[1] = num;
            return Observable.create((ObservableOnSubscribe<Long>) e -> {
                MagicExplorer.getAllVideoCount(e::onNext);
            });
        }).flatMap(num -> {
            newNumbers[2] = num;
            return Observable.create((ObservableOnSubscribe<long[]>) e -> {
                MagicExplorer.getAllFileByType(e::onNext);
            });
        }).compose(RxSchedulersHelper.INSTANCE.io())
                .subscribe(nums -> {
                    newNumbers[3] = nums[0];
                    newNumbers[4] = nums[1];
                    newNumbers[5] = nums[2];
                    newNumbers[6] = nums[3];
                    newNumbers[7] = nums[4];
                    newNumbers[8] = nums[5];
                    callback.event(newNumbers);
                }, error -> {
                });
    }


    /**
     * 获取所有图片数量
     *
     * @param callback
     */
    public static void getAllPicCount(Callback<Long> callback) {
        final String[] projectionPhotos = {
                MediaStore.Images.Media.DATA
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = MediaStore.Images.Media.query(
                    Appli.Companion.getContext().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos
                    , MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " NOT IN ('picture','es','picTemp')"
                    , MediaStore.Images.Media.DATE_TAKEN + " DESC"
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .subscribe(cursor -> {
                    callback.event(Long.parseLong(String.valueOf(cursor.getCount())));
                    cursor.close();
                }, error -> {
                });
    }

    /**
     * 获取所有音频数量
     *
     * @param callback
     */
    public static void getAllVoiceCount(Callback<Long> callback) {
        final String[] projectionPhotos = {
                MediaStore.Audio.Media.DATA
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = Appli.Companion.getContext().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos
                    , null, null, null
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .subscribe(cursor -> {
                    callback.event(Long.parseLong(String.valueOf(cursor.getCount())));
                    cursor.close();
                }, error -> {
                });
    }

    /**
     * 获取所有视频数量
     *
     * @param callback
     */
    public static void getAllVideoCount(Callback<Long> callback) {
        final String[] projectionPhotos = {
                MediaStore.Video.Media.DATA
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = Appli.Companion.getContext().getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos
                    , null, null, null
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .subscribe(cursor -> {
                    callback.event(Long.parseLong(String.valueOf(cursor.getCount())));
                    cursor.close();
                }, error -> {
                });
    }

    /**
     * 获取所有类型的文件数量
     *
     * @param callback
     */
    public static void getAllFileByType(Callback<long[]> callback) {
        final String[] projectionPhotos = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = Appli.Companion.getContext().getContentResolver().query(
                    MediaStore.Files.getContentUri(VOLUME_NAME)
                    , projectionPhotos
                    , null
                    , null, null
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .subscribe(cursor -> {
                    long[] fileNumbers = new long[6];
                    while (cursor.moveToNext()) {
                        final int pathColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                        final int sizeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                        final String path = cursor.getString(pathColumn);
                        final long size = cursor.getLong(sizeColumn);
                        String exc = FileUtils.getExtensionName(path);
                        int fileType = FileType.createFileType(exc);

                        if (size <= 4 * 1024 && fileType != FileType.TXT && fileType != FileType.OTHER) {
                            continue;
                        }
                        if ((size <= 8 * 1024 || !path.contains("com.eblog")) && fileType == FileType.OTHER) {
                            continue;
                        }
                        switch (fileType) {
                            case FileType.TXT:
                                fileNumbers[0]++;
                                break;
                            case FileType.EXCEL:
                                fileNumbers[1]++;
                                break;
                            case FileType.PPT:
                                fileNumbers[2]++;
                                break;
                            case FileType.PDF:
                                fileNumbers[3]++;
                                break;
                            case FileType.WORD:
                                fileNumbers[4]++;
                                break;
                            case FileType.OTHER:
                                fileNumbers[5]++;
                                break;
                        }
                    }
                    cursor.close();
                    callback.event(fileNumbers);
                }, error -> {
                    Log.e("tag", error.getMessage());
                });
    }

    /**
     * 获取微信文件列表
     *
     * @param searchKey
     * @param callback
     * @return
     */
    public static Disposable getWXFileList(String searchKey, FileCallback1<List<FileInfo>> callback) {
        return getFileList(searchKey, callback, WX_PIC_PATH, WX_FILE_PATH);
    }

    /**
     * 获取qq文件列表
     *
     * @param searchKey
     * @param callback
     * @return
     */
    public static Disposable getQQFileList(String searchKey, FileCallback1<List<FileInfo>> callback) {
        return getFileList(searchKey, callback, QQ_PIC_PATH, QQ_FILE_PATH);
    }

    private static Disposable getFileList(String searchKey, FileCallback1<List<FileInfo>> callback, String... rootPaths) {
        boolean isHasSearch = !TextUtils.isEmpty(searchKey);
        List<FileInfo> returnList = new ArrayList<>();

        Stack<String> rootStack = new Stack<>();

        for (String rootPath : rootPaths) {//将根目录入栈
            rootStack.push(rootPath);
        }
        return Flowable.create((FlowableOnSubscribe<File>) e -> {
            while (!rootStack.isEmpty()) {
                String temp = rootStack.pop();
                File path = new File(temp);
                File[] files = path.listFiles();
                if (null == files)
                    continue;
                for (File f : files) {
                    // 递归监听目录
                    if (isNeedPathName(f.getName()))
                        if (f.isDirectory()) {
                            rootStack.push(f.getAbsolutePath());
                        } else {
                            e.onNext(f);
                        }
                }
            }
            e.onComplete();
        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(() -> {
                    if (isHasSearch) {
                        callback.callComplete();
                    } else {
                        callback.callListener(returnList);
                    }
                })
                .subscribe(file -> {
                    String exc = FileUtils.getExtensionName(file.getPath());
                    if (!TextUtils.isEmpty(exc)) {
                        FileInfo info = new FileInfo();
                        info.fileName = file.getName();
                        info.path = file.getPath();
                        info.mimeType = getMIMEType(file);
                        info.fileSize = file.length();
                        info.modifyDate = file.lastModified();
                        info.position = FileType.createFileType(exc);

                        if (isHasSearch) {//如果有查询条件
                            if (file.getPath().contains(searchKey)) {//匹配查询
                                returnList.clear();
                                returnList.add(info);
                                callback.callListener(returnList);
                            }
                        } else {
                            returnList.add(info);
                        }
                    }
                }, error -> {
                });
    }

//    private static Disposable getFileList(String searchKey, FileCallback1<List<FileInfo>> callback, String rootPath, String otherPath) {
//        boolean isHasSearch = !TextUtils.isEmpty(searchKey);
//        List<FileInfo> returnList = new ArrayList<>();
//        File folderFile = new File(rootPath);
//        if (!folderFile.exists()) {
//            folderFile.mkdirs();
////            callback.callListener(returnList);
////            return Flowable.empty().subscribe();
//        }
//
//        List<String> rootFolderList = new ArrayList<>();
//        List<String> rootFileList = new ArrayList<>();
//
//        File[] folderFiles = folderFile.listFiles();
//        for (File folderChildFile : folderFiles) {
//            if (folderChildFile.isDirectory()) {
//                rootFolderList.add(folderChildFile.getPath());
//            } else {
//                rootFileList.add(folderChildFile.getPath());
//            }
//        }
//        for (int i = 0; i < rootFileList.size(); i++) {
//            String fileName = FileUtils.getFolderName(rootFileList.get(i));
//            if (isHasSearch && !fileName.contains(searchKey)) {
//                continue;
//            }
//            File file = new File(rootFileList.get(i));
//            String exc = FileUtils.getExtensionName(file.getPath());
//            if (!TextUtils.isEmpty(exc)) {
//                FileInfo info = new FileInfo();
//                info.fileName = fileName;
//                info.path = rootFileList.get(i);
//                info.mimeType = getMIMEType(file);
//                info.fileSize = file.length();
//                info.modifyDate = file.lastModified();
//                info.position = FileType.createFileType(exc);
//                if (isHasSearch) {
//                    returnList.clear();
//                    returnList.add(info);
//                    callback.callListener(returnList);
//                } else {
//                    returnList.add(info);
//                }
//            }
//        }
//        return Flowable.create((FlowableOnSubscribe<File>) e -> {
//            File firstFolderFile = new File(otherPath);
//            if (firstFolderFile.exists()) {
//                File[] firstChildFiles = firstFolderFile.listFiles();
//                for (int pos = 0; pos < firstChildFiles.length; pos++) {
//                    File firstChildFile = firstChildFiles[pos];
//                    if (firstChildFile.isFile() && !firstChildFile.isDirectory() && !firstChildFile.isHidden() && firstChildFile.length() > 0) {
//                        if (isHasSearch && !firstChildFile.getName().contains(searchKey)) {
//                            continue;
//                        }
//                        e.onNext(firstChildFile);
//                    }
//                }
//                for (int i = 0; i < rootFolderList.size(); i++) {
//                    getFolderList(rootFolderList.get(i), path -> {
//                        File childFolderFile = new File(rootPath);
//                        File[] childFiles = childFolderFile.listFiles();
//                        for (int pos = 0; pos < childFiles.length; pos++) {
//                            File childFile = childFiles[pos];
//                            if (childFile.isFile() && !childFile.isDirectory() && !childFile.isHidden() && childFile.length() > 0) {
//                                if (isHasSearch && !childFile.getName().contains(searchKey)) {
//                                    continue;
//                                }
//                                e.onNext(childFile);
//                            }
//                        }
//                    });
//
//                }
//            }
//            e.onComplete();
//        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.INSTANCE.fio())
//                .doOnComplete(() -> {
//                    if (isHasSearch) {
//                        callback.callComplete();
//                    } else {
//                        callback.callListener(returnList);
//                    }
//                })
//                .subscribe(file -> {
//                    String exc = FileUtils.getExtensionName(file.getPath());
//                    if (!TextUtils.isEmpty(exc)) {
//                        FileInfo info = new FileInfo();
//                        info.fileName = file.getName();
//                        info.path = file.getPath();
//                        info.mimeType = getMIMEType(file);
//                        info.fileSize = file.length();
//                        info.modifyDate = file.lastModified();
//                        info.position = FileType.createFileType(exc);
//                        if (isHasSearch) {
//                            returnList.clear();
//                            returnList.add(info);
//                            callback.callListener(returnList);
//                        } else {
//                            returnList.add(info);
//                        }
//                    }
//                }, error -> {
//                });
//    }

    /**
     * 获取所有的图片目录和文件夹，从数据库中查找
     *
     * @param listener
     */
    public static void getAllPicFolderListFromDB(ExplorerListener1 listener) {
        List<MagicPicEntity> folderList = new ArrayList<>();
        List<MagicFileEntity> allPicList = new ArrayList<>();
        Map<String, Integer> folderItemPosMap = new HashMap<>();

        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.MIME_TYPE
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = MediaStore.Images.Media.query(Appli.Companion.getContext().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .doOnComplete(() -> listener.success(folderList, allPicList))
                .subscribe(cursor -> {
                    int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    while (cursor.moveToNext()) {
                        final int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        final int sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                        final int nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
                        final int mimeColumn = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
                        String bucketName = cursor.getString(bucketNameColumn);
                        final String path = cursor.getString(pathColumn);
                        File file = new File(path);
                        if (file.exists() && file.length() > 0) {
                            if (folderItemPosMap.get(bucketName) == null) {
                                folderItemPosMap.put(bucketName, folderList.size());
                            }
//                            folderItemPosMap.putIfAbsent(bucketName, folderList.size());
                            if (folderList.size() < folderItemPosMap.get(bucketName) + 1) {
                                MagicPicEntity magicPicEntity = new MagicPicEntity();
                                magicPicEntity.name = bucketName;
                                magicPicEntity.icon = path;
                                magicPicEntity.path = file.getParent();
                                magicPicEntity.childNum = 1;
                                folderList.add(magicPicEntity);
                            } else {
                                folderList.get(folderItemPosMap.get(bucketName)).childNum += 1;
                            }
                        }
                        MagicFileEntity entity = new MagicFileEntity();
                        entity.name = cursor.getString(nameColumn);
                        entity.size = cursor.getLong(sizeColumn);
                        entity.path = path;
                        entity.time = file.lastModified();
                        entity.mimieType = cursor.getString(mimeColumn);
                        entity.isFile = true;
                        allPicList.add(entity);
                    }
                    cursor.close();
                }, error -> {
                    error.getMessage();
                });
    }

    public static void getAllPicListFromDB(FileCallback<List<MagicFileEntity>> callback) {
        List<MagicFileEntity> allPicList = new ArrayList<>();
        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.MIME_TYPE
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = MediaStore.Images.Media.query(Appli.Companion.getContext().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(() -> callback.callListener(allPicList))
                .subscribe(cursor -> {
                    while (cursor.moveToNext()) {
                        final int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        final String path = cursor.getString(pathColumn);
                        File file = new File(path);
                        if (file.exists() && file.length() > 0) {
                            MagicFileEntity allPicChild = new MagicFileEntity();
                            allPicChild.name = file.getName();
                            allPicChild.size = file.length();
                            allPicChild.path = file.getPath();
                            allPicChild.time = file.lastModified();
                            allPicChild.isFile = true;
                            allPicList.add(allPicChild);
                        }
                    }
                    ArrayList<MagicFileEntity> needDeleteList1 = new ArrayList<>();
                    for (int i = 0; i < allPicList.size(); i++) {
                        String parentPath = new File(allPicList.get(i).path).getPath() + File.separator;
                        boolean isEBlogPicFolder = TextUtils.equals(parentPath, CacheManager.getCachePath(CacheManager.PIC));
                        boolean isEBlogESFolder = TextUtils.equals(parentPath, CacheManager.getCachePath(CacheManager.ES));
                        boolean isEBlogTempFolder = TextUtils.equals(parentPath, CacheManager.getCachePath(CacheManager.PIC_TEMP));

                        boolean isUnDisplayFolder = isEBlogPicFolder || isEBlogESFolder || isEBlogTempFolder;//不需要显示的文件
                        if (isUnDisplayFolder) {
                            needDeleteList1.add(allPicList.get(i));
                        }
                    }
                    allPicList.removeAll(needDeleteList1);

                    cursor.close();
                }, error -> {
                });
    }


    public static void getAllPicFolderListFromDB(FileCallback<ArrayList<MagicPicEntity1>> callback) {
        ArrayList<MagicPicEntity1> dataList = new ArrayList<>();
        Map<String, Integer> folderItemPosMap = new HashMap<>();

        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.MIME_TYPE
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = MediaStore.Images.Media.query(
                    Appli.Companion.getContext().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos
                    , MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " NOT IN ('picture','es','picTemp')"
                    , MediaStore.Images.Media.DATE_TAKEN + " DESC"
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(() -> {
                    callback.callListener(dataList);
                })
                .subscribe(cursor -> {
                    List<MagicFileEntity> allPicList = new ArrayList<>();

                    int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    while (cursor.moveToNext()) {
                        final int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        final int sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                        final int nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
                        final int mimeColumn = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
                        String bucketName = cursor.getString(bucketNameColumn);
                        final String path = cursor.getString(pathColumn);
                        File file = new File(path);
                        if (file.exists() && file.length() > 0) {
                            //生成所有图片
                            MagicFileEntity allPicChild = new MagicFileEntity();
                            allPicChild.name = file.getName();
                            allPicChild.size = file.length();
                            allPicChild.path = file.getPath();
                            allPicChild.time = file.lastModified();
                            allPicChild.isFile = true;
                            allPicList.add(allPicChild);


                            if (folderItemPosMap.get(bucketName) == null) {
                                folderItemPosMap.put(bucketName, dataList.size());
                            }
                            if (dataList.size() < folderItemPosMap.get(bucketName) + 1) {
                                MagicPicEntity1 itemEntity = new MagicPicEntity1();
                                itemEntity.name = bucketName;
                                itemEntity.icon = path;
                                itemEntity.path = file.getParent();

                                if (TextUtils.equals(bucketName, "EB_photo")) {
                                    File file1 = new File(file.getParent());
                                    itemEntity.childNum = file1.list().length;

                                    itemEntity.childList = new ArrayList<>();
                                    for (int childPos = 0; childPos < file1.list().length; childPos++) {
                                        File childFile = file1.listFiles()[childPos];

                                        MagicFileEntity child = new MagicFileEntity();
                                        child.name = childFile.getName();
                                        child.size = childFile.length();
                                        child.path = childFile.getPath();
                                        child.time = childFile.lastModified();
                                        child.isFile = true;
                                        itemEntity.childList.add(child);
                                    }

                                    itemEntity.icon = itemEntity.childList.get(0).path;

                                } else {
                                    itemEntity.childNum = 1;

                                    itemEntity.childList = new ArrayList<>();
                                    MagicFileEntity firstChild = new MagicFileEntity();
                                    firstChild.name = cursor.getString(nameColumn);
                                    firstChild.size = cursor.getLong(sizeColumn);
                                    firstChild.path = path;
                                    firstChild.time = file.lastModified();
                                    firstChild.mimieType = cursor.getString(mimeColumn);
                                    firstChild.isFile = true;
                                    itemEntity.childList.add(firstChild);
                                }
                                dataList.add(itemEntity);
                            } else {
                                if (!TextUtils.equals(bucketName, "EB_photo")) {
                                    dataList.get(folderItemPosMap.get(bucketName)).childNum += 1;

                                    MagicFileEntity child = new MagicFileEntity();
                                    child.name = cursor.getString(nameColumn);
                                    child.size = cursor.getLong(sizeColumn);
                                    child.path = path;
                                    child.time = file.lastModified();
                                    child.mimieType = cursor.getString(mimeColumn);
                                    child.isFile = true;
                                    dataList.get(folderItemPosMap.get(bucketName)).childList.add(child);
                                }
                            }
                        }
                    }

                    MagicPicEntity1 itemEntity = new MagicPicEntity1();
                    itemEntity.name = "ALL";
                    itemEntity.icon = allPicList.size() > 0 ? allPicList.get(0).path : "";
                    itemEntity.path = "";
                    itemEntity.childNum = allPicList.size();
                    itemEntity.childList = new ArrayList<>();
                    itemEntity.childList.addAll(allPicList);
                    dataList.add(0, itemEntity);

                    cursor.close();
                }, error -> {
                    error.getMessage();
                });
    }

    /**
     * 获取wps文件列表
     *
     * @param callback
     */
    public static Disposable getWPSFileList(String searchKey, FileCallback1<List<FileInfo>> callback) {
        boolean isHasSearch = !TextUtils.isEmpty(searchKey);
        List<FileInfo> returnList = new ArrayList<>();
        return Flowable.create((FlowableEmitter<String> e) -> {
            File file = new File(WPS_PATH);
            if (file.isFile() && file.exists()) {
                StringBuilder stringBuffer = new StringBuilder();
                String line;
                FileInputStream fileInputStream = null;
                InputStreamReader inputStreamReader = null;
                BufferedReader reader = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                    reader = new BufferedReader(inputStreamReader);
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                } catch (IOException exce) {
                    exce.printStackTrace();
                } finally {
                    try {
                        if (reader != null)
                            reader.close();
                        if (inputStreamReader != null)
                            inputStreamReader.close();
                        if (fileInputStream != null)
                            fileInputStream.close();
                    } catch (IOException exce) {
                        exce.printStackTrace();
                    }
                }
                e.onNext(stringBuffer.toString());
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.INSTANCE.fio())
                .flatMap(json -> {
                    List<WPSUtils.WPSJSONBean> list = new Gson().fromJson(json, new TypeToken<List<WPSUtils.WPSJSONBean>>() {
                    }.getType());
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    return Flowable.just(list);
                })
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(() -> {
                    if (isHasSearch) {
                        callback.callComplete();
                    } else {
                        callback.callListener(returnList);
                    }
                })
                .subscribe(list -> {
                    for (int i = 0; i < list.size(); i++) {
                        WPSUtils.WPSJSONBean bean = list.get(i);
                        File itemFile = new File(bean.filePath);
                        if (itemFile.isFile()) {
                            String fileName = FileUtils.getFolderName(bean.filePath);
                            if (isHasSearch && !fileName.contains(searchKey)) {
                                continue;
                            }
                            FileInfo info = new FileInfo();
                            info.fileName = fileName;
                            info.path = bean.filePath;
                            String exc = FileUtils.getExtensionName(bean.filePath);
                            if (!TextUtils.isEmpty(exc)) {
                                info.position = FileType.createFileType(exc);
                            }
                            info.fileSize = itemFile.length();
                            info.isFile = true;
                            info.modifyDate = itemFile.lastModified();
                            info.mimeType = getMIMEType(itemFile);

                            if (isHasSearch) {
                                returnList.clear();
                                returnList.add(info);
                                callback.callListener(returnList);
                            } else
                                returnList.add(info);
                        }
                    }
                });
    }


    /**
     * 获取本机文件（目录）
     *
     * @param rootPath
     * @param callback
     */
    public static Disposable getFolderAndFileList(String rootPath, FileCallback<List<FileInfo>> callback) {
        if (TextUtils.isEmpty(rootPath)) {
            rootPath = CacheManager.getSaveFilePath();
        }
        File fileFolder = new File(rootPath);

        List<FileInfo> folderList = new ArrayList<>();
        List<FileInfo> fileList = new ArrayList<>();
        List<FileInfo> retrunList = new ArrayList<>();
        return Flowable.create((FlowableOnSubscribe<String>) e -> {
            for (int i = 0; i < fileFolder.list().length; i++) {
                e.onNext(fileFolder.getPath() + File.separator + fileFolder.list()[i]);
            }
            e.onComplete();
        }, BackpressureStrategy.BUFFER)
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .flatMap(childFilePath -> {
                    FileInfo info = new FileInfo();
                    if (!childFilePath.contains("nomedia") && !childFilePath.startsWith(".")) {
                        File childFile = new File(childFilePath);
                        String exc = FileUtils.getExtensionName(childFile.getPath());
                        if (!childFile.isHidden()) {
                            info.fileName = childFile.getName();
                            info.path = childFilePath;
                            info.fileSize = childFile.length();
                            info.mimeType = getMIMEType(childFile);
                            info.modifyDate = childFile.lastModified();
                            if (!TextUtils.isEmpty(exc)) {
                                info.position = FileType.createFileType(exc);
                            }
                            info.isFile = !childFile.isDirectory();
                            if (!info.isFile) {
                                info.position = FileType.FOLDER;
                            }
                        }
                    }
                    return Flowable.just(info);
                })
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(() -> {
                    try {
                        Collections.sort(folderList, OrderingConstants.Model_NAME_ORDERING);
                        Collections.sort(fileList, OrderingConstants.Model_NAME_ORDERING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    retrunList.addAll(folderList);
                    retrunList.addAll(fileList);
                    callback.callListener(retrunList);
                })
                .subscribe(info -> {
                    if (!TextUtils.isEmpty(info.path)) {
                        if (info.isFile) {
                            fileList.add(info);
                        } else {
                            folderList.add(info);
                        }
                    }
                }, error -> {
                });

    }

    /**
     * 搜索本机文件（目录）
     *
     * @param searchKey
     * @param callback
     */
    public static Disposable searchInFolderAndFileList(String searchKey, FileCallback1<List<FileInfo>> callback) {
        String rootPath = CacheManager.getSaveFilePath();
        File folderFile = new File(rootPath);
        List<String> rootFolderList = new ArrayList<>();
        List<String> rootFileList = new ArrayList<>();
        for (int i = 0; i < folderFile.list().length; i++) {
            File childFile = new File(folderFile.getPath() + File.separator + folderFile.list()[i]);
            if (childFile.isDirectory()) {
                rootFolderList.add(childFile.getPath());
            } else {
                rootFileList.add(childFile.getPath());
            }
        }
        for (int i = 0; i < rootFileList.size(); i++) {
            String fileName = FileUtils.getFolderName(rootFileList.get(i));
            if (fileName.contains(searchKey)) {
                File file = new File(rootFileList.get(i));
                String exc = FileUtils.getExtensionName(file.getPath());
                if (!TextUtils.isEmpty(exc)) {
                    FileInfo info = new FileInfo();
                    info.fileName = fileName;
                    info.path = rootFileList.get(i);
                    info.mimeType = getMIMEType(file);
                    info.fileSize = file.length();
                    info.modifyDate = file.lastModified();
                    info.position = FileType.createFileType(exc);
                    List<FileInfo> list = new ArrayList<>();
                    list.add(info);
                    callback.callListener(list);
                }
            }
        }
        return Flowable.create((FlowableOnSubscribe<File>) e -> {
            for (int i = 0; i < rootFolderList.size(); i++) {
                getFolderList(rootFolderList.get(i), path -> {
                    File childFolderFile = new File(rootPath);
                    File[] childFiles = childFolderFile.listFiles();
                    for (int pos = 0; pos < childFiles.length; pos++) {
                        File childFile = childFiles[pos];
                        if (childFile.isFile() && !childFile.isDirectory() && childFile.getName().contains(searchKey)
                                && !childFile.isHidden() && childFile.length() > 0) {
                            e.onNext(childFile);
                        }
                    }
                });
            }
            e.onComplete();
        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(callback::callComplete)
                .subscribe(file -> {
                    String exc = FileUtils.getExtensionName(file.getPath());
                    if (!TextUtils.isEmpty(exc)) {
                        FileInfo info = new FileInfo();
                        info.fileName = file.getName();
                        info.path = file.getPath();
                        info.mimeType = getMIMEType(file);
                        info.fileSize = file.length();
                        info.modifyDate = file.lastModified();
                        info.position = FileType.createFileType(exc);
                        List<FileInfo> list = new ArrayList<>();
                        list.add(info);
                        callback.callListener(list);
                    }
                }, error -> {
                });
    }

    /**
     * 搜索本机文件（目录）
     *
     * @param searchKey
     * @param callback
     */
    public static Disposable searchInFolderAndFileListFromDB(String searchKey, FileCallback1<List<FileInfo>> callback) {
        final String[] projectionPhotos = {
                MediaStore.Files.FileColumns.DATA
        };
        Disposable disposable = Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = Appli.Companion.getContext().getContentResolver().query(
                    MediaStore.Files.getContentUri(VOLUME_NAME)
                    , projectionPhotos
                    , MediaStore.Files.FileColumns.DATA + " LIKE ?"
                    , new String[]{"%" + searchKey + "%"}
                    , MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC"
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(callback::callComplete)
                .subscribe(cursor -> {
                    while (cursor.moveToNext()) {
                        final int pathColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                        final String path = cursor.getString(pathColumn);
                        File file = new File(path);
                        String exc = FileUtils.getExtensionName(file.getPath());
                        if (!TextUtils.isEmpty(exc)) {
                            FileInfo info = new FileInfo();
                            info.fileName = file.getName();
                            info.path = path;
                            info.mimeType = getMIMEType(file);
                            info.fileSize = file.length();
                            info.modifyDate = file.lastModified();
                            info.position = FileType.createFileType(exc);
                            List<FileInfo> list = new ArrayList<>();
                            list.add(info);
                            callback.callListener(list);
                        }


                    }
                    cursor.close();
                }, error -> {
                });
        return disposable;
    }

    /**
     * 获得文件列表
     *
     * @param path
     * @return
     */
    @Nullable
    public static void getFileList(@NonNull String path, ExplorerListener listener) {
        getFileList(path, FileType.ALL, true, listener);
    }

    /**
     * 获取所有文件列表
     *
     * @param path
     * @param fileType
     * @param listener
     */
    public static void getAllFileList(@NonNull String path, int fileType, boolean isNeedFolder, int getNum, ExplorerListener listener) {
        if (TextUtils.equals(path, "/")) {
            path = CacheManager.getSaveFilePath();
        } else if (!path.startsWith(CacheManager.getSaveFilePath())) {
            path = CacheManager.getSaveFilePath() + path;
        }

        List<String> folderList = new ArrayList<>();
        folderList.add(path);
        String finalPath = path;
        create((FlowableOnSubscribe<List<String>>) e -> {
            getFolderList(finalPath, folderList);
            e.onNext(folderList);
        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.INSTANCE.fio())
                .subscribe(value -> getAllFileList(value, fileType, isNeedFolder, getNum, listener), error -> {
                });
    }

    /**
     * 根据文件路径获取所有文件列表
     *
     * @param folderList
     * @param fileType
     * @param isNeedFolder 是否需要返回文件夹
     * @param getNum       获取数量
     * @param listener
     */
    public static void getAllFileList(List<String> folderList, int fileType, boolean isNeedFolder, int getNum, ExplorerListener listener) {
        List<MagicFileEntity> returnList = new ArrayList<>();

        Flowable<File> flowable = create((FlowableOnSubscribe<String>) e -> {
            for (int i = 0; i < folderList.size(); i++) {
                e.onNext(folderList.get(i));
            }
            e.onComplete();
        }, BackpressureStrategy.BUFFER).flatMap(folderPath -> {
            File rootFile = new File(folderPath);
            File[] listFile = rootFile.listFiles();
            return Flowable.fromArray(listFile);
        });

        if (getNum > 0) {
            flowable.compose(RxSchedulersHelper.INSTANCE.fio_main())
                    .filter(file -> isExcAndFolder(fileType, isNeedFolder, file) && file.exists()).take(getNum)
                    .doOnComplete(() -> listener.success(returnList))
                    .subscribe(file -> {
                        MagicFileEntity entity = new MagicFileEntity();
                        entity.name = file.getName();
                        entity.mimieType = getMIMEType(file);
                        entity.size = file.length();
                        entity.time = file.lastModified();
                        entity.path = file.getPath();
                        entity.isFile = file.isFile();
                        entity.companyId = getCompanyIdByFilePath(file.getPath());

                        returnList.add(entity);
                    }, error -> {
                    });
        } else {
            flowable.compose(RxSchedulersHelper.INSTANCE.fio_main())
                    .doOnComplete(() -> listener.success(returnList))
                    .subscribe(file -> {
                        if (isExcAndFolder(fileType, isNeedFolder, file) && file.exists()) {
                            MagicFileEntity entity = new MagicFileEntity();
                            entity.name = file.getName();
                            entity.mimieType = getMIMEType(file);
                            entity.size = file.length();
                            entity.time = file.lastModified();
                            entity.path = file.getPath();
                            entity.isFile = file.isFile();
                            entity.companyId = getCompanyIdByFilePath(file.getPath());

                            returnList.add(entity);
                        }
                    }, error -> listener.success(returnList));
        }
    }

    private static Long getCompanyIdByFilePath(String path) {
        Long companyId = null;
        if (path.contains("es")) {
            String[] paths = path.split("\\/");
            for (int i = 0; i < paths.length; i++) {
                if (paths[i].contains("es")) {
                    if (i + 1 < paths.length) {
                        companyId = Utils.isNumber(paths[i + 1]) ? Long.parseLong(paths[i + 1]) : null;
                        break;
                    }
                }
            }
        }
        return companyId;
    }

    /**
     * 获取文件列表
     *
     * @param path     路径
     * @param fileType {@link FileType}
     * @param listener
     */
    public static void getFileList(@NonNull String path, int fileType, boolean isNeedFolder, ExplorerListener listener) {
        if (TextUtils.equals(path, "/")) {
            path = CacheManager.getSaveFilePath();
        } else if (!path.startsWith(CacheManager.getSaveFilePath())) {
            path = CacheManager.getSaveFilePath() + path;
        }

        List<MagicFileEntity> returnList = new ArrayList<>();
        File rootFile = new File(path);
        if (!rootFile.isFile()) {
            boolean isThingsFile = path.startsWith(ES_PATH);
            File[] listFile = rootFile.listFiles();

            if (listFile != null)
                Observable.fromArray(listFile).compose(RxSchedulersHelper.INSTANCE.io_main())
                        .doOnComplete(() -> listener.success(returnList))
                        .subscribe(file -> {
                            if (isExcAndFolder(fileType, isNeedFolder, file) && file.exists()) {
                                MagicFileEntity entity = new MagicFileEntity();
                                entity.name = file.getName();
                                entity.mimieType = getMIMEType(file);
                                entity.size = file.length();
                                entity.time = file.lastModified();
                                entity.path = file.getPath();
                                entity.isFile = file.isFile();

                                if (isThingsFile) {
                                    //todo 需要查询数据库
                                }
                                returnList.add(entity);
                            }
                        }, error -> listener.success(returnList));
            else
                listener.success(returnList);
        }
    }

    /**
     * 获取所有的文件列表
     *
     * @param fileType
     * @param listener
     */
    public static void getAllFileList(int fileType, boolean isNeedFolder, int getNum, ExplorerListener listener) {
        String path = CacheManager.getSaveFilePath();
        getAllFileList(path, fileType, isNeedFolder, getNum, listener);
    }

    /**
     * 获得QQ文件列表
     *
     * @return
     */
    public static void getQQFileList(int fileType, boolean isNeedSearchChild, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(QQ_PIC_PATH, fileType, true, -1, listener);
        else
            getFileList(QQ_PIC_PATH, fileType, true, listener);
    }

    public static void getQQFileList(int fileType, boolean isNeedSearchChild, boolean isNeedFolder, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(QQ_PIC_PATH, fileType, isNeedFolder, -1, listener);
        else
            getFileList(QQ_PIC_PATH, fileType, isNeedFolder, listener);
    }

    /**
     * 获得微信文件列表
     *
     * @return
     */
    public static void getWxFileList(int fileType, boolean isNeedSearchChild, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(WX_PIC_PATH, fileType, true, -1, listener);
        else
            getFileList(WX_PIC_PATH, fileType, true, listener);
    }

    public static void getWxFileList(int fileType, boolean isNeedSearchChild, boolean isNeedFolder, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(WX_PIC_PATH, fileType, isNeedFolder, -1, listener);
        else
            getFileList(WX_PIC_PATH, fileType, isNeedFolder, listener);
    }

    /**
     * 获得 camera文件列表
     *
     * @param fileType
     * @param listener
     */
    public static void getSystemCameraFileList(int fileType, boolean isNeedSearchChild, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(SYS_CAMERA_PATH, fileType, true, -1, listener);
        else
            getFileList(SYS_CAMERA_PATH, fileType, true, listener);
    }

    public static void getSystemCameraFileList(int fileType, boolean isNeedSearchChild, boolean isNeedFolder, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(SYS_CAMERA_PATH, fileType, isNeedFolder, -1, listener);
        else
            getFileList(SYS_CAMERA_PATH, fileType, isNeedFolder, listener);
    }

    /**
     * 获取图像快照
     *
     * @param fileType
     * @param isNeedSearchChild
     * @param listener
     */
    public static void getScreenshotsFileList(int fileType, boolean isNeedSearchChild, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(SCREENSHOTS_PATH, fileType, true, -1, listener);
        else
            getFileList(SCREENSHOTS_PATH, fileType, true, listener);
    }

    public static void getScreenshotsFileList(int fileType, boolean isNeedSearchChild, boolean isNeedFolder, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(SCREENSHOTS_PATH, fileType, isNeedFolder, -1, listener);
        else
            getFileList(SCREENSHOTS_PATH, fileType, isNeedFolder, listener);
    }

    /**
     * 获取华为屏幕保护
     *
     * @param fileType
     * @param isNeedSearchChild
     * @param listener
     */
    public static void getHWScreenSaverFileList(int fileType, boolean isNeedSearchChild, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(HW_SCREEN_SAVER_PATH, fileType, true, -1, listener);
        else
            getFileList(HW_SCREEN_SAVER_PATH, fileType, true, listener);
    }

    public static void getHWScreenSaverFileList(int fileType, boolean isNeedSearchChild, boolean isNeedFolder, ExplorerListener listener) {
        if (isNeedSearchChild)
            getAllFileList(HW_SCREEN_SAVER_PATH, fileType, isNeedFolder, -1, listener);
        else
            getFileList(HW_SCREEN_SAVER_PATH, fileType, isNeedFolder, listener);
    }

    /**
     * 获取事事根目录文件列表
     *
     * @return
     */
    public static void getThingsFileList(int fileType, boolean isNeedSearchChild, ExplorerListener listener) {
        String path = ES_PATH;
        if (isNeedSearchChild)
            getAllFileList(path, fileType, true, -1, listener);
        else
            getFileList(path, fileType, true, listener);
    }

    public static void getThingsFileList(int fileType, boolean isNeedSearchChild, boolean isNeedFolder, ExplorerListener listener) {
        String path = ES_PATH;
        if (isNeedSearchChild)
            getAllFileList(path, fileType, isNeedFolder, -1, listener);
        else
            getFileList(path, fileType, isNeedFolder, listener);
    }

    /**
     * 获取相机拍照临时目录
     *
     * @param fileType
     * @param isNeedSearchChild
     * @param listener
     */
    public static void getPicTempFileList(int fileType, boolean isNeedSearchChild, ExplorerListener listener) {
        String path = PIC_EBPHOTO_PATH;
        if (isNeedSearchChild)
            getAllFileList(path, fileType, true, -1, listener);
        else
            getFileList(path, fileType, true, listener);
    }

    public static void getPicTempFileList(int fileType, boolean isNeedSearchChild, boolean isNeedFolder, ExplorerListener listener) {
        String path = PIC_EBPHOTO_PATH;
        if (isNeedSearchChild)
            getAllFileList(path, fileType, isNeedFolder, -1, listener);
        else
            getFileList(path, fileType, isNeedFolder, listener);
    }

    public interface ExplorerListener {
        void success(List<MagicFileEntity> returnList);
    }

    public interface ExplorerListener1 {
        void success(List<MagicPicEntity> parentList, List<MagicFileEntity> childList);
    }

    public interface FileCallback<T> {
        void callListener(T param);
    }

    public interface FileCallback1<T> extends FileCallback<T> {
        void callComplete();
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 获取ES里的文件信息
     *
     * @param callback
     */
    public static void getESInfo(FileCallback<CategoryInfo> callback) {
        CategoryInfo categoryInfo = new CategoryInfo();
        Uri searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
        String[] columns = new String[]{
                "COUNT(*)", "SUM(_size)"
        };
        Cursor cursor = Appli.Companion.getContext().getContentResolver().query(searchUrl, columns,
                (MediaStore.Files.FileColumns.DATA + " like '%") + ES_PATH + "%'", null, null);
        if (cursor == null) {
            callback.callListener(categoryInfo);
            return;
        }
        ;
        Flowable.just(cursor).flatMap(c -> {
            if (cursor.moveToNext()) {
                categoryInfo.count = cursor.getLong(0);
                categoryInfo.size = cursor.getLong(1);
            }
            c.close();
            return Flowable.just(categoryInfo);
        }).compose(RxSchedulersHelper.INSTANCE.fio_main()).subscribe(info -> callback.callListener(categoryInfo), error -> {
        });
    }

    /**
     * 获取es文件夹下的文件列表
     *
     * @param type
     * @param companyId
     * @param callback
     */
    public static void getEsMediaFileInfoListByFileType(int type, Long companyId, int start, int size, FileCallback<List<MagicFileEntity>> callback) {
        List<MagicFileEntity> returnList = new ArrayList<>();
        String path = CacheManager.getEsCompanyPath(getCacheManagerType(type), companyId);

        create((FlowableOnSubscribe<String>) emitter -> {
            File directoryFile = new File(path);
            if (directoryFile.isDirectory()) {
                int listSize = directoryFile.list().length;
                if (start < 0) {
                    for (String item : directoryFile.list()) {
                        emitter.onNext(directoryFile.getPath() + File.separator + item);
                    }
                } else {
                    int startPos = start > listSize - 1 ? listSize - 1 : start;
                    int endPos = start + size > listSize ? listSize : start + size;

                    for (int i = startPos; i < endPos; i++) {
                        String item = directoryFile.list()[i];
                        emitter.onNext(directoryFile.getPath() + File.separator + item);
                    }
                }
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .doOnComplete(() -> callback.callListener(returnList)).subscribe(value -> {
            File file = new File(value);
            if (file.exists()) {
                MagicFileEntity entity = new MagicFileEntity();
                entity.companyId = companyId;
                entity.name = file.getName();
                entity.mimieType = getMIMEType(file);
                entity.size = file.length();
                entity.time = file.lastModified();
                entity.path = file.getPath();
                entity.isFile = file.isFile();
                Thread.sleep(type * 100);
                returnList.add(entity);
            }
        }, error -> {
        });
    }

    /**
     * 获取 es 最近30天的文件倒叙排列，如果为搜索则不再倒叙 排列
     *
     * @param searchKey
     * @param callback
     */
    public static void getEsNear30DaysFileDesc(String searchKey, FileCallback1<List<FileInfo>> callback) {
        boolean isHasSearchKey = !TextUtils.isEmpty(searchKey);
        String rootPath = CacheManager.getCachePath();
//        List<FileInfo> returnList = new ArrayList<>();
        List<String> folderList = new ArrayList<>();
        Flowable.just(folderList)
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .flatMap(list -> {
                    getFolderList(rootPath, folderList);
                    return Flowable.fromIterable(folderList);
                })
                .flatMap(folder -> {
                    if (!folder.contains("nomedia") && !folder.contains("glide")
                            && !folder.contains("oss_record") && !folder.contains("rxCache") && !folder.startsWith(".")) {
                        return Flowable.just(new File(folder));
                    }
                    return Flowable.just(new File("/"));
                })
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(() -> {
//                    if (isHasSearchKey) {
//                        callback.callComplete();
//                    } else {
//                        Collections.sort(returnList, (first, second) -> first.modifyDate < second.modifyDate ? 1 : -1);
//                        callback.callListener(returnList);
//                    }
                    callback.callComplete();
                })
                .subscribe(folderFile -> {
                    if (!folderFile.getPath().equals("/") && folderFile.isDirectory()) {
                        long nowTime = System.currentTimeMillis();
                        long lestTime = nowTime - (30 * 24 * 60 * 60) * 1000L;
                        for (int i = 0; i < folderFile.list().length; i++) {
                            String filePath = folderFile.getPath() + File.separator + folderFile.list()[i];
                            File file = new File(filePath);

                            if (file.isFile() && file.exists() && file.lastModified() > lestTime && file.lastModified() < nowTime) {
                                if (isHasSearchKey) {
                                    if (!filePath.contains(searchKey)) {
                                        continue;
                                    }
                                    List<FileInfo> backList = new ArrayList<>();
                                    String exc = FileUtils.getExtensionName(file.getPath());
                                    if (!TextUtils.isEmpty(exc)) {
                                        int type = FileType.createFileType(exc);
                                        FileInfo info = new FileInfo();
                                        info.fileName = file.getName();
                                        info.path = file.getPath();
                                        info.fileSize = file.length();
                                        info.mimeType = getMIMEType(file);
                                        info.modifyDate = file.lastModified();
                                        info.position = type;
                                        if (!info.fileName.startsWith("."))
                                            backList.add(info);
                                    }
                                    callback.callListener(backList);
                                } else {
                                    List<FileInfo> backList = new ArrayList<>();
                                    String exc = FileUtils.getExtensionName(file.getPath());
                                    if (!TextUtils.isEmpty(exc)) {
                                        int type = FileType.createFileType(exc);
                                        FileInfo info = new FileInfo();
                                        info.fileName = file.getName();
                                        info.path = file.getPath();
                                        info.fileSize = file.length();
                                        info.mimeType = getMIMEType(file);
                                        info.modifyDate = file.lastModified();
                                        info.position = type;

                                        if (!info.fileName.startsWith("."))
                                            backList.add(info);
                                    }
                                    callback.callListener(backList);
                                }
                            }
                        }
                    }
                }, error -> {
                });
    }


    public static void getMediaFileInfoListByFileType(int type, Long companyId, int isEs, FileCallback<List<MagicFileEntity>> callback) {
        int fileType = getCacheManagerType(type);

        boolean isHasCompanyId = companyId != null && companyId > 0;
        String rootPath;
        if (isEs == 0) {
            rootPath = isHasCompanyId ? CacheManager.getEsCompanyPath(fileType, companyId) : CacheManager.getCachePath(Appli.Companion.getContext(), CacheManager.ES);
        } else {
            rootPath = CacheManager.getCachePath(Appli.Companion.getContext(), CacheManager.IM);
        }

        List<String> folderList = new ArrayList<>();
        List<MagicFileEntity> returnList = new ArrayList<>();

        Flowable.just(folderList)
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .flatMap(list -> {
                    folderList.add(rootPath);
                    getFolderList(rootPath, folderList);
                    return Flowable.fromIterable(folderList);
                })
                .flatMap(folder -> Flowable.just(new File(folder)))
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(() -> {
                    callback.callListener(returnList);
                })
                .subscribe(folderFile -> {
                    if (folderFile.isDirectory()) {
                        for (int i = 0; i < folderFile.list().length; i++) {
                            String filePath = folderFile.getPath() + File.separator + folderFile.list()[i];
                            if (!filePath.contains("nomedia") && !filePath.startsWith(".")) {
                                File file = new File(filePath);
                                if (file.isFile() && file.exists()) {
                                    String exc = FileUtils.getExtensionName(file.getPath());
                                    int t = FileType.createFileType(exc);
                                    if (t == type) {
                                        MagicFileEntity entity = new MagicFileEntity();
                                        entity.companyId = companyId;
                                        entity.name = file.getName();
                                        entity.mimieType = getMIMEType(file);
                                        entity.size = file.length();
                                        entity.time = file.lastModified();
                                        entity.path = file.getPath();
                                        entity.isFile = file.isFile();

                                        returnList.add(entity);
                                    }
                                }
                            }
                        }
                    }
                }, error -> {
                });
    }


    public static void getMediaFileInfoListByFileType(int type, String path, FileCallback<List<MagicFileEntity>> callback) {
        List<MagicFileEntity> returnList = new ArrayList<>();
        Uri searchUrl = null;
        String[] columns = new String[]{
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_MODIFIED
        };

        StringBuilder selection = new StringBuilder();
        String selectLikeStr = "";
        switch (type) {
            case FileType.PIC:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PIC);
                break;
            case FileType.AUDIO:
                searchUrl = MediaStore.Audio.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.VIDEO:
                searchUrl = MediaStore.Video.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.TXT:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PIC);
                break;
            case FileType.EXCEL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.EXCEL);
                break;
            case FileType.PPT:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PPT);
                break;
            case FileType.WORD:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.WORD);
                break;
            case FileType.PDF:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PDF);
                break;
            case FileType.OTHER:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addNotLike(MediaStore.Files.FileColumns.DATA, selection, Extension.TXT);
                selection.append(" AND ");
                Extension.addNotLike(MediaStore.Files.FileColumns.DATA, selection, Extension.EXCEL);
                selection.append(" AND ");
                Extension.addNotLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PPT);
                selection.append(" AND ");
                Extension.addNotLike(MediaStore.Files.FileColumns.DATA, selection, Extension.WORD);
                selection.append(" AND ");
                Extension.addNotLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PDF);
                break;
            case FileType.ES_ALL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                break;
        }
        if (!TextUtils.isEmpty(path)) {
            if (selection.length() > 0) {
                selection.insert(0, "(");
                selection.insert(selection.length(), ")");
                selection.append(" AND ");
            }
            selection.append(MediaStore.Files.FileColumns.DATA);
            selection.append(" LIKE ").append(" '%").append(path).append("%' ");
        }

        if (searchUrl == null) {
            callback.callListener(returnList);
            return;
        }
        String selectionStr = TextUtils.isEmpty(selection.toString()) ? null : selection.toString();
        Cursor cursor = Appli.Companion.getContext().getContentResolver().query(searchUrl, columns, selectionStr, null, null);
        if (cursor == null) {
            callback.callListener(returnList);
            return;
        }


        create((FlowableOnSubscribe<Cursor>) emitter -> {
            while (cursor.moveToNext()) {
                emitter.onNext(cursor);
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .doOnComplete(() -> {
                    cursor.close();
                    callback.callListener(returnList);
                })
                .subscribe(c -> {
                    MagicFileEntity entity = new MagicFileEntity();
                    entity.name = c.getString(0);
                    entity.mimieType = c.getString(1);
                    entity.size = c.getLong(2);
                    entity.time = c.getLong(3);
                    entity.path = c.getString(4);

                    Log.e("tag", entity.path);

                    returnList.add(entity);
                }, error -> {
                });
    }

    /**
     * 获取文件数量和大小
     *
     * @param type
     * @return
     */
    public static void getInfoByFileType(int type, Long companyId, int isEs, int position, FileCallback<CategoryInfo> callback) {
        List<String> folderList = new ArrayList<>();

        int fileType = getCacheManagerType(type);
        boolean isHasCompanyId = companyId != null && companyId > 0;
        String rootPath;
        if (isEs == 0) {
            rootPath = isHasCompanyId ? CacheManager.getEsCompanyPath(fileType, companyId) : CacheManager.getCachePath(Appli.Companion.getContext(), CacheManager.ES);
        } else {
            rootPath = CacheManager.getCachePath(Appli.Companion.getContext(), CacheManager.IM);
        }
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.extra = position;

        Flowable.just(folderList)
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .flatMap(list -> {
                    getFolderList(rootPath, folderList);
                    folderList.add(rootPath);
                    return Flowable.fromIterable(folderList);
                })
                .flatMap(folder -> Flowable.just(new File(folder)))
                .compose(RxSchedulersHelper.INSTANCE.fio())
                .doOnComplete(() -> {
                    callback.callListener(categoryInfo);
                })
                .subscribe(folderFile -> {
                    if (folderFile.isDirectory()) {
                        for (int i = 0; i < folderFile.listFiles().length; i++) {
                            File file = folderFile.listFiles()[i];
                            if (file.exists() && file.isFile() && !file.getPath().startsWith(".")) {
                                String exc = FileUtils.getExtensionName(file.getPath());
                                int t = FileType.createFileType(exc);
                                if (t == type) {
                                    categoryInfo.count++;
                                    categoryInfo.size += file.length();
                                }
                            }
                        }
                    }
                }, error -> {
                });
    }


    /**
     * 获取文件数量
     *
     * @param type     文件类型
     * @param position 哪个item 的position 直接返回
     * @param path     用于路径比较
     * @param callback
     */
    public static void getCountByFileType(int type, int position, String path, FileCallback<CategoryInfo> callback) {
        CategoryInfo categoryInfo = new CategoryInfo();

        Uri searchUrl = null;
        String[] columns = new String[]{
                "COUNT(*)", "SUM(_size)"
        };
        StringBuilder selection = new StringBuilder();
        switch (type) {
            case FileType.PIC:
                searchUrl = MediaStore.Images.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.AUDIO:
                searchUrl = MediaStore.Audio.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.VIDEO:
                searchUrl = MediaStore.Video.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.TXT:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.TXT);
                break;
            case FileType.EXCEL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.EXCEL);
                break;
            case FileType.PPT:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PPT);
                break;
            case FileType.WORD:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.WORD);
                break;
            case FileType.PDF:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PDF);
                break;
            case FileType.OTHER:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.ZIP);
                break;
            case FileType.ES_ALL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                break;
        }
        if (!TextUtils.isEmpty(path)) {
            if (selection.length() > 0) {
                selection.insert(0, "(");
                selection.insert(selection.length(), ")");
                selection.append(" AND ");
            }
            selection.append(MediaStore.Files.FileColumns.DATA);
            selection.append(" LIKE ").append(" '%").append(path).append("%' ");
        }

        if (searchUrl == null) {
            callback.callListener(categoryInfo);
            return;
        }
        String selectionStr = TextUtils.isEmpty(selection.toString()) ? null : selection.toString();
        Cursor cursor = Appli.Companion.getContext().getContentResolver().query(searchUrl, columns, selectionStr, null, null);
        if (cursor == null) {
            callback.callListener(categoryInfo);
            return;
        }

        Flowable.just(cursor).flatMap(c -> {
            if (cursor.moveToNext()) {
                categoryInfo.count = cursor.getLong(0);
                categoryInfo.size = cursor.getLong(1);
                categoryInfo.extra = position;
            }
            c.close();
            return Flowable.just(categoryInfo);
        }).compose(RxSchedulersHelper.INSTANCE.fio()).subscribe(info -> {
            callback.callListener(categoryInfo);
        }, error -> {
        });
    }

    /**
     * 删除信息
     *
     * @param type
     * @param path
     */
    public static void deleteByFileType(int type, String path) {
        Uri searchUrl = null;
        switch (type) {
            case FileType.PIC:
                searchUrl = MediaStore.Images.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.AUDIO:
                searchUrl = MediaStore.Audio.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.VIDEO:
                searchUrl = MediaStore.Video.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.TXT:
            case FileType.EXCEL:
            case FileType.PPT:
            case FileType.WORD:
            case FileType.PDF:
            case FileType.OTHER:
            case FileType.ES_ALL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                break;
        }
        if (searchUrl == null) {
            return;
        }

        String whereStr = MediaStore.Files.FileColumns.DATA + "='" + path + "'";
        Appli.Companion.getContext().getContentResolver().delete(searchUrl, whereStr, null);
    }

    /**
     * 更新数据库
     *
     * @param type
     * @param path
     * @param fileName
     */
    public static void updateInfoByFileType(int type, String oldPath, String path, String fileName) {
        Uri searchUrl = null;
        switch (type) {
            case FileType.PIC:
                searchUrl = MediaStore.Images.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.AUDIO:
                searchUrl = MediaStore.Audio.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.VIDEO:
                searchUrl = MediaStore.Video.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.TXT:
            case FileType.EXCEL:
            case FileType.PPT:
            case FileType.WORD:
            case FileType.PDF:
            case FileType.OTHER:
            case FileType.ES_ALL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                break;
        }
        if (searchUrl == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Files.FileColumns.DATA, path);
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);

        String whereStr = MediaStore.Files.FileColumns.DATA + "='" + oldPath + "'";
        Appli.Companion.getContext().getContentResolver().update(searchUrl, contentValues, whereStr, null);
    }

    /**
     * 获取文件信息
     *
     * @param type     文件类型
     * @param position 哪个item 的position 直接返回
     * @param path     用于路径比较
     * @param callback
     */
    public static void getInfoByFileType(int type, int position, String path, FileCallback<List<FileInfo>> callback) {
        getInfoByFileType(type, position, path, null, callback);
    }

    public static void getInfoByFileType(int type, int position, String path, String searchKey, FileCallback<List<FileInfo>> callback) {
        List<FileInfo> list = new ArrayList<>();

        Uri searchUrl = null;
        StringBuilder selection = new StringBuilder();
        String[] columns = new String[]{"_data", "_display_name", "_size", "mime_type", "duration", "date_modified"};

        switch (type) {
            case FileType.PIC:
                searchUrl = MediaStore.Images.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.AUDIO:
                searchUrl = MediaStore.Audio.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.VIDEO:
                searchUrl = MediaStore.Video.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.TXT:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.TXT);
                break;
            case FileType.EXCEL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.EXCEL);
                break;
            case FileType.PPT:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PPT);
                break;
            case FileType.WORD:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.WORD);
                break;
            case FileType.PDF:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PDF);
                break;
            case FileType.OTHER:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.ZIP);
                break;
            case FileType.ES_ALL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                break;
        }
        if (!TextUtils.isEmpty(path)) {
            if (selection.length() > 0) {
                selection.insert(0, "(");
                selection.insert(selection.length(), ")");
                selection.append(" AND ");
            }
            selection.append(MediaStore.Files.FileColumns.DATA);
            selection.append(" LIKE ").append(" '%").append(path).append("%' ");

            if (!TextUtils.isEmpty(searchKey)) {
                selection.append(" AND ");
                selection.append(MediaStore.Files.FileColumns.DATA);
                selection.append(" LIKE ").append(" '%").append(searchKey).append("%' ");
            }
        }

        if (searchUrl == null) {
            callback.callListener(list);
            return;
        }
        String selectionStr = TextUtils.isEmpty(selection.toString()) ? null : selection.toString();
        Cursor cursor = Appli.Companion.getContext().getContentResolver().query(searchUrl, columns, selectionStr, null, null);
        if (cursor == null) {
            callback.callListener(list);
            return;
        }

        create((FlowableOnSubscribe<FileInfo>) e -> {
            while (cursor.moveToNext()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.path = cursor.getString(0);
                File file = new File(fileInfo.path);
                if (!file.exists()
                        || file.length() < 8 * 1024 && (type == FileType.EXCEL || type == FileType.WORD || type == FileType.PPT))
                    continue;
                fileInfo.fileName = cursor.getString(1);
                fileInfo.fileSize = file.length();
                fileInfo.mimeType = cursor.getString(3);
                fileInfo.duration = cursor.getLong(4);
                fileInfo.modifyDate = file.lastModified();

                fileInfo.position = position;
                e.onNext(fileInfo);
            }
            cursor.close();
            e.onComplete();
        }, BackpressureStrategy.BUFFER).doOnComplete(() -> callback.callListener(list)).subscribe(list::add, error -> {
        });
    }

    /**
     * 获取文件信息
     *
     * @param type      文件类型
     * @param position  哪个item 的position 直接返回
     * @param path      用于路径比较
     * @param searchKey 搜索关键字
     * @param callback  回掉
     * @param isOneBack 数据是否一次返回
     */
    public static void getInfoByFileType(int type, int position, String path, String searchKey, FileCallback1<List<FileInfo>> callback, boolean isOneBack) {
        List<FileInfo> list = new ArrayList<>();

        Uri searchUrl = null;
        StringBuilder selection = new StringBuilder();
        String[] columns = new String[]{"_data", "_display_name", "_size", "mime_type", "duration", "date_modified"};

        switch (type) {
            case FileType.PIC:
                searchUrl = MediaStore.Images.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.AUDIO:
                searchUrl = MediaStore.Audio.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.VIDEO:
                searchUrl = MediaStore.Video.Media.getContentUri(VOLUME_NAME);
                break;
            case FileType.TXT:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.TXT);
                break;
            case FileType.EXCEL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.EXCEL);
                break;
            case FileType.PPT:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PPT);
                break;
            case FileType.WORD:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.WORD);
                break;
            case FileType.PDF:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.PDF);
                break;
            case FileType.OTHER:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                Extension.addLike(MediaStore.Files.FileColumns.DATA, selection, Extension.ZIP);
                break;
            case FileType.ES_ALL:
                searchUrl = MediaStore.Files.getContentUri(VOLUME_NAME);
                break;
        }
        if (!TextUtils.isEmpty(path)) {
            if (selection.length() > 0) {
                selection.insert(0, "(");
                selection.insert(selection.length(), ")");
                selection.append(" AND ");
            }
            selection.append(MediaStore.Files.FileColumns.DATA);
            selection.append(" LIKE ").append(" '%").append(path).append("%' ");

            if (!TextUtils.isEmpty(searchKey)) {
                selection.append(" AND ");
                selection.append(MediaStore.Files.FileColumns.DATA);
                selection.append(" LIKE ").append(" '%").append(searchKey).append("%' ");
            }
        }

        if (searchUrl == null) {
            callback.callListener(list);
            return;
        }
        String selectionStr = TextUtils.isEmpty(selection.toString()) ? null : selection.toString();
        Cursor cursor = Appli.Companion.getContext().getContentResolver().query(searchUrl, columns, selectionStr, null, null);
        if (cursor == null) {
            callback.callListener(list);
            return;
        }

        Flowable<FileInfo> flowable = create(e -> {
            while (cursor.moveToNext()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.path = cursor.getString(0);
                fileInfo.fileName = cursor.getString(1);
                fileInfo.fileSize = cursor.getLong(2);
                fileInfo.mimeType = cursor.getString(3);
                fileInfo.duration = cursor.getLong(4);
                fileInfo.modifyDate = cursor.getLong(5) * 1000;

                fileInfo.position = position;
                e.onNext(fileInfo);
            }
            cursor.close();
            e.onComplete();
        }, BackpressureStrategy.BUFFER);

        if (isOneBack) {
            flowable.toList().subscribe(callback::callListener, error -> {
            });
        } else {
            flowable.doOnComplete(callback::callComplete).subscribe(fileInfo -> {
                List<FileInfo> infoList = new ArrayList<>();
                infoList.add(fileInfo);
                callback.callListener(infoList);
            }, error -> {
            });
        }
    }

    /**
     * 删除文件
     *
     * @param filePathList 文件列表
     * @param callback
     */
    public static void deleteFiles(List<String> filePathList, Callback<String> callback) {
        Flowable.fromIterable(filePathList).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .doOnComplete(() -> callback.event("succ"))
                .subscribe(filePath -> {
                    File file = new File(filePath);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }, error -> {
                });
    }

    public static void deleteFiles(int fileType, List<String> filePathList, Callback<String> callback) {
        Flowable.fromIterable(filePathList).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .doOnComplete(() -> callback.event("succ"))
                .subscribe(filePath -> {
                    File file = new File(filePath);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                    MagicExplorer.deleteByFileType(fileType, filePath);
                }, error -> {
                });
    }

    public static SDCardInfo getSDCardInfo() {
        SDCardInfo info = new SDCardInfo();
        String sDcString = android.os.Environment.getExternalStorageState();

        if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File pathFile = android.os.Environment.getExternalStorageDirectory();

            try {
                android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());

                // 获取SDCard上BLOCK总数
                long nTotalBlocks = statfs.getBlockCount();

                // 获取SDCard上每个block的SIZE
                long nBlocSize = statfs.getBlockSize();

                // 获取可供程序使用的Block的数量
                long nAvailaBlock = statfs.getAvailableBlocks();

                // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
                long nFreeBlock = statfs.getFreeBlocks();

                // 计算SDCard 总容量大小MB
                info.total = nTotalBlocks * nBlocSize;

                // 计算 SDCard 剩余大小MB
                info.free = nAvailaBlock * nBlocSize;

            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return info;
    }

    // storage, G M K B
    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format(Locale.CHINA, "%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format(Locale.CHINA, "%d B", size);
    }

    public static int getCacheManagerType(int type) {
        int fileType;
        switch (type) {
            case FileType.PIC:
                fileType = CacheManager.PIC;
                break;
            case FileType.AUDIO:
                fileType = CacheManager.VOICE;
                break;
            case FileType.VIDEO:
                fileType = CacheManager.VIDEO;
                break;
            case FileType.TXT:
                fileType = CacheManager.TXT;
                break;
            case FileType.EXCEL:
                fileType = CacheManager.EXCEL;
                break;
            case FileType.PPT:
                fileType = CacheManager.PPT;
                break;
            case FileType.WORD:
                fileType = CacheManager.WORD;
                break;
            case FileType.PDF:
                fileType = CacheManager.PDF;
                break;
            default:
                fileType = CacheManager.OTHER;
                break;
        }
        return fileType;
    }

    public static void getFolderList(String path, List<String> resultList) {
        File[] files = new File(path).listFiles();
        File f;
        if (files == null) {
            return;
        }
        for (File file : files) {
            f = file;
            if (!f.canRead()) {
                return;
            }
            if (f.isDirectory() && !f.getPath().contains("nomedia") && !f.getPath().contains("-1")) {
                resultList.add(f.getPath());
                getFolderList(f.getPath(), resultList);
            }
        }
        return;
    }

    public static void getFileNum(String folderPath, Callback<Long> callback) {
        Stack<String> stack = new Stack<>();
        stack.push(folderPath);
        long fileNum = 0;
        while (!stack.isEmpty()) {
            File path = new File(stack.pop());
            File[] files = path.listFiles();
            if (null == files)
                continue;
            for (File f : files) {
                // 递归监听目录
                if (f.isDirectory() && !f.getPath().contains("nomedia") && !f.getPath().contains("-1")) {
                    stack.push(f.getAbsolutePath());
                } else {
                    fileNum++;
                }
            }
        }
        callback.event(fileNum);
    }


    private static void getFolderList(String path, FileCallback<String> callback) {
        File[] files = new File(path).listFiles();
        File f;
        if (files == null) {
            return;
        }
        for (File file : files) {
            f = file;
            if (!f.canRead()) {
                return;
            }
            if (f.isDirectory() && !f.getPath().contains("nomedia") && !f.getPath().contains("-1")) {
                callback.callListener(f.getPath());
                getFolderList(f.getPath(), callback);
            }
        }
    }

    private static boolean isNeedPathName(String pathName) {
        return !pathName.contains("nomedia") && !pathName.startsWith(".") && !pathName.equals("-1");
    }

    /**
     * 如果文件是文件夹或者fileType类型无法判断，则直接返回true
     *
     * @param fileType
     * @param file
     * @return
     */
    private static boolean isExcAndFolder(int fileType, boolean isNeedFolder, File file) {
        if (file.isDirectory()) {
            if (isNeedFolder)
                return true;
            else return false;
        }
        String[] excs = FileType.getExc(fileType);
        if (excs.length == 0) {
            return true;
        }
        String exc = FileUtils.getExtensionName(file.getPath());
        for (int i = 0; i < excs.length; i++) {
            if (TextUtils.equals(exc, excs[i])) {
                return true;
            }
        }
        return false;
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".result", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };
}
