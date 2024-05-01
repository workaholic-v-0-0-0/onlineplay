package online.caltuli.model;

public enum ColumnNumber {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX;


    public int toInt() {
        return this.ordinal();
    }

    public static ColumnNumber intToColumnNumber(int i) {
        return ColumnNumber.values()[i];
    }
}

