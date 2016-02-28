package com.yilvtzj.test;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

import com.yilvtzj.pojo.DongTai;
import com.yilvtzj.util.JSONHelper;

public class Test extends AndroidTestCase {
	public void test() {
		String JSON = "{'name':'dongtai','account2':{'nickname':'昵称'}}";
		try {
			DongTai dongTai = JSONHelper.JSONToBean(new JSONObject(JSON), DongTai.class);
			System.out.println(">>>>>>>>>>>" + dongTai);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
