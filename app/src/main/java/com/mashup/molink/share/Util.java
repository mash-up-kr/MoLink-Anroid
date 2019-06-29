package com.mashup.molink.share;

import android.media.Image;
import android.util.Log;
import kotlin.Metadata;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    // 텍스트에서 대표 URL을 추출하는 메소드
    public static String extractUrlFromText(String text) {
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text); ArrayList<String> urls = new ArrayList<>();
        while (urlMatcher.find()) {
            urls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }
        String resultUrl = null;
        for (String url : urls) {
            if (Validator.isUrl(url)) {
                resultUrl = url;
                break;
            }
        }
        return resultUrl;
    }

}
