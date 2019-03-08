package com.jixiang.chat.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jixiang.chat.R;
import com.jixiang.chat.adapter.ChatAdapter;
import com.jixiang.chat.adapter.FaceGVAdapter;
import com.jixiang.chat.adapter.FaceVPAdapter;
import com.jixiang.chat.adapter.RobotAdapter;
import com.jixiang.chat.entity.ChatEntity;
import com.jixiang.chat.entity.Emoji;
import com.jixiang.chat.entity.History;
import com.jixiang.chat.entity.Options;
import com.jixiang.chat.entity.Robot;
import com.jixiang.chat.httpdns.NetworkRequestUsingHttpDNS;
import com.jixiang.chat.util.CommonUtils;
import com.jixiang.chat.util.Conversion;
import com.jixiang.chat.util.DownLoad;
import com.jixiang.chat.util.Encypt;
import com.jixiang.chat.util.GetEmjo;
import com.jixiang.chat.util.HttpUtils;
import com.jixiang.chat.util.ImageUtil;
import com.jixiang.chat.util.ReadJsonFile;
import com.jixiang.chat.util.Spliced;
import com.jixiang.chat.util.SystemUtil;
import com.jixiang.chat.util.ToastControll;
import com.jixiang.chat.util.weileHelper;
import com.jixiang.chat.view.ContentListView;
import com.jixiang.chat.view.ContentListView.OnRefreshListenerHeader;
import com.jixiang.chat.view.EvaluateView;
import com.jixiang.chat.view.EvaluateViewFinish;
import com.jixiang.chat.voice.AudioRecorderButton;
import com.jixiang.chat.voice.MediaPlayerManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnRefreshListenerHeader {
	private EditText etChat;
	private LinearLayout llGongNeng, llEmjo, mDotsLayout, llNetwork, llSheild;
	private ImageView ivJiaHao;
	private ImageView ivFaSong, ivPhone, ivBack, ivEmjo, ivVoice, ivKey;
	private RadioButton rbZP, rbPZ, rbMYD;
	private ContentListView lvContent;
	private ChatAdapter chatAdapter;
	private List<ChatEntity> chatList = new ArrayList<ChatEntity>();
	// 7列
	private int columns = 7;
	// 3行
	private int rows = 3;
	private List<View> views = new ArrayList<View>();
	private List<Emoji> emojiList = new ArrayList<Emoji>();
	private List<String> staticFacesList;
	public static Bitmap photo;
	private AudioRecorderButton mAudioRecorderButton;
	private BroadcastReceiver receiver1, receiver2;
	public static final int REQUEST_CODE_CAPTURE_CAMEIA = 0;
	public static final int REQUEST_CODE_CAPTURE_IMAGES = 1;
	public static String domain;
	private ViewPager mViewPager;
	private String content;
	public static String photoPath;
	private String[] image;
	private File e;
	private String sessionid;
	private String serverSessionid;
	private String service_avatar;
	private String service_nickname;
	private String avatar;
	private String ip;
	private int port;
	private int appId;
	private int channelId;
	private int userId;
	private int roomid;
	private int gameid;
	private String clientVersion;
	private String version;
	private int region;
	public static int max;
	private int lastId = 0;
	private int size = 20;
	private int tag = 1;
	private String ui;
	private String devicecode;
	private String url_vc;
	private String url_login;
	private int sdkversions = 2;
	private String phone_info;
	private Socket socket = null;
	private String httpStr;
	private String tel;
	private boolean little = false;
	private boolean isReading = true;
	private boolean isEvaluate = false;
	private boolean startEvaluate = false;
	private boolean OffLine = true;
	private boolean isQueue = true;
	private boolean isXinTiao = false;
	public static boolean isOnService = false;
	private boolean isClickOnServer = false;
	private boolean isFirst = true;
	private boolean isHttpSuccess = false;
	private boolean isOnServerFirst = true;
	public static boolean isHttpFirst = true;
	public static boolean isLock = true;
	public boolean connectFail = false;
	private DataOutputStream out;
	private DataInputStream in;
	private List<Long> reTimeList = new ArrayList<Long>();
	public static boolean stopThread = false;
	public static final int RECEIVESUCCESS = 3;
	public static final int IMAGE = 2;
	public static final int SENDSUCCESS = 4;
	public static final int CONTEXTMESSAGE = 5;
	public static final int SYSTEMMESSAGE = 6;
	public static final int TIPSMESSAGE = 7;
	public static final int PICMESSAGE = 8;
	public static final int QUEUEMESSAGE = 9;
	public static final int COMMANDMESSAGE = 10;
	public static final int VOICEMESSAGE = 11;
	public static final int VIDEOMESSAGE = 12;
	public static final int POSITIONMESSAGE = 13;
	public static final int UPLOADSUCCESS = 14;
	public static final int UPLOADFAIL = 15;
	public static boolean isLoading = false;
	public static final int SERVERAVATARSUCCESS = 16;
	public static final int AVATARSUCCESS = 17;
	public static final int HTTPGETSUCCESS = 18;
	public static final int SESSIONIDSUCCESS = 19;
	public static final int ONSERVER = 20;
	public static final int USEROFFLINE = 21;
	public static final int EVALUATESUCCESS = 22;
	public static final int DOWNLOADPIC = 24;
	public static final int DOWNLOADVOICE = 25;
	public static final int ROBOTSUCCESS = 26;
	public static final int ROBOTREQUEST = 27;
	public static final int ROBOTONSERVICE = 28;
	public static final int HISTORYSERVICEBITMAP = 29;
	public static final int CONNECTTIMEOUT = 30;
	public static final int CONNECTIONFAIL = 31;
	public static final int SERVERSESSIONIDSUCCESS = 32;
	public static final int HTTPGETFAIL = 33;
	public static final int DOWNLOADVIDEO = 34;
	public static final int SSLHANDSHAKE = 35;
	public static String key = "d96b08cc41cf81c6c71e6355387c4268";
	private Handler handler = new Handler() {
		private String nickname;
		private Bitmap serviceB;

		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case GetEmjo.ZIPSUCCESS:
					String png = Environment.getExternalStorageDirectory()
							+ "/PngImage";
					String ajosnStr = png + "/list.json";
					String ajsonStr = ReadJsonFile.readJson(ajosnStr);
					JSONArray array = new JSONArray(ajsonStr);
					image = new String[array.length()];
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = (JSONObject) array.get(i);
						Emoji emoji = new Emoji();
						emoji.setText(obj.getString("text"));
						emoji.setFile(obj.getString("file"));
						emojiList.add(emoji);
						image[i] = obj.getString("file") + ".png";
					}
					chatAdapter.setEmojiList(emojiList);
					initStaticFaces();
					InitViewPager();
					break;
				case IMAGE:
					initStaticFaces();
					InitViewPager();
					break;
				case RECEIVESUCCESS:
					String str = (String) msg.obj;
					long time1 = Long.valueOf(str);
					reTimeList.add(time1);
					break;
				case SENDSUCCESS:
					String jsonStr = (String) msg.obj;
					JSONObject json = new JSONObject(jsonStr);
					int time = json.getInt("ts");
					int mId = json.getInt("lmsgid");
					int length = chatList.size();
					for (int j = 0; j < length; j++) {
						if (mId == chatList.get(j).getTag()) {
							chatList.get(j).setSuccess(true);
							chatList.get(j)
									.setChatTime(
											CommonUtils
													.getFormattedTime((long) time * 1000));
							chatAdapter.notifyDataSetChanged();
							j = length - 1;
						}
					}
					break;
				case CONTEXTMESSAGE:
					String contextStr = (String) msg.obj;
					JSONObject cJson = new JSONObject(contextStr);
					long cTime = cJson.getInt("ts");
					String contentText = cJson.getString("body");
					ChatEntity cEntity = new ChatEntity();
					cEntity.setChatTime(CommonUtils
							.getFormattedTime(cTime * 1000));
					cEntity.setContent(contentText);
					cEntity.setComeMsg(true);
					cEntity.setRobot(false);
					cEntity.setServerName(nickname);
					cEntity.setServerAvatar(serviceB);
					chatList.add(cEntity);
					chatAdapter.notifyDataSetChanged();
					lvContent.setSelection(lvContent.getBottom());
					break;
				case SYSTEMMESSAGE:

					break;
				case TIPSMESSAGE:
					String tipmsg = (String) msg.obj;
					JSONObject tipJson = new JSONObject(tipmsg);
					int tipTime = tipJson.getInt("ts");
					String tipText = tipJson.getString("body");
					if (tipText != null && tipText != "") {
						ChatEntity tipEntity = new ChatEntity();
						tipEntity.setTipMsg(tipText);
						tipEntity.setComeMsg(true);
						tipEntity.setTip(true);
						chatList.add(tipEntity);
						chatAdapter.notifyDataSetChanged();
						lvContent.setSelection(lvContent.getBottom());
						Log.i("http: ", "id: 3" + "  value: " + tipText);
					}
					break;
				case SESSIONIDSUCCESS:
					connectFail = false;
					if (!isOnService) {
						for (int j = 0; j < chatList.size(); j++) {
							if (chatList.get(j).isTip()) {
								chatList.remove(j);
								j = j - 1;
							}
						}
						chatAdapter.notifyDataSetChanged();
					}
					String sessionidmsg = (String) msg.obj;
					JSONObject sessionidJson = new JSONObject(sessionidmsg);
					int sessionidTime = sessionidJson.getInt("ts");
					String sessionidText = sessionidJson.getString("body");
					isXinTiao = true;
					if (isFirst) {
						xintiao();
						isFirst = false;
					}
					Log.i("http: ", "id: 4" + "  value: " + sessionidText);
					break;
				case ONSERVER:
					connectFail = false;
					if (welcome) {
						for (int i = 1; i < chatList.size(); i++) {
							chatList.remove(i);
							i = i - 1;
						}
						welcome = false;
					} else {
						if (chatList.get(chatList.size() - 1).isTip()) {
							chatList.remove(chatList.size() - 1);
						}
					}
					chatAdapter.notifyDataSetChanged();
					llSheild.setVisibility(View.GONE);
					startEvaluate = true;
					isOnService = true;
					isClickOnServer = true;
					detection();
					String onmsg = (String) msg.obj;
					JSONObject onJson = new JSONObject(onmsg);
					int onTime = onJson.getInt("ts");
					String onText = onJson.getString("body");
					serverSessionid = onJson.getString("sid");
					if (serverSessionid != null) {
						getServerInfo(serverSessionid);
					}
					sessionid = serverSessionid;
					ChatEntity onEntity = new ChatEntity();
					onEntity.setComeMsg(true);
					onEntity.setTip(true);
					onEntity.setTipMsg(onText);
					chatList.add(onEntity);
					chatAdapter.notifyDataSetChanged();
					lvContent.setSelection(lvContent.getBottom());
					check();
					Log.i("http: ", "id: 5" + "  value: " + onText);
					Log.i("http: ", "id: 5" + "  value: " + serverSessionid);
					break;
				case USEROFFLINE:
					String usermsg = (String) msg.obj;
					JSONObject userJson = new JSONObject(usermsg);
					int userTime = userJson.getInt("ts");
					String userText = userJson.getString("body");
					OffLine = false;
					ChatEntity offEntity = new ChatEntity();
					offEntity.setComeMsg(true);
					offEntity.setTip(true);
					offEntity.setTipMsg(userText);
					chatList.add(offEntity);
					chatAdapter.notifyDataSetChanged();
					lvContent.setSelection(lvContent.getBottom());
					hideSoftInputView();
					llEmjo.setVisibility(View.GONE);
					llGongNeng.setVisibility(View.GONE);
					llSheild.setVisibility(View.VISIBLE);
					etChat.setFocusable(false);
					etChat.setFocusableInTouchMode(false);
					if (!isEvaluate) {
						evaluateFinish();
					}
					isXinTiao = false;
					Log.i("http: ", "id: 6" + "  value: " + userText);
					break;
				case QUEUEMESSAGE:
					String queuemsg = (String) msg.obj;
					JSONObject queueJson = new JSONObject(queuemsg);
					int queueTime = queueJson.getInt("ts");
					String queueText = queueJson.getString("body");
					if (queueText != null && queueText != "") {
						if (chatList.get(chatList.size() - 1).isTip()) {
							chatList.remove(chatList.size() - 1);
						}
						ChatEntity queueEntity = new ChatEntity();
						queueEntity.setTipMsg(queueText);
						queueEntity.setComeMsg(true);
						queueEntity.setTip(true);
						chatList.add(queueEntity);
						chatAdapter.notifyDataSetChanged();
						lvContent.setSelection(lvContent.getBottom());
						Log.i("http: ", "id: 7" + "  value: " + queueText);
					}
					break;
				case COMMANDMESSAGE:

					break;
				case PICMESSAGE:
					String picStr = (String) msg.obj;
					JSONObject picJson = new JSONObject(picStr);
					long picTime = picJson.getInt("ts");
					String picText = picJson.getString("body");
					ChatEntity picEntity = new ChatEntity();
					picEntity.setChatTime(CommonUtils
							.getFormattedTime(picTime * 1000));
					picEntity.setContent("【 图片消息 】");
					picEntity.setComeMsg(true);
					picEntity.setServerName(nickname);
					picEntity.setServerAvatar(serviceB);
					int picMsgIndex = tag++;
					picEntity.setTag(picMsgIndex);
					chatList.add(picEntity);
					chatAdapter.notifyDataSetChanged();
					lvContent.setSelection(lvContent.getBottom());
					DownLoad.downLoadBitmap(picText, handler, picMsgIndex);
					break;
				case VOICEMESSAGE:
					String voiceStr = (String) msg.obj;
					JSONObject voiceJson = new JSONObject(voiceStr);
					long voiceTime = voiceJson.getInt("ts");
					String voiceText = voiceJson.getString("body");
					int voiceSeconds = voiceJson.getInt("ext");
					ChatEntity voiceEntity = new ChatEntity();
					voiceEntity.setChatTime(CommonUtils
							.getFormattedTime(voiceTime * 1000));
					voiceEntity.setContent("【 语音消息 】");
					voiceEntity.setTime(voiceSeconds);
					voiceEntity.setComeMsg(true);
					voiceEntity.setServerName(nickname);
					voiceEntity.setServerAvatar(serviceB);
					int voiceMsgIndex = tag++;
					voiceEntity.setTag(voiceMsgIndex);
					chatList.add(voiceEntity);
					chatAdapter.notifyDataSetChanged();
					lvContent.setSelection(lvContent.getBottom());
					DownLoad.downLoadVoice(voiceText, handler, voiceMsgIndex);
					break;
				case VIDEOMESSAGE:
					String videoStr = (String) msg.obj;
					JSONObject videoJson = new JSONObject(videoStr);
					long videoTime = videoJson.getInt("ts");
					String videoText = videoJson.getString("body");
					ChatEntity videoEntity = new ChatEntity();
					videoEntity.setChatTime(CommonUtils
							.getFormattedTime(videoTime * 1000));
					videoEntity.setContent("【 视频消息 】");
					videoEntity.setComeMsg(true);
					videoEntity.setServerName(nickname);
					videoEntity.setServerAvatar(serviceB);
					int videoMsgIndex = tag++;
					videoEntity.setTag(videoMsgIndex);
					chatList.add(videoEntity);
					chatAdapter.notifyDataSetChanged();
					lvContent.setSelection(lvContent.getBottom());
					DownLoad.downLoadVideo(videoText, handler, videoMsgIndex);
					break;
				case POSITIONMESSAGE:

					break;
				case UPLOADSUCCESS:
					JSONObject uploadJson = new JSONObject((String) msg.obj);
					int uploadtype = msg.arg1;
					int lmsgid = msg.arg2;
					String pathUrl = uploadJson.getJSONObject("data")
							.getString("file");
					int uploadlength = chatList.size();
					for (int j = 0; j < uploadlength; j++) {
						if (lmsgid == chatList.get(j).getTag()) {
							chatList.get(j).setUploadPath(pathUrl);
							j = uploadlength - 1;
						}
					}
					int msgid = 0;
					String seconds = null;
					if (uploadtype == 0) {
						msgid = 12;
						seconds = "";
					} else if (uploadtype == 1) {
						msgid = 13;
						int uploadlength1 = chatList.size();
						for (int j = 0; j < uploadlength1; j++) {
							if (lmsgid == chatList.get(j).getTag()) {
								seconds = String.valueOf(Math.round(chatList
										.get(j).getTime()));
								j = uploadlength1 - 1;
							}
						}
					} else if (uploadtype == 2) {
						msgid = 14;
						seconds = "";
					}
					sendServer(pathUrl, msgid, lmsgid, seconds);
					break;
				case UPLOADFAIL:
					ToastControll.showToast("发送失败，请重试", MainActivity.this);
					break;
				case HTTPGETSUCCESS:
					isHttpSuccess = true;
					isHttpFirst = false;
					chatList.removeAll(chatList);
					String initStr = (String) msg.obj;
					Log.i("http", initStr);
					JSONObject initJson = new JSONObject(initStr);
					sessionid = initJson.getString("sessionid");
					int sessionid_timeout = initJson
							.getInt("sessionid_timeout");
					if (userId == 0) {
						SharedPreferences sp1 = getSharedPreferences(
								"sessionid", MODE_PRIVATE);
						Editor et1 = sp1.edit();
						et1.putString("sessionid", sessionid);
						et1.putLong("time", System.currentTimeMillis());
						et1.putInt("sessionid_timeout", sessionid_timeout);
						et1.putString("result", initStr);
						et1.commit();
					}
					lastId = initJson.getInt("lastmsgid");
					tel = initJson.getString("tel");
					service_avatar = initJson.getString("service_avatar");
					avatar = initJson.getString("avatar");
					service_nickname = initJson.getString("service_nickname");
					String imaddr = initJson.getString("imaddr");
					int face = initJson.getInt("face");
					int faceVersion = initJson.getInt("face_version");
					int robot = initJson.getInt("robot");
					String facezip = initJson.getString("facezip");
					String welcome = initJson.getString("welcome");
					if (service_avatar != null || avatar != null) {
						DownLoad.downLoadServiceBitmap(service_avatar, handler);
						DownLoad.downLoadAvatarBitmap(avatar, handler);
					}
					if (imaddr != null) {
						String[] ddr = imaddr.split(":");
						ip = ddr[0];
						port = Integer.valueOf(ddr[1]);
					}
					ChatEntity entity = new ChatEntity();
					if (welcome != null) {
						entity.setComeMsg(true);
						entity.setContent(welcome);
						entity.setChatTime(CommonUtils.getFormattedTime(System
								.currentTimeMillis()));
					}
					if (robot == 1) {
						RequestRobot(0, "");
						chatList.add(entity);
						isClickOnServer = false;
						isOnService = false;
						chatAdapter.notifyDataSetChanged();
					} else {
						chatList.add(entity);
						chatAdapter.notifyDataSetChanged();
						isXinTiao = false;
						isClickOnServer = true;
						conn(sessionid, ip, port);
					}
					if (face == 0) {
						ivEmjo.setVisibility(View.GONE);
					} else {
						ivEmjo.setVisibility(View.VISIBLE);
						SharedPreferences sp = getSharedPreferences("face",
								MODE_PRIVATE);
						int shared = sp.getInt("face_version", 0);
						if (faceVersion != shared && faceVersion > shared) {
							get(facezip, true);
							Editor et = sp.edit();
							et.putInt("face_version", faceVersion);
							et.commit();
						} else {
							get(facezip, false);
						}
					}
					break;
				case EVALUATESUCCESS:
					String evaluateStr = (String) msg.obj;
					JSONObject evaluateJson = new JSONObject(evaluateStr);
					String evaluateMsg = evaluateJson.getString("msg");
					if (evaluateMsg != null) {
						ToastControll.showToast(evaluateMsg, MainActivity.this);
						if (evaluateMsg.contains("会话不存在")) {
							isEvaluate = false;
						}
					}
					break;
				case SERVERAVATARSUCCESS:
					serviceB = (Bitmap) msg.obj;
					chatAdapter.setServerAvatar(serviceB);
					chatAdapter.notifyDataSetChanged();
					break;
				case AVATARSUCCESS:
					Bitmap avatarB = (Bitmap) msg.obj;
					chatAdapter.setAvatar(avatarB);
					chatAdapter.notifyDataSetChanged();
					break;
				case HISTORYSERVICEBITMAP:
					Bitmap hisService = (Bitmap) msg.obj;
					int hisMsgid = msg.arg1;
					chatList.get(hisMsgid).getHistory().setBitmap(hisService);
					chatAdapter.notifyDataSetChanged();
					break;
				case DOWNLOADPIC:
					String picPath = (String) msg.obj;
					int piclmsgid = msg.arg1;
					Bitmap pic = ImageUtil.compressImage(ImageUtil.getImage(
							picPath, max));
					ImageSpan imgSpan = new ImageSpan(MainActivity.this, pic);
					SpannableString spannableString = new SpannableString(
							"icon");
					spannableString.setSpan(imgSpan, 0, 4,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					int picsize = chatList.size();
					for (int j = 0; j < picsize; j++) {
						if (piclmsgid == chatList.get(j).getTag()) {
							chatList.get(j).setBitmap(spannableString);
							chatList.get(j).setPhoto(pic);
							chatList.get(j).setPhotoPath(picPath);
							chatList.get(j).setPhoto(true);
							chatList.get(j).setVideo(false);
							chatAdapter.notifyDataSetChanged();
							j = picsize - 1;
						}
					}
					break;
				case DOWNLOADVOICE:
					String voicePath = (String) msg.obj;
					float voiceLenth = getVoiceLength(voicePath);
					int voicelmsgid = msg.arg1;
					int voicesize = chatList.size();
					for (int k = 0; k < voicesize; k++) {
						if (voicelmsgid == chatList.get(k).getTag()) {
							chatList.get(k).setYuYin(true);
							chatList.get(k).setFilePath(voicePath);
							chatList.get(k).setTime(voiceLenth / 1000);
							chatAdapter.notifyDataSetChanged();
							k = voicesize - 1;
						}
					}
					break;
				case DOWNLOADVIDEO:
					String videoPath = (String) msg.obj;
					int videolmsgid = msg.arg1;
					Bitmap firstBit = ImageUtil.getFirstBitmap(videoPath);
					String firstBitPath = ImageUtil.saveImageToGallery(
							MainActivity.this, firstBit);
					Bitmap videopic = ImageUtil.compressImage(ImageUtil
							.getImage(firstBitPath, max));
					Bitmap mergeBit = ImageUtil.mergeBitmap(videopic,
							BitmapFactory.decodeResource(getResources(),
									R.drawable.chat_video_img));
					ImageSpan imgSpanVideo = new ImageSpan(MainActivity.this,
							mergeBit);
					SpannableString spannableStringVideo = new SpannableString(
							"icon");
					spannableStringVideo.setSpan(imgSpanVideo, 0, 4,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					int videosize = chatList.size();
					for (int j = 0; j < videosize; j++) {
						if (videolmsgid == chatList.get(j).getTag()) {
							chatList.get(j).setBitmap(spannableStringVideo);
							chatList.get(j).setPhoto(mergeBit);
							chatList.get(j).setPhotoPath(firstBitPath);
							chatList.get(j).setPhoto(true);
							chatList.get(j).setVideo(true);
							chatList.get(j).setVideoPath(videoPath);
							chatAdapter.notifyDataSetChanged();
							j = videosize - 1;
						}
					}
					break;
				case ROBOTSUCCESS:
					for (int l = 0; l < chatList.size(); l++) {
						if (chatList.get(l).isLoading()) {
							chatList.remove(l);
							chatAdapter.notifyDataSetChanged();
							isLoading = false;
							l = l - 1;
						}
					}
					for (int k2 = 0; k2 < chatList.size(); k2++) {
						if (chatList.get(k2).isTip()) {
							chatList.remove(k2);
							k2 = k2 - 1;
						}
					}
					String robotResult = (String) msg.obj;
					ChatEntity pEntity = new ChatEntity();
					pEntity.setComeMsg(true);
					pEntity.setRobot(true);
					pEntity.setChatTime(CommonUtils.getFormattedTime(System
							.currentTimeMillis()));
					List<Robot> robotList = new ArrayList<Robot>();
					JSONObject robotJson = new JSONObject(robotResult);
					int isList = robotJson.getInt("is_list");
					JSONArray robotArray = robotJson.getJSONArray("list");
					for (int i = 0; i < robotArray.length(); i++) {
						JSONObject robotObj = (JSONObject) robotArray.get(i);
						Robot robot1 = new Robot();
						int opsmsgid = robotObj.getInt("msgid");
						robot1.setIsList(isList);
						robot1.setSort(i + 1);
						if (isList == 0) {
							if (opsmsgid == 11) {
								robot1.setMsgid(robotObj.getInt("msgid"));
								robot1.setBody(robotObj.getString("body"));
								robotList.add(robot1);
								pEntity.setRobotList(robotList);
							} else if (opsmsgid == 12) {
								int pmsgid = tag++;
								ChatEntity c = new ChatEntity();
								c.setComeMsg(true);
								c.setTag(pmsgid);
								c.setContent("【 图片消息 】");
								c.setChatTime(CommonUtils
										.getFormattedTime(System
												.currentTimeMillis()));
								chatList.add(c);
								chatAdapter.notifyDataSetChanged();
								lvContent.setSelection(lvContent.getBottom());
								DownLoad.downLoadBitmap(
										robotObj.getString("body"), handler,
										pmsgid);
								return;
							} else {
								robot1.setBody(robotObj.getString("body"));
								robot1.setMsgid(robotObj.getInt("msgid"));
								if (robotObj.has("options")) {
									JSONArray opsArray = robotObj
											.getJSONArray("options");
									if (opsArray.length() >= 1) {
										List<Options> opsList = new ArrayList<Options>();
										for (int j = 0; j < opsArray.length(); j++) {
											JSONObject opsObj = (JSONObject) opsArray
													.get(j);
											Options ops = new Options();
											ops.setBody(opsObj
													.getString("body"));
											ops.setText(opsObj
													.getString("text"));
											ops.setMsgid(opsObj.getInt("msgid"));
											opsList.add(ops);
										}
										robot1.setOptions(true);
										robot1.setOptions(opsList);
									} else {
										robot1.setOptions(false);
									}
								} else {
									robot1.setOptions(false);
								}
								robotList.add(robot1);

							}
						} else if (isList == 2) {
							handler.sendEmptyMessage(MainActivity.ROBOTONSERVICE);
							return;
						} else {
							robot1.setBody(robotObj.getString("body"));
							robot1.setMsgid(robotObj.getInt("msgid"));
							if (robotObj.has("options")) {
								JSONArray opsArray = robotObj
										.getJSONArray("options");
								if (opsArray.length() >= 1) {
									List<Options> opsList = new ArrayList<Options>();
									for (int j = 0; j < opsArray.length(); j++) {
										JSONObject opsObj = (JSONObject) opsArray
												.get(j);
										Options ops = new Options();
										ops.setBody(opsObj.getString("body"));
										ops.setText(opsObj.getString("text"));
										ops.setMsgid(opsObj.getInt("msgid"));
										opsList.add(ops);
									}
									robot1.setOptions(true);
									robot1.setOptions(opsList);
								} else {
									robot1.setOptions(false);
								}
							} else {
								robot1.setOptions(false);
							}
							robotList.add(robot1);
						}
					}
					RobotAdapter robotAdapter = new RobotAdapter(
							MainActivity.this, robotList, handler);
					pEntity.setAdapter(robotAdapter);
					chatList.add(pEntity);
					chatAdapter.notifyDataSetChanged();
					lvContent.setSelection(lvContent.getBottom());
					break;
				case ROBOTREQUEST:
					if (isClickOnServer) {
						ToastControll.showToast("正在为您分配客服...请稍后",
								MainActivity.this);
					} else {
						ChatEntity chatE = new ChatEntity();
						chatE.setLoading(true);
						chatList.add(chatE);
						chatAdapter.notifyDataSetChanged();
						lvContent.setSelection(lvContent.getBottom());
						String rqbody = (String) msg.obj;
						int rqmsgid = msg.arg1;
						RequestRobot(rqmsgid, rqbody);
					}
					break;
				case ROBOTONSERVICE:
					isClickOnServer = true;
					if (socket == null) {
						if (isOnServerFirst) {
							ChatEntity en = new ChatEntity();
							en.setComeMsg(true);
							en.setTip(true);
							en.setTipMsg("正在连接客服,请稍候...");
							chatList.add(en);
							chatAdapter.notifyDataSetChanged();
							lvContent.setSelection(lvContent.getBottom());
							isOnServerFirst = false;
							isXinTiao = false;
							conn(sessionid, ip, port);
						} else {
							isXinTiao = false;
							conn(sessionid, ip, port);
						}
					} else {
						ToastControll.showToast("正在为您分配客服...请稍候",
								MainActivity.this);
					}
					break;
				case CONNECTTIMEOUT:
					for (int l = 0; l < chatList.size(); l++) {
						if (chatList.get(l).isLoading()) {
							chatList.remove(l);
							isLoading = false;
							l = l - 1;
						}
					}
					chatAdapter.notifyDataSetChanged();
					isClickOnServer = true;
					if (socket == null) {
						isXinTiao = false;
						conn(sessionid, ip, port);
					}
					break;
				case CONNECTIONFAIL:
					for (int l = 0; l < chatList.size(); l++) {
						if (chatList.get(l).isLoading()) {
							chatList.remove(l);
							isLoading = false;
							l = l - 1;
						}
					}
					chatAdapter.notifyDataSetChanged();
					ToastControll.showToast("请求失败，请重试", MainActivity.this);
					break;
				case SERVERSESSIONIDSUCCESS:
					String sss = (String) msg.obj;
					JSONObject sssJson = new JSONObject(sss);
					String avatar = sssJson.optString("avatar","");
					nickname = sssJson.optString("nickname","");
					if (!TextUtils.isEmpty(avatar)) {
						DownLoad.downLoadServiceBitmap(avatar, handler);
					}
					chatAdapter.notifyDataSetChanged();
					break;
				case HTTPGETFAIL:
					chatList.get(0).setTipMsg("(初始化失败)");
					chatAdapter.notifyDataSetChanged();
					isHttpFirst = false;
					break;
			
				case SSLHANDSHAKE:
			
					ToastControll.showToast("请把手机本地时间设置为当前时间", MainActivity.this);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity_main);
		try {
			initViews();
			setAdapter();
			getParams();
			setMaxWidth();
			setListeners();
			receiver1 = new MyReciver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(CameraActivity.SEND_PHOTO);
			registerReceiver(receiver1, filter);
			receiver2 = new NetworkReceiver();
			IntentFilter filter2 = new IntentFilter();
			filter2.addAction("android.net.conn.CONNECTIVITY_CHANGE");
			registerReceiver(receiver2, filter2);
			reading task = new reading();
			task.execute();
		} catch (Exception e) {
			ToastControll.showToast("数据异常", MainActivity.this);
		}
	}

	/**
	 * socket连接成功之后向服务器发送sessionId
	 * 
	 * @param str
	 * @param msg
	 */
	protected void sendsessionid(final String str, final int msg) {
		try {
			byte[] buffer = change(str, msg);
			byte[] b = weileHelper.EscapeMessage(buffer);
			out = new DataOutputStream(socket.getOutputStream());
			out.write(b);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 每5分钟检测一次 是否有发送中的消息
	 */
	private void detection() {
		try {
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					if (isXinTiao && OffLine) {
						check();
					}
					handler.postDelayed(this, 300000);
				}
			}, 300000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 心跳包检测长连接是否断线
	 */
	private int i = 0;

	private void xintiao() {
		if (socket != null) {
			long currentTime = System.currentTimeMillis();
			String str = String.valueOf(currentTime);
			sendXinTiao(str, 1);
		}
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if (isClickOnServer) {
					if (isXinTiao) {
						if (OffLine) {
							if (socket != null) {
								long currentTime = System.currentTimeMillis();
								String str = String.valueOf(currentTime);
								sendXinTiao(str, 1);
							}
						}
					}
				}
				handler.postDelayed(this, 7000);
			}
		}, 7000);
		final Handler handler1 = new Handler();
		handler1.postDelayed(new Runnable() {
			public void run() {
				try {
					if (isClickOnServer) {
						if (isXinTiao) {
							if (OffLine) {
								if (reTimeList.size() < 1) {
									if (isLock) {
										isXinTiao = false;
										if (socket != null) {
											socket.close();
											socket = null;
										}
										conn(sessionid, ip, port);
									}
								}
								Log.i("xintiao",
										"reTimeList" + reTimeList.size());
								reTimeList.removeAll(reTimeList);
							}
						}
					}
					handler1.postDelayed(this, 10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 15000);
	}

	/**
	 * 获取屏幕宽度的一半
	 */
	private void setMaxWidth() {
		// 获取屏幕的宽度
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		max = (int) (outMetrics.widthPixels / 10 * 5);
	}

	/**
	 * 无限读取服务器发送过来的消息
	 * 
	 * @author Administrator WangDa
	 */
	private class reading extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			if (stopThread) {
				try {
					while (isReading) {
						Thread.sleep(800);
						if (socket != null && !socket.isClosed()) {
							in = new DataInputStream(socket.getInputStream());
							int i = in.available();
							byte[] by = new byte[i];
							int len = 0;
							len = in.read(by);
							if (len != 0 && len != -1) {
								relieve(by);
							}
							if (len == -1) {
								isXinTiao = false;
								socket.close();
								socket = null;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

	}

	/**
	 * 获取游戏跳转客服界面时传过来的数据
	 */
	private void getParams() {
		try {
			stopThread = true;
			Intent intent = getIntent();
			appId = intent.getIntExtra("appId", 0);
			channelId = intent.getIntExtra("channelId", 0);
			userId = intent.getIntExtra("userId", 0);
			// userId = 5475815;
			String cv = intent.getStringExtra("clientVersion");
			if (cv == null) {
				clientVersion = "";
			} else {
				clientVersion = cv;
			}
			region = intent.getIntExtra("region", 0);
			String ui1 = intent.getStringExtra("ui");
			if (ui1 == null) {
				ui = "";
			} else {
				ui = ui1;
			}
			String dc = intent.getStringExtra("devicecode");
			if (dc == null) {
				devicecode = "";
			} else {
				devicecode = dc;
			}
			String vs = intent.getStringExtra("version");
			if (vs == null) {
				version = "";
			} else {
				version = vs;
			}
			domain = intent.getStringExtra("domain");
			roomid = intent.getIntExtra("roomid", 0);
			gameid = intent.getIntExtra("gameid", 0);
			url_vc = intent.getStringExtra("url_vc");
			url_login = intent.getStringExtra("url_login");
			// gps = GPS.gps(MainActivity.this);
			try {
				phone_info = "android$" + SystemUtil.getSystemVersion() + "$"
						+ SystemUtil.getSystemModel() + "$"
						+ SystemUtil.getOperators(this) + "$"
						+ SystemUtil.getNetWorkStatus(this);
			} catch (Exception e) {
			} finally {
				if (isHttpFirst) {
					if (userId == 0) {
						long nowTime = System.currentTimeMillis();
						SharedPreferences sp = getSharedPreferences(
								"sessionid", MODE_PRIVATE);
						long time = sp.getLong("time", 0);
						int sessionid_timeout = sp.getInt("sessionid_timeout",
								0);
						if (nowTime / 1000 < time / 1000 + sessionid_timeout) {
							String result = sp.getString("result", "");
							init(result);
						} else {
							httpStr = Spliced.splicedInitHttp(appId, channelId,
									userId, clientVersion, region, ui,
									devicecode, version, url_vc, url_login,
									sdkversions, phone_info);
							String crypt = Encypt.encerpt(httpStr, "ENCODE",
									key, 0);
							String conversion = Conversion.ToServer(crypt);
							initHttpGet(conversion);
						}
					} else {
						httpStr = Spliced.splicedInitHttp(appId, channelId,
								userId, clientVersion, region, ui, devicecode,
								version, url_vc, url_login, sdkversions,
								phone_info);
						String crypt = Encypt
								.encerpt(httpStr, "ENCODE", key, 0);
						String conversion = Conversion.ToServer(crypt);
						initHttpGet(conversion);
					}
				}
			}
			Log.i("initInfo", "appid: " + appId + "\n" + "channelid: "
					+ channelId + "\n" + "userid: " + userId + "\n"
					+ "clientVersion: " + clientVersion + "\n" + "region: "
					+ region + "\n" + "ui: " + ui + "\n" + "devicecode: "
					+ devicecode + "\n" + "version: " + version + "\n"
					+ "domain: " + domain + "\n" + "roomid: " + roomid + "\n"
					+ "gameid: " + gameid + "\n" + "httpStr: " + httpStr + "\n"
					+ "url_vc: " + url_vc + "\n" + "url_login: " + url_login
					+ "\n" + "phone_info: " + phone_info);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 匿名用户的sessionid没有过期
	 */
	private void init(String initStr) {
		try {
			isHttpSuccess = true;
			isHttpFirst = false;
			chatList.removeAll(chatList);
			JSONObject initJson = new JSONObject(initStr);
			sessionid = initJson.getString("sessionid");
			lastId = initJson.getInt("lastmsgid");
			tel = initJson.getString("tel");
			service_avatar = initJson.getString("service_avatar");
			avatar = initJson.getString("avatar");
			service_nickname = initJson.getString("service_nickname");
			String imaddr = initJson.getString("imaddr");
			int face = initJson.getInt("face");
			int faceVersion = initJson.getInt("face_version");
			int robot = initJson.getInt("robot");
			String facezip = initJson.getString("facezip");
			String welcome = initJson.getString("welcome");
			if (service_avatar != null || avatar != null) {
				DownLoad.downLoadServiceBitmap(service_avatar, handler);
				DownLoad.downLoadAvatarBitmap(avatar, handler);
			}
			if (imaddr != null) {
				String[] ddr = imaddr.split(":");
				ip = ddr[0];
				port = Integer.valueOf(ddr[1]);
			}
			ChatEntity entity = new ChatEntity();
			if (welcome != null) {
				entity.setComeMsg(true);
				entity.setContent(welcome);
				entity.setChatTime(CommonUtils.getFormattedTime(System
						.currentTimeMillis()));
			}
			if (robot == 1) {
				RequestRobot(0, "");
				chatList.add(entity);
				isClickOnServer = false;
				isOnService = false;
				chatAdapter.notifyDataSetChanged();
			} else {
				chatList.add(entity);
				chatAdapter.notifyDataSetChanged();
				isXinTiao = false;
				isClickOnServer = true;
				conn(sessionid, ip, port);
			}
			if (face == 0) {
				ivEmjo.setVisibility(View.GONE);
			} else {
				ivEmjo.setVisibility(View.VISIBLE);
				SharedPreferences sp = getSharedPreferences("face",
						MODE_PRIVATE);
				int shared = sp.getInt("face_version", 0);
				if (faceVersion != shared && faceVersion > shared) {
					get(facezip, true);
					Editor et = sp.edit();
					et.putInt("face_version", faceVersion);
					et.commit();
				} else {
					get(facezip, false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化向服务器发送http请求获取初始化信息
	 * 
	 * @param conversion
	 *            加密转换之后的信息
	 */
	private void initHttpGet(final String conversion) {
		new Thread() {
			public void run() {
				if (stopThread) {
					try {

						String path = "";
						String str = "";
						
						if (TextUtils.isEmpty(domain)) {
							path = "https://chat.weile.com/api/init?cdata=" + conversion;
						} else {
							path = "https://chat." + domain + "/api/init?cdata=" + conversion;
						}
						
						if(path.startsWith("https://")){
							str = HttpUtils.httpsGet(getApplicationContext(),path, handler);
						}else{
							str = HttpUtils.httpGet(getApplicationContext(),path, handler);
						}
						// String str = HttpUtils.httpGet(conversion, domain,handler);
						String encode = Encypt.encerpt(Conversion.FromServer(str), "DECODE", key, 0);
						Log.i("server", encode);
						if (encode != null && !encode.contains("参数错误")) {
							Message msg = handler.obtainMessage(50);
							msg.what = HTTPGETSUCCESS;
							msg.obj = encode;
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage();
							msg.what = HTTPGETFAIL;
							msg.obj = encode;
							handler.sendMessage(msg);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 建立socket连接
	 */
	protected void conn(final String sessionid, final String ip, final int port) {
		new Thread() {
			public void run() {
				if (stopThread) {
					try {
						if (socket == null) {
							socket = new Socket(ip, port);
							if (sessionid != null) {
								sendsessionid(sessionid, 4);
							}
						}
					} catch (UnknownHostException e) {
						handler.sendEmptyMessage(CONNECTIONFAIL);
					} catch (IOException e) {
					}
				}
			}
		}.start();
	}

	/**
	 * 获取表情图标
	 */
	private void get(final String path, final boolean isNew) {
		new Thread() {
			public void run() {
				if (stopThread) {
					try {
						if (isNew) {
							String zipFileString = Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "load.zip";
							String outPathString = Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "PngImage";
							File file = new File(zipFileString);
							File file1 = new File(outPathString);
							delete(file);
							delete(file1);
							if (Environment.getExternalStorageState().equals(
									Environment.MEDIA_MOUNTED)) {
								GetEmjo.LoadList(zipFileString, outPathString,
										handler, path);
							}
						} else {
							String zipFileString = Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "load.zip";
							String outPathString = Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "PngImage";
							if (Environment.getExternalStorageState().equals(
									Environment.MEDIA_MOUNTED)) {
								File file = new File(zipFileString);
								File f = new File(outPathString);
								if (!file.exists()) {
									if (!f.exists()) {
										GetEmjo.LoadList(zipFileString,
												outPathString, handler, path);
									} else {
										String path = Environment
												.getExternalStorageDirectory()
												+ "/" + "PngImage";
										e = new File(path);
										delete(e);
										GetEmjo.LoadList(zipFileString,
												outPathString, handler, path);
									}
								} else {
									if (!f.exists()) {
										GetEmjo.UnZipFloader(zipFileString,
												outPathString, handler);
									} else {
										String path = Environment
												.getExternalStorageDirectory()
												+ "/" + "PngImage";
										e = new File(path);
										delete(e);
										GetEmjo.UnZipFloader(zipFileString,
												outPathString, handler);
									}
								}

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	/**
	 * 初始化表情
	 */
	private void InitViewPager() {
		for (int i = 0; i < getPagerCount(); i++) {
			views.add(viewPagerItem(i));
			LayoutParams params = new LayoutParams(18, 18);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	/**
	 * 为控件设置适配器
	 */
	private void setAdapter() {
		try {
			chatAdapter = new ChatAdapter(this, chatList);
			lvContent.setAdapter(chatAdapter);
			ChatEntity entity = new ChatEntity();
			entity.setComeMsg(true);
			entity.setTip(true);
			entity.setTipMsg("正在初始化…请稍候");
			chatList.add(entity);
			chatAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听器
	 */
	private void setListeners() {
		try {
			mViewPager.setOnPageChangeListener(new PageChange());
			/**
			 * 点击录音完成后的回调
			 */
			mAudioRecorderButton
					.setFinishRecorderCallBack(new AudioRecorderButton.AudioFinishRecorderCallBack() {

						public void onFinish(float seconds, String filePath) {
							final ChatEntity recorder = new ChatEntity(seconds,
									filePath);
							recorder.setSuccess(false);
							recorder.setYuYin(true);
							int lmsgid = tag++;
							recorder.setTag(lmsgid);
							chatList.add(recorder);
							// 更新数据
							chatAdapter.notifyDataSetChanged();
							// 设置位置
							lvContent.smoothScrollToPositionFromTop(
									chatList.size(), 0);
							try {
								HttpUtils.uploadResoures(filePath, domain,
										lmsgid, 1, handler);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
			/**
			 * 输入框状态监听
			 */
			etChat.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					listenIsNoEmpty();
					listenIsEmpty();

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});
			/**
			 * 输入框点击监听
			 */
			etChat.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (llSheild.getVisibility() == View.GONE) {
						if (etChat.isFocusable() == false
								&& etChat.isFocusableInTouchMode() == false) {
							InputMethodManager imm = (InputMethodManager) etChat
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							etChat.setFocusable(true);
							etChat.setFocusableInTouchMode(true);
							etChat.requestFocus();
							imm.toggleSoftInput(0,
									InputMethodManager.SHOW_FORCED);
							lvContent.setSelection(lvContent.getBottom());
						}
						llGongNeng.setVisibility(View.GONE);
						llEmjo.setVisibility(View.GONE);
						lvContent.setSelection(lvContent.getBottom());
					} else {
						ToastControll.showToast("请先接通客服", MainActivity.this);
					}
				}

			});
			/**
			 * 下拉监听
			 */
			lvContent.setOnRefreshListenerHead(this);
			/**
			 * 点击listview的空白处，隐藏输入法，表情布局，功能布局
			 */
			lvContent.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						hideSoftInputView();
						llEmjo.setVisibility(View.GONE);
						llGongNeng.setVisibility(View.GONE);
					}
					return false;
				}
			});
			/**
			 * 显示和隐藏功能布局
			 */
			ivJiaHao.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (llSheild.getVisibility() == View.GONE) {
						if (llGongNeng.getVisibility() == View.VISIBLE) {
							llGongNeng.setVisibility(View.GONE);
							llEmjo.setVisibility(View.GONE);
						} else {
							hideSoftInputView();
							lvContent.smoothScrollToPositionFromTop(
									chatList.size(), 0);
							llGongNeng.setVisibility(View.VISIBLE);
							llEmjo.setVisibility(View.GONE);
							etChat.setVisibility(View.VISIBLE);
							mAudioRecorderButton.setVisibility(View.GONE);
							ivVoice.setVisibility(View.VISIBLE);
							ivKey.setVisibility(View.GONE);
						}
					} else {
						ToastControll.showToast("请先接通客服", MainActivity.this);
					}
				}

			});
			/**
			 * 满意度
			 */
			rbMYD.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (startEvaluate) {
						if (!isEvaluate) {
							evaluate();
						} else {
							ToastControll.showToast("您已经对客服进行了评价",
									MainActivity.this);
						}
					} else {
						ToastControll.showToast("您还没有接通客服，请稍后评价",
								MainActivity.this);
					}
				}
			});
			/**
			 * 切换成语音状态
			 */
			ivVoice.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (llSheild.getVisibility() == View.GONE) {
						if (etChat.getText().toString() != null) {
							content = etChat.getText().toString();
						}
						ivVoice.setVisibility(View.GONE);
						ivKey.setVisibility(View.VISIBLE);
						llEmjo.setVisibility(View.GONE);
						hideSoftInputView();
						ChatEntity chat = new ChatEntity();
						if (mAudioRecorderButton.getVisibility() == View.GONE) {
							etChat.setVisibility(View.GONE);
							etChat.setFocusable(false);
							etChat.setFocusableInTouchMode(false);
							mAudioRecorderButton.setVisibility(View.VISIBLE);
							llGongNeng.setVisibility(View.GONE);
						} else {
							etChat.setVisibility(View.VISIBLE);
							mAudioRecorderButton.setVisibility(View.GONE);
							chat.setYuYin(false);
							llGongNeng.setVisibility(View.GONE);
						}
					} else {
						ToastControll.showToast("请先接通客服", MainActivity.this);
					}
				}
			});
			/**
			 * 切换成输入框状态
			 */
			ivKey.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (llSheild.getVisibility() == View.GONE) {
						ivKey.setVisibility(View.GONE);
						ivVoice.setVisibility(View.VISIBLE);
						etChat.setVisibility(View.VISIBLE);
						mAudioRecorderButton.setVisibility(View.GONE);
						llGongNeng.setVisibility(View.GONE);
						llEmjo.setVisibility(View.GONE);
						etChat.setFocusable(true);
						etChat.setFocusableInTouchMode(true);
						etChat.requestFocus();
						if (content != null && !content.contains("#[qq_face")) {
							etChat.setText(content);
						} else {
							String str = content
									.replaceAll(
											"(\\#\\[qq_face_)(\\d{1}|\\d{2})(.png\\]\\#)",
											"");
							etChat.setText(str);
						}
						lvContent.setSelection(lvContent.getBottom());
						InputMethodManager imm = (InputMethodManager) etChat
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
					} else {
						ToastControll.showToast("请先接通客服", MainActivity.this);
					}
				}
			});
			/**
			 * 点击拍照 进入相机
			 */
			rbPZ.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// initCamera();
					Intent intent = new Intent(MainActivity.this,
							VideoActivity.class);
					startActivity(intent);
				}
			});
			/**
			 * 点击照片 进入相册
			 */
			rbZP.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					initPhone();
				}
			});
			/**
			 * 点击电话弹出dialog
			 */
			ivPhone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog dialog;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.this);
					builder.setTitle("客服电话:");
					builder.setMessage("请拨打:" + tel);
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.setPositiveButton("拨打",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String number = tel;
									Intent intent = new Intent(
											Intent.ACTION_CALL, Uri
													.parse("tel:" + number));
									startActivity(intent);
								}
							});
					dialog = builder.create();
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();
				}
			});
			/**
			 * 返回
			 */
			ivBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (isOnService && OffLine) {
							evaluateFinish();
						} else {
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			/**
			 * 发送消息
			 */
			ivFaSong.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (llSheild.getVisibility() == View.GONE) {
						if (!etChat.getText().toString().trim().equals("")) {
							send();
						} else {
							ToastControll
									.showToast("内容不能为空", MainActivity.this);
						}
					} else {
						ToastControll.showToast("请先接通客服", MainActivity.this);
					}
				}
			});
			/**
			 * 显示和隐藏表情布局
			 */
			ivEmjo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (llSheild.getVisibility() == View.GONE) {
						if (llEmjo.getVisibility() == View.VISIBLE) {
							llEmjo.setVisibility(View.GONE);
						} else {
							if (mAudioRecorderButton.getVisibility() == View.VISIBLE) {
								mAudioRecorderButton.setVisibility(View.GONE);
								etChat.setVisibility(View.VISIBLE);
								ivVoice.setVisibility(View.VISIBLE);
								ivKey.setVisibility(View.GONE);
							}
							hideSoftInputView();
							lvContent.smoothScrollToPositionFromTop(
									chatList.size(), 0);
							llEmjo.setVisibility(View.VISIBLE);
							llGongNeng.setVisibility(View.GONE);
						}
					} else {
						ToastControll.showToast("请先接通客服", MainActivity.this);
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 表情页改变时，dots效果也要跟着改变
	 */
	class PageChange implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
				mDotsLayout.getChildAt(i).setSelected(false);
			}
			mDotsLayout.getChildAt(arg0).setSelected(true);
		}

	}

	/**
	 * 根据表情数量以及GridView设置的行数和列数计算Pager数量
	 * 
	 * @return
	 */
	private int getPagerCount() {
		int count = staticFacesList.size();
		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
	}

	/**
	 * 初始化表情列表staticFacesList
	 */
	private void initStaticFaces() {
		try {
			staticFacesList = new ArrayList<String>();
			if (image != null) {
				for (int i = 0; i < image.length; i++) {
					staticFacesList.add(image[i]);
				}
				// 去掉删除图片
				staticFacesList.remove("shanchu.png");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用GridView加载每一个表情
	 * 
	 * @param position
	 * @return
	 */
	private View viewPagerItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.chat_face_gridview, null);
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		/**
		 * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns * rows － 1; 空出最后一个位置给删除图标
		 */
		List<String> subList = new ArrayList<String>();
		subList.addAll(staticFacesList
				.subList(position * (columns * rows - 1),
						(columns * rows - 1) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size() : (columns
								* rows - 1)
								* (position + 1)));
		/**
		 * 末尾添加删除图标
		 */
		subList.add("shanchu.png");
		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		// 单击表情执行的操作
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					String png = ((TextView) ((LinearLayout) view)
							.getChildAt(1)).getText().toString();
					if (!png.contains("shanchu")) {// 如果不是删除图标
						int length = etChat.getText().length();
						if (500 - length >= 4) {
							insert(getFace(png));
						} else {
							etChat.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
									length) });
							ToastControll.showToast("本次输入已达上限",
									MainActivity.this);
						}
					} else {
						delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return gridview;
	}

	/**
	 * 把输入的表情内容转成SpannableStringBuilder
	 * 
	 * @param png
	 * @return
	 */
	private String getFace(String png) {
		String tempText = null;
		int index = png.indexOf(".");
		String name = png.substring(0, index);
		for (int i = 0; i < emojiList.size(); i++) {
			if (emojiList.get(i).getFile().equals(name)) {
				tempText = emojiList.get(i).getText();
			}
		}
		// try {
		// /**
		// * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
		// * 所以这里对这个tempText值做特殊处理
		// * 格式：#[face/png/f_static_000.png]#，以方便判断当前图片是哪一个
		// */
		// tempText = "[" + text + "]";
		// String path = Environment.getExternalStorageDirectory() + "/" +
		// "PngImage" + "/" + png;
		// sb.setSpan(new ImageSpan(MainActivity.this,
		// BitmapFactory.decodeFile(path)),
		// sb.length() - tempText.length(), sb.length(),
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return tempText;
	}

	/**
	 * 删除图标执行事件
	 * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
	 */
	private void delete() {
		if (etChat.getText().length() != 0) {
			int iCursorEnd = Selection.getSelectionEnd(etChat.getText());
			int iCursorStart = Selection.getSelectionStart(etChat.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(iCursorEnd)) {
						String st = "[汉字]";
						((Editable) etChat.getText()).delete(
								iCursorEnd - st.length() - 1, iCursorEnd);
					} else {
						((Editable) etChat.getText()).delete(iCursorEnd - 1,
								iCursorEnd);
					}
				} else {
					((Editable) etChat.getText()).delete(iCursorStart,
							iCursorEnd);
				}
			}
		}
	}

	/**
	 * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则将删除整个tempText
	 **/
	private boolean isDeletePng(int cursor) {
		String st = "[汉字]";
		String content = etChat.getText().toString().substring(0, cursor);
		if (content.length() >= st.length()) {
			String checkStr = content.substring(content.length() - st.length()
					- 1, content.length());
			String regex = "\\[[\u4E00-\u9FA5]{1,2}\\]";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);
			return m.matches();
		}
		return false;
	}

	/**
	 * 向输入框里添加表情
	 */
	private void insert(String text) {
		int iCursorStart = Selection.getSelectionStart((etChat.getText()));
		int iCursorEnd = Selection.getSelectionEnd((etChat.getText()));
		if (iCursorStart != iCursorEnd) {
			((Editable) etChat.getText()).replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd((etChat.getText()));
		((Editable) etChat.getText()).insert(iCursor, text);
	}

	/**
	 * 轮播的小点
	 * 
	 * @param position
	 * @return
	 */
	private ImageView dotsItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.chat_dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	/**
	 * 发送消息
	 */
	protected void send() {
		try {
			final ChatEntity chatEntity = new ChatEntity();
			String time = CommonUtils.getFormattedTime(System
					.currentTimeMillis());
			String content = etChat.getText().toString();
			chatEntity.setChatTime(time);
			chatEntity.setContent(content);
			chatEntity.setComeMsg(false);
			chatEntity.setSuccess(false);
			int lmsgid = tag++;
			chatEntity.setTag(lmsgid);
			chatList.add(chatEntity);
			chatAdapter.notifyDataSetChanged();
			lvContent.setSelection(lvContent.getBottom());
			etChat.setText("");
			String seconds = "";
			if (socket != null && !socket.isClosed()) {
				sendServer(content, 11, lmsgid, seconds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向服务器发送数据(心跳包不要通过这个方法发送)
	 * 
	 * @param str
	 *            需要发送的字符串
	 */
	protected void sendServer(final String str, final int msg,
			final int lmsgid, final String seconds) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				if (stopThread) {
					try {
						JSONObject json = new JSONObject();
						json.put("msgid", msg);
						json.put("sid", sessionid);
						json.put("lmsgid", lmsgid);
						int time = (int) System.currentTimeMillis() / 1000;
						json.put("ts", time);
						json.put("body", str);
						Log.i("seconds", seconds);
						json.put("ext", seconds);
						String jsonStr = json.toString();
						
						byte[] buffer = change(jsonStr, msg);
						buffer[5] = 14;
						byte[] b = weileHelper.EscapeMessage(buffer);
						out = new DataOutputStream(socket.getOutputStream());
						out.write(b);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					
						try {
							isXinTiao = false;
							if (socket != null) {
								socket.close();
								socket = null;
							}
							conn(sessionid, ip, port);
						} catch (Exception e2) {
							e.printStackTrace();
						}
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			}
		};
		task.executeOnExecutor(Executors.newCachedThreadPool());
	}

	/**
	 * 向服务器发送心跳包
	 * 
	 * @param str
	 *            需要发送的字符串
	 */
	protected void sendXinTiao(final String str, final int msg) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				if (stopThread) {
					try {
						byte[] buffer = change(str, msg);
						byte[] b = weileHelper.EscapeMessage(buffer);
						out = new DataOutputStream(socket.getOutputStream());
						out.write(b);
						out.flush();
						Log.i("xintiao", "xintiao: " + i++);
					} catch (Exception e) {
						e.printStackTrace();
						connectFail = true;
						try {
							if (isLock) {
								isXinTiao = false;
								if (socket != null) {
									socket.close();
									socket = null;
								}
								conn(sessionid, ip, port);
							}
						} catch (Exception e2) {
							e.printStackTrace();
						}
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			}
		};
		task.executeOnExecutor(Executors.newCachedThreadPool());
	}

	/**
	 * 调用相册单张选择图片
	 */
	protected void initPhone() {
		try {
			Intent intent;
			if (Build.VERSION.SDK_INT < 19) {
				intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
			} else {
				intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			}
			startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调用相机拍照
	 */
	/*
	 * protected void initCamera() {
	 *//**
	 * 在启动拍照之前最好先判断一下sdcard是否可用
	 */
	/*
	 * try { String state = Environment.getExternalStorageState(); //
	 * 拿到sdcard是否可用的状态码 if (state.equals(Environment.MEDIA_MOUNTED)) { // 如果可用
	 * Uri imageUri = Uri.fromFile(new
	 * File(Environment.getExternalStorageDirectory(), "image.jpg")); //
	 * 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换 Intent intent = new
	 * Intent("android.media.action.IMAGE_CAPTURE");
	 * intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	 * startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA); } else {
	 * ToastControll.showToast("sdcard不可用", MainActivity.this); } } catch
	 * (Exception e) { ToastControll.showToast("启动相机失败", MainActivity.this); } }
	 */

	/**
	 * 输入框不为空
	 */
	protected void listenIsNoEmpty() {
		if (!TextUtils.isEmpty(etChat.getText().toString())) {
			ivJiaHao.setVisibility(View.GONE);
			ivFaSong.setVisibility(View.VISIBLE);
			etChat.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					500) });
			int count = etChat.getText().length();
			if (count >= 500) {
				ToastControll.showToast("本次输入已达上限", MainActivity.this);
			}
		}
	}

	/**
	 * 输入框为空
	 */
	protected void listenIsEmpty() {
		if (TextUtils.isEmpty(etChat.getText().toString())) {
			ivJiaHao.setVisibility(View.VISIBLE);
			ivFaSong.setVisibility(View.GONE);
		}
	}

	/**
	 * 接收AlbumActivity和CameraActivity发出的广播
	 * 
	 * @author Wangda
	 */
	class MyReciver extends BroadcastReceiver {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String imagePath = intent.getStringExtra("b");
			int type = intent.getIntExtra("type", 0);
			String videoPath = intent.getStringExtra("videoPath");
			if (CameraActivity.SEND_PHOTO.equals(action)) {
				ImageSpan imgSpan = new ImageSpan(MainActivity.this, photo);
				SpannableString spannableString = new SpannableString("icon");
				spannableString.setSpan(imgSpan, 0, 4,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ChatEntity chat = new ChatEntity();
				chat.setBitmap(spannableString);
				chat.setPhoto(photo);
				chat.setPhotoPath(photoPath);
				chat.setChatTime(CommonUtils.getFormattedTime(System
						.currentTimeMillis()));
				chat.setPhoto(true);
				chat.setSuccess(false);
				if (type == 1) {
					chat.setVideo(true);
					chat.setVideoPath(videoPath);
				} else {
					chat.setVideo(false);
				}
				int lmsgid = tag++;
				chat.setTag(lmsgid);
				chatList.add(chat);
				chatAdapter.notifyDataSetChanged();
				lvContent.setSelection(lvContent.getBottom());
				if (type == 1) {
					HttpUtils.uploadResoures(videoPath, domain, lmsgid, 2,
							handler);
				} else {
					HttpUtils.uploadResoures(imagePath, domain, lmsgid, 0,
							handler);
				}
			}
		}

	}

	/**
	 * 监测手机网络状态(接受系统发出的广播)
	 * 
	 * @author Wangda
	 * 
	 */
	public class NetworkReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
			NetworkInfo mobNetInfo = connectionManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectionManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null && networkInfo.isAvailable()) {
				if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()) {
					llNetwork.setVisibility(View.GONE);
					isXinTiao = true;
					if (!isHttpSuccess) {
						if (!isHttpFirst) {
							try {
								httpStr = Spliced.splicedInitHttp(appId,
										channelId, userId, clientVersion,
										region, ui, devicecode, version,
										url_vc, url_login, sdkversions,
										phone_info);
								String crypt = Encypt.encerpt(httpStr,
										"ENCODE", key, 0);
								String conversion = Conversion.ToServer(crypt);
								initHttpGet(conversion);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if (isClickOnServer) {
						if (OffLine) {
							if (socket == null) {
								isXinTiao = false;
								conn(sessionid, ip, port);
							}
						}
					}
				}
			} else {
				// 网络连接不可用
				isXinTiao = false;
				llNetwork.setVisibility(View.VISIBLE);
				ToastControll.showToast("网络连接不可用，请稍后重试", context);
				try {
					if (socket != null) {
						socket.close();
						socket = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * 检测是否有未发送成功的信息
	 */
	private void check() {
		if (chatList.size() > 0) {
			for (int i = 0; i < chatList.size(); i++) {
				if (!chatList.get(i).isComeMsg()) {
					if (!chatList.get(i).isSuccess()) {
						int lmsgid = chatList.get(i).getTag();
						int msgid;
						String seconds = null;
						if (chatList.get(i).isPhoto()) {
							msgid = 12;
							HttpUtils
									.uploadResoures(chatList.get(i)
											.getPhotoPath(), domain, lmsgid, 0,
											handler);
						} else if (chatList.get(i).isYuYin()) {
							msgid = 13;
							HttpUtils.uploadResoures(chatList.get(i)
									.getFilePath(), domain, lmsgid, 1, handler);
						} else if (chatList.get(i).isVideo()) {
							msgid = 14;
							HttpUtils
									.uploadResoures(chatList.get(i)
											.getVideoPath(), domain, lmsgid, 2,
											handler);
						} else {
							msgid = 11;
							seconds = "";
							String textStr = chatList.get(i).getContent();
							sendServer(textStr, msgid, lmsgid, seconds);
						}
					}
				}
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		lvContent = (ContentListView) findViewById(R.id.lv_content);
		llNetwork = (LinearLayout) findViewById(R.id.ll_network_unuse);
		llSheild = (LinearLayout) findViewById(R.id.ll_bottom_shield);
		etChat = (EditText) findViewById(R.id.et_chat);
		llGongNeng = (LinearLayout) findViewById(R.id.ll_gongneng);
		ivJiaHao = (ImageView) findViewById(R.id.iv_jiahao);
		ivFaSong = (ImageView) findViewById(R.id.iv_fasong);
		rbZP = (RadioButton) findViewById(R.id.rb_xiangCe);
		rbPZ = (RadioButton) findViewById(R.id.rb_paiZhao);
		rbMYD = (RadioButton) findViewById(R.id.rb_manYiDu);
		ivPhone = (ImageView) findViewById(R.id.iv_phone);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivEmjo = (ImageView) findViewById(R.id.iv_biaoqing);
		llEmjo = (LinearLayout) findViewById(R.id.ll_biaoqing);
		mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
		ivVoice = (ImageView) findViewById(R.id.iv_voice);
		ivKey = (ImageView) findViewById(R.id.iv_keyboard);
		// 表情布局
		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		// 表情下小圆点
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
	}

	/**
	 * 隐藏输入框
	 */
	public void hideSoftInputView() {
		try {
			InputMethodManager manager = ((InputMethodManager) this
					.getSystemService(Activity.INPUT_METHOD_SERVICE));
			if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
				if (getCurrentFocus() != null)
					manager.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 封包
	 * 
	 * @param str
	 *            需要发送字符串
	 * @param msgId
	 *            消息ID
	 * @return 返回封包完成的byte[]
	 */
	public byte[] change(String str, int msgId) {
		int len = str.getBytes().length + 8;
		byte[] buffer = new byte[8];
		buffer[0] = (byte) (len & 0xff);
		buffer[1] = (byte) ((len >> 8) & 0xff);
		buffer[2] = (byte) (msgId & 0xff);
		buffer[3] = (byte) ((msgId >> 8) & 0xff);
		byte[] strByte = str.getBytes();
		byte[] all = new byte[buffer.length + strByte.length];
		System.arraycopy(buffer, 0, all, 0, buffer.length);
		System.arraycopy(strByte, 0, all, buffer.length, strByte.length);
		return all;
	}

	// 消息部分
	private byte[] msgByte;
	// 长度不够，需要拼接的部分
	private byte[] portion;
	// 每条消息总长度
	private int len;
	// 消息id
	private int msgId;

	/**
	 * 解包
	 * 
	 * @param buffer
	 *            需要解包的byte[]
	 */
	public void relieve(byte[] buffer) {
		try {
			if (!little) {
				len = ((buffer[0] & 0xff) | ((buffer[1] & 0xff) << 8));
			} else {
				byte[] joint = new byte[buffer.length + portion.length];
				System.arraycopy(portion, 0, joint, 0, portion.length);
				System.arraycopy(buffer, 0, joint, portion.length,
						buffer.length);
				if (joint.length >= len) {
					little = false;
					relieve(joint);
					return;
				} else {
					little = true;
					portion = joint;
					return;
				}
			}
			if (len > buffer.length) {
				little = true;
				portion = buffer;
				return;
			}
			byte[] all = new byte[len];
			System.arraycopy(buffer, 0, all, 0, len);
			if (all != null) {
				byte[] unpack = weileHelper.UnEscapeMessage(all);
				if (unpack != null) {
					msgId = ((unpack[2] & 0xff) | ((unpack[3] & 0xff) << 8));
					msgByte = new byte[unpack.length - 8];
					System.arraycopy(unpack, 8, msgByte, 0, unpack.length - 8);
					if (msgByte != null) {
						String str = new String(msgByte);
						if (msgId == 8) {
							Message msg = handler.obtainMessage(50);
							msg.what = SENDSUCCESS;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 1) {
							Message msg = handler.obtainMessage(50);
							msg.what = RECEIVESUCCESS;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 11) {
							Message msg = handler.obtainMessage(50);
							msg.what = CONTEXTMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 2) {
							Message msg = handler.obtainMessage(50);
							msg.what = SYSTEMMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 3) {
							Message msg = handler.obtainMessage(50);
							msg.what = TIPSMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 4) {
							Message msg = handler.obtainMessage(50);
							msg.what = SESSIONIDSUCCESS;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 5) {
							Message msg = handler.obtainMessage(50);
							msg.what = ONSERVER;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 6) {
							Message msg = handler.obtainMessage(50);
							msg.what = USEROFFLINE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 7) {
							Message msg = handler.obtainMessage(50);
							msg.what = QUEUEMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 9) {
							Message msg = handler.obtainMessage(50);
							msg.what = COMMANDMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 12) {
							Message msg = handler.obtainMessage(50);
							msg.what = PICMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 13) {
							Message msg = handler.obtainMessage(50);
							msg.what = VOICEMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 14) {
							Message msg = handler.obtainMessage(50);
							msg.what = VIDEOMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						} else if (msgId == 15) {
							Message msg = handler.obtainMessage(50);
							msg.what = POSITIONMESSAGE;
							msg.obj = str;
							handler.sendMessage(msg);
						}
					}
				}
			}
			if (buffer.length > len) {
				byte[] surplus = new byte[buffer.length - len];
				System.arraycopy(buffer, len, surplus, 0, buffer.length - len);
				if (surplus != null) {
					relieve(surplus);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收相册和相机返回的照片及路径
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA
					&& resultCode == RESULT_OK) {
				String indexPath = Environment.getExternalStorageDirectory()
						+ "/image.jpg";
				photo = ImageUtil.getImage(indexPath, max);
				photoPath = ImageUtil.saveImageToGallery(MainActivity.this,
						BitmapFactory.decodeFile(indexPath));
				Intent intent = new Intent(MainActivity.this,
						CameraActivity.class);
				intent.putExtra("path", photoPath);
				startActivity(intent);
			}
			if (requestCode == REQUEST_CODE_CAPTURE_IMAGES && data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					Cursor cursor = getContentResolver().query(uri, null, null,
							null, null);
					if (cursor != null) {
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex("_data");
						String name = cursor.getString(columnIndex);
						cursor.close();
						cursor = null;

						if (name == null || name.equals("null")) {
							ToastControll.showToast("找不到图片", this);
							return;
						}
						photo = ImageUtil.getImage(name, max);
						photoPath = ImageUtil.saveImageToGallery(
								MainActivity.this,
								BitmapFactory.decodeFile(name));
						Intent intent = new Intent(MainActivity.this,
								AlbumActivity.class);
						intent.putExtra("path", photoPath);
						startActivity(intent);
					} else {
						File file = new File(uri.getPath());
						if (!file.exists()) {
							ToastControll.showToast("找不到图片", this);
							return;

						}
						photo = ImageUtil.getImage(file.getAbsolutePath(), max);
						photoPath = ImageUtil.saveImageToGallery(
								MainActivity.this, photo);
						Intent intent = new Intent(MainActivity.this,
								AlbumActivity.class);
						intent.putExtra("path", photoPath);
						startActivity(intent);
					}
				}
			}
		} catch (Exception e) {
			ToastControll.showToast("找不到图片", this);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		MediaPlayerManager.pause();
	}

	@Override
	protected void onRestart() {
		try {
			isLock = true;
			if (isClickOnServer) {
				if (socket == null) {
					isXinTiao = false;
					conn(sessionid, ip, port);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		try {
			isLock = false;
			hideSoftInputView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		try {
			isLoading = false;
			stopThread = false;
			tag = 1;
			isLock = false;
			isXinTiao = false;
			isClickOnServer = false;
			isOnService = false;
			isReading = false;
			isHttpFirst = true;
			isHttpSuccess = false;
			ToastControll.cancelToast();
			unregisterReceiver(receiver1);
			unregisterReceiver(receiver2);
			String dir = Environment.getExternalStorageDirectory()
					+ "/ldm_voice";
			delete(new File(dir));
			String path = Environment.getExternalStorageDirectory() + "/"
					+ "PngImage";
			delete(new File(path));
			String path1 = Environment.getExternalStorageDirectory() + "/"
					+ "ldm_pic";
			delete(new File(path1));
			String path2 = Environment.getExternalStorageDirectory() + "/"
					+ "ldm_video";
			delete(new File(path2));
			String image = Environment.getExternalStorageDirectory()
					+ "/image.jpg";
			delete(new File(image));
			if (socket != null) {
				socket.close();
				socket = null;
			}
			MediaPlayerManager.release();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	/**
	 * 刷新完成后更新列表
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
					String history = (String) msg.obj;
					String decode = Encypt.encerpt(
							Conversion.FromServer(history), "DECODE", key, 0);
					Log.i("history", decode);
					JSONObject json = new JSONObject(decode);
					JSONArray array = json.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						JSONObject temp = (JSONObject) array.get(i);
						History his = new History();
						lastId = temp.getInt("id");
						his.setId(lastId);
						int dir = Integer.valueOf(temp.getString("dir"));
						his.setDir(temp.getString("dir"));
						int msgid = Integer.valueOf(temp.getString("msgid"));
						his.setMsgid(temp.getString("msgid"));
						String time = CommonUtils.getFormattedTime(Long
								.valueOf(temp.getString("sendtime")) * 1000);
						his.setSendtime(time);
						String body = temp.getString("body");
						his.setBody(body);
						String nickname = temp.getString("nickname");
						his.setNickname(nickname);
						String avatarHistory = temp.getString("avatar");
						ChatEntity chatEntity = new ChatEntity();
						chatEntity.setHistory(true);
						chatEntity.setChatTime(time);
						if (avatarHistory != null && avatarHistory != "") {
							DownLoad.downLoadHistoryServiceBitmap(
									avatarHistory, handler, array.length() - 1
											- i);
						}
						if (dir == 0) {
							chatEntity.setComeMsg(false);
						} else {
							chatEntity.setComeMsg(true);
						}
						if (msgid == 12) {
							chatEntity.setContent("【 图片消息 】");
							chatEntity.setSuccess(true);
							int index = tag++;
							chatEntity.setTag(index);
							DownLoad.downLoadBitmap(body, handler, index);
						} else if (msgid == 13) {
							chatEntity.setContent("【 语音消息 】");
							chatEntity.setSuccess(true);
							int voiceindex = tag++;
							chatEntity.setTag(voiceindex);
							DownLoad.downLoadVoice(body, handler, voiceindex);
						} else if (msgid == 14) {
							chatEntity.setContent("【 视频消息 】");
							chatEntity.setSuccess(true);
							int videoindex = tag++;
							chatEntity.setTag(videoindex);
							DownLoad.downLoadVideo(body, handler, videoindex);
						} else {
							chatEntity.setContent(body);
							chatEntity.setSuccess(true);
						}
						chatEntity.setHistory(his);
						chatList.add(0, chatEntity);
					}
					lvContent.onRefreshCompleteHeader();
					chatAdapter.notifyDataSetChanged();
					lvContent.setSelection(array.length());
				} catch (Exception e) {
					lvContent.onRefreshCompleteHeader();
					chatAdapter.notifyDataSetChanged();
				}
				break;
			case 1:
				lvContent.onRefreshCompleteHeader();
				chatAdapter.notifyDataSetChanged();
				break;
			case 2:
				lvContent.onRefreshCompleteHeader();
				chatAdapter.notifyDataSetChanged();
				break;
			}

		}
	};

	/**
	 * 拉下刷新时
	 */
	@Override
	public void onRefresh() {
		new Thread() {
			@Override
			public void run() {
				if (stopThread) {
					try {
						if (isOnService) {
							if (lastId == 0) {
								Message msg = mHandler.obtainMessage(1);
								mHandler.sendMessage(msg);
							} else {
								Log.i("history", "" + lastId);
								String spliced = Spliced.splicedHistory(
										version, sessionid, lastId, size);
								Log.i("spliced", spliced);
								String encode = Encypt.encerpt(spliced,
										"ENCODE", key, 0);
								
								
								String path = "";
								String result = "";
								
								if (TextUtils.isEmpty(domain)) {
									path = "https://chat.weile.com/api/history?cdata=" + Conversion.ToServer(encode);
								} else {
									path = "https://chat." + domain + "/api/history?cdata=" + Conversion.ToServer(encode);
								}
								
								if(path.startsWith("https://")){
									result = HttpUtils.getHistoryHttps(getApplicationContext(),path, handler, mHandler);
								}else{
									result = HttpUtils.getHistoryHttp(getApplicationContext(),path, handler, mHandler);
								}
								
//								String result = HttpUtils.getHistory(Conversion.ToServer(encode), domain,handler, mHandler);
								Message msg = mHandler.obtainMessage(0);
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						} else {
							Message msg = mHandler.obtainMessage(1);
							mHandler.sendMessage(msg);
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(CONNECTIONFAIL);
					}
				}
			}
		}.start();
	}

	/**
	 * 点击手机的返回键
	 */
	@Override
	public void onBackPressed() {
		try {
			if (llEmjo.getVisibility() == View.VISIBLE
					|| llGongNeng.getVisibility() == View.VISIBLE) {
				hideSoftInputView();
				llEmjo.setVisibility(View.GONE);
				llGongNeng.setVisibility(View.GONE);
			}
			if (isOnService && OffLine) {
				evaluateFinish();
			} else {
				ToastControll.cancelToast();
				tag = 1;
				isXinTiao = false;
				isClickOnServer = false;
				isOnService = false;
				isHttpFirst = true;
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测当前的网络状态(WIFI、3G/2G)
	 * 
	 * @param context
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			// 当前网络是连接的
			if (info.getState() == NetworkInfo.State.CONNECTED) {
			}
			{
				// 当前所连接的网络可用
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				file.delete();
				return;
			}
			for (int i = 0; i < listFiles.length; i++) {
				delete(listFiles[i]);
			}
			file.delete();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 评价dialog
	 */
	public static TextView tvTitle;

	private void evaluate() {
		final Dialog dialog = new Dialog(MainActivity.this, R.style.MyDialog);
		View view = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.chat_evaluate1, null);
		dialog.setContentView(view);
		EvaluateView ev = (EvaluateView) view.findViewById(R.id.evaluate_view);
		Button btCancel = (Button) view.findViewById(R.id.bt_cancel);
		Button btCommit = (Button) view.findViewById(R.id.bt_commit);
		final EditText et = (EditText) view.findViewById(R.id.et_yijian);
		tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btCommit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (EvaluateView.isEvaluate) {
					try {
						dialog.dismiss();
						int count = EvaluateView.count;
						isEvaluate = true;
						String comment = et.getText().toString().trim();
						String spliced = Spliced.splicedEvaluete(sessionid,
								version, count, comment);
						String encode = Encypt.encerpt(spliced, "ENCODE", key,
								0);
						String conversion = Conversion.ToServer(encode);
						HttpEvaluate(conversion, false);
					} catch (Exception e) {
					}
				} else {
					ToastControll.showToast("您还没有评价！", MainActivity.this);
				}
			}
		});

	}

	/**
	 * 用户主动退出时评价dialog
	 */
	private void evaluateFinish() {
		final Dialog dialog = new Dialog(MainActivity.this, R.style.MyDialog);
		View view = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.chat_evaluate2, null);
		dialog.setContentView(view);
		EvaluateViewFinish ev = (EvaluateViewFinish) view
				.findViewById(R.id.evaluate_view_finish);
		Button btCancel = (Button) view.findViewById(R.id.bt_cancel);
		Button btCommit = (Button) view.findViewById(R.id.bt_commit);
		final EditText et = (EditText) view.findViewById(R.id.et_yijian);
		tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		setEvaluateStr("非常满意");
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
		btCommit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dialog.dismiss();
					int count = EvaluateViewFinish.count;
					String comment = et.getText().toString().trim();
					String spliced = Spliced.splicedEvaluete(sessionid,
							version, count, comment);
					String encode = Encypt.encerpt(spliced, "ENCODE", key, 0);
					String conversion = Conversion.ToServer(encode);
					HttpEvaluate(conversion, true);
				} catch (Exception e) {
				}
			}
		});

	}

	public static void setEvaluateStr(String str) {
		tvTitle.setText(str);
	}

	/**
	 * 退出dialog
	 */
	private void backDialog() {
		final Dialog dialog = new Dialog(MainActivity.this, R.style.MyDialog);
		View view = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.chat_back_dialog, null);
		dialog.setContentView(view);
		Button btCancel = (Button) view.findViewById(R.id.bt_cancel);
		Button btCommit = (Button) view.findViewById(R.id.bt_commit);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btCommit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					tag = 1;
					isXinTiao = false;
					isClickOnServer = false;
					isOnService = false;
					if (socket != null) {
						socket.close();
						socket = null;
					}
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 评价请求
	 * 
	 * @param conversion
	 *            加密后的参数
	 */
	private void HttpEvaluate(final String conversion, final boolean isFinish) {
		new Thread() {
			public void run() {
				if (stopThread) {
					try {
						
						
						String path = "";
						String str = "";
						
						if (TextUtils.isEmpty(domain)) {
							path = "https://chat.weile.com/api/appraise?cdata=" + conversion;
						} else {
							path = "https://chat." + domain + "/api/appraise?cdata=" + conversion;
						}
						
						if(path.startsWith("https://")){
							str = HttpUtils.EvaluateHttps(getApplicationContext(),path, handler);
						}else{
							str = HttpUtils.EvaluateHttp(getApplicationContext(),path, handler);
						}
						
//						String str = HttpUtils.Evaluate(conversion, domain,handler);
						if (isFinish) {
							finish();
						} else {
							String encode = Encypt.encerpt(
									Conversion.FromServer(str), "DECODE", key,
									0);
							if (encode != null && !encode.contains("参数错误")) {
								Message msg = handler.obtainMessage(50);
								msg.what = EVALUATESUCCESS;
								msg.obj = encode;
								handler.sendMessage(msg);
							}
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(CONNECTIONFAIL);
						isEvaluate = false;
					}
				}
			}
		}.start();
	}

	/**
	 * 机器人请求
	 * 
	 * @param msgid
	 *            消息id
	 * @param body
	 *            请求内容
	 */
	public void RequestRobot(final int msgid, final String body) {
		new Thread() {
			public void run() {
				if (stopThread) {
					try {
						String spliced = Spliced.splicedRobot(version, msgid,
								body, sessionid);
						Log.i("wangda", spliced);
						String encode = Encypt.encerpt(spliced, "ENCODE", key,
								0);
						String conversion = Conversion.ToServer(encode);
						
						
						String path = "";
						String result = "";
						
						if (TextUtils.isEmpty(domain)) {
							path = "http://robot.chat.weile.com/api/query?cdata=" + conversion;
						} else {
							path = "http://robot.chat." + domain + "/api/query?cdata=" + conversion;
						}

						if(path.startsWith("https://")){
							result = HttpUtils.httpRobotHttps(getApplicationContext(),path, msgid,handler);
						}else{
							result = HttpUtils.httpRobotHttp(getApplicationContext(),path,msgid, handler);
						}
						
//						String result = HttpUtils.httpRobot(conversion, domain,msgid, handler);
						String decode = Encypt.encerpt(Conversion.FromServer(result),"DECODE", key, 0);
						if (decode != null) {
							Message msg = handler.obtainMessage();
							msg.what = ROBOTSUCCESS;
							msg.obj = decode;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(CONNECTIONFAIL);
						for (int l = 0; l < chatList.size(); l++) {
							if (chatList.get(l).isLoading()) {
								chatList.remove(l);
								chatAdapter.notifyDataSetChanged();
								isLoading = false;
								l = l - 1;
							}
						}
					}
				}
			}
		}.start();
	}

	/**
	 * 销毁机器人
	 */
	private boolean welcome = true;

	public void destroyRobot() {

	}

	/**
	 * 接通客服后 获取客服的头像和名字
	 * 
	 * @param str
	 */
	public void getServerInfo(final String str) {
		new Thread() {
			public void run() {
				if (stopThread) {
					try {
						
						
						String path = "";
						String result = "";
						
						if (TextUtils.isEmpty(domain)) {
							path = "https://chat.weile.com/api/serviceInfo?sid=" + str;
						} else {
							path = "https://chat." + domain + "/api/serviceInfo?sid=" + str;
						}
						
						if(path.startsWith("https://")){
							result = HttpUtils.getServerInfoHttps(getApplicationContext(),path,handler);
						}else{
							result = HttpUtils.getServerInfoHttp(getApplicationContext(),path, handler);
						}
						
//						String result = HttpUtils.getServerInfo(str, domain,handler);
						Message msg = handler.obtainMessage();
						msg.what = SERVERSESSIONIDSUCCESS;
						msg.obj = result;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 读取amr（语音文件）文件的播放时长
	 * 
	 * @param voicePath
	 *            amr文件路径
	 * @return float类型的秒数
	 */
	public float getVoiceLength(String voicePath) {
		double duration = 0;
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(voicePath);
			player.prepare();
			duration = player.getDuration();
			player.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (float) duration;
	}

}
