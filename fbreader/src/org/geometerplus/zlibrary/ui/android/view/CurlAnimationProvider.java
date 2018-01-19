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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import org.geometerplus.zlibrary.core.view.ZLView.PageIndex;

class CurlAnimationProvider extends AnimationProvider {
    final Path myFgPath = new Path();
    final Path myEdgePath = new Path();
    final Path myQuadPath = new Path();
    private final Paint myPaint = new Paint();
    private final Paint myBackPaint = new Paint();
    private final Paint myEdgePaint = new Paint();
    float mMiddleX;
    float mMiddleY;
    float mDegrees;
    PointF mTouch = new PointF(); // 拖拽点
    PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
    PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
    PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
    PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点
    PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
    PointF mBezierControl2 = new PointF();
    PointF mBeziervertex2 = new PointF();
    PointF mBezierEnd2 = new PointF();
    float mTouchToCornerDis;
    boolean mIsRTandLB; // 是否属于右上左下
    int[] mBackShadowColors = new int[]{0xff111111, 0x111111};
    int[] mFrontShadowColors;
    GradientDrawable mBackShadowDrawableLR;
    GradientDrawable mBackShadowDrawableRL;
    GradientDrawable mFolderShadowDrawableLR;
    GradientDrawable mFolderShadowDrawableRL;
    GradientDrawable mFrontShadowDrawableHBT;
    GradientDrawable mFrontShadowDrawableHTB;
    GradientDrawable mFrontShadowDrawableVLR;
    GradientDrawable mFrontShadowDrawableVRL;
    ;
    Matrix mMatrix = new Matrix();
    float[] mMatrixArray = {0, 0, 0, 0, 0, 0, 0, 0, 1.0f};
    ColorMatrixColorFilter mColorMatrixFilter;
    private float mySpeedFactor = 1;
    private Bitmap myBuffer;
    private volatile boolean myUseCanvasHack = false;
    private int mCornerX = 0; // 拖拽点对应的页脚
    private int mCornerY = 0;
    private Path mPath0 = new Path();
    private Path mPath1 = new Path();
    private boolean isPre = false;


    public CurlAnimationProvider(BitmapManager bitmapManager) {
        super(bitmapManager);

        myBackPaint.setAntiAlias(true);
//		myBackPaint.setAlpha(0x40);
        myBackPaint.setColor(Color.BLACK);

        myPaint.setColor(Color.RED);

        myEdgePaint.setAntiAlias(true);
        myEdgePaint.setStyle(Paint.Style.FILL);
//		myEdgePaint.setShadowLayer(15, 0, 0, 0xC0000000);
        myEdgePaint.setShadowLayer(15, 0, 0, Color.RED);

        createDrawable();
    }

    @Override
    protected void drawInternal(Canvas canvas) {
        if (myUseCanvasHack) {
            // This is a hack that disables hardware acceleration
            //   1) for GLES20Canvas we got an UnsupportedOperationException in clipPath
            //   2) View.setLayerType(LAYER_TYPE_SOFTWARE) does not work properly in some cases
            if (myBuffer == null ||
                    myBuffer.getWidth() != myWidth ||
                    myBuffer.getHeight() != myHeight) {
//				myBuffer = BitmapUtil.createBitmap(myWidth, myHeight, getBitmapTo().getConfig());
            }
            final Canvas softCanvas = new Canvas(myBuffer);
            drawInternalNoHack(softCanvas);
            canvas.drawBitmap(myBuffer, 0, 0, myPaint);
        } else {
            try {
                drawInternalNoHack(canvas);
            } catch (UnsupportedOperationException e) {
                myUseCanvasHack = true;
                drawInternal(canvas);
            }
        }
    }

    /**
     * Author : hmg25 Version: 1.0 Description : 创建阴影的GradientDrawable
     */
    private void createDrawable() {
        int[] color = {0x333333, 0xb0333333};
        mFolderShadowDrawableRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, color);
        mFolderShadowDrawableRL
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFolderShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, color);
        mFolderShadowDrawableLR
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowColors = new int[]{0xff111111, 0x111111};
        mBackShadowDrawableRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
        mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowColors = new int[]{0x80111111, 0x111111};
        mFrontShadowDrawableVLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
        mFrontShadowDrawableVLR
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mFrontShadowDrawableVRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
        mFrontShadowDrawableVRL
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableHTB = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
        mFrontShadowDrawableHTB
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableHBT = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
        mFrontShadowDrawableHBT
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);


        ColorMatrix cm = new ColorMatrix();
        float array[] = {0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
                0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0};
        cm.set(array);
        mColorMatrixFilter = new ColorMatrixColorFilter(cm);

    }

    /**
     * /**
     * Author : hmg25 Version: 1.0 Description : 求解直线P1P2和直线P3P4的交点坐标
     */
    public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
        PointF CrossP = new PointF();
        // 二元函数通式： y=ax+b
        float a1 = (P2.y - P1.y) / (P2.x - P1.x);
        float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

        float a2 = (P4.y - P3.y) / (P4.x - P3.x);
        float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
        CrossP.x = (b2 - b1) / (a1 - a2);
        CrossP.y = a1 * CrossP.x + b1;
        return CrossP;
    }

    /**
     * Author : hmg25 Version: 1.0 Description : 计算拖拽点对应的拖拽脚
     */
    private void calcCornerXY(float x, float y) {

//		x = (float) (myWidth*0.9);
        if (x < myWidth * 0.5) {
            isPre = true;
            x = myWidth;
        } else {
            isPre = false;
        }
        if (y < myHeight * 0.9) {
            y = (float) (myHeight * 0.9);
        }
        if (x <= myWidth / 2)
            mCornerX = 0;
        else
            mCornerX = myWidth;
        if (y <= myHeight / 2)
            mCornerY = 0;
        else
            mCornerY = myHeight;
        if ((mCornerX == 0 && mCornerY == myHeight)
                || (mCornerX == myWidth && mCornerY == 0))
            mIsRTandLB = true;
        else
            mIsRTandLB = false;
    }

    private void calcPoints() {
        mTouch.y = (float) (myHeight * 0.9);
        mMiddleX = (mTouch.x + mCornerX) / 2;
        mMiddleY = (mTouch.y + mCornerY) / 2;
        mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
                * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
        mBezierControl1.y = mCornerY;
        mBezierControl2.x = mCornerX;
        mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
                * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

        mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
                / 2;
        mBezierStart1.y = mCornerY;

        // 当mBezierStart1.x < 0或者mBezierStart1.x > 480时
        // 如果继续翻页，会出现BUG故在此限制
//		if (mBezierStart1.x < 0 || mBezierStart1.x > 1200) {
//			if (mBezierStart1.x < 0)
//				mBezierStart1.x = myWidth - mBezierStart1.x;
//
//			float f1 = Math.abs(mCornerX - mTouch.x);
//			float f2 = myWidth * f1 / mBezierStart1.x;
//			mTouch.x = Math.abs(mCornerX - f2);
//
//			float f3 = Math.abs(mCornerX - mTouch.x)
//					* Math.abs(mCornerY - mTouch.y) / f1;
//			mTouch.y = Math.abs(mCornerY - f3);
//			mMiddleX = (mTouch.x + mCornerX) / 2;
//			mMiddleY = (mTouch.y + mCornerY) / 2;
//
//			mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
//					* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
//			mBezierControl1.y = mCornerY;
//
//			mBezierControl2.x = mCornerX;
//			mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
//					* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
//			Log.i("hmg", "mTouchX --> " + mTouch.x + "  mTouchY-->  "
//					+ mTouch.y);
//			Log.i("hmg", "mBezierControl1.x--  " + mBezierControl1.x
//					+ "  mBezierControl1.y -- " + mBezierControl1.y);
//			Log.i("hmg", "mBezierControl2.x -- " + mBezierControl2.x
//					+ "  mBezierControl2.y -- " + mBezierControl2.y);
//			mBezierStart1.x = mBezierControl1.x
//					- (mCornerX - mBezierControl1.x) / 2;
//		}

        mBezierStart2.x = mCornerX;
        mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
                / 2;

        mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX),
                (mTouch.y - mCornerY));

        mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1,
                mBezierStart2);
        mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1,
                mBezierStart2);
        /*
		 * mBeziervertex1.x 推导
		 * ((mBezierStart1.x+mBezierEnd1.x)/2+mBezierControl1.x)/2 化简等价于
		 * (mBezierStart1.x+ 2*mBezierControl1.x+mBezierEnd1.x) / 4
		 */
        mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
        mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
        mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
        mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
    }

    private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
        mPath0.reset();
        mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
        mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x,
                mBezierEnd1.y);
        mPath0.lineTo(mTouch.x, mTouch.y);
        mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
        mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x,
                mBezierStart2.y);
        mPath0.lineTo(mCornerX, mCornerY);
        mPath0.close();

        canvas.save();
        canvas.clipPath(path, Region.Op.XOR);
        canvas.drawBitmap(bitmap, 0, mTop, null);
        canvas.restore();
    }

    private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
        mPath1.reset();
        mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
        mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
        mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
        mPath1.lineTo(mCornerX, mCornerY);
        mPath1.close();

        mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x
                - mCornerX, mBezierControl2.y - mCornerY));
        int leftx;
        int rightx;
        GradientDrawable mBackShadowDrawable;
        if (mIsRTandLB) {
            leftx = (int) (mBezierStart1.x);
            rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
            mBackShadowDrawable = mBackShadowDrawableLR;
        } else {
            leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
            rightx = (int) mBezierStart1.x;
            mBackShadowDrawable = mBackShadowDrawableRL;
        }
        canvas.save();
        canvas.clipPath(mPath0);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        canvas.drawBitmap(bitmap, 0, mTop, null);
        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);

        float mMaxLength = (float) Math.hypot(myWidth, myHeight);
        mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx,
                (int) (mMaxLength + mBezierStart1.y));
        mBackShadowDrawable.draw(canvas);
        canvas.restore();
    }

    /**
     * Author : hmg25 Version: 1.0 Description : 绘制翻起页的阴影
     */
    public void drawCurrentPageShadow(Canvas canvas) {
        double degree;
        if (mIsRTandLB) {
            degree = Math.PI
                    / 4
                    - Math.atan2(mBezierControl1.y - mTouch.y, mTouch.x
                    - mBezierControl1.x);
        } else {
            degree = Math.PI
                    / 4
                    - Math.atan2(mTouch.y - mBezierControl1.y, mTouch.x
                    - mBezierControl1.x);
        }
        //翻起页阴影顶点与touch点的距离
        double d1 = (float) 25 * 1.414 * Math.cos(degree);
        double d2 = (float) 25 * 1.414 * Math.sin(degree);
        float x = (float) (mTouch.x + d1);
        float y;
        if (mIsRTandLB) {
            y = (float) (mTouch.y + d2);
        } else {
            y = (float) (mTouch.y - d2);
        }
        mPath1.reset();
        mPath1.moveTo(x, y);
        mPath1.lineTo(mTouch.x, mTouch.y);
        mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
        mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
        mPath1.close();
        float rotateDegrees;
        canvas.save();

        canvas.clipPath(mPath0, Region.Op.XOR);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        int leftx;
        int rightx;
        GradientDrawable mCurrentPageShadow;
        if (mIsRTandLB) {
            leftx = (int) (mBezierControl1.x);
            rightx = (int) mBezierControl1.x + 25;
            mCurrentPageShadow = mFrontShadowDrawableVLR;
        } else {
            leftx = (int) (mBezierControl1.x - 25);
            rightx = (int) mBezierControl1.x + 1;
            mCurrentPageShadow = mFrontShadowDrawableVRL;
        }

        rotateDegrees = (float) Math.toDegrees(Math.atan2(mTouch.x
                - mBezierControl1.x, mBezierControl1.y - mTouch.y));
        canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);

        float mMaxLength = (float) Math.hypot(myWidth, myHeight);
        mCurrentPageShadow.setBounds(leftx,
                (int) (mBezierControl1.y - mMaxLength), rightx,
                (int) (mBezierControl1.y));
        mCurrentPageShadow.draw(canvas);
        canvas.restore();

        mPath1.reset();
        mPath1.moveTo(x, y);
        mPath1.lineTo(mTouch.x, mTouch.y);
        mPath1.lineTo(mBezierControl2.x, mBezierControl2.y);
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
        mPath1.close();
        canvas.save();
        canvas.clipPath(mPath0, Region.Op.XOR);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        if (mIsRTandLB) {
            leftx = (int) (mBezierControl2.y);
            rightx = (int) (mBezierControl2.y + 25);
            mCurrentPageShadow = mFrontShadowDrawableHTB;
        } else {
            leftx = (int) (mBezierControl2.y - 25);
            rightx = (int) (mBezierControl2.y + 1);
            mCurrentPageShadow = mFrontShadowDrawableHBT;
        }
        rotateDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl2.y
                - mTouch.y, mBezierControl2.x - mTouch.x));
        canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
        float temp;
        if (mBezierControl2.y < 0)
            temp = mBezierControl2.y - 800;
        else
            temp = mBezierControl2.y;

        int hmg = (int) Math.hypot(mBezierControl2.x, temp);
        if (hmg > mMaxLength)
            mCurrentPageShadow
                    .setBounds((int) (mBezierControl2.x - 25) - hmg, leftx,
                            (int) (mBezierControl2.x + mMaxLength) - hmg,
                            rightx);
        else
            mCurrentPageShadow.setBounds(
                    (int) (mBezierControl2.x - mMaxLength), leftx,
                    (int) (mBezierControl2.x), rightx);

        Log.i("hmg", "mBezierControl2.x   " + mBezierControl2.x
                + "  mBezierControl2.y  " + mBezierControl2.y);
        mCurrentPageShadow.draw(canvas);
        canvas.restore();
    }

    /**
     * Author : hmg25 Version: 1.0 Description : 绘制翻起页背面
     */
    private void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {
        int i = (int) (mBezierStart1.x + mBezierControl1.x) / 2;
        float f1 = Math.abs(i - mBezierControl1.x);
        int i1 = (int) (mBezierStart2.y + mBezierControl2.y) / 2;
        float f2 = Math.abs(i1 - mBezierControl2.y);
        float f3 = Math.min(f1, f2);
        mPath1.reset();
        mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y);
        mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
        mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
        mPath1.lineTo(mTouch.x, mTouch.y);
        mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
        mPath1.close();
        GradientDrawable mFolderShadowDrawable;
        int left;
        int right;
        if (mIsRTandLB) {
            left = (int) (mBezierStart1.x - 1);
            right = (int) (mBezierStart1.x + f3 + 1);
            mFolderShadowDrawable = mFolderShadowDrawableLR;
        } else {
            left = (int) (mBezierStart1.x - f3 - 1);
            right = (int) (mBezierStart1.x + 1);
            mFolderShadowDrawable = mFolderShadowDrawableRL;
        }
        canvas.save();
        canvas.clipPath(mPath0);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        Paint mPaint = new Paint();
        mPaint.setColorFilter(mColorMatrixFilter);

        float dis = (float) Math.hypot(mCornerX - mBezierControl1.x,
                mBezierControl2.y - mCornerY);
        float f8 = (mCornerX - mBezierControl1.x) / dis;
        float f9 = (mBezierControl2.y - mCornerY) / dis;
        mMatrixArray[0] = 1 - 2 * f9 * f9;
        mMatrixArray[1] = 2 * f8 * f9;
        mMatrixArray[3] = mMatrixArray[1];
        mMatrixArray[4] = 1 - 2 * f8 * f8;
        mMatrix.reset();
        mMatrix.setValues(mMatrixArray);
        mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
        mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);
        canvas.drawBitmap(bitmap, mMatrix, mPaint);
        // canvas.drawBitmap(bitmap, mMatrix, null);
        mPaint.setColorFilter(null);
        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);

        float mMaxLength = (float) Math.hypot(myWidth, myHeight);
        mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right,
                (int) (mBezierStart1.y + mMaxLength));
        mFolderShadowDrawable.draw(canvas);
        canvas.restore();
    }

    private void drawInternalNoHack(Canvas canvas) {
        /********绘制新的一页面*********/
//		drawBitmapTo(canvas, 0, 0, myPaint);
        canvas.drawColor(0xFFAAAAAA);
        calcCornerXY(myStartX, myStartY);
        mTouch.x = myEndX;
        mTouch.y = (float) (myHeight * 0.9);
        ;

        Bitmap curBitmap = getBitmapFrom();
        Bitmap nextBitmp = getBitmapTo();
        if (isPre) {
            curBitmap = getBitmapTo();
            nextBitmp = getBitmapFrom();
            mTouch.x = myEndX - myWidth;

        }
        calcPoints();
        drawCurrentPageArea(canvas, curBitmap, mPath0);
        drawNextPageAreaAndShadow(canvas, nextBitmp);
        drawCurrentPageShadow(canvas);
        drawCurrentBackArea(canvas, curBitmap);
    }
//
//	@Override
//	public PageIndex getPageToScrollTo(int x, int y) {
//		if (myDirection == null) {
//			return PageIndex.current;
//		}
//
//		switch (myDirection) {
//			case leftToRight:
//				return myStartX < myWidth / 2 ? PageIndex.next : PageIndex.previous;
//			case rightToLeft:
//				return myStartX < myWidth / 2 ? PageIndex.previous : PageIndex.next;
//			case up:
//				return myStartY < myHeight / 2 ? PageIndex.previous : PageIndex.next;
//			case down:
//				return myStartY < myHeight / 2 ? PageIndex.next : PageIndex.previous;
//		}
//		return PageIndex.current;
//	}

    @Override
    protected void startAnimatedScrollingInternal(int speed) {
        mySpeedFactor = (float) Math.pow(2.0, 0.25 * speed);
        //调整翻页速度
//		mySpeed *= 1.5;
        mySpeed /= 5;
        doStep();
    }

    @Override
    protected void setupAnimatedScrollingStart(Integer x, Integer y) {
        if (x == null || y == null) {
            if (myDirection.IsHorizontal) {
                x = mySpeed < 0 ? myWidth - 3 : 3;
                y = 1;
            } else {
                x = 1;
                y = mySpeed < 0 ? myHeight - 3 : 3;
            }
        } else {
            final int cornerX = x > myWidth / 2 ? myWidth : 0;
            final int cornerY = y > myHeight / 2 ? myHeight : 0;
            int deltaX = Math.min(Math.abs(x - cornerX), myWidth / 5);
            int deltaY = Math.min(Math.abs(y - cornerY), myHeight / 5);
            if (myDirection.IsHorizontal) {
                deltaY = Math.min(deltaY, deltaX / 3);
            } else {
                deltaX = Math.min(deltaX, deltaY / 3);
            }
            x = Math.abs(cornerX - deltaX);
            y = Math.abs(cornerY - deltaY);
        }
        myEndX = myStartX = x;
        myEndY = myStartY = y;
    }

    @Override
    public void doStep() {
        if (!getMode().Auto) {
            return;
        }

        final int speed = (int) Math.abs(mySpeed);
        mySpeed *= mySpeedFactor;

        final int cornerX = myStartX > myWidth / 2 ? myWidth : 0;
        final int cornerY = myStartY > myHeight / 2 ? myHeight : 0;

        final int boundX, boundY;
        if (getMode() == Mode.AnimatedScrollingForward) {
            boundX = cornerX == 0 ? 2 * myWidth : -myWidth;
            boundY = cornerY == 0 ? 2 * myHeight : -myHeight;
        } else {
            boundX = cornerX;
            boundY = cornerY;
        }

        final int deltaX = Math.abs(myEndX - cornerX);
        final int deltaY = Math.abs(myEndY - cornerY);
        final int speedX, speedY;
        if (deltaX == 0 || deltaY == 0) {
            speedX = speed;
            speedY = speed;
        } else if (deltaX < deltaY) {
            speedX = speed;
            speedY = speed * deltaY / deltaX;
        } else {
            speedX = speed * deltaX / deltaY;
            speedY = speed;
        }

        final boolean xSpeedIsPositive, ySpeedIsPositive;
        if (getMode() == Mode.AnimatedScrollingForward) {
            xSpeedIsPositive = cornerX == 0;
            ySpeedIsPositive = cornerY == 0;
        } else {
            xSpeedIsPositive = cornerX != 0;
            ySpeedIsPositive = cornerY != 0;
        }

        if (xSpeedIsPositive) {
            myEndX += speedX;
            if (myEndX >= boundX) {
                terminate();
            }
        } else {
            myEndX -= speedX;
            if (myEndX <= boundX) {
                terminate();
            }
        }

        if (ySpeedIsPositive) {
            myEndY += speedY;
            if (myEndY >= boundY) {
                terminate();
            }
        } else {
            myEndY -= speedY;
            if (myEndY <= boundY) {
                terminate();
            }
        }
    }

    @Override
    PageIndex getPageToScrollTo(int x, int y) {
        if (myDirection == null) {
            return PageIndex.current;
        }

        switch (myDirection) {
            case leftToRight:
                return myStartX < myWidth / 2 ? PageIndex.next : PageIndex.previous;
            case rightToLeft:
                return myStartX < myWidth / 2 ? PageIndex.previous : PageIndex.next;
            case up:
                return myStartY < myHeight / 2 ? PageIndex.previous : PageIndex.next;
            case down:
                return myStartY < myHeight / 2 ? PageIndex.next : PageIndex.previous;
        }
        return PageIndex.current;
    }
}
