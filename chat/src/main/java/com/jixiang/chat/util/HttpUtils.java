package com.jixiang.chat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.conn.ConnectTimeoutException;

import com.jixiang.chat.httpdns.NetworkRequestUsingHttpDNS;
import com.jixiang.chat.ui.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class HttpUtils {

	/**
	 * 客服API初始化时向服务器发送GET请求
	 * 
	 * @param str
	 *            加密之后的cdata值
	 * @param domain
	 *            可变的域名 请求资源路径
	 * @return 返回初始化需要的json数据
	 * @throws Exception
	 */
//	public static String httpGet(final String str, String domain, Handler handler) {
	public static String httpGet( Context ctx,String path, Handler handler) {
		String result = "";
		try {
			/*URL url = new URL(path);
			if (domain == null) {
				String path = "https://chat.weile.com/api/init?cdata=" + str;
				url = new URL(path);
			} else {
				String path1 = "https://chat." + domain + "/api/init?cdata=" + str;
				url = new URL(path1);
			}*/
//			System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
			result = httpComm(ctx, path, result);
		} catch (ConnectTimeoutException e) {
//			httpGet(str, domain, path, handler);
			httpGet(ctx,path, handler);
		} catch (Exception e) {
			MainActivity.isHttpFirst = false;
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}


	private static String httpComm(Context ctx, String path, String result)
			throws ProtocolException, IOException {
		final HttpURLConnection conn = NetworkRequestUsingHttpDNS.gethttpConn(ctx, path);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "text/plain");
		conn.setRequestProperty("WeileIM", "yes");
		conn.setConnectTimeout(10*1000);
		
		int responseCode = conn.getResponseCode();
		if (responseCode == 200) {
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			result = sb.toString();
		}
		return result;
	}
	
	
	public static String httpsGet( Context ctx,String path, Handler handler) {
		String result = "";
		try {
			result = httpsComm(ctx, path, result);
		} catch (ConnectTimeoutException e) {
//			httpGet(str, domain, path, handler);
			httpsGet(ctx,path, handler);
		}catch (SSLHandshakeException e) {
			MainActivity.isHttpFirst = false;
			handler.sendEmptyMessage(MainActivity.SSLHANDSHAKE);
		} catch (Exception e) {
			MainActivity.isHttpFirst = false;
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}


	private static String httpsComm(Context ctx, String path, String result)
			throws ProtocolException, IOException {
		final HttpsURLConnection conn = NetworkRequestUsingHttpDNS.gethttpsConn(ctx, path);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "text/plain");
		conn.setRequestProperty("WeileIM", "yes");
		conn.setConnectTimeout(10*1000);
		conn.setHostnameVerifier(new HostnameVerifier() {
		     /*
		      * 关于这个接口的说明，官方有文档描述：
		      * This is an extended verification option that implementers can provide.
		      * It is to be used during a handshake if the URL's hostname does not match the
		      * peer's identification hostname.
		      *
		      * 使用HTTPDNS后URL里设置的hostname不是远程的主机名(如:m.taobao.com)，与证书颁发的域不匹配，
		      * Android HttpsURLConnection提供了回调接口让用户来处理这种定制化场景。
		      * 在确认HTTPDNS返回的源站IP与Session携带的IP信息一致后，您可以在回调方法中将待验证域名替换为原来的真实域名进行验证。
		      *
		      */
		     @Override
		     public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
		         String host = conn.getRequestProperty("Host");
		         if (null == host) {
		             host = conn.getURL().getHost();
		         }
		         return HttpsURLConnection.getDefaultHostnameVerifier().verify(host, session);
		     }
		 });
		
		int responseCode = conn.getResponseCode();
		if (responseCode == 200) {
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			result = sb.toString();
		}
		return result;
	}

	/**
	 * 向服务器发送评价星级
	 * 
	 * @param str
	 *            加密后的cdata值
	 * @param domain
	 *            可变的域名
	 * @return 返回提示消息
	 * @throws Exception
	 */
	public static String EvaluateHttps(Context ctx, String path, Handler handler) {
		String result = "";
		try {
			result = httpsComm(ctx, path, result);
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}
	
	public static String EvaluateHttp(Context ctx, String path, Handler handler) {
		String result = "";
		try {
			result = httpComm(ctx, path, result);
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}
	
	/*public static String Evaluate(final String str, String domain, Handler handler) {
		String result = "";
		try {
			URL url;
			if (domain == null) {
				String path = "https://chat.weile.com/api/appraise?cdata=" + str;
				url = new URL(path);
			} else {
				String path1 = "https://chat." + domain + "/api/appraise?cdata=" + str;
				url = new URL(path1);
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setRequestProperty("WeileIM", "yes");
			conn.setConnectTimeout(10000);
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				result = sb.toString();
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}*/

	/**
	 * 发送GET获取历史消息（默认50条）
	 * 
	 * @param str
	 *            加密后的cdata值
	 * @param domain
	 *            可变的域名
	 * @return 返回提示消息
	 * @throws Exception
	 */
	
	public static String getHistoryHttps(Context ctx, String path, Handler handler, Handler mHandler) {
		String result = "";
		try {
			result = httpsComm(ctx, path, result);
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
			Message msg = mHandler.obtainMessage(2);
			mHandler.sendMessage(msg);
		}
		return result;
	}
	
	public static String getHistoryHttp(Context ctx, String path, Handler handler, Handler mHandler) {
		String result = "";
		try {
			result = httpComm(ctx, path, result);
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
			Message msg = mHandler.obtainMessage(2);
			mHandler.sendMessage(msg);
		}
		return result;
	}
	
	/*public static String getHistory(final String str, String domain, Handler handler, Handler mHandler) {
		String result = "";
		try {
			URL url;
			if (domain == null) {
				String path = "https://chat.weile.com/api/history?cdata=" + str;
				url = new URL(path);
			} else {
				String path1 = "https://chat." + domain + "/api/history?cdata=" + str;
				Log.i("splicedpath", path1);
				url = new URL(path1);
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setRequestProperty("WeileIM", "yes");
			conn.setConnectTimeout(10000);
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				result = sb.toString();
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
			Message msg = mHandler.obtainMessage(2);
			mHandler.sendMessage(msg);
		}
		return result;
	}*/

	/**
	 * 向服务器发送GET请求获取qiniu(七牛)需要的token
	 * 
	 * @param str
	 *            加密之后的cdata的值
	 * @param domain
	 *            可变的域名
	 * @return 返回token
	 * @throws Exception
	 */
	public static String getTokenhttps(Context ctx, String path, Handler handler) {
		String result = "";
		try {
			result = httpsComm(ctx, path, result);
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}
	public static String getTokenhttp(Context ctx, String path, Handler handler) {
		String result = "";
		try {
			result = httpComm(ctx, path, result);
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}
	/*public static String getToken(final String str, String domain, Handler handler) {
		String result = "";
		try {
			URL url;
			if (domain == null) {
				String path = "https://chat.weile.com/qn/auth?cdata=" + str;
				url = new URL(path);
			} else {
				String path1 = "https://chat." + domain + "/qn/auth?cdata=" + str;
				url = new URL(path1);
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setRequestProperty("WeileIM", "yes");
			conn.setConnectTimeout(10000);
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				result = sb.toString();
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}*/

	/**
	 * GET 机器人请求
	 * 
	 * @param str
	 *            加密之后的cdata的值
	 * @param domain
	 *            可变的域名
	 * @param msgid
	 *            消息id
	 * @param handler
	 * @return
	 */
	public static String httpRobotHttps(Context ctx, String path,int msgid, Handler handler) {
		String result = "";
		try {
			result = httpsComm(ctx, path, result);
		} catch (IOException e) {
			if (msgid == 0) {
				handler.sendEmptyMessage(MainActivity.CONNECTTIMEOUT);
			} else {
				handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
			}
		}
		return result;
	}
	
	public static String httpRobotHttp(Context ctx, String path,int msgid, Handler handler) {
		String result = "";
		try {
			result = httpComm(ctx, path, result);
		} catch (IOException e) {
			if (msgid == 0) {
				handler.sendEmptyMessage(MainActivity.CONNECTTIMEOUT);
			} else {
				handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
			}
		}
		return result;
	}
	/*public static String httpRobot(final String str, String domain, int msgid, Handler handler) {
		String result = "";
		URL url;
		try {
			if (domain == null) {
				String path = "http://robot.chat.weile.com/api/query?cdata=" + str;
				url = new URL(path);
			} else {
				String path = "http://robot.chat." + domain + "/api/query?cdata=" + str;
				url = new URL(path);
			}
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setRequestProperty("WeileIM", "yes");
			conn.setConnectTimeout(10000);
			int responseCode;
			responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is;
				is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				result = sb.toString();
			}
		} catch (IOException e) {
			if (msgid == 0) {
				handler.sendEmptyMessage(MainActivity.CONNECTTIMEOUT);
			} else {
				handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
			}
		}
		return result;
	}
*/
	// 获取客服信息
	public static String getServerInfoHttps(Context ctx, String path, Handler handler) {
		String result = "";
		try {
			result = httpsComm(ctx, path, result);
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}
	public static String getServerInfoHttp(Context ctx, String path, Handler handler) {
		String result = "";
		try {
			result = httpComm(ctx, path, result);
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}
	/*public static String getServerInfo(final String str, String domain, Handler handler) {
		String result = "";
		try {
			URL url;
			if (domain == null) {
				String path = "https://chat.weile.com/api/serviceInfo?sid=" + str;
				url = new URL(path);
			} else {
				String path1 = "https://chat." + domain + "/api/serviceInfo?sid=" + str;
				url = new URL(path1);
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setRequestProperty("WeileIM", "yes");
			conn.setConnectTimeout(10000);
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				result = sb.toString();
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(MainActivity.CONNECTIONFAIL);
		}
		return result;
	}*/

	/**
	 * POST 上传图片语音视频
	 * 
	 * @param filePath
	 *            文件的本地地址
	 * @param domain
	 *            可变的域名
	 * @param lmsgid
	 *            客户端对信息的唯一标识tag
	 * @param type
	 *            文件类型
	 * @param handler
	 *            回调handler
	 */
	public static void uploadResoures(String filePath, String domain, final int lmsgid, final int type, final Handler handler) {
		try {
			String url;
			if (domain == null) {
				url = "https://chat.jixiang.cn/UpData/File";
			} else {
				url = "https://chat." + domain + "/UpData/File";
			}
			String spliced = new URL(url).getHost() + "-" + (int)(System.currentTimeMillis()/1000);
			String encode = Encypt.encerpt(spliced, "ENCODE", MainActivity.key, 0);
			String WeileIMS = Conversion.ToServer(encode);
			OkHttpUtils.post().url(url).addFile("file", filePath.substring(filePath.lastIndexOf("/") + 1), new File(filePath)).addHeader("WeileIMS", WeileIMS)
				.build().execute(new StringCallback() {
					@Override
					public void onResponse(String result, int arg1) {
						try {
							String json = Encypt.encerpt(Conversion.FromServer(result), "DECODE", MainActivity.key, 0);
							Message msg = handler.obtainMessage(50);
							msg.what = MainActivity.UPLOADSUCCESS;
							msg.obj = json;
							msg.arg1 = type;
							msg.arg2 = lmsgid;
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
						};
					}
					
					@Override
					public void onError(Call arg0, Exception arg1, int arg2) {
						Message msg = handler.obtainMessage(50);
						msg.what = MainActivity.UPLOADFAIL;
						handler.sendMessage(msg);
					}
				});
			
		} catch (Exception e) {
			Message msg = handler.obtainMessage(50);
			msg.what = MainActivity.UPLOADFAIL;
			handler.sendMessage(msg);
		}
	}

}
