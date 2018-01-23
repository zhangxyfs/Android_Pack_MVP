package com.z7dream.apm.base.utils.explorer.collator;


import com.z7dream.apm.base.utils.explorer.FileInfo;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by Z7Dream on 2017/3/31 14:51.
 * Email:zhangxyfs@126.com
 */

public interface OrderingConstants {
    Collator collator = Collator.getInstance(Locale.CHINA);

    /**
     * Model为要排序的对象model，如Person，等等类型的自定义model。
     */
    Ordering<FileInfo> Model_NAME_ORDERING = new Ordering<FileInfo>() {
        @Override
        public int compare(FileInfo left, FileInfo right) {
            if (left == null || left.fileName == null) {
                return -1;
            }
            if (right == null || right.fileName == null) {
                return 1;
            }
            return collator.compare(left.fileName, right.fileName);
        }
    };
}
