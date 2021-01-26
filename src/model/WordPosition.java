package model;

public class WordPosition {
    private long lineOffset;
    private long charOffset;

    public WordPosition(long lineOffset, long charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
    }
    @Override
    public String toString() {
        return "[lineOffset=" + lineOffset +", charOffset=" + charOffset +']';
    }
}
