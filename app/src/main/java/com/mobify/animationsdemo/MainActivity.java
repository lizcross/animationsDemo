package com.mobify.animationsdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class MainActivity extends Activity {

    private float slidingStartLocationX;
    private int spinDirection = 1;
    private int spinDirectionRotationPoint = 1;

    private ObjectAnimator createSlideMeAnimator(View v, TimeInterpolator timeInterpolator) {
        float xLocation = v.getX();

        float buttonWidth = v.getWidth();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        float halfWidth = width/2.0f;

        float startLocation;
        float endLocation;
        if (xLocation < halfWidth) {
            startLocation = xLocation;
            endLocation = xLocation + width - (buttonWidth * 1.5f);
        } else {
            startLocation = xLocation - (buttonWidth * 0.5f);
            endLocation = slidingStartLocationX;
        }

        ObjectAnimator slideAnimator = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, startLocation, endLocation);
        if (timeInterpolator != null) {
            slideAnimator.setInterpolator(timeInterpolator);
        }
        return slideAnimator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup mainView = (ViewGroup) findViewById(R.id.mainView);
        final Button slideMeButton = (Button) findViewById(R.id.slideMeButton);
        slidingStartLocationX = slideMeButton.getX();

        final Button slideMeOptionsButton = (Button) findViewById(R.id.slideMeInterpolatorButton);
        final Spinner slideMeInterpolationOptions = (Spinner) findViewById(R.id.slideMeInterpolationOptions);

        final Button clickMeButton = (Button) findViewById(R.id.clickMeButton);
        final Button spinReverseButton = (Button) findViewById(R.id.spinReverseButton);
        final Button spinRotationPointButton = (Button) findViewById(R.id.spinRotationPointButton);
        spinRotationPointButton.setPivotY(spinRotationPointButton.getPivotY() + (spinRotationPointButton.getHeight()/2.0f));

        final Button growingButton = (Button) findViewById(R.id.growingButton);
        final Button movingButton = (Button) findViewById(R.id.movingButton);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.interpolations, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slideMeInterpolationOptions.setAdapter(spinnerAdapter);

        slideMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator slideAnimator = createSlideMeAnimator(v, null);
                slideAnimator.start();
            }
        });

        slideMeOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int interpolationOptionPosition = slideMeInterpolationOptions.getSelectedItemPosition();
                TimeInterpolator timeInterpolator = null;
                switch (interpolationOptionPosition) {
                    case 0:
                        timeInterpolator = null;
                        break;
                    case 1:
                        timeInterpolator = new AnticipateOvershootInterpolator();
                        break;
                    case 2:
                        timeInterpolator = new BounceInterpolator();
                        break;
                    case 3:
                        timeInterpolator = new LinearInterpolator();
                        break;
                    case 4:
                        timeInterpolator = new FastOutSlowInInterpolator();
                        break;
                }
                ObjectAnimator slideAnimator = createSlideMeAnimator(v, timeInterpolator);
                slideAnimator.start();
            }
        });


        clickMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, View.ROTATION_X, 360 * spinDirection);
                objectAnimator.start();
                spinDirection = spinDirection * -1;
            }
        });

        spinReverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, View.ROTATION_X, 360);
                objectAnimator.setRepeatCount(1);
                objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
                objectAnimator.start();
            }
        });

        spinRotationPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, View.ROTATION_X, 360 * spinDirectionRotationPoint);
                objectAnimator.start();
                spinDirectionRotationPoint = spinDirectionRotationPoint * -1;
            }
        });

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2);
        final ObjectAnimator scalingAnimator = ObjectAnimator.ofPropertyValuesHolder(growingButton, pvhScaleX, pvhScaleY);
        scalingAnimator.setRepeatCount(1);
        scalingAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        growingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scalingAnimator.start();
            }
        });

        movingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float originalX = v.getX();
                float originalY = v.getY();

                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(v, View.Y, originalY, mainView.getBottom() - v.getHeight() * 1.5f);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(v, View.X, originalX, mainView.getRight() - v.getWidth() * 1.5f);
                ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(v, View.Y, mainView.getBottom() - v.getHeight() * 1.5f, mainView.getTop() + v.getHeight() * 0.5f);
                ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(v, View.X, mainView.getRight() - v.getWidth() * 1.5f, originalX);
                ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(v, View.Y, mainView.getTop() + v.getHeight() * 0.5f, originalY);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(objectAnimator1, objectAnimator2, objectAnimator3, objectAnimator4, objectAnimator5);
                animatorSet.start();
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
