package com.yilvtzj.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {
	/**
	 * 将JSON对象变成实体对象，实体对象的属性支持java基本数据类型，支持对象嵌套，仅支持List集合
	 * 
	 * @param json
	 * @param cls
	 * @return
	 * @throws JSONException
	 */
	public static <T> T JSONToBean(JSONObject json, Class<?> cls) throws JSONException {
		if (json == null) {
			return null;
		}
		Object obj = null;
		try {
			obj = cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String name = keys.next();
			Field field = Reflections.getAccessibleField(obj, name);
			if (field == null) {
				continue;
			}
			if (isBaseType(field)) {
				Object value = json.get(name);
				if (value != null && !"null".equals(value.toString())) {
					Reflections.invokeSetter(obj, name, value);
				}
			} else if (isListType(field)) {
				JSONArray values = (JSONArray) json.get(name);
				List<String> list = new ArrayList<>();
				for (int i = 0, l = values.length(); i < l; i++) {
					String str = values.get(i).toString();
					list.add(str);
				}
				Reflections.invokeSetter(obj, name, list);
			} else {
				JSONObject json2 = (JSONObject) json.get(name);
				Object obj2 = JSONToBean(json2, field.getType());
				Reflections.invokeSetter(obj, name, obj2);
			}

		}
		if (obj == null) {
			return null;
		}
		return (T) obj;
	}

	private static boolean isListType(Field field) {
		int a = field.getType().getSimpleName().indexOf("List");
		if (a == -1) {
			return false;
		}
		return true;
	}

	private static boolean isBaseType(Field field) {
		boolean flag = false;
		String type = field.getType().getSimpleName();
		if (type.indexOf("int") != -1 || type.indexOf("Integer") != -1) {
			flag = true;
		} else if (type.indexOf("String") != -1) {
			flag = true;
		} else if (type.indexOf("char") != -1 || type.indexOf("Character") != -1) {
			flag = true;
		} else if (type.indexOf("double") != -1 || type.indexOf("Double") != -1) {
			flag = true;
		} else if (type.indexOf("float") != -1 || type.indexOf("Float") != -1) {
			flag = true;
		} else if (type.indexOf("long") != -1 || type.indexOf("Long") != -1) {
			flag = true;
		} else if (type.indexOf("short") != -1 || type.indexOf("Short") != -1) {
			flag = true;
		}
		return flag;
	}
}