/*package com.jixiang.chat.util;

import java.text.DecimalFormat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
*//**
 * 获取经纬度
 * @author Wangda
 *//*
public class GPS {
	private static LocationManager lm;
	private static String location;
	
	public static String gps(Context context) {
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (isOpen(context)) {
			location = getGPSConfi(context);
		} else {
			openGPS(context);
			location = getGPSConfi(context);
		}
		return location;
	}

	*//**
	 * 判断手机GPS是否开启
	 * 
	 * @param mainActivity
	 * @return
	 *//*
	public static boolean isOpen(Context context) {
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位,定位级别到街
		boolean gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或者移动网络确定位置
		boolean network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}

	*//**
	 * 开启手机GPS
	 *//*
	public static void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvide");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			// 使用PendingIntent发送广播告诉手机去开启GPS功能
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (PendingIntent.CanceledException e) {
			e.printStackTrace();
		}
	}

	*//**
	 * GPS功能已经打开-->根据GPS去获取经纬度
	 *//*
	public static String getGPSConfi(Context context) {
		double latitude, longitude;
		Location location;
		if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, ll);
			location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, ll);
			location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}

		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		} else {
			latitude = 0;
			longitude = 0;
		}
		DecimalFormat df = new DecimalFormat(".000000");
		latitude = Double.valueOf(df.format(latitude));
		longitude = Double.valueOf(df.format(longitude));
		df.format(longitude);
		return latitude+","+longitude;
	}

	public static LocationListener ll = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onLocationChanged(Location location) {

		}
	};
}
*/