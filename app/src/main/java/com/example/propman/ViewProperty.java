package com.example.senior1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewProperty extends AppCompatActivity {
    TextView tw4,tw5,tw6,tw7,tw8,tw9;
    ImageView iw2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);
        setTitle("View");
        Intent intent = getIntent();

        ParcelableProperty parcelableProperty = (ParcelableProperty) intent
                .getParcelableExtra("property");
        Property property = parcelableProperty.getProperty();

        tw4 = findViewById(R.id.textView4);
        tw5 = findViewById(R.id.textView5);
        tw6 = findViewById(R.id.textView6);
        tw7 = findViewById(R.id.textView7);
        tw8 = findViewById(R.id.textView8);
        tw9 = findViewById(R.id.textView9);
        iw2 = findViewById(R.id.imageView2);

        tw4.setText(property.getTitle());
        tw5.setText("Price: " + property.getPrice());
        tw6.setText("Number of rooms: " +property.getOdasalon());
        tw7.setText("Size: " + property.getM2() + "");
        tw8.setText("Description: " + property.getDescription());
        tw9.setText("Address: " + property.getAddress());


        String uri = "@drawable/" + property.getImage();

        int imageResource = getResources().getIdentifier(uri, null, getPackageName()); //get image  resource

        Drawable res = getResources().getDrawable(imageResource); // convert into drawble

        iw2.setImageDrawable(res);



    }
}
