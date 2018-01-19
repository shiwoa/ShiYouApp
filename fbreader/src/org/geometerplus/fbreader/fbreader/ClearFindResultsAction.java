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

import android.content.Intent;

import com.linthink.reader.ReaderConfig;
import com.mouee.fbreader.activity.TextSearchActivity;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.zlibrary.ui.android.R;

public class ClearFindResultsAction extends FBAction {
    private FBReader baseActivity;

    public ClearFindResultsAction(FBReader baseActivity, FBReaderApp fbreader) {
        super(fbreader);
        this.baseActivity = baseActivity;
    }

    @Override
    protected void run(Object... params) {
        Reader.getTextView().clearFindResults();
        Intent intent = new Intent(baseActivity, TextSearchActivity.class);
        baseActivity.startActivity(intent);
        if (ReaderConfig.instance().textSearchAcitivtySlideInUp()) {
            baseActivity.overridePendingTransition(R.anim.slide_in_up, 0);
        }
    }
}
