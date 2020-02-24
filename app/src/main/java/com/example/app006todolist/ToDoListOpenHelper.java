package com.example.app006todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * メモリストのDBヘルパークラス.
 */
public class ToDoListOpenHelper extends SQLiteOpenHelper {
    /**
     * シングルトン
     */
    private static ToDoListOpenHelper sSingleton = null;

    /**
     * シングルトン.
     * スレッドセーフにするためにsynchronizedに指定
     *
     * @param context コンテキスト
     * @return ReadAloudDatabaseOpenHelperインスタンス.
     */
    public static synchronized ToDoListOpenHelper getInstance(final Context context) {
        if (sSingleton == null) {
            sSingleton = new ToDoListOpenHelper(context, MemoDatabase.DATABASE_NAME);
        }
        return sSingleton;
    }

    /**
     * コンストラクタ.
     *
     * @param context
     */
    ToDoListOpenHelper(Context context, String dbFileName) {
        super(context, dbFileName, null, MemoDatabase.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(
                "CREATE TABLE " +
                        MemoDatabase.TABLE_NAME + " (" +
                        MemoDatabase.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MemoDatabase.COLUMN_TITLE + " TEXT NOT NULL," +
                        MemoDatabase.COLUMN_TEXT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + MemoDatabase.TABLE_NAME);
        onCreate(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onUpgrade(database, oldVersion, newVersion);
    }
}
