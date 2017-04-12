package com.raul.rsd.android.smanager.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import com.raul.rsd.android.smanager.R;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public abstract class UIUtils {

    public static CharSequence getCustomDurationString(Context context, int totalMinutes){
        int hours = (int) Math.floor(totalMinutes/60.0);
        int minutes = totalMinutes%60;

        Resources resources = context.getResources();
        String[] items = new String[hours > 0 ? 4 : 2];
        if(hours > 0){
            items[0] = String.valueOf(hours);
            items[1] = context.getString(R.string.hour_unit);
            items[2] = String.valueOf(minutes);
            items[3] = context.getString(R.string.minute_unit);
        } else {
            items[0] = String.valueOf(minutes);
            items[1] = context.getString(R.string.minute_unit);
        }

        CharSequence finalText = "";
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            int textSize, textColor, itemLength = item.length();
            if( i==0 || i==2 ){
                textSize = resources.getDimensionPixelSize(R.dimen.hour_size);
                textColor = Color.BLACK;
            } else {
                textSize = resources.getDimensionPixelSize(R.dimen.minute_size);
                textColor = Color.GRAY;
            }

            SpannableString span = new SpannableString(item);
            span.setSpan(new AbsoluteSizeSpan(textSize), 0, itemLength, SPAN_INCLUSIVE_INCLUSIVE);
            span.setSpan(new ForegroundColorSpan(textColor), 0, itemLength, 0);

            finalText = TextUtils.concat(finalText, span);
        }
        return finalText;
    }
}
