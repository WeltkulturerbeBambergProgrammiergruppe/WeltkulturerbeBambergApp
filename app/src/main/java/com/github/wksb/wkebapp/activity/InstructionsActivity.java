package com.github.wksb.wkebapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.wksb.wkebapp.R;

/**
 * This activity shows a greeting and instructions how to use the World-heritage-Application.
 *
 * @author Projekt-Seminar "Schnitzeljagd World-heritage" 2015/2016 des Clavius Gymnasiums Bamberg
 * @version 1.0
 * @since 2015-06-04
 */
public class InstructionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    /**
     * Reset the previous Tour
     */
    private void resetPreviousTour() {
        // Reset SharedPreferences
        getSharedPreferences("TOUR", MODE_PRIVATE).edit().putBoolean("IS_IN_PROGRESS", false).commit();
        getSharedPreferences("TOUR", MODE_PRIVATE).edit().putInt("PROGRESS", 1).commit();
        getSharedPreferences("TOUR", MODE_PRIVATE).edit().putInt("CURRENT_QUIZ_ID", -1).commit();
        getSharedPreferences("TOUR", MODE_PRIVATE).edit().putString("ROUTE_NAME", null).commit();
    }

    //TODO Documentation
    public void onBtnClickShortRoute(View view) {
        resetPreviousTour();

        getSharedPreferences("TOUR", MODE_PRIVATE).edit().putString("ROUTE_NAME", "short").commit();
        Intent startShortRoute = new Intent(this, NavigationActivity.class);
        startActivity(startShortRoute);
    }

    //TODO Documentation
    public void onBtnClickLongRoute(View view) {
        resetPreviousTour();

        getSharedPreferences("TOUR", MODE_PRIVATE).edit().putString("ROUTE_NAME", "long").commit();
        Intent startLongRoute = new Intent(this, NavigationActivity.class);
        startActivity(startLongRoute);
    }
}