package online.caltuli.business.ai;

public enum Column {
    C0(0), C1(1), C2(2), C3(3), C4(4), C5(5), C6(6);

    private final int index;

    Column(int index) {
        this.index = index;
    }
    public int getIndex() {
        return this.index;
    }
    public static Column fromIndex(int index) {
        for (Column col : Column.values()) {
            if (col.getIndex() == index) {
                return col;
            }
        }
        throw new IllegalArgumentException("Invalid column index: " + index);
    }
}