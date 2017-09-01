package com.helado.customratingview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.helado.indicatorratingbar.IndicatorRatingBar;
import com.helado.indicatorratingbar.RateCellType;
import com.helado.indicatorratingbar.RatingView;
import com.helado.indicatorratingbar.utils.IndicatorPosition;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        IndicatorRatingBar indicatorRatingBar = (IndicatorRatingBar) findViewById(R.id.indicator_rating_view_3);
        indicatorRatingBar.setRateList(Arrays.asList("New star", "Rising star", "Super star"));
        indicatorRatingBar.setSelectedRate("Super star");
    }

}
