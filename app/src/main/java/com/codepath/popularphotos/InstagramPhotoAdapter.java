package com.codepath.popularphotos;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by k.sridhar on 12/4/2015.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context,android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.individual_item,parent,false);
        }
        try {
            TextView username = (TextView) convertView.findViewById(R.id.username_textview);
            username.setText(photo.username);
            com.makeramen.roundedimageview.RoundedImageView userprofileimage = (com.makeramen.roundedimageview.RoundedImageView) convertView.findViewById(R.id.userprofileimage);
            Picasso.with(getContext()).load(photo.userProfileUrl).into(userprofileimage);
            ImageView imgView = (ImageView) convertView.findViewById(R.id.instaImage);
            Picasso.with(getContext()).load(photo.imageurl).fit().centerCrop().placeholder(R.drawable.placeholder).error(R.drawable.placeholder_error).into(imgView);
            ImageView love_img =(ImageView)convertView.findViewById(R.id.love_icon);
            love_img.setImageResource(R.drawable.blue_heart);
            TextView like_count = (TextView) convertView.findViewById(R.id.likecount);
            like_count.setText(Integer.toString(photo.likeCount));
            TextView caption = (TextView) convertView.findViewById(R.id.caption);
            caption.setText(photo.caption);
            caption.setMaxLines(2);
            caption.setMinLines(1);
            caption.setEllipsize(TextUtils.TruncateAt.END);

        }catch (Exception e){
            e.printStackTrace();
        }


        return convertView;
    }
}
