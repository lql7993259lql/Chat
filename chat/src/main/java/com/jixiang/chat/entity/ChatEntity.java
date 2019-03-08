package com.jixiang.chat.entity;

import java.util.List;

import com.jixiang.chat.adapter.RobotAdapter;

import android.graphics.Bitmap;
import android.text.SpannableString;
/**
 * 聊天内容实体类
 * @author Wangda
 */
public class ChatEntity{
	private int userImage;
	private String content;
	private String chatTime;
	private SpannableString bitmap;
	private Bitmap photo;
	private float time;// 时间长度
	private String filePath;// 语音文件路径
	private String photoPath;//图片路径
	private String videoPath;//视频路径
	private String photoUrl;
	private String yuyinUrl;
	private String tipMsg;
	private String serverName;
	private Bitmap serverAvatar;
	private String uploadPath;
	private boolean isComeMsg;
	private boolean isYuYin;
	private boolean isPhoto;
	private boolean isVideo;
	private boolean is11;
	private boolean is12;
	private boolean is13;
	private boolean is14;
	private boolean is15;
	private boolean is16;
	private boolean is17;
	private boolean is18;
	private boolean is19;
	private boolean is20;
	private boolean isSuccess = true;
	private boolean isTip;
	private boolean isRobot = false;
	private boolean isFirst = false;
	private List<Robot> robotList;
	private RobotAdapter adapter;
	private boolean isHistory = false;
	private History history;
	private int tag = 0;
	private boolean isLoading = false;
	
	
	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public boolean isVideo() {
		return isVideo;
	}

	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Bitmap getServerAvatar() {
		return serverAvatar;
	}

	public void setServerAvatar(Bitmap serverAvatar) {
		this.serverAvatar = serverAvatar;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	public boolean isHistory() {
		return isHistory;
	}

	public void setHistory(boolean isHistory) {
		this.isHistory = isHistory;
	}

	public RobotAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(RobotAdapter adapter) {
		this.adapter = adapter;
	}

	public List<Robot> getRobotList() {
		return robotList;
	}

	public void setRobotList(List<Robot> robotList) {
		this.robotList = robotList;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isRobot() {
		return isRobot;
	}

	public void setRobot(boolean isRobot) {
		this.isRobot = isRobot;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}


	public String getTipMsg() {
		return tipMsg;
	}

	public void setTipMsg(String tipMsg) {
		this.tipMsg = tipMsg;
	}

	public boolean isTip() {
		return isTip;
	}

	public void setTip(boolean isTip) {
		this.isTip = isTip;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getYuyinUrl() {
		return yuyinUrl;
	}

	public void setYuyinUrl(String yuyinUrl) {
		this.yuyinUrl = yuyinUrl;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public boolean isIs12() {
		return is12;
	}

	public void setIs12(boolean is12) {
		this.is12 = is12;
	}

	public boolean isIs13() {
		return is13;
	}

	public void setIs13(boolean is13) {
		this.is13 = is13;
	}

	public boolean isIs14() {
		return is14;
	}

	public void setIs14(boolean is14) {
		this.is14 = is14;
	}

	public boolean isIs15() {
		return is15;
	}

	public void setIs15(boolean is15) {
		this.is15 = is15;
	}

	public boolean isIs16() {
		return is16;
	}

	public void setIs16(boolean is16) {
		this.is16 = is16;
	}

	public boolean isIs17() {
		return is17;
	}

	public void setIs17(boolean is17) {
		this.is17 = is17;
	}

	public boolean isIs18() {
		return is18;
	}

	public void setIs18(boolean is18) {
		this.is18 = is18;
	}

	public boolean isIs19() {
		return is19;
	}

	public void setIs19(boolean is19) {
		this.is19 = is19;
	}

	public boolean isIs20() {
		return is20;
	}

	public void setIs20(boolean is20) {
		this.is20 = is20;
	}

	public boolean isIs11() {
		return is11;
	}

	public void setIs11(boolean is11) {
		this.is11 = is11;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public boolean isPhoto() {
		return isPhoto;
	}

	public void setPhoto(boolean isPhoto) {
		this.isPhoto = isPhoto;
	}

	public ChatEntity() {
		super();
	}

	public ChatEntity(float time, String filePath) {
		super();
		this.time = time;
		this.filePath = filePath;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public SpannableString getBitmap() {
		return bitmap;
	}

	public void setBitmap(SpannableString bitmap) {
		this.bitmap = bitmap;
	}

	public int getUserImage() {
		return userImage;
	}

	public void setUserImage(int userImage) {
		this.userImage = userImage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getChatTime() {
		return chatTime;
	}

	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}

	public boolean isComeMsg() {
		return isComeMsg;
	}

	public void setComeMsg(boolean isComeMsg) {
		this.isComeMsg = isComeMsg;
	}

	public boolean isYuYin() {
		return isYuYin;
	}

	public void setYuYin(boolean isYuYin) {
		this.isYuYin = isYuYin;
	}
	
	@Override
	public String toString() {
		return "ChatEntity [userImage=" + userImage + ", content=" + content + ", chatTime=" + chatTime + ", bitmap="
				+ bitmap + ", time=" + time + ", filePath=" + filePath + ", isComeMsg=" + isComeMsg + ", isYuYin="
				+ isYuYin + ", isPhoto=" + isPhoto + "]";
	}
	
	

}
