package com.example.mpip;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);


        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://driving-tests.org/beginner-drivers/how-to-change-tires/");
            }
        });


        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://drivinglife.net/sound-of-a-healthy-engine/");
            }
        });


        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://www.cbac.com/grand-parkway/media-center/blog/2017/march/transmission-trouble-10-warning-signs-you-need-r/");
            }
        });


        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://www.sullivantire.com/blog/safety/run-out-of-gas");
            }
        });


        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://news.motability.co.uk/everyday-tips/when-to-use-car-lights-the-complete-guide/");
            }
        });


        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://living.geico.com/driving/auto/auto-care/always-find-your-car-keys/#:~:text=If%20you%20lose%20it%3A%20You,or%20an%20independent%20repair%20shop.");
            }
        });

        Button button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://www.rac.co.uk/drive/advice/car-maintenance/car-overheating/#:~:text=The%20most%20common%20causes%20of,need%20to%20get%20looked%20at.");
            }
        });

        Button button8 = findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://gomechanic.in/blog/5-symptoms-of-a-failing-handbrake/#:~:text=If%20your%20handbrake%20feels%20tighter,wear%20out%20faster%20than%20expected.");
            }
        });

        Button button9 = findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://www.allstate.com/resources/car-insurance/in-case-of-a-car-accident");
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TipsActivity.this, SecondActivity.class);
        startActivity(intent);
    }


    private void openExternalLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
