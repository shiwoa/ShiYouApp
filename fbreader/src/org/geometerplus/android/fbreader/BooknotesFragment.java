package org.geometerplus.android.fbreader;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mouee.fbreader.interfaces.Refresh;

import org.geometerplus.android.util.UIUtil;
import org.geometerplus.fbreader.bookmodel.TOCTree;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.fbreader.library.Booknote;
import org.geometerplus.fbreader.library.Library;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.ui.android.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BooknotesFragment extends ListFragment implements Refresh {
    private static final String TAG = "BooknotesFragment";
    private static final int OPEN_ITEM_ID = 0;
    private static final int DELETE_ITEM_ID = 2;

    private final List<Booknote> myThisBookBooknotes = new LinkedList<Booknote>();

    private final ZLResource myResource = ZLResource.resource("booknotesView");
    private BooknotesAdapter adapter;
    private TOCTree tocTree;
    private float down_x, up_x, move_x;
    private boolean closeOnclick = false; // 是否关闭onclick监听 false：否 ，true：是
    private boolean isOpen = true; // 是否执行滑动出现删除按钮 false：不执行使之出现语句 true 执行

    public static Booknote addBooknoteOutside() {
        Booknote Booknote = null;
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        Booknote = fbreader.addBooknote(60, true);
        return Booknote;
    }

    // 出现删除按钮的语句
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_booknotes, container, false);
        ImageView back_read_btn = (ImageView) view.findViewById(R.id.back_read_btn);
        back_read_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (FBReader.menu != null) {
                    FBReader.menu.toggle();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated notesCount=" + myThisBookBooknotes.size());
        refresh();
    }

    public void refresh() {
        myThisBookBooknotes.clear();

        List<Booknote> AllBooksBooknotes = Library.Instance().allBooknotes();
        Collections.sort(AllBooksBooknotes, new Booknote.ByTimeComparator());
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        tocTree = fbreader.Model.TOCTree;

        if (fbreader.Model != null) {
            final long bookId = fbreader.Model.Book.getId();
            for (Booknote Booknote : AllBooksBooknotes) {
                if (Booknote.getBookId() == bookId) {
                    myThisBookBooknotes.add(Booknote);
                }
            }
        }
        adapter = new BooknotesAdapter(getListView(), myThisBookBooknotes, true);
    }

    @Override
    public void onPause() {
        for (Booknote Booknote : myThisBookBooknotes) {
            Booknote.save();
        }
        super.onPause();
    }

    private void invalidateView() {
        getListView().invalidateViews();
        getListView().requestLayout();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int position = ((AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo()).position;
        final Booknote Booknote = ((BooknotesAdapter) getListView()
                .getAdapter()).getItem(position);
        switch (item.getItemId()) {
            case OPEN_ITEM_ID:
                gotoBooknote(Booknote);
                return true;
            case DELETE_ITEM_ID:
                Booknote.delete();
                myThisBookBooknotes.remove(Booknote);
                invalidateView();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void addBooknote() {
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        final Booknote Booknote = fbreader.addBooknote(20, true);
        if (Booknote != null && !myThisBookBooknotes.contains(Booknote)) {
            myThisBookBooknotes.add(0, Booknote);
            invalidateView();
        }
    }

    private void gotoBooknote(Booknote Booknote) {
        Booknote.onOpen();
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        final long bookId = Booknote.getBookId();
        if ((fbreader.Model == null) || (fbreader.Model.Book.getId() != bookId)) {
            // 打开其他书的书签
            final Book book = Book.getById(bookId);
            if (book != null) {
                getActivity().finish();
                fbreader.openBookNote(book, Booknote, null);
            } else {
                UIUtil.showErrorMessage(getActivity(), "cannotOpenBook");
            }
        } else {
            FBReader activity = (FBReader) getActivity();
            activity.getHandler().obtainMessage(FBReader.MSG_WHAT_SWITCH).sendToTarget();
            fbreader.gotoBooknote(Booknote);
        }
    }

    private final class BooknotesAdapter extends BaseAdapter implements
            AdapterView.OnItemClickListener, View.OnCreateContextMenuListener {
        private final List<Booknote> myBooknotes;
        private final boolean myCurrentBook;
        private int paragraphsNumber;

        BooknotesAdapter(ListView listView, List<Booknote> booknotes,
                         boolean currentBook) {
            myBooknotes = booknotes;
            myCurrentBook = currentBook;
            listView.setAdapter(this);
//			listView.setOnItemClickListener(this);
//			listView.setOnCreateContextMenuListener(this);

            FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
            paragraphsNumber = fbreader.Model.getTextModel().getParagraphsNumber();
        }

        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            final int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
            if (getItem(position) != null) {
                menu.setHeaderTitle(getItem(position).getText());
                menu.add(0, OPEN_ITEM_ID, 0, myResource.getResource("open")
                        .getValue());
                menu.add(0, DELETE_ITEM_ID, 0, myResource.getResource("delete")
                        .getValue());
            }
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = (convertView != null) ? convertView
                    : LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.booknote_item, parent, false);
            final ImageView imageView = (ImageView) view
                    .findViewById(R.id.bookmark_item_icon);
            final TextView textView = (TextView) view
                    .findViewById(R.id.bookmark_item_text);
            final TextView bookTitleView = (TextView) view
                    .findViewById(R.id.bookmark_item_booktitle);
            final TextView chapterView = (TextView) view
                    .findViewById(R.id.bookmark_item_chapter);
            final TextView dateView = (TextView) view
                    .findViewById(R.id.bookmark_item_date);
            final TextView rateView = (TextView) view.findViewById(R.id.bookmark_item_rate);

            Button deletBtn = (Button) view.findViewById(R.id.delete_btn);
            deletBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Booknote Booknote = getItem(position);
                    Booknote.delete();
                    myThisBookBooknotes.remove(Booknote);
                    invalidateView();
                    notifyDataSetChanged();
                }
            });
            final LinearLayout deleteLy = (LinearLayout) view
                    .findViewById(R.id.delete_ly);
            final LinearLayout animLy = (LinearLayout) view.findViewById(R.id.anim_ly);
            //滑动删除
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (deleteLy.getVisibility() == View.VISIBLE) {
                        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.closeanim);
                        animLy.setAnimation(anim);
                        anim.setAnimationListener(new AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                deleteLy.setVisibility(View.INVISIBLE);
                                animLy.setVisibility(View.INVISIBLE);

                            }
                        });
                        animLy.setVisibility(View.VISIBLE);
                        closeOnclick = true;
                        isOpen = false;
                        return;
                    } else {
                        final Booknote Booknote = getItem(position);
                        if (Booknote != null) {
                            gotoBooknote(Booknote);
                        } else {
                            addBooknote();
                        }
                    }

                }
            });
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int action = event.getAction();

                    switch (action) {
                        case MotionEvent.ACTION_DOWN:

                            down_x = event.getX();
                            closeOnclick = false;
                            isOpen = true;
                            return closeOnclick;

                        case MotionEvent.ACTION_UP:
                            up_x = event.getX();

                            return closeOnclick;

                        case MotionEvent.ACTION_MOVE:
                            move_x = event.getX();
                            float distance = Math.abs(move_x - down_x);

                            if (distance > 20 && isOpen) {
                                if (deleteLy.getVisibility() == View.INVISIBLE) {// 如果隐藏
                                    Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.openanim);
                                    animLy.setAnimation(anim);
                                    anim.setAnimationListener(new AnimationListener() {

                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            animLy.setVisibility(View.INVISIBLE);

                                        }
                                    });
                                    animLy.setVisibility(View.VISIBLE);
                                    deleteLy.setVisibility(View.VISIBLE);
                                    closeOnclick = true;
                                    isOpen = false;
                                } else {
                                    Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.closeanim);
                                    animLy.setAnimation(anim);
                                    anim.setAnimationListener(new AnimationListener() {

                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            deleteLy.setVisibility(View.INVISIBLE);
                                            animLy.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                    animLy.setVisibility(View.VISIBLE);
                                    closeOnclick = true;
                                    isOpen = false;
                                }

                            }
                            break;

                    }
                    return false;
                }
            });
            final Booknote Booknote = getItem(position);
            if (Booknote == null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_list_plus);
                textView.setText(myResource.getResource("new").getValue());
                bookTitleView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.GONE);
                textView.setText(Booknote.getText());
                //开始获取章节信息
                int paragraphIndex = Booknote.getParagraphIndex() + 1;//+1是为了避免在某个章节开始处添加书签导致显示为上一个章节
                double rate = (double) paragraphIndex * 100.0d / (double) paragraphsNumber;
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String rateStr = decimalFormat.format(rate);
                rateView.setText(rateStr + "%");

                TOCTree resultTree = null;
                for (TOCTree tree : tocTree) {
                    final TOCTree.Reference reference = tree.getReference();
                    if (reference == null) {
                        continue;
                    }
                    if (reference.ParagraphIndex > paragraphIndex) {
                        break;
                    }
                    resultTree = tree;
                }
                if (resultTree != null) {
                    String chapterText = resultTree.getText();
                    chapterView.setText(chapterText);
                }
                //设置创建时间信息
                String pattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                String dateStr = format.format(Booknote.getTime(Booknote.CREATION));
                dateView.setText(dateStr);
                if (myCurrentBook) {
                    bookTitleView.setVisibility(View.GONE);
                } else {
                    bookTitleView.setVisibility(View.VISIBLE);
                    bookTitleView.setText(Booknote.getBookTitle());
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

        public final Booknote getItem(int position) {
            /*
			 * //源码：用于显示“添加书签”项 if (myCurrentBook) { --position; }
			 */
            return (position >= 0) ? myBooknotes.get(position) : null;
        }

        public final int getCount() {
            // 源码
            // return myCurrentBook ? myBooknotes.size() + 1 :
            // myBooknotes.size();
            return myBooknotes.size();
        }

        public final void onItemClick(AdapterView<?> parent, View view,
                                      int position, long id) {
            final Booknote Booknote = getItem(position);
            if (Booknote != null) {
                gotoBooknote(Booknote);
            } else {
                addBooknote();
            }
        }
    }
}
