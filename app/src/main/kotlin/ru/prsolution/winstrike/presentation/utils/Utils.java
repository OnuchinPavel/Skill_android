package ru.prsolution.winstrike.presentation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.prsolution.winstrike.presentation.utils.date.DateTransform;

public class Utils {

    public static String getFormattedDate(String date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00");
        Date date1 = null;
        try {
            date1 = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateformate = new SimpleDateFormat("dd MMM yyyy", new Locale("RU"));
        String currentDateStr = dateformate.format(date1);
        return currentDateStr;
    }

    public static String formatTime(String start_at) {
        String[] start = start_at.split("T");
        String[] startTmp = start[1].split("\\+");
        String startTime = startTmp[0];
        String[] startTm = startTime.split(":");
        String hours = startTm[0];
        String min = startTm[1];
        String stTime = hours + ":" + min;
        return stTime;
    }


    public static void setBtnEnable(View v, Boolean isEnable) {
        if (isEnable) {
            v.setAlpha(1f);
            v.setClickable(true);
        } else {
            v.setAlpha(.5f);
            v.setClickable(false);
        }

    }


    public static boolean validateDate(String stDate, String edDate) {
        if (stDate.isEmpty() || edDate.isEmpty()) {
            return false;
        }
        Date current = new Date();
        Date startDate = DateTransform.Companion.getDateInUTC(stDate);
        Date endDate = DateTransform.Companion.getDateInUTC(edDate);
        Boolean isDateValid = startDate.before(endDate) && (startDate.after(current) || startDate.equals(current));
        return isDateValid;
    }

    public static Bitmap decodeUri(Context context, Uri uri,
                                   final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, Context context){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static String parseNumber(String seatName) {
        String pattern="(\\D*)(\\d+)(\\D*)";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(seatName);
        if (m.find()) {
            System.out.println(m.group(2));
            return m.group(2);
        }
        return "";
    }

    public static void toast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

}
