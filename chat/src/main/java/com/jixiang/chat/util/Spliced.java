package com.jixiang.chat.util;
/**
 * 拼接需要传递的参数
 * @author Wangda
 */
public class Spliced{
	public static String splicedInitHttp(int appId,int channelId,int userId,String clientVersion,int region,String ui,String devicecode,String version,String url_vc,String url_login,int sdkversions,String phone_info){
		String str = "version="+version+"&app_id="+appId+"&channel_id="
					 +channelId+"&user_id="+userId+"&region="+region+"&device_code="
					 +devicecode+"&client_version="+clientVersion+"&ui="+ui+"&url_vc="
					 +url_vc+"&url_login="+url_login+"&sdkversions="+sdkversions
					 +"&phone_info="+phone_info;
		return str;
	}
	
	public static String splicedRobot(String version,int msgid,String body,String sid){
		String str = "version="+version+"&msgid="+msgid+"&body="+body+"&sid="+sid;
		return str;
	}
	
	public static String splicedEvaluete(String sessionid,String version,int score,String comment){
		String str;
		if(comment == null){
			str = "sid="+sessionid+"&version="+version+"&score="+score;
		}else{
			str = "sid="+sessionid+"&version="+version+"&score="+score+"&comment="+comment;
		}
		return str;
	}
	
	public static String splicedQiniu(String version,int user_id,String ext, int type){
		String str = null;
		if(type == 0){
			str = "version="+version+"&user_id="+user_id+"&path=images"+"&ext="+ext;
		}else if(type == 1){
			str = "version="+version+"&user_id="+user_id+"&path=sounds"+"&ext="+ext;
		}else if(type == 2){
			str = "version="+version+"&user_id="+user_id+"&path=video"+"&ext="+ext;
		}
		return str;
	}
	
	public static String splicedHistory(String version,String sid,int last_id, int size){
		String str = "version="+version+"&sid="+sid+"&last_id="+last_id+"&size="+size;
		return str;
	}
}
