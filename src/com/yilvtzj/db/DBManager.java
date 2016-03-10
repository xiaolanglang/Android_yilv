package com.yilvtzj.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yilvtzj.util.Reflections;

/**
 * DBManager是建立在DBHelper之上，封装了常用的业务方法
 * 
 */
public class DBManager {

	public static final String MESSAGEITEM = "messageitem";

	private DBHelper helper;
	private SQLiteDatabase db;
	private static final int pageSize = 20;

	public static final String TAG = "SQLite";

	public DBManager(Context context) {
		helper = DBHelper.getInstance(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 插入数据，id自增长
	 * 
	 * @param table
	 * @param cv
	 */
	public void insert(String table, ContentValues cv) {
		Log.i(TAG, "------insert----------");
		db.insert(table, null, cv);
	}

	/**
	 * 通过ID删除数据
	 * 
	 * @param name
	 */
	public void delById(String table, int id) {
		String[] args = { String.valueOf(id) };
		db.delete(table, "id=?", args);
		Log.i(TAG, "delete data by " + id);
	}

	/**
	 * 通过ID来修改值
	 * 
	 * @param raw
	 * @param rawValue
	 * @param whereName
	 */
	public void updateById(String table, ContentValues cv, int id) {
		db.update(table, cv, "id=?", new String[] { String.valueOf(id) });

		Log.i(TAG, "update data by " + id);
	}

	/**
	 * 分页查询，如果超过页数，就返回null
	 * 
	 * @param name
	 */
	public <T> ArrayList<T> findPageList(String table, int pageNum, Class<T> clz) {
		String sql = "select count(*) as COUNT from " + table;
		Cursor cursor = execSQLForCursor(sql);
		int count = cursor.getInt(0);
		double c = count / pageSize;
		c = Math.ceil(c);// 向上取整
		cursor.close();// 关闭游标
		if (c < pageNum) {
			return null;
		}
		sql = String.format("select * from " + table + " limit {0} offset {0}*{1}", pageSize, (pageNum - 1) * pageSize);
		cursor = execSQLForCursor(sql);
		for (String name : cursor.getColumnNames()) {
			System.out.println(">>>>>>>>>>" + name);
		}
		return null;
	}

	/**
	 * 分页查询，如果超过页数，就返回null
	 * 
	 * @param name
	 */
	public <T> List<T> findAllList(String table, Class<T> clz) {
		String sql = String.format("select * from " + table);
		Cursor cursor = execSQLForCursor(sql);
		return getObject(cursor, clz);
	}

	/**
	 * 利用反射从光标中遍历数据
	 * 
	 * @param cursor
	 * @param clz
	 * @return
	 */
	private <T> List<T> getObject(Cursor cursor, Class<T> clz) {
		if (cursor.getCount() == 0) {
			return null;
		}
		T t = null;
		List<T> list = new ArrayList<T>();
		try {
			while (cursor.moveToNext()) {
				t = clz.newInstance();
				for (String name : cursor.getColumnNames()) {
					Reflections.invokeSetter(t, name, cursor.getString(cursor.getColumnIndex(name)));
				}
				list.add(t);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 单纯执行一个SQL语句
	 * 
	 * @param sql
	 */
	public void ExecSQL(String sql) {
		try {
			db.execSQL(sql);
			Log.i("execSql: ", sql);
		} catch (Exception e) {
			Log.e("ExecSQL Exception", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 执行SQL，返回一个游标
	 * 
	 * @param sql
	 * @return
	 */
	private Cursor execSQLForCursor(String sql) {
		Cursor c = db.rawQuery(sql, null);
		return c;
	}

	public void closeDB() {
		db.close();
	}

}
