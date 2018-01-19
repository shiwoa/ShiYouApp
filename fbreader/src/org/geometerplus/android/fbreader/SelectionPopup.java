/*
 * Copyright (C) 2007-2012 Geometer Plus <contact@geometerplus.com>
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


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.fbreader.FBView;
import org.geometerplus.zlibrary.ui.android.R;

class SelectionPopup extends ButtonsPopupPanel {
    final static String ID = "SelectionPopup";

    SelectionPopup(FBReaderApp fbReader) {
        super(fbReader);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void createControlPanel(FBReader activity, RelativeLayout root) {
        if (myWindow != null && activity == myWindow.getActivity()) {
            return;
        }
        myWindow = new PopupWindow(activity, root, PopupWindow.Location.Floating, false);
        myWindow.setBackgroundColor(Color.TRANSPARENT);
        //修改笔记功能
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_note_layout, null);
        Button btn_select_copy = (Button) view.findViewById(R.id.btn_select_copy);
        Button btn_select_share = (Button) view.findViewById(R.id.btn_select_share);
        Button btn_select_search = (Button) view.findViewById(R.id.btn_select_search);
        Button btn_select_baike = (Button) view.findViewById(R.id.btn_select_baike);

        ImageView iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
        ImageView btn_yellow = (ImageView) view.findViewById(R.id.btn_yellow);
        ImageView btn_green = (ImageView) view.findViewById(R.id.btn_green);
        ImageView btn_blue = (ImageView) view.findViewById(R.id.btn_blue);
        ImageView btn_purple = (ImageView) view.findViewById(R.id.btn_purple);
        btn_yellow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                storePosition();
                StartPosition = null;
                Application.hideActivePopup();
                Application.runAction(ActionCode.SELECTION_DRAW_YELLOW_LINE);
            }
        });
        btn_green.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                storePosition();
                StartPosition = null;
                Application.hideActivePopup();
                Application.runAction(ActionCode.SELECTION_DRAW_GREEN_LINE);
            }
        });
        btn_blue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                storePosition();
                StartPosition = null;
                Application.hideActivePopup();
                Application.runAction(ActionCode.SELECTION_DRAW_BLUE_LINE);
            }
        });
        btn_purple.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                storePosition();
                StartPosition = null;
                Application.hideActivePopup();
                Application.runAction(ActionCode.SELECTION_DRAW_PURPLE_LINE);
            }
        });
        iv_clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                storePosition();
                StartPosition = null;
                Application.hideActivePopup();
                Application.runAction(ActionCode.SELECTION_DELETE_LINE);

            }
        });
        btn_select_copy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                storePosition();
                StartPosition = null;
                Application.hideActivePopup();
                Application.runAction(ActionCode.SELECTION_COPY_TO_CLIPBOARD);
            }
        });
        btn_select_share.setOnClickListener(new OnClickListener() {//变成编写笔记了

            @Override
            public void onClick(View arg0) {

                Application.hideActivePopup();
//				Application.runAction(ActionCode.SELECTION_SHARE);
                final EditText inputServer = new EditText(arg0.getContext());
                inputServer.setFocusable(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
                builder.setTitle("笔记")
                        .setIcon(android.R.drawable.ic_menu_edit)
                        .setView(inputServer)
                        .setNegativeButton("取消",
                                null);
                builder.setPositiveButton("保存",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String inputName = inputServer.getText().toString();
                                FBReaderApp reader = (FBReaderApp) FBReaderApp.Instance();
                                FBView fbview = reader.getTextView();
                                fbview.clearSelection();
                                StartPosition = fbview.getStartCursor();//.getSelectionStartPosition();
                                storePositionNote(inputName);
                                StartPosition = null;
                            }
                        });
                builder.show();
            }
        });
        btn_select_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                storePosition();
                StartPosition = null;
                Application.hideActivePopup();
                Application.runAction(ActionCode.SELECTION_SEARCH);
            }
        });


        btn_select_baike.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                storePosition();
                StartPosition = null;
                Application.hideActivePopup();
                Application.runAction(ActionCode.SELECTION_BAIKE);
            }
        });

        myWindow.addView(view);
//		addButton(ActionCode.SELECTION_COPY_TO_CLIPBOARD, true, R.drawable.selection_copy);
//		if(ReaderConfig.instance().needShare()){
//			addButton(ActionCode.SELECTION_SHARE, true, R.drawable.selection_share);
//		}
////		//addButton(ActionCode.SELECTION_TRANSLATE, true, R.drawable.selection_translate);
//		addButton(ActionCode.SELECTION_SEARCH, true, R.drawable.selection_bookmark);
//		addButton(ActionCode.SELECTION_CLEAR, true, R.drawable.selection_close);
    }

    public void move(int selectionStartY, int selectionEndY) {
        if (myWindow == null) {
            return;
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        final int verticalPosition;
        final int screenHeight = ((View) myWindow.getParent()).getHeight();
        final int diffTop = screenHeight - selectionEndY;
        if (diffTop > selectionStartY) {
            verticalPosition = diffTop > myWindow.getHeight() + 20
                    ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.CENTER_VERTICAL;
        } else {
            verticalPosition = selectionStartY > myWindow.getHeight() + 20
                    ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.CENTER_VERTICAL;
        }
        layoutParams.addRule(verticalPosition);
        myWindow.setLayoutParams(layoutParams);
    }
}
