package com.mouee.fbreader.data;

public class SearchResult {
    private int startIndex;
    private int queryLength;
    private String content;

    public SearchResult(int startIndex, int queryLength, String content) {
        super();
        this.startIndex = startIndex;
        this.queryLength = queryLength;
        this.content = content;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getQueryLength() {
        return queryLength;
    }

    public String getContent() {
        return content;
    }
}
