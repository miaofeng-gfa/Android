package cn.com.gfa.android.commonutil.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

public class ClipboardUtil {
    //复制剪贴工具类

    private static ClipboardManager mClipboardManager;
    private static ClipboardManager mNewCliboardManager;

    private static boolean isNew() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private static void instance(Context context) {
        if (isNew()) {
            if (mNewCliboardManager == null) {
                mNewCliboardManager = (ClipboardManager) context.getSystemService(
                        Context.CLIPBOARD_SERVICE);
            }
        } else {
            if (mClipboardManager == null) {
                mClipboardManager = (ClipboardManager) context.getSystemService(
                        Context.CLIPBOARD_SERVICE);
            }
        }
    }

    /**
     * 为剪切板设置内容
     */
    public static void setText(Context context, CharSequence text) {
        if (isNew()) {
            instance(context);
            ClipData clip = ClipData.newPlainText("simple text", text);
            mNewCliboardManager.setPrimaryClip(clip);
        } else {
            instance(context);
            mClipboardManager.setText(text);
        }
    }


    /**
     * 获取剪切板的内容
     */
    public static CharSequence getText(Context context) {
        StringBuilder sb = new StringBuilder();
        if (isNew()) {
            instance(context);
            if (!mNewCliboardManager.hasPrimaryClip()) {
                return sb.toString();
            } else {
                ClipData clipData = (mNewCliboardManager).getPrimaryClip();
                int count = clipData.getItemCount();
                for (int i = 0; i < count; ++i) {
                    ClipData.Item item = clipData.getItemAt(i);
                    CharSequence str = item.coerceToText(context);
                    sb.append(str);
                }
            }
        } else {
            instance(context);
            sb.append(mClipboardManager.getText());
        }
        return sb.toString();
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    public static void copyText(final CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager)ApplicationUtil.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    public static CharSequence getText() {
        ClipboardManager clipboard = (ClipboardManager)ApplicationUtil.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(ApplicationUtil.getApp());
        }
        return null;
    }

    /**
     * 复制uri到剪贴板
     *
     * @param uri uri
     */
    public static void copyUri(final Uri uri) {
        ClipboardManager clipboard = (ClipboardManager)ApplicationUtil.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newUri(ApplicationUtil.getApp().getContentResolver(), "uri", uri));
    }

    /**
     * 获取剪贴板的uri
     *
     * @return 剪贴板的uri
     */
    public static Uri getUri() {
        ClipboardManager clipboard = (ClipboardManager)ApplicationUtil.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getUri();
        }
        return null;
    }

    /**
     * 复制意图到剪贴板
     *
     * @param intent 意图
     */
    public static void copyIntent(final Intent intent) {
        ClipboardManager clipboard = (ClipboardManager)ApplicationUtil.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newIntent("intent", intent));
    }

    /**
     * 获取剪贴板的意图
     *
     * @return 剪贴板的意图
     */
    public static Intent getIntent() {
        ClipboardManager clipboard = (ClipboardManager)ApplicationUtil.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getIntent();
        }
        return null;
    }
}
