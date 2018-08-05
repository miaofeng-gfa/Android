package cn.com.gfa.android.commonutil.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

public class TypefaceUtil {

    public static void setTypefaceWithColor(Context context,TextView tv, int textId,
                                            String colorStr) {
        try {
            int color = Color.parseColor(colorStr);
            tv.setTextColor(color);
        } catch (Exception e) {
        }
        setTypeface(context,tv, textId);
    }

    public static void setTypefaceWithColor(Context context,TextView tv, int textId, int colorId) {
        tv.setTextColor(context.getResources()
                .getColor(colorId));
        setTypeface(context,tv, textId);
    }

    public static void setTypefaceWithColor(Context context,TextView tv, int colorId) {
        tv.setTextColor(context.getResources()
                .getColor(colorId));
        setTypeface(context,tv);
    }

    public static void setTypefaceWithColor(Context context,TextView tv, String colorStr) {
        try {
            int color = Color.parseColor(colorStr);
            tv.setTextColor(color);
        } catch (Exception e) {
        }

        setTypeface(context,tv);
    }

    public static void setTypeface(Context context,TextView tv, int textId) {
        setTypeface(context,tv, context.getString(textId));
    }

    public static void setTypeface(Context context,TextView tv, String text) {
        if (StringUtil.isEmpty(text))
            return;
        tv.setText(text);
        setTypeface(context,tv);
    }

    public static void setTypeFaceWithText(Context context,TextView tv, int faRes, String text) {
        String lastText = context.getResources().getString(faRes) + " " + text;
        setTypeface(context,tv, lastText);
    }

    public static void setTypeface(Context context,TextView tv) {
        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "fontawesome-webfont.ttf");
        tv.setTypeface(font);
    }

}
