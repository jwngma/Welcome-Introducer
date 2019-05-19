package com.example.welcomeintroducer;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;

    private Button skipBtn, nextBtn;
    private LinearLayout dots_layout;
    private ImageView[] dots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT>=19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (new PreferenceManager(this).checkPreferences()) {
            loadHome();
        }

        skipBtn = findViewById(R.id.skipBtn);
        nextBtn = findViewById(R.id.nextBtn);

        skipBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        viewPager = findViewById(R.id.slideViewPager);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);


        dots_layout = findViewById(R.id.linear_layout);

        CreateDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                CreateDots(i);
                if (i == dots.length - 1) {
                    nextBtn.setText("Start ");
                    skipBtn.setVisibility(View.INVISIBLE);
                } else {
                    nextBtn.setText("Next");
                    skipBtn.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public void CreateDots(int current_position) {

        if (dots_layout != null) {
            dots_layout.removeAllViews();

            dots = new ImageView[13];

            for (int i = 0; i < 13; i++) {
                dots[i] = new ImageView(this);

                if (i == current_position) {

                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
                } else {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dots_layout.addView(dots[i], params);
            }
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nextBtn:
                nextSlide();
                break;

            case R.id.skipBtn:

                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }
    }
    private void nextSlide() {
        int nextSlide = viewPager.getCurrentItem() + 1;
        if (nextSlide < 13) {
            viewPager.setCurrentItem(nextSlide);
        } else {
            loadHome();
            new PreferenceManager(this).writePreference();
        }

    }

    private void loadHome() {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
    }
}
