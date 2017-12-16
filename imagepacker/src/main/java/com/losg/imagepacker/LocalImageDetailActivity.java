package com.losg.imagepacker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.losg.imagepacker.adapter.LocalImageDetailAdapter;
import com.losg.imagepicker.R;

import java.util.ArrayList;

public class LocalImageDetailActivity extends AppCompatActivity implements LocalImageDetailAdapter.ImageCheckListener {


    private static final String INTENT_IMAGES          = "intent_images";
    private static final String INTENT_SELECTED_IMAGES = "intent_selected_images";
    private static final String INTENT_MAXSIZE         = "intent_maxsize";
    private static final String INTENT_MAXCONTENT      = "intent_maxcontent";
    private static final String INTENT_TITLE           = "intent_title";

    private RecyclerView            recyclerView;
    private ArrayList<String>       images;
    private ArrayList<String>       selectedImages;
    private int                     maxSize;
    private int                     maxContent;
    private TextView                submit;
    private TextView                prview;
    private LocalImageDetailAdapter localImageDetailAdapter;
    private Toolbar                 mToolbar;
    private TextView                mTitleView;
    private String                  mTitle;

    public static void startActivitResult(Context context, String title, ArrayList<String> images, ArrayList<String> selectedImages, int maxSize, int maxContent) {
        Intent intent = new Intent(context, LocalImageDetailActivity.class);
        intent.putStringArrayListExtra(INTENT_SELECTED_IMAGES, selectedImages);
        intent.putStringArrayListExtra(INTENT_IMAGES, images);
        intent.putExtra(INTENT_MAXSIZE, maxSize);
        intent.putExtra(INTENT_TITLE, title);
        intent.putExtra(INTENT_MAXCONTENT, maxContent);
        ((Activity) context).startActivityForResult(intent, 100);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image_picker_detail);
        images = getIntent().getStringArrayListExtra(INTENT_IMAGES);
        selectedImages = getIntent().getStringArrayListExtra(INTENT_SELECTED_IMAGES);
        maxSize = getIntent().getIntExtra(INTENT_MAXSIZE, 0);
        maxContent = getIntent().getIntExtra(INTENT_MAXCONTENT, 0);
        mTitle = getIntent().getStringExtra(INTENT_TITLE);
        initView();
        initToolbar();
        mTitleView.setText(mTitle);
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


    protected void initView() {

        if (selectedImages == null) {
            selectedImages = new ArrayList<>();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        localImageDetailAdapter = new LocalImageDetailAdapter(this, images, selectedImages, maxSize, maxContent);
        localImageDetailAdapter.setImageCheckListener(this);
        recyclerView.setAdapter(localImageDetailAdapter);
        prview = (TextView) findViewById(R.id.prview);
        submit = (TextView) findViewById(R.id.submit);

        if (selectedImages.size() == 0) {
            prview.setEnabled(false);
            submit.setEnabled(false);
            submit.setText("确定");
        } else {
            prview.setEnabled(true);
            submit.setEnabled(true);
            submit.setText("确定(" + selectedImages.size() + ")");
        }

        initEvent();
    }

    private void initEvent() {

        prview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PhotoViewActivity.startToActivity(LocalImageDetailActivity.this, 0, selectedImages);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("data", selectedImages);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "取消").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("data", selectedImages);
            setResult(RESULT_CANCELED, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("data", selectedImages);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void checked() {
        if (selectedImages.size() == 0) {
            prview.setEnabled(false);
            submit.setEnabled(false);
            submit.setText("确定");
        } else {
            prview.setEnabled(true);
            submit.setEnabled(true);
            submit.setText("确定(" + selectedImages.size() + ")");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoViewActivity.DEFAULT_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("data", data.getStringArrayListExtra("data"));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ArrayList<String> result = data.getStringArrayListExtra("data");
                if (result != null) {
                    selectedImages.clear();
                    selectedImages.addAll(result);
                    localImageDetailAdapter.notifyDataSetChanged();
                    if (selectedImages.size() == 0) {
                        prview.setEnabled(false);
                        submit.setEnabled(false);
                        submit.setText("确定");
                    } else {
                        prview.setEnabled(true);
                        submit.setEnabled(true);
                        submit.setText("确定(" + selectedImages.size() + ")");
                    }
                }
            }
        }
    }
}
