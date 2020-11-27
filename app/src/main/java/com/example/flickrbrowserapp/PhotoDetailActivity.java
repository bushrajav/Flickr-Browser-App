package com.example.flickrbrowserapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        TextView photo_author = findViewById(R.id.photo_author);
        photo_author.setText(photo.getAuthor());

        TextView photo_title = findViewById(R.id.photo_title);

        Resources resources=getResources();
        String text=resources.getString(R.string.photo_title_text,photo.getTitle());
        photo_title.setText(text);
      //  photo_title.setText(photo.getTitle());

        TextView photo_tags = findViewById(R.id.photo_tags);
        photo_title.setText(resources.getString(R.string.photo_title_text,photo.getTitle()));
      //  photo_tags.setText(photo.getTags());

        ImageView photo_image = findViewById(R.id.photo_image);
        Picasso.with(this).load(photo.getLink())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(photo_image);
    }

}
