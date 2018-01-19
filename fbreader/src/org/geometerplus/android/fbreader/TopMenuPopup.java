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

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.zlibrary.ui.android.R;

final class TopMenuPopup extends PopupPanel {
    final static String ID = "TopMenuPopup";

    private volatile boolean myIsInProgress;

    private FBReaderApp mFbReader;

    TopMenuPopup(FBReaderApp fbReader) {
        super(fbReader);
        mFbReader = fbReader;
    }

    public void runNavigation() {
        if (myWindow == null || myWindow.getVisibility() == View.GONE) {
            myIsInProgress = false;
            initPosition();
            Application.showPopup(ID);
        }
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void show_() {
        super.show_();
        if (myWindow != null) {
            setupNavigation(myWindow);
        }
    }

    @Override
    protected void update() {
        if (!myIsInProgress && myWindow != null) {
            setupNavigation(myWindow);
        }
    }

    @Override
    public void createControlPanel(FBReader activity, RelativeLayout root) {
        if (myWindow != null && activity == myWindow.getActivity()) {
            return;
        }

        myWindow = new PopupWindow(activity, root, PopupWindow.Location.Top, true);

        final View layout = activity.getLayoutInflater().inflate(R.layout.topmenu, myWindow, false);
        Button btnBookIndex = (Button) layout.findViewById(R.id.btnIndex);

        Button btnBookMarks = (Button) layout.findViewById(R.id.btnMarks);
        btnBookMarks.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mFbReader.runAction(ActionCode.SHOW_BOOKMARKS);
            }
        });
        btnBookIndex.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mFbReader.runAction(ActionCode.SHOW_TOC);
            }

        });
        myWindow.addView(layout);
    }

    private void setupNavigation(PopupWindow panel) {

    }

}
