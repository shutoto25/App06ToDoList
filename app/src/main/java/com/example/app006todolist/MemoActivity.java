package com.example.app006todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * メモ内容作成画面.
 */
public class MemoActivity extends AppCompatActivity
        implements View.OnClickListener {
    /**
     * タイトル入力エリア.
     */
    private EditText mTitle;
    /**
     * 本文入力エリア.
     */
    private EditText mText;
    /**
     * 保存ボタン.
     */
    private Button mSave;
    /**
     * 作成中止ボタン.
     */
    private Button mCancel;
    /**
     * インテント.
     */
    private Intent mResultIntent = new Intent();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        findViews();
        setListeners();

        Intent intent = getIntent();
        boolean request = intent.getBooleanExtra(MainActivity.EXTRA_REQUEST_EDIT, false);
        // 新規メモの場合は何もしない.
        if (request) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSave:
                //TODO 空白拾えてない。。
                if (!TextUtils.isEmpty(mTitle.getText().toString())) {
                    MemoDatabase memoDatabase = new MemoDatabase(this);
                    memoDatabase.insertData(mTitle.getText().toString(), mText.getText().toString());
                    setResult(RESULT_OK, mResultIntent);
                    finish();
                    Toast.makeText(this, "登録しました", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "タイトルを入力してください", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btCancel:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("下書きを削除しますか？");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED, mResultIntent);
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 何もしない(ダイアログを閉じてメモ作成を継続)
                    }
                });
                builder.show();
                break;
        }
    }

    /**
     * 各部品の紐付け.
     */
    public void findViews() {
        mTitle = findViewById(R.id.etTitle);
        mText = findViewById(R.id.etText);
        mSave = findViewById(R.id.btSave);
        mCancel = findViewById(R.id.btCancel);
    }

    /**
     * リスナー登録.
     */
    public void setListeners() {
        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }
}
