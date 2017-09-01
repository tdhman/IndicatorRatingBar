package com.helado.indicatorratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by hueman.trandiep on 01/09/2017.
 * Copyright Alteom@2017
 */

public class RatingCell extends FrameLayout {
    private boolean isSquare;

    public RatingCell(Context context) {
        super(context);
        init(null);
    }

    public RatingCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RatingCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public static RatingCell create(ViewGroup parent, LayoutInflater inflater, RateCellDescriptor cellDescriptor, Typeface rateTypeFace) {

        int layoutId = R.layout.rating_cell;
        if (cellDescriptor.getCellType() == RateCellType.SQUARE)
            layoutId = R.layout.rating_square_cell;

        final RatingCell view = (RatingCell) inflater.inflate(layoutId, parent, false);
        view.setBackgroundColor(cellDescriptor.getCellColor());

        TextView rateTextView = (TextView) view.findViewById(R.id.rate_text);
        rateTextView.setText(cellDescriptor.getValue().toString());
        rateTextView.setTextColor(cellDescriptor.getTextColor());

        if (rateTypeFace != null)
            rateTextView.setTypeface(rateTypeFace);

        return view;
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RatingCell);
            isSquare = a.getBoolean(R.styleable.RatingCell_layout_square, false);
            a.recycle();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isSquare) {
            int width = this.getMeasuredWidth();
            int height = this.getMeasuredHeight();
            int size = Math.max(width, height);
            int widthSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
            super.onMeasure(widthSpec, heightSpec);
            setMeasuredDimension(widthSpec, heightSpec);
        }
    }
}