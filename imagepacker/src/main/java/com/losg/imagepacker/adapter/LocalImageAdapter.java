package com.losg.imagepacker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.losg.imagepacker.LocalImageActivity;
import com.losg.imagepacker.LocalImageDetailActivity;
import com.losg.imagepacker.picasso.GlideUtils;
import com.losg.imagepicker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by losg on 2016/4/1.
 */
public class LocalImageAdapter extends RecyclerView.Adapter<LocalImageAdapter.Holder> {

    private Context                          mContext;
    private HashMap<String, ArrayList<String>> images;

    public LocalImageAdapter(Context mContext, LinkedHashMap<String, ArrayList<String>> images) {
        this.mContext = mContext;
        this.images = images;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.view_local_image_picker_list, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ArrayList<String> childImages = null;
        String dirName = "";
        int currentPosition = 0;
        for (Map.Entry<String, ArrayList<String>> entry : images.entrySet()) {
            if (currentPosition == position) {
                childImages = entry.getValue();
                dirName = entry.getKey();
                break;
            }
            currentPosition++;
        }

        GlideUtils.loadUrlImage(holder.firstImage, childImages.get(0), true);

        holder.dirName.setText(dirName + "(" + childImages.size() + ")");

        final ArrayList<String> finalChildImages = childImages;
        final String            name = dirName;
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LocalImageDetailActivity.startActivitResult(mContext,name ,finalChildImages, ((LocalImageActivity) mContext).getSelectedItem(), ((LocalImageActivity) mContext).getMaxSize(),((LocalImageActivity) mContext).getMaxContent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView firstImage;
        TextView  dirName;

        public Holder(View itemView) {
            super(itemView);
            firstImage = (ImageView) itemView.findViewById(R.id.first_image);
            dirName = (TextView) itemView.findViewById(R.id.dir_name);
        }
    }
}

