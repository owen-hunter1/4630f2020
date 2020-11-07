package com.hunter.owen.assignment4;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intro animation
        FrameLayout mrorange_cover = (FrameLayout) findViewById(R.id.mr_orange_cover);
        ObjectAnimator.ofObject(mrorange_cover, "alpha", new FloatEvaluator(), 1.0f, 0.0f).setDuration(10000).start();


        //Sliders, images, check box declarations

        final ImageView mrorange = (ImageView) findViewById(R.id.mr_orange);
        final ImageView hat = (ImageView) findViewById(R.id.hat);
        final ImageView mustache = (ImageView) findViewById(R.id.mustache);

        final SeekBar longSlider = (SeekBar) findViewById(R.id.long_slider);
        final SeekBar tallSlider = (SeekBar) findViewById(R.id.tall_slider);
        final SeekBar orangeSlider = (SeekBar) findViewById(R.id.orange_slider);

        final CheckBox hairChecker = (CheckBox) findViewById(R.id.hair_check);
        final CheckBox fashionChecker = (CheckBox) findViewById(R.id.fashion_check);

        orangeSlider.setMax(100);
        orangeSlider.setProgress(100);

        //SeekBar listeners

        longSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mrorange.setScaleX(seekBar.getProgress() * 0.1f + 1.0f);
                mustache.setScaleX(seekBar.getProgress() * 0.1f * 0.25f + 0.25f);            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tallSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mrorange.setScaleY(seekBar.getProgress() * 0.1f + 1.0f);
                mustache.setScaleY(seekBar.getProgress() * 0.1f * 0.25f + 0.25f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        orangeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mrorange.setAlpha(seekBar.getProgress() * 0.01f);
                hat.setAlpha(seekBar.getProgress() * 0.01f);
                mustache.setAlpha(seekBar.getProgress() * 0.01f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //CheckBox  listeners

        hairChecker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mustache.setVisibility(mustache.getVisibility() != View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        });

        fashionChecker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hat.setVisibility(hat.getVisibility() != View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        });

        //more button
        Button more = findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
