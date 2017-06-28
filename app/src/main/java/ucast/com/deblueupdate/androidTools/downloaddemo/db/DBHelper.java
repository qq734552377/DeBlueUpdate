package ucast.com.deblueupdate.androidTools.downloaddemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * 用来创建数据库
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME="download.db";
	private static final int VERSION=1;//数据库的版本
	/**
	 * 建表语法
	 */
	private static final String TABLE_CREATE="create table thread_info(_id integer primary key autoincrement," +
			"thread_id integer,url text,start integer,end integer,finished integer)";
	/**
	 * 删表语法
	 */
	private static final String TABLE_DROP="drop table if exits thread_info";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TABLE_DROP);
		db.execSQL(TABLE_CREATE);
	}
}
