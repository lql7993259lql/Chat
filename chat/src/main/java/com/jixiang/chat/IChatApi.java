package com.jixiang.chat;

import java.net.InetAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.jixiang.chat.ui.MainActivity;
import com.jixiang.chat.util.ToastControll;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * SDK初始化接口
 * 
 * @author Wangda
 */
public class IChatApi {
	private static String version = "1.0.0";
	private static int app_id;
	private static int channel_id;
	private static int user_id;
	private static int region;
	private static String client_version;
	private static String ui;
	private static String device_code;
	private static String domain;
	private static int game_id = 0;
	private static int room_id = 0;
	private static String url_vc = "";
	private static String url_login = "";

	public static void startService(Context context, String jsonArgs) {
		try {
			Log.i("Initjson", jsonArgs);
			if (TextUtils.isEmpty(jsonArgs))
				return;
			JSONObject jso = new JSONObject(jsonArgs);
			if (jso == null || jso.length() == 0)
				return;
			if (jso.has("channel_id"))
				channel_id = jso.getInt("channel_id");
			if (jso.has("app_id"))
				app_id = jso.getInt("app_id");
			if (jso.has("ui"))
				ui = jso.getString("ui");
			if (jso.has("client_version"))
				client_version = jso.getString("client_version");
			if (jso.has("user_id"))
				user_id = jso.getInt("user_id");
			if (jso.has("region"))
				region = jso.getInt("region");
			if (jso.has("device_code"))
				device_code = jso.getString("device_code");
			if (jso.has("domain"))
				domain = jso.getString("domain");
			if (jso.has("room_id"))
				room_id = jso.getInt("room_id");
			if (jso.has("game_id"))
				game_id = jso.getInt("game_id");
			if (jso.has("url_vc")) {
				String urlVc = jso.getString("url_vc");
				url_vc = getIp(urlVc);
			}
			if (jso.has("url_login")) {
				String urlLogin = jso.getString("url_login");
				url_login = getIp(urlLogin);
			}
			start(context);
		} catch (JSONException e) {
			@SuppressWarnings("unused")
			String error = String.format("Alipay 配置解析失败:%1$s", e.getMessage());
		}
	}

	public static void start(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra("appId", app_id);
		intent.putExtra("channelId", channel_id);
		intent.putExtra("userId", user_id);
		intent.putExtra("clientVersion", client_version);
		intent.putExtra("region", region);
		intent.putExtra("ui", ui);
		intent.putExtra("devicecode", device_code);
		intent.putExtra("version", version);
		intent.putExtra("domain", domain);
		intent.putExtra("gameid", game_id);
		intent.putExtra("roomid", room_id);
		intent.putExtra("url_vc", url_vc);
		intent.putExtra("url_login", url_login);
		context.startActivity(intent);
	}
	
	private static String ip = "";
	public static String httpChangeIp(final String path) throws InterruptedException {
		Thread thread;
		thread = new Thread(){
			public void run() {
				try {
					URL url = new URL(path);
					InetAddress x = java.net.InetAddress.getByName(url.getHost());
					ip = x.getHostAddress();
				} catch (Exception e) {
					ip = "";
				}
			};
		};
		thread.start();
		thread.join();
		return ip;
	}

	public static String changeIp(final String path) throws InterruptedException {
		Thread thread;
		thread = new Thread(){
			public void run() {
				try {
					InetAddress x = java.net.InetAddress.getByName(path);
					ip = x.getHostAddress();
				} catch (Exception e) {
					ip = "";
				}
			};
		};
		thread.start();
		thread.join();
		return ip;
	}

	public static String getIp(String str) {
		String conversion = "";
		if (str.contains(",")) {
			String[] ips = str.split(",");
			String[] ip = new String[ips.length];
			for (int i = 0; i < ips.length; i++) {
				if (ips[i].contains("http://") || ips[i].contains("https://")) {
					try {
						ip[i] = httpChangeIp(ips[i]);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					if (isIP(ips[i])) {
						ip[i] = ips[i];
					} else {
						try {
							ip[i] = changeIp(ips[i]);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			String result = "";
			for (int i = 0; i < ip.length; i++) {
				result = result + ips[i] + "=" + ip[i] + ",";
			}
			conversion = result.substring(0, result.length() - 1);
		} else {
			String ip = null;
			if (str.contains("http://") || str.contains("https://")) {
				try {
					ip = httpChangeIp(str);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				if (isIP(str)) {
					ip = str;
				} else {
					try {
						ip = changeIp(str);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			conversion = str + "=" + ip;
		}
		return conversion;
	}

	/**
	 * 判断IP格式和范围
	 */
	public static boolean isIP(String addr) {
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(addr);
		boolean ipAddress = mat.find();

		return ipAddress;
	}

}
