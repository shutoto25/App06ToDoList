package com.example.app006todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.lang.annotation.Target;

/**
 * データベース操作クラス.
 * メモの登録、削除、参照を行う.
 */
public class MemoDatabase extends ContextWrapper {
    /**
     * Database Version.
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * Database Name.
     */
    public static final String DATABASE_NAME = "toDoListDB.db";
    /**
     * Table Name.
     */
    public static final String TABLE_NAME = "toDoListDB";
    /**
     * Column id.
     */
    public static final String COLUMN_ID = "_id";
    /**
     * Column Name Title.
     */
    public static final String COLUMN_TITLE = "title";
    /**
     * Column Name Text .
     */
    public static final String COLUMN_TEXT = "text";
    /**
     * DBインスタンス.
     */
    private SQLiteDatabase mMemoDb = null;

    /**
     * コンストラクタ.
     *
     * @param context
     */
    public MemoDatabase(final Context context) {
        super(context);
        openDB(context);
    }

    /**
     * DBをオープンする.
     *
     * @param context
     */
    public void openDB(final Context context) {
        mMemoDb = null;
        mMemoDb = ToDoListOpenHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * DBをクローズする.
     */
    public void closeDB() {
        if (mMemoDb != null) {
            mMemoDb.close();
            mMemoDb = null;
        }
    }

    /**
     * データ登録.
     *
     * @param title
     * @param text
     */
    public void insertData(String title, String text) {

        // トランザクション開始.
        mMemoDb.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_TEXT, text);
            mMemoDb.insert(TABLE_NAME, null, values);
            // トランザクションへコミット.
            mMemoDb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // トランザクション終了.
            mMemoDb.endTransaction();
        }
    }

    /**
     * データ取得.
     *
     * @param columns if {@code null}, get all data.
     * @return
     */
    public Cursor getData(String[] columns) {
        return mMemoDb.query(
                TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor readData(String[] columns, String[] column, String[] title) {
        return mMemoDb.query(TABLE_NAME,columns,
                column + "like?",
                title,
                null,
                null,
                null,
                null);
    }
}
