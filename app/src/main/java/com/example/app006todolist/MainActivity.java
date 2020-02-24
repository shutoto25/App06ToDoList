package com.example.app006todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * @author shutoto25
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    /**
     * リクエストコード.
     */
    private final static int REQUEST_CODE = 1;
    /**
     * メモ新規作成ボタン.
     */
    private Button mNew;
    /**
     * メモリスト表示用.
     */
    private ListView mListView;
    /**
     * アダプター.
     */
    private ArrayAdapter<String> mAdapter;
    /**
     * メモアイテムリスト.
     */
    private ArrayList<String> mItems;
    /**
     * Extra情報 編集画面.
     */
    public static String EXTRA_REQUEST_EDIT =
            "com.example.app006todolist.MainActivity.extra.request.edit";
    /**
     * データベースのインスタンス.
     */
    public MemoDatabase mMemoDatabase;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();

        // DBをオープンする.
        mMemoDatabase = new MemoDatabase(this);

        mItems = new ArrayList<>();
        String[] columns = {MemoDatabase.COLUMN_TITLE};
        Cursor cursor = mMemoDatabase.getData(columns);

        if (cursor.moveToFirst()) {
            do {
                mItems.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        // DBをクローズする.
        mMemoDatabase.closeDB();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        mListView.setAdapter(mAdapter);
        // リストを更新する.
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // DBをオープンする.
                    mMemoDatabase = new MemoDatabase(this);

                    mItems = new ArrayList<>();
                    String[] columns = {MemoDatabase.COLUMN_TITLE};
                    Cursor cursor = mMemoDatabase.getData(columns);

                    if (cursor.moveToFirst()) {
                        do {
                            mItems.add(cursor.getString(0));
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    // DBをクローズする.
                    mMemoDatabase.closeDB();

                    mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
                    mListView.setAdapter(mAdapter);
                    // リスト更新.
                    mAdapter.notifyDataSetChanged();

                } else if (requestCode == RESULT_CANCELED) {
                    // 何もしない.
                }

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Intent createMemo = new Intent(MainActivity.this, MemoActivity.class);
        createMemo.putExtra(EXTRA_REQUEST_EDIT,false);
        // メモ作成画面を起動.
        startActivityForResult(createMemo, REQUEST_CODE);
    }

    /**
     * 各部品の紐付け.
     */
    protected void findViews() {
        mListView = findViewById(R.id.lvToDoList);
        mNew = findViewById(R.id.btNew);
    }

    /**
     * リスナー登録.
     */
    protected void setListeners() {
        mNew.setOnClickListener(this);
        // リストタップ時.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // メモ編集画面を起動
                Intent editMemo = new Intent(MainActivity.this, MemoActivity.class);
                String clickedTitle = (String)parent.getItemAtPosition(position);
                editMemo.putExtra(EXTRA_REQUEST_EDIT,true);
                editMemo.putExtra("title", clickedTitle);
                startActivityForResult(editMemo, REQUEST_CODE);
            }
        });
        // リストロングタップ時.
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }
}
