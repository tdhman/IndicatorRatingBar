package com.helado.indicatorratingbar;

import android.content.Context;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helado.indicatorratingbar.utils.IndicatorPosition;

import java.util.List;


/**
 * Custom rating view with the indicator bar.
 */
public class RatingView extends LinearLayout implements RatingGridEvent {

    private TextView mIndicatorTextView;
    private ImageView mIndicatorImage;
    private RatingGrid mRatingGrid;
    private ConstraintLayout mConstraintLayout;

    private int cellMinColor;
    private int cellMaxColor;
    private int cellHighlightColor;
    private int textLightColor;
    private int textDarkColor;

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        cellMinColor = ContextCompat.getColor(context, R.color.colorPrimaryLight);
        cellMaxColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        cellHighlightColor = ContextCompat.getColor(context, R.color.colorAccent);
        textLightColor = ContextCompat.getColor(context, R.color.white);
        textDarkColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIndicatorTextView = (TextView) findViewById(R.id.rating_title);
        mIndicatorImage = (ImageView) findViewById(R.id.rating_indicator);
        mRatingGrid = (RatingGrid) findViewById(R.id.rating_grid);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.layout_rating_view);
    }

    /**
     * The rating bar will be constructed using the given min rate and max rate value. This means
     * that all values of rating bar will be in range of (minRate, maxRate).
     * <p></p>
     * Set the cell type is either RateCellType.SQUARE or RateCellType.RECTANGLE.
     * Set the indicator title and the position [UP, DOWN] of the indicator image.
     * <p></p>
     * @param title The indicator title text.
     * @param minRate The min rate value, inclusive. Must be less than {@code maxRate} and greater than 0.
     * @param maxRate The max rate value, inclusive. Must be greater than {@code minRate} and greater than 0.
     * @param selectedRate The highlight rate value. Must be between than {@code minRate} and {@code maxRate}.
     * @param cellType
     * @param indicatorPosition
     */
    public void init(String title, Integer minRate, Integer maxRate, Integer selectedRate, Integer highlightColor, RateCellType cellType, IndicatorPosition indicatorPosition) {

        if (minRate == null || maxRate == null) {
            throw new IllegalArgumentException("minRate and maxRate must not be null.");
        } else if (minRate.intValue() == 0 || maxRate.intValue() == 0) {
            throw new IllegalArgumentException("minRate and maxRate must be greater than 0.");
        } else if (minRate == maxRate) {
            throw new IllegalArgumentException("maxRate must be greater than minRate.");
        } else if (selectedRate != null) {
            if (selectedRate < minRate || selectedRate > maxRate)
                throw new IllegalArgumentException("selectedRate must be between minRate and maxRate (inclusively).");
        }

        cellHighlightColor = highlightColor;

        // Initialize the grid rating cell (1-row grid)
        mRatingGrid.setRatingGridEvent(this);
        mRatingGrid.initColors(cellMinColor, cellMaxColor, cellHighlightColor, textLightColor, textDarkColor, null);
        mRatingGrid.init(minRate, maxRate, selectedRate, cellType);

        refreshIndicatorLayout(indicatorPosition);

        // Set indicator title
        mIndicatorTextView.setText(title);
    }

    /**
     * The rating bar will be constructed by a list of rating values.
     * <p></p>
     * Set the cell type is either RateCellType.SQUARE or RateCellType.RECTANGLE.
     * Set the indicator title and the position [UP, DOWN] of the indicator image.
     * <p></p>
     * @param title The indicator title text.
     * @param rates The list of rating values to be generated
     * @param selectedRate The highlight rate value. Must be defined in the list {@code rates}
     * @param cellType
     * @param indicatorPosition
     */
    public void init(String title, List<String> rates, String selectedRate, Integer highlightColor, RateCellType cellType, IndicatorPosition indicatorPosition) {

        if (rates.size() < 2) {
            throw new IllegalArgumentException("Rating list must have at least 2 items.");
        } else if (selectedRate != null && !rates.contains(selectedRate)) {
            throw new IllegalArgumentException("Rating list must contain the selected rate value.");
        }

        cellHighlightColor = highlightColor;

        // Initialize the grid rating cell (1-row grid)
        mRatingGrid.setRatingGridEvent(this);
        mRatingGrid.initColors(cellMinColor, cellMaxColor, cellHighlightColor, textLightColor, textDarkColor, null);
        mRatingGrid.init(rates, selectedRate, cellType);

        refreshIndicatorLayout(indicatorPosition);

        // Set indicator title
        mIndicatorTextView.setText(title);
    }

    /**
     * Add listener to the layout of rating gridview to detect
     * if all child views are visible then set position of rating indicator
     * based on the highlight cell in gridview
     * @param indicatorPosition
     */
    private void refreshIndicatorLayout(final IndicatorPosition indicatorPosition){
        positionConstraintLayout(indicatorPosition);

        mRatingGrid.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldleft, int oldtop, int oldright, int oldbottom) {
                // Calculate position of text & image indicator
                // based on the center of highlight cell
                View cell = mRatingGrid.getHighlightCell();
                if (cell != null) {
                    float centerX = cell.getX() + cell.getWidth() / 2;
                    float posTextX = centerX - mIndicatorTextView.getWidth() / 2;

                    // Check if textview overflows the parent's border
                    // then reset position
                    if (posTextX > 0) {

                        if (posTextX > (view.getWidth() - mIndicatorTextView.getWidth()))
                            posTextX = view.getWidth() - mIndicatorTextView.getWidth();

                        mIndicatorTextView.setX(posTextX);
                        mIndicatorTextView.setVisibility(VISIBLE);
                    }
                    mIndicatorImage.setX(centerX - mIndicatorImage.getWidth() / 2);
                    mIndicatorImage.setVisibility(VISIBLE);
                }

                //mRatingGrid.removeOnLayoutChangeListener(this);
            }
        });
    }

    /**
     * Default indicator position is DOWN
     * Only change constraint layout if UP position
     * @param indicatorPosition
     */
    private void positionConstraintLayout(IndicatorPosition indicatorPosition){

        if (indicatorPosition == IndicatorPosition.UP) {
            ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout_rating_view);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(mRatingGrid.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(mIndicatorImage.getId(), ConstraintSet.TOP, mRatingGrid.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(mIndicatorTextView.getId(), ConstraintSet.TOP, mIndicatorImage.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    public void setTextColor(int color) {
        mIndicatorTextView.setTextColor(color);
    }

    public void setTypeface(Typeface typeface) {
        mIndicatorTextView.setTypeface(typeface);
    }

    public void setIndicatorImageRes(int imageRes) {
        mIndicatorImage.setImageResource(imageRes);
    }

    public void setHighlightRate(String rate){
        mRatingGrid.setHighlightRate(rate);
    }
}
