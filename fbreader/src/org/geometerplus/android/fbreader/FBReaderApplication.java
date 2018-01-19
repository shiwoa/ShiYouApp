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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Environment;

import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;

public class FBReaderApplication extends ZLAndroidApplication {

    public static Context context;

    public static String APP_VERSION;

    public static String APP_PACKAGE;

    public static File EXT_STORAGE;

    public static File APP_STORAGE;

    public static Properties BUILD_PROPS;

    public static String APP_NAME;

    public static Locale defLocale;

    private static Locale appLocale;

    public static void setAppLocale(final String lang) {
        if (isNotEmpty(lang) && !getAppLocale().getLanguage().equals(lang)) {
            final Configuration config = context.getResources().getConfiguration();
            appLocale = new Locale(lang);
            setAppLocale(config);
        }
    }

    public static Locale getAppLocale() {
        return appLocale != null ? appLocale : defLocale;
    }

    protected static void setAppLocale(final Configuration config) {
        Locale.setDefault(appLocale);
        config.locale = appLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static boolean isNotEmpty(final String s) {
        return length(s) > 0;
    }

    public static int length(final String s) {
        return s != null ? s.length() : 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();

        this.init();

    }

    protected void init() {
        context = getApplicationContext();

        final Configuration config = context.getResources().getConfiguration();
        defLocale = config.locale;

        BUILD_PROPS = new Properties();
        try {
            BUILD_PROPS.load(new FileInputStream("/system/build.prop"));
        } catch (final Throwable th) {
        }

        final PackageManager pm = getPackageManager();
        try {
            final PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            APP_NAME = getApplicationInfo().name;
            APP_VERSION = pi.versionName;
            APP_PACKAGE = pi.packageName;
            EXT_STORAGE = Environment.getExternalStorageDirectory();
            APP_STORAGE = getAppStorage(APP_PACKAGE);

        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (appLocale != null) {
            setAppLocale(newConfig);
        }
    }

    protected File getAppStorage(final String appPackage) {
        File dir = EXT_STORAGE;
        if (dir != null) {
            final File appDir = new File(dir, "." + appPackage);
            if (appDir.isDirectory() || appDir.mkdir()) {
                dir = appDir;
            }
        } else {
            dir = context.getFilesDir();
        }
        dir.mkdirs();
        return dir.getAbsoluteFile();
    }

}
