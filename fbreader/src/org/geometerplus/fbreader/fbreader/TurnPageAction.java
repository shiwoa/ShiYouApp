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

package org.geometerplus.fbreader.fbreader;

import android.content.Context;
import android.content.Intent;

import org.geometerplus.android.fbreader.FBReader;

public class TurnPageAction extends FBAction {
    private final boolean myForward;
    private Context mContext;

    public TurnPageAction(FBReaderApp fbreader, boolean forward, Context context) {
        super(fbreader);
        myForward = forward;
        mContext = context;
    }

    @Override
    public boolean isEnabled() {
        final ScrollingPreferences preferences = ScrollingPreferences.Instance();

        final ScrollingPreferences.FingerScrolling fingerScrolling =
                preferences.FingerScrollingOption.getValue();
        return
                fingerScrolling == ScrollingPreferences.FingerScrolling.byTap ||
                        fingerScrolling == ScrollingPreferences.FingerScrolling.byTapAndFlick;
    }

    @Override
    protected void run(Object... params) {
        if (FBReader.probationRate > 0 && myForward) {
            int current = Reader.getTextView().pagePosition().Current;
            int limit = Reader.getPageUpperLimit();
            if (current >= limit) {
                Intent intent = new Intent(FBReader.ACTION_TIP_PURCHASE);
                intent.putExtra(FBReader.EXTRA_PAGE_LIMIT, limit);
                mContext.sendBroadcast(intent);
                return;
            }
        }

        final ScrollingPreferences preferences = ScrollingPreferences.Instance();
        if (params.length == 2 && params[0] instanceof Integer && params[1] instanceof Integer) {
            final int x = (Integer) params[0];
            final int y = (Integer) params[1];
            Reader.getViewWidget().startAnimatedScrolling(
                    myForward ? FBView.PageIndex.next : FBView.PageIndex.previous,
                    x, y,
                    preferences.HorizontalOption.getValue()
                            ? FBView.Direction.rightToLeft : FBView.Direction.up,
                    preferences.AnimationSpeedOption.getValue()
            );
        } else {
            Reader.getViewWidget().startAnimatedScrolling(
                    myForward ? FBView.PageIndex.next : FBView.PageIndex.previous,
                    preferences.HorizontalOption.getValue()
                            ? FBView.Direction.rightToLeft : FBView.Direction.up,
                    preferences.AnimationSpeedOption.getValue()
            );
        }
        //增加去掉选择箭头 弹出菜单
        Reader.hideActivePopup();
        Reader.getTextView().clearSelection();
    }
}
