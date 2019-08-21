package ru.prsolution.winstrike.presentation.utils;
/*
 * Created by oleg on 31.01.2018.
 */

import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.PhoneNumberUnderscoreSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;


public class TextFormat {
    public static void formatText(EditText phoneEditText, String mask) {
        Slot[] slots = new PhoneNumberUnderscoreSlotsParser().parseSlots(mask);
        MaskFormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(slots));
        formatWatcher.installOn(phoneEditText);
    }

    public static String formatPhone(String phone) {
        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");
        return "+7" + phone;
    }

    public static String simplePhoneFormat(String input) {
        String output = input.substring(0, 2) + " (" + input.substring(2, 5) + ")" + "-" + input.substring(5, 8) + "-" + input.substring(8, 10) + "-" + input.substring(10, 12);
        return output;
    }


    public static void setTextColor(TextView textView, String firstString, String secondString, String firstStringColor, String secondStringColor) {
        String text = "<font color=" + firstStringColor + ">" + firstString + "</font> <font color=" + secondStringColor + ">" + secondString + "</font>";
        textView.setText(Html.fromHtml(text));
    }

    public static void setTextFoot1Color(TextView textView, String firstString,  String firstStringColor) {
        String text = "<font color=" + firstStringColor + ">" + firstString + "</font>" ;
        textView.setText(Html.fromHtml(text));
    }

    public static void setTextFoot2Color(TextView textView, String secondString,  String secondStringColor) {
        String text = "<font color=" + secondStringColor + ">" + "<u>" + secondString +"</u>" + "</font>";
        textView.setText(Html.fromHtml(text));
    }


}
