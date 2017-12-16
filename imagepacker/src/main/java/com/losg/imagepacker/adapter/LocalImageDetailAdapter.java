package com.losg.imagepacker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.losg.imagepacker.PhotoViewActivity;
import com.losg.imagepacker.picasso.GlideUtils;
import com.losg.imagepicker.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by losg on 2016/4/1.
 */
public class LocalImageDetailAdapter extends RecyclerView.Adapter<LocalImageDetailAdapter.Holder> {

    private Context            mContext;
    private ArrayList<String>  images;
    private int                perSize;
    private ArrayList<String>  selectedImages;
    private ImageCheckListener imageCheckListener;
    private int                maxSize;
    private int                maxContent;

    public LocalImageDetailAdapter(Context mContext, ArrayList<String> images, ArrayList<String> selectedImages, int maxSize, int maxContent) {
        this.mContext = mContext;
        this.images = images;
        this.selectedImages = selectedImages;
        this.maxSize = maxSize;
        this.maxContent = maxContent;
        perSize = (int) (mContext.getResources().getDisplayMetrics().widthPixels / 3 - mContext.getResources().getDimension(R.dimen.line_space));
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.view_local_image_picker_detail, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final String imageBean = images.get(position);
        ViewGroup.LayoutParams layoutParams = holder.localImage.getLayoutParams();
        layoutParams.width = perSize;
        layoutParams.height = perSize;
        holder.localImage.setLayoutParams(layoutParams);
        GlideUtils.loadUrlImage(holder.localImage, imageBean, true);
        if (selectedImages.contains(imageBean)) {
            holder.localImage.setColorFilter(new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY));
            holder.checkSelect.setSelected(true);
            holder.checkSelect.setText(selectedImages.indexOf(imageBean) + 1 + "");
        } else {
            holder.localImage.clearColorFilter();
            holder.checkSelect.setSelected(false);
            holder.checkSelect.setText("");
        }

        holder.localImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PhotoViewActivity.startToActivity(mContext, images, selectedImages, position, PhotoViewActivity.DEFAULT_CODE, maxSize, maxContent);
            }
        });

        holder.checkLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.checkSelect.setSelected(!holder.checkSelect.isSelected());
                if (holder.checkSelect.isSelected()) {
                    File file = new File(imageBean);
                    if(maxContent != 0 && file.length() > maxContent){
                        Toast.makeText(mContext, "单个图片大小不能超过" + (maxContent / 1024 / 1024) + "M", Toast.LENGTH_SHORT).show();
                        holder.checkSelect.setSelected(false);
                    }else if (selectedImages.size() >= maxSize) {
                        Toast.makeText(mContext, "选择不能超过" + maxSize + "张", Toast.LENGTH_SHORT).show();
                        holder.checkSelect.setSelected(false);
                    } else {
                        holder.localImage.setColorFilter(new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY));
                        selectedImages.add(imageBean);
                        holder.checkSelect.setText(selectedImages.indexOf(imageBean) + 1 + "");
                    }
                } else {
                    holder.localImage.clearColorFilter();
                    selectedImages.remove(imageBean);
                    notifyDataSetChanged();
                }
                if (imageCheckListener != null) {
                    imageCheckListener.checked();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView    localImage;
        TextView     checkSelect;
        LinearLayout checkLayer;

        public Holder(View itemView) {
            super(itemView);
            localImage = (ImageView) itemView.findViewById(R.id.local_image);
            checkSelect = (TextView) itemView.findViewById(R.id.check_select);
            checkLayer = (LinearLayout) itemView.findViewById(R.id.check_layer);
        }
    }

    public void setImageCheckListener(ImageCheckListener imageCheckListener) {
        this.imageCheckListener = imageCheckListener;
    }

    public interface ImageCheckListener {

        void checked();
    }

}
