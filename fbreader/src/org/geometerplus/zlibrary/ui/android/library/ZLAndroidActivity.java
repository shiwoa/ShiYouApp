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

package org.geometerplus.zlibrary.ui.android.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.ui.android.R;
import org.geometerplus.zlibrary.ui.android.application.ZLAndroidApplicationWindow;

import java.lang.reflect.Field;

public abstract class ZLAndroidActivity extends FragmentActivity {
    private PowerManager.WakeLock myWakeLock;
    private boolean myWakeLockToCreate;
    private BroadcastReceiver myBatteryInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final int level = intent.getIntExtra("level", 100);
            final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
            application.myMainWindow.setBatteryLevel(level);
            switchWakeLock(getLibrary().BatteryLevelToTurnScreenOffOption
                    .getValue() < level);
        }
    };
    private boolean myStartTimer;

    private static ZLAndroidLibrary getLibrary() {
        return (ZLAndroidLibrary) ZLAndroidLibrary.Instance();
    }

    protected abstract ZLApplication createApplication();

    private void setScreenBrightnessAuto() {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.screenBrightness = -1.0f;
        getWindow().setAttributes(attrs);
    }

    final int getScreenBrightness() {
        final int level = (int) (100 * getWindow().getAttributes().screenBrightness);
        return (level >= 0) ? level : 50;
    }

    final void setScreenBrightness(int percent) {
        if (percent < 1) {
            percent = 1;
        } else if (percent > 100) {
            percent = 100;
        }
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.screenBrightness = percent / 100.0f;
        getWindow().setAttributes(attrs);
        getLibrary().ScreenBrightnessLevelOption.setValue(percent);
    }

    private void setButtonLight(boolean enabled) {
        try {
            final WindowManager.LayoutParams attrs = getWindow()
                    .getAttributes();
            final Class<?> cls = attrs.getClass();
            final Field fld = cls.getField("buttonBrightness");
            if (fld != null && "float".equals(fld.getType().toString())) {
                fld.setFloat(attrs, enabled ? -1.0f : 0.0f);
                getWindow().setAttributes(attrs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract ZLFile fileFromIntent(Intent intent);

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Thread.setDefaultUncaughtExceptionHandler(new
        // UncaughtExceptionHandler(this));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_epub);
//
//		LayoutInflater inflater = getLayoutInflater();
//		View contentView = inflater.inflate(R.layout.main_epub, null);
//
//		setContentView(FBReader.guide.showGuide(contentView));
        // setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        getLibrary().setActivity(this);

        final ZLAndroidApplication androidApplication = (ZLAndroidApplication) getApplication();
        if (androidApplication.myMainWindow == null) {
            final ZLApplication application = createApplication();
            androidApplication.myMainWindow = new ZLAndroidApplicationWindow(application);
            application.initWindow();
        }

		/*
         * new Thread() { public void run() {
		 */
        ZLFile zfile = fileFromIntent(getIntent());
        Log.d("zfile", (zfile == null) + "????");
        if (zfile == null) {
            Toast.makeText(this, "δ�ҵ�epub�ļ�,�봫����ȷ�ز���", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        ZLApplication.Instance().openFile(zfile, getPostponedInitAction());
        Log.d("header", "ZLAndroidActivity onCreate openFile");
        ZLApplication.Instance().getViewWidget().repaint();
        // }
        /*
         * }.start();
		 *
		 * ZLApplication.Instance().getViewWidget().repaint();
		 */
    }

    protected abstract Runnable getPostponedInitAction();

    public final void createWakeLock() {
        if (myWakeLockToCreate) {
            synchronized (this) {
                if (myWakeLockToCreate) {
                    myWakeLockToCreate = false;
                    final PowerManager manager = (PowerManager) getSystemService(POWER_SERVICE);
                    myWakeLock = ((PowerManager) manager)
                            .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                                    | PowerManager.ON_AFTER_RELEASE, "FBReader");
                    myWakeLock.acquire();

                    if (FBReader.locktime != 0) {
                        try {
                            int screenOffTime = Settings.System.getInt(getContentResolver(),
                                    Settings.System.SCREEN_OFF_TIMEOUT);
                            new CountDownTimer(Math.abs(FBReader.locktime - screenOffTime), FBReader.locktime) {

                                @Override
                                public void onTick(long arg0) {

                                }

                                @Override
                                public void onFinish() {
                                    switchWakeLock(false);
                                }
                            }.start();
                        } catch (SettingNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        getWindow().setFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            }
        }
        if (myStartTimer) {
            ZLApplication.Instance().startTimer();
            myStartTimer = false;
        }
    }

    private final void switchWakeLock(boolean on) {
        if (on) {
            if (myWakeLock == null) {
                myWakeLockToCreate = true;
            }
        } else {
            if (myWakeLock != null) {
                synchronized (this) {
                    if (myWakeLock != null) {
                        myWakeLock.release();
                        myWakeLock = null;
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        switchWakeLock(true);
        // switchWakeLock(
        // getLibrary().BatteryLevelToTurnScreenOffOption.getValue() <
        // ZLApplication.Instance().getBatteryLevel()
        // );
        myStartTimer = true;
        /*
         * final int brightnessLevel =
		 * getLibrary().ScreenBrightnessLevelOption.getValue(); if
		 * (brightnessLevel != 0) { setScreenBrightness(brightnessLevel); } else
		 * { setScreenBrightnessAuto(); }
		 */
        if (getLibrary().DisableButtonLightsOption.getValue()) {
            setButtonLight(false);
        }

        registerReceiver(myBatteryInfoReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onPause() {
        unregisterReceiver(myBatteryInfoReceiver);
        ZLApplication.Instance().stopTimer();
        if (getLibrary().DisableButtonLightsOption.getValue()) {
            setButtonLight(true);
        }
        ZLApplication.Instance().onWindowClosing();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        switchWakeLock(false);
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        ZLApplication.Instance().onWindowClosing();
        super.onLowMemory();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action) || "android.fbreader.action.VIEW".equals(action)) {
            ZLApplication.Instance().openFile(fileFromIntent(intent), null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        View view = findViewById(R.id.main_view);
        return ((view != null) && view.onKeyDown(keyCode, event))
                || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        View view = findViewById(R.id.main_view);
        return ((view != null) && view.onKeyUp(keyCode, event))
                || super.onKeyUp(keyCode, event);
    }
}
