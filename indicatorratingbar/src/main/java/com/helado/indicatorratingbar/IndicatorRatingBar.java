package com.helado.indicatorratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.helado.indicatorratingbar.utils.IndicatorPosition;
import com.helado.indicatorratingbar.utils.RateCellType;
import com.helado.indicatorratingbar.utils.RateType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class IndicatorRatingBar extends LinearLayout {

    private boolean selectable;
    private String indicatorTitle;
    private IndicatorPosition indicatorPosition;
    private RateType rateType;
    private RateCellType rateCellType;
    private List<String> rateList = new ArrayList<>();
    private int minRate;
    private int maxRate;
    private int selectedRate;

    private RatingView ratingView;

    public IndicatorRatingBar(Context context) {
        super(context);
        setupAttributes(null);
        setupView();
    }

    public IndicatorRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
        setupView();
    }

    public IndicatorRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes(attrs);
        setupView();
    }

    private void setupView(){
        View view = inflate(getContext(), R.layout.rating_view, null);
        addView(view);
        if (rateType == RateType.NUMERIC) {
            createNumericRateBar();
        }
    }

    private void createNumericRateBar(){
        ratingView = (RatingView) findViewById(R.id.rating_view);
        ratingView.init(indicatorTitle, minRate, maxRate, selectedRate, ContextCompat.getColor(getContext(), R.color.colorAccent), rateCellType, indicatorPosition);
        setupIndicatorBar();
    }

    private void createCustomRateBar(){
        ratingView = (RatingView) findViewById(R.id.rating_view);
        ratingView.init(indicatorTitle, rateList, null, ContextCompat.getColor(getContext(), R.color.colorAccent), rateCellType, indicatorPosition);
        setupIndicatorBar();
    }

    private void setupAttributes(AttributeSet attrs) {
        // Create view

        // Set attributes
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IndicatorRatingBar);

            try {
                selectable = a.getBoolean(R.styleable.IndicatorRatingBar_selectable, true);
                indicatorTitle = a.getString(R.styleable.IndicatorRatingBar_indicatorTitle);

                int type = a.getInteger(R.styleable.IndicatorRatingBar_rateType, 0);
                if (type == 0) {
                    rateType = RateType.NUMERIC;
                    minRate = a.getInteger(R.styleable.IndicatorRatingBar_minRate, 0);
                    maxRate = a.getInteger(R.styleable.IndicatorRatingBar_maxRate, 0);
                    selectedRate = a.getInteger(R.styleable.IndicatorRatingBar_selectedRate, 0);
                } else {
                    rateType = RateType.CUSTOM;
                }

                int cell = a.getInteger(R.styleable.IndicatorRatingBar_rateShape, 0);
                if (cell == 0) {
                    rateCellType = RateCellType.RECTANGLE;
                } else {
                    rateCellType = RateCellType.SQUARE;
                }

                int position = a.getInteger(R.styleable.IndicatorRatingBar_indicatorPosition, 0);
                if (position == 0) {
                    indicatorPosition = IndicatorPosition.UP;
                } else {
                    indicatorPosition = IndicatorPosition.DOWN;
                }

            } finally {
                a.recycle();
            }
        }
    }

    public void setupIndicatorBar(){
        ratingView.setCellSelectable(selectable);
        if (indicatorPosition == IndicatorPosition.DOWN) {
            ratingView.setIndicatorImageRes(R.drawable.indicator_triangle_down);
            ratingView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        } else {
            ratingView.setIndicatorImageRes(R.drawable.indicator_triangle_up);
            ratingView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
        }
    }

    public void setRateList(List<String> rateList) {
        this.rateList = rateList;
        createCustomRateBar();
    }

    public void setSelectedRate(String rate) {
        if (ratingView != null)
            ratingView.setHighlightRate(rate);
    }
}
