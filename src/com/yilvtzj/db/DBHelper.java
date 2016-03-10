package com.yilvtzj.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DBHelper继承了SQLiteOpenHelper，作为维护和管理数据库的基类
 * 
 */
class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "yilv.db";
	private static final int DB_VERSION = 1;
	private static DBHelper dbHelper;

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public synchronized static DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			return dbHelper = new DBHelper(context);
		}
		return dbHelper;
	}

	// 数据第一次创建的时候会调用onCreate,表的列名要和实体中的属性名称一样
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE IF NOT EXISTS " + DBManager.MESSAGEITEM
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR, fromWho VARCHAR)");
	}

	// 数据库第一次创建时onCreate方法会被调用，我们可以执行创建表的语句，当系统发现版本变化之后，会调用onUpgrade方法，我们可以执行修改表结构等语句
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 在表info中增加一列other
		// db.execSQL("ALTER TABLE info ADD COLUMN other STRING");
		Log.i("WIRELESSQA", "update sqlite " + oldVersion + "---->" + newVersion);
	}

}
