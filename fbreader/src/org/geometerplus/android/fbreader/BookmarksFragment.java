package org.geometerplus.android.fbreader;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
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
import org.geometerplus.fbreader.library.Bookmark;
import org.geometerplus.fbreader.library.Library;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.ui.android.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BookmarksFragment extends ListFragment implements Refresh {
    private static final String TAG = "BookmarksFragment";
    private static final int OPEN_ITEM_ID = 0;
    private static final int DELETE_ITEM_ID = 2;

    private final List<Bookmark> myThisBookBookmarks = new LinkedList<Bookmark>();

    private final ZLResource myResource = ZLResource.resource("bookmarksView");
    private BookmarksAdapter adapter;
    private TOCTree tocTree;
    private float down_x, up_x, move_x;
    private boolean closeOnclick = false; // 是否关闭onclick监听 false：否 ，true：是
    private boolean isOpen = true; // 是否执行滑动出现删除按钮 false：不执行使之出现语句 true 执行
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
//			switch(msg.what){
//			case ShareConstants.COMPLETE:
//				if (getActivity() != null) {
//					Toast.makeText(getActivity(), R.string.share_completed, Toast.LENGTH_SHORT).show();
//				}
//				break;
//			case ShareConstants.ERROR:
//				if (getActivity() != null){
//					Toast.makeText(getActivity(), R.string.share_failed, Toast.LENGTH_SHORT).show();
//				}
//				break;
//			case ShareConstants.CANCEL:
//				if (getActivity() != null){
//					Toast.makeText(getActivity(), R.string.share_canceled, Toast.LENGTH_SHORT).show();
//				}
//				break;
//			}
            super.handleMessage(msg);
        }
    };

    public static Bookmark addBookmarkOutside() {
        Bookmark bookmark = null;
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        bookmark = fbreader.addBookmark(60, true);
        return bookmark;
    }

    // 出现删除按钮的语句
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_bookmarks, container, false);
        ImageView back_read_btn = (ImageView) view.findViewById(R.id.back_read_btn);
        back_read_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (FBReader.menu != null) {
                    FBReader.menu.toggle();
                }
            }
        });
        ImageView share_btn = (ImageView) view.findViewById(R.id.share_btn);
        share_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    shareBook(activity);
                }
            }
        });

        return view;
    }

    private void shareBook(final FragmentActivity activity) {
//		 final FBReaderApp fbReader = (FBReaderApp) FBReaderApp
//					.Instance();
//		final Dialog shareDialog = new Dialog(activity,
//				R.style.Translucent_NoTitle);
//		View view_dialog = LayoutInflater.from(activity).inflate(
//				R.layout.dialog_share_layout, null);
//		Button cancelBtn = (Button) view_dialog.findViewById(R.id.button1);
//		ImageView img_sina = (ImageView) view_dialog.findViewById(R.id.img_sina);
//		ImageView img_wechat = (ImageView) view_dialog.findViewById(R.id.img_wechat);
//		ImageView img_wechatcomment = (ImageView) view_dialog
//				.findViewById(R.id.img_wechatcomment);
//		shareDialog.setContentView(view_dialog);
//		shareDialog.setCanceledOnTouchOutside(true);
//		Window window = shareDialog.getWindow();
//		android.view.WindowManager.LayoutParams params = window.getAttributes();
//		params.gravity = Gravity.BOTTOM;
////		 params.width = LayoutParams.WRAP_CONTENT;
////		 params.height = activity.getWindowManager().getDefaultDisplay().getHeight()/3;
//		window.setAttributes(params);
//		shareDialog.show();
//		img_sina.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
////				shareDialog.dismiss();
////				String bookTitle = fbReader.Model.Book.getTitle();
////
////				OnekeyShare oks = new OnekeyShare();
////				oks.setNotification(R.drawable.icon,
////						getResources().getString(R.string.app_name_share));
////				oks.setText(String.format(
////						getResources().getString(R.string.share_book_content),
////						bookTitle));
////				oks.setImageUrl(FBReader.coverPath);
////				oks.setPlatform(SinaWeibo.NAME);
////				oks.setSilent(true);
////
////				// 去除注释，则快捷分享的分享加过将听过OneKeyShareCallback回调
////				oks.setCallback(new PlatformActionListener() {
////
////					@Override
////					public void onCancel(Platform arg0, int arg1) {
////						handler.obtainMessage(ShareConstants.CANCEL).sendToTarget();
////					}
////
////					@Override
////					public void onComplete(Platform arg0, int arg1,
////							HashMap<String, Object> arg2) {
////						handler.obtainMessage(ShareConstants.COMPLETE).sendToTarget();
////					}
////
////					@Override
////					public void onError(Platform arg0, int arg1, Throwable arg2) {
////						handler.obtainMessage(ShareConstants.ERROR).sendToTarget();
////					}
////
////				});
////				oks.show(activity);
//			}
//		});
//		img_wechat.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				shareDialog.dismiss();
//				Wechat.ShareParams sp = new Wechat.ShareParams();
//				sp.shareType = Platform.SHARE_WEBPAGE;
//				sp.imagePath = FBReader.coverPath;
//				sp.url = "http://v.dajianet.com/client.html?validated=true";
//				sp.text = "";
//				sp.title = "#好书推荐#  《" + fbReader.Model.Book.getTitle() + "》 ";
//				Platform platform = ShareSDK.getPlatform(
//						activity, Wechat.NAME);
//				if (platform != null && platform.isValid()) {
//					platform.share(sp);
//				}
//
//			}
//		});
//		img_wechatcomment.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				shareDialog.dismiss();
//
//				 WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
//					sp.shareType = Platform.SHARE_WEBPAGE;
//					sp.imagePath = FBReader.coverPath;
//					sp.url = "http://v.dajianet.com/client.html?validated=true";
//					sp.text = "";
//					sp.title = "#好书推荐#  《" + fbReader.Model.Book.getTitle() + "》 ";
//					Platform platform = ShareSDK.getPlatform(activity, WechatMoments.NAME);
//					if (platform.isValid()) {
//						platform.share(sp);
//					}
//
//			}
//		});
//		cancelBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				shareDialog.dismiss();
//			}
//		});
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated marksCount=" + myThisBookBookmarks.size());
        refresh();
    }

    public void refresh() {
        myThisBookBookmarks.clear();

        List<Bookmark> AllBooksBookmarks = Library.Instance().allBookmarks();
        Collections.sort(AllBooksBookmarks, new Bookmark.ByTimeComparator());
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        tocTree = fbreader.Model.TOCTree;

        if (fbreader.Model != null) {
            final long bookId = fbreader.Model.Book.getId();
            for (Bookmark bookmark : AllBooksBookmarks) {
                if (bookmark.getBookId() == bookId) {
                    myThisBookBookmarks.add(bookmark);
                }
            }
        }
        adapter = new BookmarksAdapter(getListView(), myThisBookBookmarks, true);
    }

    @Override
    public void onPause() {
        for (Bookmark bookmark : myThisBookBookmarks) {
            bookmark.save();
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
        final Bookmark bookmark = ((BookmarksAdapter) getListView()
                .getAdapter()).getItem(position);
        switch (item.getItemId()) {
            case OPEN_ITEM_ID:
                gotoBookmark(bookmark);
                return true;
            case DELETE_ITEM_ID:
                bookmark.delete();
                myThisBookBookmarks.remove(bookmark);
                invalidateView();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void addBookmark() {
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        final Bookmark bookmark = fbreader.addBookmark(20, true);
        if (bookmark != null && !myThisBookBookmarks.contains(bookmark)) {
            myThisBookBookmarks.add(0, bookmark);
            invalidateView();
        }
    }

    private void gotoBookmark(Bookmark bookmark) {
        bookmark.onOpen();
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        final long bookId = bookmark.getBookId();
        if ((fbreader.Model == null) || (fbreader.Model.Book.getId() != bookId)) {
            // 打开其他书的书签
            final Book book = Book.getById(bookId);
            if (book != null) {
                getActivity().finish();
                fbreader.openBook(book, bookmark, null);
            } else {
                UIUtil.showErrorMessage(getActivity(), "cannotOpenBook");
            }
        } else {
            FBReader activity = (FBReader) getActivity();
            activity.getHandler().obtainMessage(FBReader.MSG_WHAT_SWITCH).sendToTarget();
            fbreader.gotoBookmark(bookmark);
        }
    }

    private final class BookmarksAdapter extends BaseAdapter implements
            AdapterView.OnItemClickListener, View.OnCreateContextMenuListener {
        private final List<Bookmark> myBookmarks;
        private final boolean myCurrentBook;
        private int paragraphsNumber;

        BookmarksAdapter(ListView listView, List<Bookmark> bookmarks,
                         boolean currentBook) {
            myBookmarks = bookmarks;
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
                    R.layout.bookmark_item, parent, false);
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
                    Bookmark bookmark = getItem(position);
                    bookmark.delete();
                    myThisBookBookmarks.remove(bookmark);
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
                        final Bookmark bookmark = getItem(position);
                        if (bookmark != null) {
                            gotoBookmark(bookmark);
                        } else {
                            addBookmark();
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
            final Bookmark bookmark = getItem(position);
            if (bookmark == null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_list_plus);
                textView.setText(myResource.getResource("new").getValue());
                bookTitleView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.GONE);
                textView.setText(bookmark.getText());
                //开始获取章节信息
                int paragraphIndex = bookmark.getParagraphIndex() + 1;//+1是为了避免在某个章节开始处添加书签导致显示为上一个章节
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
                String dateStr = format.format(bookmark.getTime(Bookmark.CREATION));
                dateView.setText(dateStr);
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
            /*
			 * //源码：用于显示“添加书签”项 if (myCurrentBook) { --position; }
			 */
            return (position >= 0) ? myBookmarks.get(position) : null;
        }

        public final int getCount() {
            // 源码
            // return myCurrentBook ? myBookmarks.size() + 1 :
            // myBookmarks.size();
            return myBookmarks.size();
        }

        public final void onItemClick(AdapterView<?> parent, View view,
                                      int position, long id) {
            final Bookmark bookmark = getItem(position);
            if (bookmark != null) {
                gotoBookmark(bookmark);
            } else {
                addBookmark();
            }
        }
    }
}
