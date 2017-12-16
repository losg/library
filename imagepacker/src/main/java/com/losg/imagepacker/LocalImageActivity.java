package com.losg.imagepacker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.losg.imagepacker.adapter.LocalImageAdapter;
import com.losg.imagepicker.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LocalImageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LinkedHashMap<String, ArrayList<String>>>, LocalPermissionUtils.PermissionListener {

    private RecyclerView                             mRecyclerView;
    private LinkedHashMap<String, ArrayList<String>> mImages;
    private LocalImageAdapter                        mLocalImageAdapter;
    private ArrayList<String>                        mSelectedItem;
    private int                                      mMaxSize;
    private int                                      mMaxContent;
    private Toolbar                                  mToolbar;
    private TextView                                 mTitleView;

    //选择的最大值
    private static final String INTENT_MMAXSIZE   = "intent_maxSize";
    //已经选择的路径
    private static final String INTENT_SELECTED   = "intent_selected";
    //单个文件的最大值
    private static final String INTENT_MAXCONTENT = "intent_maxcontent";

    //权限管理
    private LocalPermissionUtils mLocalPermissionUtils;


    public static void startForResult(Context context, int code, int mMaxSize, ArrayList<String> mSelectedItem) {
        Intent intent = new Intent(context, LocalImageActivity.class);
        intent.putStringArrayListExtra(INTENT_SELECTED, mSelectedItem);
        intent.putExtra(INTENT_MMAXSIZE, mMaxSize);
        ((Activity) context).startActivityForResult(intent, code);
    }

    public static void startForResult(Context context, int code, int mMaxSize, ArrayList<String> mSelectedItem, int maxContent) {
        Intent intent = new Intent(context, LocalImageActivity.class);
        intent.putStringArrayListExtra(INTENT_SELECTED, mSelectedItem);
        intent.putExtra(INTENT_MMAXSIZE, mMaxSize);
        intent.putExtra(INTENT_MAXCONTENT, maxContent);
        ((Activity) context).startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_picker_image);
        mSelectedItem = getIntent().getStringArrayListExtra(INTENT_SELECTED);
        mMaxSize = getIntent().getIntExtra(INTENT_MMAXSIZE, 1);
        mMaxContent = getIntent().getIntExtra(INTENT_MAXCONTENT, 0);
        initToolbar();
        mTitleView.setText("图片列表");
        mLocalPermissionUtils = new LocalPermissionUtils(this);
        mLocalPermissionUtils.onReBackState(savedInstanceState);
        mLocalPermissionUtils.setPermissionListener(this);
        mLocalPermissionUtils.permissionCheck(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public ArrayList<String> getSelectedItem() {
        return mSelectedItem;
    }

    public int getMaxSize() {
        return mMaxSize;
    }

    public int getMaxContent() {
        return mMaxContent;
    }

    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mImages = new LinkedHashMap<>();
        mLocalImageAdapter = new LocalImageAdapter(this, mImages);
        mRecyclerView.setAdapter(mLocalImageAdapter);
        LoaderManager supportLoaderManager = getSupportLoaderManager();
        supportLoaderManager.initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalPermissionUtils.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLocalPermissionUtils.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocalPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //toolBar
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_toolbar_back);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);

        mTitleView = new TextView(this);
        mTitleView.setTextSize(16);
        mTitleView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        mTitleView.setTextColor(0xffffffff);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(-2, -2);
        params.gravity = params.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
        getSupportActionBar().setCustomView(mTitleView, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public Loader<LinkedHashMap<String, ArrayList<String>>> onCreateLoader(int id, Bundle args) {
        return new LocalImageLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LinkedHashMap<String, ArrayList<String>>> loader, LinkedHashMap<String, ArrayList<String>> data) {
        mImages.clear();
        mImages.putAll(data);
        mLocalImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<LinkedHashMap<String, ArrayList<String>>> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            if (data == null) {
                finish();
            } else {
                mSelectedItem.clear();
                mSelectedItem.addAll(data.getStringArrayListExtra("data"));
            }
        }
        if (resultCode == RESULT_OK) {
            setResult(resultCode, data);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void permissionSuccess() {
        initView();
    }

    @Override
    public void permissionFailure() {
        finish();
    }
}
