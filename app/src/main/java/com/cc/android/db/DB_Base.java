package com.cc.android.db;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import org.apache.commons.codec.binary.Base64;

import java.io.*;

public class DB_Base {

	public SharedPreferences settings;
	private Context context;
	private String db_name = "DB_BASE";

	public DB_Base(Context context) {
		this.context = context;
	}

	public void setName(String db_name) {
		this.db_name = db_name;
	}

	public String getName() {
		return db_name;
	}

	public SharedPreferences getSettings() {
		if (settings == null && context != null) {
			settings = context.getSharedPreferences(db_name, Application.MODE_PRIVATE);
		}
		return settings;
	}

	public void setSaveString(String key, String value) {
		if (settings == null) {
			settings = getSettings();
		}
		settings.edit().putString(key, value).commit();
	}

	public String getSaveString(String key, String defaultStr) {
		if (settings == null) {
			settings = getSettings();
		}
		if (!settings.contains(key)) {
			return defaultStr;
		}

		return settings.getString(key, defaultStr);
	}

	public void setSaveBoolean(String key, boolean value) {
		if (settings == null) {
			settings = getSettings();
		}
		settings.edit().putBoolean(key, value).commit();
	}

	public boolean getSaveBoolean(String key, boolean defaultStr) {
		if (settings == null) {
			settings = getSettings();
		}
		if (!settings.contains(key)) {
			return defaultStr;
		}

		return settings.getBoolean(key, defaultStr);
	}

	public void setSaveLong(String key, long value) {
		if (settings == null) {
			settings = getSettings();
		}
		settings.edit().putLong(key, value).commit();
	}

	public Long getSaveLong(String key, long defaultStr) {
		if (settings == null) {
			settings = getSettings();
		}
		if (!settings.contains(key)) {
			return defaultStr;
		}

		return settings.getLong(key, defaultStr);
	}

	public void setSaveMode(String key, Object value) {
		String valueStr = null;
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(value);
			// 将字节流编码成base64的字符窜
			valueStr = new String(Base64.encodeBase64(baos.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setSaveString(key, valueStr);
	}

	public Object getSaveMode(String key, Object defaultValue) {
		Object model = null;
		String modelStr = getSaveString(key, "");
		// 读取字节
		byte[] base64 = Base64.decodeBase64(modelStr.getBytes());
		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				model = bis.readObject();
				return model;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
}
