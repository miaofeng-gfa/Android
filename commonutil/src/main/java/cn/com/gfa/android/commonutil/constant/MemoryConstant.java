package cn.com.gfa.android.commonutil.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MemoryConstant {

    /**
     * Byte 与 Byte 的倍数
     */
    public static final int BYTE = 1;
    /**
     * KB 与 Byte 的倍数
     */
    public static final int KB   = 1024;
    /**
     * MB 与 Byte 的倍数
     */
    public static final int MB   = 1048576;
    /**
     * GB 与 Byte 的倍数
     */
    public static final int GB   = 1073741824;


    public static final int TB   = 1073741824*1024;

    @IntDef({BYTE, KB, MB, GB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
