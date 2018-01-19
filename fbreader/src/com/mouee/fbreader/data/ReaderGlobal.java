package com.mouee.fbreader.data;

import java.util.List;


public class ReaderGlobal {
    private static List<SearchResult> searchResultList;

    public static List<SearchResult> getSearchResultList() {
        return searchResultList;
    }

    public static void setSearchResultList(List<SearchResult> searchResultList) {
        ReaderGlobal.searchResultList = searchResultList;
    }
}
