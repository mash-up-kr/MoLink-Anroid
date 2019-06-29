package com.mashup.molink.share;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//url 이 유효한지 확인
public class Validator {

    public static boolean isUrl(String text) {
        Pattern p = Pattern.compile("^(?:https?:\\/\\/)?(?:www\\.)?[a-zA-Z0-9./]+$");
        Matcher m = p.matcher(text);
        if (m.matches())
            return true;
        URL u = null;
        try {
            u = new URL(text);
        } catch (MalformedURLException e) {
            return false;
        } try {
            u.toURI();
        }
        catch (URISyntaxException e) {
            return false;
        }
        return true;
    }

}
