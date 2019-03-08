package com.example.servicetest;

import org.json.JSONObject;

import com.jixiang.chat.IChatApi;
import com.jixiang.chat.util.SystemUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv1);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        String str = "android$" + SystemUtil.getSystemVersion() + "$" + SystemUtil.getSystemModel() + "$" + SystemUtil.getOperators(this) + "$" + SystemUtil.getNetWorkStatus(this);
        tv.setText(str);
    }

    private void startService() {
        try {
            int app_id = 1002;
            int channel_id = 13;
            int user_id = 44134388;
            int region = 220101;
            String client_version = "2.0.0";
            String ui = "tag_login";
            String device_code = "34E67031A5504AFF1D90199B96FE851340BBFB72";
            String domain = "jixiang.cn";//weile.com
            int game_id = 226;
            int room_id = 610;
            String url_vc = "http://p3kcsai8.weile.com";
            String url_login = "192.168.1.200";
            JSONObject json = new JSONObject();
            json.put("app_id", app_id);
            json.put("channel_id", channel_id);
            json.put("user_id", user_id);
            json.put("region", region);
            json.put("client_version", client_version);
            json.put("ui", ui);
            json.put("device_code", device_code);
            json.put("domain", domain);
            json.put("game_id", game_id);
            json.put("room_id", room_id);
            json.put("url_vc", url_vc);
            json.put("url_login", url_login);
            IChatApi.startService(MainActivity.this, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
