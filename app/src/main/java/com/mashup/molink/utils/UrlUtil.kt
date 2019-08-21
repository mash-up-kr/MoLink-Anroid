package com.mashup.molink.utils

import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.ArrayList
import java.util.regex.Pattern

class UrlUtil {
    // 텍스트에서 대표 URL을 추출하는 메소드
    fun extractUrlFromText(text: String): String? {
        val urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
        val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val urlMatcher = pattern.matcher(text)
        val urls = ArrayList<String>()
        while (urlMatcher.find()) {
            urls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)))
        }
        var resultUrl: String? = null
        for (url in urls) {
            if (Validator().isUrl(url)) {
                resultUrl = url
                break
            }
        }
        return resultUrl
    }

    inner class Validator{

            fun isUrl(text: String): Boolean {
                val p = Pattern.compile("^(?:https?:\\/\\/)?(?:www\\.)?[a-zA-Z0-9./]+$")
                val m = p.matcher(text)
                if (m.matches())
                    return true
                var u: URL? = null
                try {
                    u = URL(text)
                } catch (e: MalformedURLException) {
                    return false
                }

                try {
                    u.toURI()
                } catch (e: URISyntaxException) {
                    return false
                }

                return true
            }

        }
}


