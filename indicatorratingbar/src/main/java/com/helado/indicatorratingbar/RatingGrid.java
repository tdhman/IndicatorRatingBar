package com.helado.indicatorratingbar;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.helado.indicatorratingbar.utils.RateCellType;
import com.helado.indicatorratingbar.utils.RateColorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Android component to visualize the square rating bar with text indicator on highlight rate. Can be
 * initialized after inflation with {@link #init(Integer, Integer, Integer, RateCellType)}.
 */
public class RatingGrid extends GridView {

    private static final int CELL_MARGIN = 15;
    private static final int COLOR_ALPHA = 255;

    private final RatingGrid.CellAdapter adapter = new CellAdapter();
    private final List<RateCellDescriptor> cells = new ArrayList<>();

    private View highlightCellView;
    private RatingGridEvent ratingGridEvent;

    private int cellMinColor;
    private int cellMaxColor;
    private int cellHighlightColor;
    private int rateTextLightColor;
    private int rateTextDarkColor;
    private Typeface rateTypeFace;

    public RatingGrid(Context context) {
        super(context);
    }

    public RatingGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * The grid view will be constructed using the given min rate and max rate value.
     * Generate the grid cells in the range (minRate, maxRate) and set the grid columns
     * to be the number of cells to fit in 1 row.
     * <p></p>
     * By default, the color of rating cells is set by min rate color and max rate color differently
     * by using the different alpha value based on the position of rating cell.
     * Since the rating cell's background color is set from light color to dark color, their text color will be
     * also set by the brightness of background color. The bright cell will have dark text color and vice-versa.
     * <p></p>
     * @param minRate The min rate value, inclusive. Must be less than {@code maxRate} and greater than 0.
     * @param maxRate The max rate value, inclusive. Must be greater than {@code minRate} and greater than 0.
     * @param selectedRate Set the highlight rate. Must be between than {@code minRate} and {@code maxRate}.
     */
    public void init(Integer minRate, Integer maxRate, Integer selectedRate, RateCellType cellType) {

        // Create and initialize rating list values
        List<String> rates = new ArrayList<>();
        for(int i=minRate; i<=maxRate; i++){
            rates.add(Integer.toString(i));
        }
        if (selectedRate != null)
            init(rates, Integer.toString(selectedRate), cellType);
        else
            init(rates, "", cellType);
    }

    /**
     * The grid view will be constructed using the list of rating values.
     * Generate the grid cells by the list items and set the grid columns
     * to be the number of cells to fit in 1 row.
     * <p></p>
     * By default, the color of rating cells is set by min rate color and max rate color differently
     * by using the different alpha value based on the position of rating cell.
     * Since the rating cell's background color is set from light color to dark color, their text color will be
     * also set by the brightness of background color. The bright cell will have dark text color and vice-versa.
     * <p></p>
     * @param rates The array list of rating values.
     * @param selectedRate Set the highlight rate. Must be between than {@code minRate} and {@code maxRate}.
     */
    public void init(List<String> rates, String selectedRate, RateCellType cellType) {
        this.cells.clear();

        // Calculate and set color for the first half of ratings
        // with the cell's minColor
        int middle = (int)Math.ceil((double) rates.size() / 2);
        int alphaRange = COLOR_ALPHA/middle;
        int alpha = 0;

        for (int i=0; i<middle; i++) {
            RateCellDescriptor cell = new RateCellDescriptor(rates.get(i), cellType);

            alpha += alphaRange;
            int color = ColorUtils.setAlphaComponent(cellMinColor, alpha);
            cell.setCellColor(color);
            cell.setHighlightColor(cellHighlightColor);
            cell.setTextColor(getCellTextColor(color));

            if (selectedRate != null && selectedRate == cell.getValue()) {
                cell.setHighlighted(true);
                cell.setTextColor(getCellTextColor(cellHighlightColor));
            }

            cells.add(cell);
        }

        // Calculate and set color for the first half of ratings
        // with the cell's maxColor
        alphaRange = (int)Math.ceil((double) COLOR_ALPHA/(rates.size()-middle));
        alpha = 0;
        for (int i=middle; i<rates.size(); i++) {
            RateCellDescriptor cell = new RateCellDescriptor(rates.get(i), cellType);

            alpha += alphaRange;
            int color = ColorUtils.setAlphaComponent(cellMaxColor, alpha);
            cell.setCellColor(color);
            cell.setHighlightColor(cellHighlightColor);
            cell.setTextColor(getCellTextColor(color));

            if (selectedRate != null && selectedRate == cell.getValue()) {
                cell.setHighlighted(true);
                cell.setTextColor(getCellTextColor(cellHighlightColor));
            }

            cells.add(cell);
        }

        // Set gridview columns properties
        setNumColumns(cells.size());
        setStretchMode(STRETCH_COLUMN_WIDTH);
        setHorizontalSpacing(CELL_MARGIN);

        validateAndUpdate();
    }

    /**
     * Initialize color properties
     */
    public void initColors(Integer minRateColor, Integer maxRateColor, Integer highlightColor, Integer textLightColor, Integer textDarkColor, Typeface typeFace){
        cellMinColor = minRateColor;
        cellMaxColor = maxRateColor;
        cellHighlightColor = highlightColor;
        rateTextLightColor = textLightColor;
        rateTextDarkColor = textDarkColor;
        rateTypeFace = typeFace;
    }

    public void setHighlightRate(String rate){
        for(int i=0; i<cells.size(); i++){
            RateCellDescriptor cell = cells.get(i);
            if (cell.getValue() == rate) {
                cell.setHighlighted(true);
                cell.setTextColor(getCellTextColor(cell.getHighlightColor()));
            } else {
                cell.setHighlighted(false);
                cell.setTextColor(getCellTextColor(cell.getCellColor()));
            }
        }
        validateAndUpdate();
    }

    public void setRatingGridEvent(RatingGridEvent event) {
        ratingGridEvent = event;
    }

    public void setCellSelectable(boolean selectable) {

        validateAndUpdate();
    }

    public View getHighlightCell() {
        return highlightCellView;
    }

    private int getCellTextColor(int color) {
        if (RateColorUtils.isColorDark(color)) {
            return rateTextLightColor;
        } else {
            return rateTextDarkColor;
        }
    }

    private void validateAndUpdate() {
        if (getAdapter() == null) {
            setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private class CellAdapter extends BaseAdapter {
        private final LayoutInflater inflater;

        private CellAdapter() {
            inflater = LayoutInflater.from(getContext());
        }

        @Override public boolean isEnabled(int position) {
            // Enable/disable item click
            return true;
        }

        @Override public int getCount() {
            return cells.size();
        }

        @Override public Object getItem(int position) {
            return cells.get(position);
        }

        @Override public long getItemId(int position) {
            return position;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            final RatingCell ratingCell;
            final RateCellDescriptor cell = cells.get(position);

            if (convertView == null) {
                ratingCell = RatingCell.create(parent, inflater, cell, rateTypeFace);
                ratingCell.setTag(R.id.rating_cell_class, ratingCell.getClass());
            } else {
                ratingCell = (RatingCell)convertView;
            }

            if(cell.getHighlighted()) {
                highlightCellView = ratingCell;
                ratingCell.setBackgroundColor(cell.getHighlightColor());
            } else {
                ratingCell.setBackgroundColor(cell.getCellColor());
            }

            ratingCell.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setHighlightRate(cell.getValue());
                    highlightCellView = ratingCell;
                    invalidateViews();
                }
            });

            return ratingCell;
        }
    }
}
