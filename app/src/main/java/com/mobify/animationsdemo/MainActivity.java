package com.mobify.animationsdemo;

import android.animation.Animator;
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
import android.view.ViewPropertyAnimator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class MainActivity extends Activity {

    private int spinDirection = 1;
    private int spinDirectionRotationPoint = 1;

    private void performSlide(View v, TimeInterpolator timeInterpolator) {
        float xLocation = v.getX();

        float buttonWidth = v.getWidth();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        float halfWidth = width/2.0f;

        float endLocation;
        if (xLocation < halfWidth) {
            endLocation = xLocation + width - (buttonWidth * 1.5f);
        } else {
            endLocation = xLocation - (width - (buttonWidth * 1.5f));
        }

        v.animate().x(endLocation).setInterpolator(timeInterpolator);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup mainView = (ViewGroup) findViewById(R.id.mainView);
        final Button slideMeButton = (Button) findViewById(R.id.slideMeButton);

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
                performSlide(v, null);
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
                performSlide(v, timeInterpolator);
            }
        });


        clickMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().rotationX(360 * spinDirection);
                spinDirection = spinDirection * -1;
            }
        });

        spinReverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinReverseButton.animate().rotationX(360).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        spinReverseButton.animate().rotationX(-360).setListener(null);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });

        spinRotationPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().rotationX(360 * spinDirectionRotationPoint);
                spinDirectionRotationPoint = spinDirectionRotationPoint * -1;
            }
        });

        growingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                growingButton.animate().scaleX(2.0f).scaleY(2.0f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        growingButton.animate().scaleX(1.0f).scaleY(1.0f);
                    }
                });
            }
        });

        // To use ViewPropertyAnimator here could chain listeners or chain calls to withEndAction
        // Seems much more complex than using ObjectAnimators
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
