package com.yilvtzj.test;

import java.util.UUID;

import android.test.AndroidTestCase;

public class Test extends AndroidTestCase {
	public void test() {
		System.out.println(UUID.randomUUID().toString().replace("-", ""));
		System.out.println(UUID.randomUUID().toString().replace("-", "").length());
	}

}
