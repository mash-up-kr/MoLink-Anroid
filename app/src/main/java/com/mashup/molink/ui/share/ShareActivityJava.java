package com.mashup.molink.ui.share;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mashup.molink.R;
import com.mashup.molink.utils.Dlog;
import com.mashup.molink.utils.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ShareActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_share);

        TextView tvSharedUrl = findViewById(R.id.tvSharedUrl); // url
        EditText etSharedTitle = findViewById(R.id.etSharedTitle); //title

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        String sharedText = null;
        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if ("text/plain".equals(type)) {

                sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);    // 가져온 인텐트의 텍스트 정보

                String url = new UrlUtil().extractUrlFromText(sharedText);
                tvSharedUrl.setText(url);

                Dlog.INSTANCE.d(url);

                //나중에 Handler로 main thread에서 돌리는 것 해보기
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Document doc = null;
                        try {
                            doc = Jsoup.connect(url).get();

                            Dlog.INSTANCE.d(doc.toString());

                            String title = doc.select("meta[property=og:title]").first().attr("content");
                            String description = doc.select("meta[property=og:description]").get(0).attr("content");
                            String imageUrl = doc.select("meta[property=og:image]").get(0).attr("content");

                            Dlog.INSTANCE.e("title : " + title);
                            Dlog.INSTANCE.e("description : " + description);
                            Dlog.INSTANCE.e("imageUrl : " + imageUrl);

                            ShareActivityJava.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    etSharedTitle.setText(title);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
            }
        }

        tvSharedUrl.setMovementMethod(new ScrollingMovementMethod());

    };



}



