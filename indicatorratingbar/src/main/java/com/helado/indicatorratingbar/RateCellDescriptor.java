package com.helado.indicatorratingbar;

import com.helado.indicatorratingbar.utils.RateCellType;

/**
 * Rate cell data model
 */
public class RateCellDescriptor {
    private final String value;

    public String getValue() {
        return value;
    }

    public boolean getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlight) {
        highlighted = highlight;
    }

    private boolean highlighted;

    public int getCellColor() {
        return cellColor;
    }

    public void setCellColor(int cellColor) {
        this.cellColor = cellColor;
    }

    private int cellColor;

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    private int highlightColor;

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    private int textColor;

    public RateCellType getCellType() {
        return cellType;
    }

    public void setCellType(RateCellType cellType) {
        this.cellType = cellType;
    }

    private RateCellType cellType;

    public RateCellDescriptor(String value, RateCellType cellType) {
        this.value = value;
        this.cellType = cellType;
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class RateCellDescriptor {\n");
        sb.append("  value: ").append(value).append("\n");
        sb.append("  textColor: ").append(textColor).append("\n");
        sb.append("  cellColor: ").append(cellColor).append("\n");
        sb.append("  cellType: ").append(cellType).append("\n");
        sb.append("  isHighlighted: ").append(highlighted).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
