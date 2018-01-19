package com.mouee.fbreader.activity;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.linthink.reader.ReaderConfig;
import com.mouee.fbreader.data.ReaderGlobal;
import com.mouee.fbreader.data.SearchResult;
import com.tider.android.common.StringUtils;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.android.util.UIUtil;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.zlibrary.ui.android.R;

import java.util.List;

public class TextSearchActivity extends ListActivity implements OnQueryTextListener {
    public static final String SP_NAME_CONFIG = "textSearchConfig";
    private static final String SP_KEY_TEXT_QUERY = "textQuery";
    private static final String SP_KEY_EMPTY_TIP = "emptyTip";
    private static final String STATE_KEY = "stateKey";
    private static List<SearchResult> searchResultList;
    private BaseAdapter adapter;
    private FBReaderApp fbReader;
    private SearchView searchView;
    private boolean saveState = false;

    public static void setSearchResult(List<SearchResult> result) {
        searchResultList = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_search);
        initSearchView();
        if (savedInstanceState != null) {
            searchResultList = ReaderGlobal.getSearchResultList();
        }
        adapter = new TextSearchAdapter(this, searchResultList);
        fbReader = (FBReaderApp) FBReaderApp.Instance();
    }

    private void initSearchView() {
        Button cancel_btn = (Button) findViewById(R.id.cancel_btn);
        searchView = (SearchView) findViewById(R.id.searchView);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);
        searchView.requestFocus();
        cancel_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        if (!StringUtils.isEmpty(text)) {
            searchView.setQuery(text, true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setQueryAndEmptyTip();
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TextSearchActivity.this, FBReader.class);
                intent.setAction(FBReader.ACTION_GO_TO_SEARCH_ITEM);
                intent.putExtra(FBReader.EXTRA_SEARCH_RESULT_INDEX, position);
                startActivity(intent);
            }
        });
    }

    private void setQueryAndEmptyTip() {
        SharedPreferences preferences = getSharedPreferences(SP_NAME_CONFIG, MODE_PRIVATE);
        String savedQuery = preferences.getString(SP_KEY_TEXT_QUERY, "");
        searchView.setQuery(savedQuery, false);

//        String emptyTip = preferences.getString(SP_KEY_EMPTY_TIP, getResources().getString(R.string.text_search_tip));
//        setEmptyTip(emptyTip);
    }

    private void setEmptyTip(String tip) {
        TextView emptyView = (TextView) getListView().getEmptyView();
        emptyView.setText(tip);
    }

    @Override
    protected void onPause() {
        saveQueryAndEmptyTip();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveState = true;
        outState.putBoolean(STATE_KEY, saveState);
        ReaderGlobal.setSearchResultList(searchResultList);
        super.onSaveInstanceState(outState);
    }

    private void saveQueryAndEmptyTip() {
        Editor edit = getSharedPreferences(SP_NAME_CONFIG, MODE_PRIVATE).edit();
        String query = searchView.getQuery().toString();
        edit.putString(SP_KEY_TEXT_QUERY, query);

        if (TextUtils.isEmpty(query)) {
            edit.putString(SP_KEY_EMPTY_TIP, null);
        } else {
            TextView emptyView = (TextView) getListView().getEmptyView();
            String emptyTip = emptyView.getText().toString();
            edit.putString(SP_KEY_EMPTY_TIP, emptyTip);
        }
        edit.apply();
    }

    private void search(final String pattern) {
        Log.i("werafasdf", "的发送到发送");
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            final String pattern = intent.getStringExtra(SearchManager.QUERY);
        Runnable runnable = new Runnable() {
            public void run() {
                if (fbReader.getTextView().search(pattern, true, false,
                        false, false) == 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            setEmptyTip(getResources().getString(R.string.text_search_empty_tip));
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        adapter = new TextSearchAdapter(TextSearchActivity.this, searchResultList);
                        getListView().setAdapter(adapter);
                    }
                });
            }
        };
        UIUtil.wait("search", runnable, this);
//        } else {
//            super.onNewIntent(intent);
//            searchView.clearFocus();
//        }
    }

    private void clearSearchResult() {
        if (searchResultList != null) {
            searchResultList.clear();
            searchResultList = null;
            Log.d(TextSearchActivity.class.getSimpleName(), "clearSearchResult runs");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSearchResult();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        search(query);
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ReaderConfig.instance().textSearchAcitivtySlideInUp()) {
            overridePendingTransition(0, R.anim.slide_out_down);
        }
    }

    class TextSearchAdapter extends BaseAdapter {
        private Context context;
        private List<SearchResult> searchResultList;

        public TextSearchAdapter(Context context, List<SearchResult> searchResultList) {
            super();
            this.context = context;
            this.searchResultList = searchResultList;
        }

        @Override
        public int getCount() {
            return searchResultList == null ? 0 : searchResultList.size();
        }

        @Override
        public SearchResult getItem(int position) {
            return searchResultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.text_search_result_item, null);
            }
            TextView tv = (TextView) convertView;
            setItem(position, tv);
            return tv;
        }

        private void setItem(int position, TextView tv) {
            try {
                SearchResult result = getItem(position);
                SpannableStringBuilder style = new SpannableStringBuilder(result.getContent());
                int start = result.getStartIndex();
                int end = start + result.getQueryLength();
                style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(style);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
