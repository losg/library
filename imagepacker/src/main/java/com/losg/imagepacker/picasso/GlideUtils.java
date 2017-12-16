package com.losg.imagepacker.picasso;

import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.losg.imagepicker.R;

import java.io.File;

/**
 * Created by losg on 2016/9/1.
 */
public class GlideUtils {

    private static int defaultResource = R.mipmap.ic_loading_icon;

    public static void loadUrlImage(ImageView imageView, String url) {
        loadUrlImage(imageView, url, false);
    }

    public static void loadUrlImageWithCircle(ImageView imageView, String url) {
        loadUrlImageWithCircle(imageView, url, false);
    }

    public static void loadUrlImageWithRound(ImageView imageView, String url) {
        loadUrlImageWithRound(imageView, url, false);
    }

    public static void loadUrlImage(ImageView imageView, String url, boolean isFile) {
        loadUrlImage(imageView, url, isFile, ImageView.ScaleType.FIT_XY);
    }

    public static void loadUrlImage(ImageView imageView, String url, boolean isFile, ImageView.ScaleType scaleType) {
        RequestManager requestManager = Glide.with(imageView.getContext());
        DrawableTypeRequest drawableTypeRequest = null;
        try {
            if (isFile) {
                drawableTypeRequest = requestManager.load(new File(url));
            } else
                drawableTypeRequest = requestManager.load(url);

            if (scaleType == ImageView.ScaleType.CENTER_INSIDE) {
                drawableTypeRequest.placeholder(defaultResource).error(defaultResource).fitCenter().into(imageView);
            } else if (scaleType == ImageView.ScaleType.CENTER_CROP)
                drawableTypeRequest.centerCrop().placeholder(defaultResource).error(defaultResource).into(imageView);
            else
                drawableTypeRequest.placeholder(defaultResource).error(defaultResource).into(imageView);
        } catch (Exception e) {

        }
    }

    public static void loadUrlImageWithCircle(ImageView imageView, String url, boolean isFile) {
        RequestManager requestManager = Glide.with(imageView.getContext());
        DrawableTypeRequest drawableTypeRequest = null;
        try {
            if (isFile) {
                drawableTypeRequest = requestManager.load(new File(url));
            } else
                drawableTypeRequest = requestManager.load(url);
            GlideCircleTransform glideCircleTransform = new GlideCircleTransform(imageView.getContext());
            drawableTypeRequest.placeholder(defaultResource).error(defaultResource).bitmapTransform(glideCircleTransform).into(imageView);
        } catch (Exception e) {

        }
    }

    public static void loadUrlImageWithRound(ImageView imageView, String url, boolean isFile) {
        RequestManager requestManager = Glide.with(imageView.getContext());
        DrawableTypeRequest drawableTypeRequest = null;
        try {
            if (isFile) {
                drawableTypeRequest = requestManager.load(new File(url));
            } else
                drawableTypeRequest = requestManager.load(url);
            drawableTypeRequest.placeholder(defaultResource).error(defaultResource).bitmapTransform(new GlideRoundTransform(imageView.getContext())).into(imageView);
        } catch (Exception e) {

        }
    }

    public static void loadUrlImage(ImageView imageView, int imageRource) {
        RequestManager requestManager = Glide.with(imageView.getContext());
        requestManager.load(imageRource).placeholder(defaultResource).error(defaultResource).into(imageView);

    }

    public static void loadUrlImageWithCircle(ImageView imageView, int imageRource) {
        RequestManager requestManager = Glide.with(imageView.getContext());
        requestManager.load(imageRource).placeholder(defaultResource).error(defaultResource).bitmapTransform(new GlideCircleTransform(imageView.getContext())).into(imageView);
    }

    public static void loadUrlImageWithRound(ImageView imageView, int imageRource) {
        RequestManager requestManager = Glide.with(imageView.getContext());
        requestManager.load(imageRource).placeholder(defaultResource).error(defaultResource).bitmapTransform(new GlideRoundTransform(imageView.getContext())).into(imageView);
    }

}
