package com.losg.imagepacker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.losg.imagepacker.picasso.GlideUtils;
import com.losg.imagepicker.R;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by losg on 2016/3/22.
 */
public class PhotoViewActivity extends AppCompatActivity implements PhotoViewAttacher.OnPhotoTapListener, View.OnClickListener {


    private static final String INTENT_IMAGES          = "intent_images";
    private static final String INTENT_SELECTED_IMAGES = "intent_selected_images";
    private static final String INTENT_POSITION        = "intent_position";
    private static final String INTENT_MAXSIZE         = "intent_maxsize";
    private static final String INTENT_MAXCONTENT        = "intent_maxcontent";
    public static final  int    DEFAULT_CODE           = 3000;

    private HackyViewPager photoPager;
    private TextView       pagerNumber;
    private int            mMaxSize;

    private ArrayList<String> mImagePath;
    private int               mSelectPosition;
    private ArrayList<String> mSelectedPath;
    private int               mPagerSize;
    private int               mMaxContent;
    private RelativeLayout    mCurrentPagerLayer;
    private ImageView         mBack;
    private TextView          mCurrentPage;
    private LinearLayout      mCheckLayer;
    private TextView          mPagerCheck;
    private LinearLayout      mSubmitLayer;
    private TextView          mPrview;
    private TextView          mSubmit;

    public static void startToActivity(Context context, ArrayList<String> imagePath, int selectPosition) {
        startToActivity(context, imagePath, null, selectPosition, DEFAULT_CODE, -1);
    }

    public static void startToActivity(Context context, int selectPosition, ArrayList<String> selectedImage) {
        startToActivity(context, null, selectedImage, selectPosition, DEFAULT_CODE, -1);
    }

    public static void startToActivity(Context context, ArrayList<String> imagePath, ArrayList<String> selectedImage, int selectPosition, int code, int maxSize) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putStringArrayListExtra(INTENT_IMAGES, imagePath);
        intent.putStringArrayListExtra(INTENT_SELECTED_IMAGES, selectedImage);
        intent.putExtra(INTENT_POSITION, selectPosition);
        intent.putExtra(INTENT_MAXSIZE, maxSize);
        ((Activity) context).startActivityForResult(intent, code);
    }

    public static void startToActivity(Context context, ArrayList<String> imagePath, ArrayList<String> selectedImage, int selectPosition, int code, int maxSize, int maxContent) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putStringArrayListExtra(INTENT_IMAGES, imagePath);
        intent.putStringArrayListExtra(INTENT_SELECTED_IMAGES, selectedImage);
        intent.putExtra(INTENT_POSITION, selectPosition);
        intent.putExtra(INTENT_MAXSIZE, maxSize);
        intent.putExtra(INTENT_MAXCONTENT, maxContent);
        ((Activity) context).startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker_view);
        mImagePath = getIntent().getStringArrayListExtra(INTENT_IMAGES);
        mSelectedPath = getIntent().getStringArrayListExtra(INTENT_SELECTED_IMAGES);
        mSelectPosition = getIntent().getIntExtra(INTENT_POSITION, 0);
        mMaxContent = getIntent().getIntExtra(INTENT_MAXCONTENT, 0);
        mMaxSize = getIntent().getIntExtra(INTENT_MAXSIZE, 0);
        initView();
    }

    private void initView() {
        photoPager = (HackyViewPager) findViewById(R.id.photo_pager);
        pagerNumber = (TextView) findViewById(R.id.pager_number);
        mCurrentPagerLayer = (RelativeLayout) findViewById(R.id.current_pager_layer);
        findViewById(R.id.back).setOnClickListener(this);
        mCurrentPage = (TextView) findViewById(R.id.current_page);
        findViewById(R.id.check_layer).setOnClickListener(this);
        mPagerCheck = (TextView) findViewById(R.id.page_check);
        mPrview = (TextView) findViewById(R.id.prview);
        mSubmit = (TextView) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
        mSubmitLayer = (LinearLayout) findViewById(R.id.submit_layer);

        mPrview.setVisibility(View.GONE);

        if (mImagePath == null) {
            mImagePath = new ArrayList<>();
            mImagePath.addAll(mSelectedPath);
        }

        mPagerSize = mImagePath.size();

        if (mSelectedPath != null) {
            mSubmit.setText("确定(" + mSelectedPath.size() + ")");
            pagerNumber.setVisibility(View.GONE);
        } else {
            pagerNumber.setVisibility(View.VISIBLE);
        }

        if (mSelectPosition > mPagerSize - 1)
            mSelectPosition = 0;

        photoPager.setAdapter(new SamplePagerAdapter());
        photoPager.setCurrentItem(mSelectPosition);
        pagerNumber.setText(mSelectPosition + 1 + "/" + mPagerSize);
        mCurrentPage.setText(mSelectPosition + 1 + "/" + mPagerSize);

        changeSelectedStatus();

        photoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagerNumber.setText((position + 1) + "/" + mPagerSize);
                mCurrentPage.setText((position + 1) + "/" + mPagerSize);
                mSelectPosition = position;
                changeSelectedStatus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void changeSelectedStatus() {
        if (mSelectedPath != null) {
            if (mSelectedPath.size() != 0) {
                mSubmit.setText("确定(" + mSelectedPath.size() + ")");
                mSubmit.setEnabled(true);
            } else {
                mSubmit.setText("确定");
                mSubmit.setEnabled(false);
            }
            String path = mImagePath.get(mSelectPosition);
            if (mSelectedPath.contains(path)) {
                mPagerCheck.setSelected(true);
                mPagerCheck.setText(mSelectedPath.indexOf(path) + 1 + "");
            } else {
                mPagerCheck.setSelected(false);
                mPagerCheck.setText("");
            }
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back) {
            back();
        } else if (i == R.id.check_layer) {
            checkLayerClick();
        } else if (i == R.id.submit) {
            submit();
        }
    }

    private void submit() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("data", mSelectedPath);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void checkLayerClick() {
        if (mSelectedPath != null) {
            String path = mImagePath.get(mSelectPosition);
            if (mSelectedPath.contains(path)) {
                mSelectedPath.remove(path);
                mPagerCheck.setSelected(false);
                mPagerCheck.setText("");
            } else {
                if(mMaxSize != -1){
                    if(mSelectedPath.size() >= mMaxSize){
                        Toast.makeText(this, "选择不能超过" + mMaxSize + "张", Toast.LENGTH_SHORT).show();
                    }else{
                        File file = new File(path);
                        if(mMaxContent != 0 && file.length() > mMaxContent) {
                            Toast.makeText(this, "单个图片大小不能超过" + (mMaxContent / 1024 / 1024) + "M", Toast.LENGTH_SHORT).show();
                            mPagerCheck.setSelected(false);
                        }else {
                            mSelectedPath.add(path);
                            mPagerCheck.setSelected(true);
                            mPagerCheck.setText(mSelectedPath.indexOf(path) + 1 + "");
                        }
                    }
                }else{
                    File file = new File(path);
                    if(mMaxContent != 0 && file.length() > mMaxContent) {
                        Toast.makeText(this, "单个图片大小不能超过" + (mMaxContent / 1024 / 1024) + "M", Toast.LENGTH_SHORT).show();
                        mPagerCheck.setSelected(false);
                    }else {
                        mSelectedPath.add(path);
                        mPagerCheck.setSelected(true);
                        mPagerCheck.setText(mSelectedPath.indexOf(path) + 1 + "");
                    }
                }
            }
            if (mSelectedPath.size() != 0) {
                mSubmit.setText("确定(" + mSelectedPath.size() + ")");
                mSubmit.setEnabled(true);
            } else {
                mSubmit.setText("确定");
                mSubmit.setEnabled(false);
            }

        }
    }

    private void back() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("data", mSelectedPath);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerSize;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            String path = (mImagePath == null ? mSelectedPath.get(position) : mImagePath.get(position));
            if (path.startsWith("http")) {
                GlideUtils.loadUrlImage(photoView, path, false, ImageView.ScaleType.CENTER_INSIDE);
            } else {
                GlideUtils.loadUrlImage(photoView, path, true, ImageView.ScaleType.CENTER_INSIDE);
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setOnPhotoTapListener(PhotoViewActivity.this);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {

        if (mSelectedPath == null) {
            mCurrentPage.setVisibility(View.VISIBLE);
            return;
        }

        if (mCurrentPagerLayer.getVisibility() == View.GONE) {
            mCurrentPagerLayer.setVisibility(View.VISIBLE);
            mSubmitLayer.setVisibility(View.VISIBLE);
        } else {
            mCurrentPagerLayer.setVisibility(View.GONE);
            mSubmitLayer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOutsidePhotoTap() {

    }

}
