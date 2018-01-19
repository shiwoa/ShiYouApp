package org.geometerplus.android.fbreader;

import android.app.ListActivity;
import android.app.SearchManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.geometerplus.android.util.UIUtil;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.fbreader.library.Bookmark;
import org.geometerplus.fbreader.library.Library;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.ui.android.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BookmarksMoueeAcvtivity extends ListActivity {
    private static final int OPEN_ITEM_ID = 0;
    private static final int EDIT_ITEM_ID = 1;
    private static final int DELETE_ITEM_ID = 2;

    private final ZLResource myResource = ZLResource.resource("bookmarksView");
    private final List<Bookmark> myThisBookBookmarks = new LinkedList<Bookmark>();
    List<Bookmark> AllBooksBookmarks;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Thread.setDefaultUncaughtExceptionHandler(new org.geometerplus.zlibrary.ui.android.library.UncaughtExceptionHandler(this));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        final SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        manager.setOnCancelListener(null);

        AllBooksBookmarks = Library.Instance().allBookmarks();
        Collections.sort(AllBooksBookmarks, new Bookmark.ByTimeComparator());
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();

        if (fbreader.Model != null) {
            final long bookId = fbreader.Model.Book.getId();
            for (Bookmark bookmark : AllBooksBookmarks) {
                if (bookmark.getBookId() == bookId) {
                    myThisBookBookmarks.add(bookmark);
                }
            }
            BookmarksAdapter adap = new BookmarksAdapter(this.getListView(), myThisBookBookmarks, true);
        } else {
            Toast.makeText(this, "param error,no book", Toast.LENGTH_LONG).show();
            finish();
        }

        this.getListView().setBackgroundColor(0xeee8aa);
    }

    @Override
    public void onPause() {
        for (Bookmark bookmark : AllBooksBookmarks) {
            bookmark.save();
        }
        super.onPause();
    }


    private void gotoBookmark(Bookmark bookmark) {
        bookmark.onOpen();
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        final long bookId = bookmark.getBookId();
        if ((fbreader.Model == null) || (fbreader.Model.Book.getId() != bookId)) {
            final Book book = Book.getById(bookId);
            if (book != null) {
                finish();
                fbreader.openBook(book, bookmark, null);
            } else {
                UIUtil.showErrorMessage(this, "cannotOpenBook");
            }
        } else {
            finish();
            fbreader.gotoBookmark(bookmark);
        }
    }

    private void addBookmark() {
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        final Bookmark bookmark = fbreader.addBookmark(20, true);
        if (bookmark != null && !myThisBookBookmarks.contains(bookmark)) {
            myThisBookBookmarks.add(0, bookmark);
            AllBooksBookmarks.add(0, bookmark);
            BaseAdapter adapter = (BaseAdapter) getListView().getAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int position = ((AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo()).position;
        final Bookmark bookmark = ((BookmarksAdapter) getListView()
                .getAdapter()).getItem(position);
        switch (item.getItemId()) {
            case OPEN_ITEM_ID:
                gotoBookmark(bookmark);
                return true;
            case DELETE_ITEM_ID:
                bookmark.delete();
                myThisBookBookmarks.remove(bookmark);
                BaseAdapter adapter = (BaseAdapter) getListView().getAdapter();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private final class BookmarksAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, View.OnCreateContextMenuListener {
        private final List<Bookmark> myBookmarks;
        private final boolean myCurrentBook;

        BookmarksAdapter(ListView listView, List<Bookmark> bookmarks, boolean currentBook) {
            myBookmarks = bookmarks;
            myCurrentBook = currentBook;
            listView.setAdapter(this);
            listView.setOnItemClickListener(this);
            listView.setOnCreateContextMenuListener(this);
        }

        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            final int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
            if (getItem(position) != null) {
                menu.setHeaderTitle(getItem(position).getText());
                menu.add(0, OPEN_ITEM_ID, 0, myResource.getResource("open").getValue());
                //menu.add(0, EDIT_ITEM_ID, 0, myResource.getResource("edit").getValue());
                menu.add(0, DELETE_ITEM_ID, 0, myResource.getResource("delete").getValue());
            }
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = (convertView != null) ? convertView :
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
            final ImageView imageView = (ImageView) view.findViewById(R.id.bookmark_item_icon);
            final TextView textView = (TextView) view.findViewById(R.id.bookmark_item_text);
            final TextView bookTitleView = (TextView) view.findViewById(R.id.bookmark_item_booktitle);

            final Bookmark bookmark = getItem(position);
            if (bookmark == null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_list_plus);
                textView.setText(myResource.getResource("new").getValue());
                bookTitleView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.GONE);
                textView.setText(bookmark.getText());
                if (myCurrentBook) {
                    bookTitleView.setVisibility(View.GONE);
                } else {
                    bookTitleView.setVisibility(View.VISIBLE);
                    bookTitleView.setText(bookmark.getBookTitle());
                }
            }
            return view;
        }

        public final boolean areAllItemsEnabled() {
            return true;
        }

        public final boolean isEnabled(int position) {
            return true;
        }

        public final long getItemId(int position) {
            return position;
        }

        public final Bookmark getItem(int position) {

            if (myCurrentBook) {
                --position;
            }
            return (position >= 0) ? myBookmarks.get(position) : null;

//			return myBookmarks.get(position);
        }

        public final int getCount() {
            return myCurrentBook ? myBookmarks.size() + 1 : myBookmarks.size();
//			return myBookmarks.size();
        }

        public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Bookmark bookmark = getItem(position);
            if (bookmark != null) {
                gotoBookmark(bookmark);
            } else {
                addBookmark();
            }
        }
    }
}
