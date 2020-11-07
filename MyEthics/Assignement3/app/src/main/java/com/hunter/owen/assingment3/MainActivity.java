package com.hunter.owen.assingment3;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
        setContentView(R.layout.content_main);

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
