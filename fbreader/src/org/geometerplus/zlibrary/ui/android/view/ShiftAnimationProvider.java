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

package org.geometerplus.zlibrary.ui.android.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class ShiftAnimationProvider extends SimpleAnimationProvider {
    private final Paint myPaint = new Paint();
    private int headerHeight;

    ShiftAnimationProvider(BitmapManager bitmapManager, int headerHeight) {
        super(bitmapManager);
        this.headerHeight = headerHeight;
    }

    @Override
    protected void drawInternal(Canvas canvas) {
        try {
            myPaint.setColor(Color.rgb(127, 127, 127));
            if (myDirection.IsHorizontal) {
                final int dX = myEndX - myStartX;
                canvas.drawBitmap(getBitmapTo(), dX > 0 ? dX - myWidth : dX + myWidth, headerHeight, myPaint);
                canvas.drawBitmap(getBitmapFrom(), dX, headerHeight, myPaint);
                if (dX > 0 && dX < myWidth) {
                    canvas.drawLine(dX, 0, dX, myHeight + 1, myPaint);
                } else if (dX < 0 && dX > -myWidth) {
                    canvas.drawLine(dX + myWidth, 0, dX + myWidth, myHeight + 1, myPaint);
                }
            } else {
                final int dY = myEndY - myStartY;
                canvas.drawBitmap(getBitmapTo(), 0, dY > 0 ? dY - myHeight : dY + myHeight, myPaint);
                canvas.drawBitmap(getBitmapFrom(), 0, dY, myPaint);
                if (dY > 0 && dY < myHeight) {
                    canvas.drawLine(0, dY, myWidth + 1, dY, myPaint);
                } else if (dY < 0 && dY > -myHeight) {
                    canvas.drawLine(0, dY + myHeight, myWidth + 1, dY + myHeight, myPaint);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
