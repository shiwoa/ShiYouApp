/*
 * Copyright (C) 2009-2012 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mouee.fbreader.interfaces.Refresh;

import org.geometerplus.fbreader.bookmodel.TOCTree;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.core.tree.ZLTree;
import org.geometerplus.zlibrary.ui.android.R;

public class TOCFragment extends ListFragment implements Refresh {
    private static final int PROCESS_TREE_ITEM_ID = 0;
    private static final int READ_BOOK_ITEM_ID = 1;
    private TOCAdapter myAdapter;
    private ZLTree<?> mySelectedItem;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        final FBReaderApp fbreader = (FBReaderApp) ZLApplication.Instance();
//        final ZLTextWordCursor cursor = fbreader.BookTextView.getStartCursor();
//        int index = cursor.getParagraphIndex();
//        if (cursor.isEndOfParagraph()) {
//            ++index;
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toc, container, false);
        ImageView back_read_btn = (ImageView) view
                .findViewById(R.id.back_read_btn);
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
//		window.setAttributes(params);
//		shareDialog.show();
//		img_sina.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				shareDialog.dismiss();
//				String bookTitle = fbReader.Model.Book.getTitle();
//
//				OnekeyShare oks = new OnekeyShare();
//				oks.setNotification(R.drawable.icon,
//						getResources().getString(R.string.app_name_share));
//				oks.setText(String.format(
//						getResources().getString(R.string.share_book_content),
//						bookTitle));
//				oks.setImageUrl(FBReader.coverPath);
//				oks.setPlatform(SinaWeibo.NAME);
//				oks.setSilent(true);
//
//				// 去除注释，则快捷分享的分享加过将听过OneKeyShareCallback回调
//				oks.setCallback(new PlatformActionListener() {
//
//					@Override
//					public void onCancel(Platform arg0, int arg1) {
//						handler.obtainMessage(ShareConstants.CANCEL).sendToTarget();
//					}
//
//					@Override
//					public void onComplete(Platform arg0, int arg1,
//							HashMap<String, Object> arg2) {
//						handler.obtainMessage(ShareConstants.COMPLETE).sendToTarget();
//					}
//
//					@Override
//					public void onError(Platform arg0, int arg1, Throwable arg2) {
//						handler.obtainMessage(ShareConstants.ERROR).sendToTarget();
//					}
//
//				});
//				oks.show(activity);
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
        refresh();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int position = ((AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo()).position;
        final TOCTree tree = (TOCTree) myAdapter.getItem(position);
        switch (item.getItemId()) {
            case PROCESS_TREE_ITEM_ID:
                myAdapter.runTreeItem(tree);
                return true;
            case READ_BOOK_ITEM_ID:
                myAdapter.openBookText(tree);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void refresh() {
        final FBReaderApp fbreader = (FBReaderApp) ZLApplication.Instance();
        final TOCTree root = fbreader.Model.TOCTree;
        myAdapter = new TOCAdapter(root);
        TOCTree treeToSelect = fbreader.getCurrentTOCElement();
        myAdapter.selectItem(treeToSelect);
        mySelectedItem = treeToSelect;
    }

    private final class TOCAdapter extends ZLTreeAdapter {
        TOCAdapter(TOCTree root) {
            super(getListView(), root);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            final int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
            final TOCTree tree = (TOCTree) getItem(position);
            if (tree.hasChildren()) {
                menu.setHeaderTitle(tree.getText());
                final ZLResource resource = ZLResource.resource("tocView");
                menu.add(
                        0,
                        PROCESS_TREE_ITEM_ID,
                        0,
                        resource.getResource(
                                isOpen(tree) ? "collapseTree" : "expandTree")
                                .getValue());
                menu.add(0, READ_BOOK_ITEM_ID, 0,
                        resource.getResource("readText").getValue());
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = (convertView != null) ? convertView
                    : LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.toc_tree_item, parent, false);
            final TOCTree tree = (TOCTree) getItem(position);
//			 view.setBackgroundColor(tree == mySelectedItem ? 0xffdcccc8 : 0);
            // view.setBackgroundResource(tree == mySelectedItem
            // ?R.drawable.selector_list: 0);
            setIcon((ImageView) view.findViewById(R.id.toc_tree_item_icon),
                    tree);
            TextView tv = (TextView) view.findViewById(R.id.toc_tree_item_text);
            tv.setText(tree.getText());
            tv.setTextColor(tree == mySelectedItem ? Color.RED : Color.BLACK);
            if (!isOpen(tree) && !getClicked()) {
                expandOrCollapseTree(tree);
            }
            return view;
        }

        void openBookText(TOCTree tree) {
            final TOCTree.Reference reference = tree.getReference();
            if (reference != null) {
                final FBReaderApp fbreader = (FBReaderApp) ZLApplication
                        .Instance();
                int paragraphsNumber = fbreader.Model.getTextModel()
                        .getParagraphsNumber();
                FBReader activity = (FBReader) getActivity();
                if (FBReader.probationRate > 0) {
                    int limitParagraphsNumber = FBReader.probationRate
                            * paragraphsNumber / 100;
                    if (reference.ParagraphIndex > limitParagraphsNumber) {
                        int pageLimit = fbreader.getPageUpperLimit();
                        Intent intent = new Intent(FBReader.ACTION_TIP_PURCHASE);
                        intent.putExtra(FBReader.EXTRA_PAGE_LIMIT, pageLimit);
                        activity.sendBroadcast(intent);
                        return;
                    }
                }

                activity.getHandler().obtainMessage(FBReader.MSG_WHAT_SWITCH)
                        .sendToTarget();

                fbreader.addInvisibleBookmark();
                fbreader.BookTextView.gotoPosition(reference.ParagraphIndex, 0,
                        0);
                fbreader.showBookTextView();
            }
        }

        @Override
        protected boolean runTreeItem(ZLTree<?> tree) {
            if (super.runTreeItem(tree)) {
                return true;
            }
            openBookText((TOCTree) tree);
            return true;
        }
    }
}
