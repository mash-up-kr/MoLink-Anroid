package com.mashup.molink.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mashup.molink.R;
import com.mashup.molink.utils.Dlog;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.Callable;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        TextView tvSharedUrl = findViewById(R.id.tvSharedUrl); // url
        EditText etSharedTitle = findViewById(R.id.etSharedTitle); //title

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        String sharedText = null;
        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if ("text/plain".equals(type)) {

                sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);    // 가져온 인텐트의 텍스트 정보

                String url = Util.extractUrlFromText(sharedText);
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

                            ShareActivity.this.runOnUiThread(new Runnable() {
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

        tvSharedUrl.setText(sharedText);

        Button.OnClickListener mClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                String url = tvSharedUrl.getText().toString();
                String title = etSharedTitle.getText().toString();

            }
        };
        findViewById(R.id.btnLinkSave).setOnClickListener(mClickListener);

    };



}



