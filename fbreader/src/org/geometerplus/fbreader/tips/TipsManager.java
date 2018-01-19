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

package org.geometerplus.fbreader.tips;

import org.geometerplus.fbreader.Paths;
import org.geometerplus.fbreader.network.atom.ATOMXMLReader;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;
import org.geometerplus.zlibrary.core.network.ZLNetworkManager;
import org.geometerplus.zlibrary.core.options.ZLBooleanOption;
import org.geometerplus.zlibrary.core.options.ZLIntegerOption;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TipsManager {
    private static TipsManager ourInstance;
    // time when last tip was shown, 2^16 milliseconds
    private final ZLIntegerOption myLastShownOption =
            new ZLIntegerOption("tips", "shownAt", 0);
    // index of next tip to show
    private final ZLIntegerOption myIndexOption =
            new ZLIntegerOption("tips", "index", 0);
    private final int DELAY = (24 * 60 * 60 * 1000) >> 16; // 1 day
    public ZLBooleanOption TipsAreInitializedOption =
            new ZLBooleanOption("tips", "tipsAreInitialized", false);
    public ZLBooleanOption ShowTipsOption =
            new ZLBooleanOption("tips", "showTips", false);
    private volatile boolean myDownloadInProgress;
    private List<Tip> myTips;

    private TipsManager() {
    }

    public static TipsManager Instance() {
        if (ourInstance == null) {
            ourInstance = new TipsManager();
        }
        return ourInstance;
    }

    private String getUrl() {
        return "https://data.fbreader.org/tips/tips.php";
    }

    private String getLocalFilePath() {
        return Paths.networkCacheDirectory() + "/tips/tips.xml";
    }

    private List<Tip> getTips() {
        if (myTips == null) {
            final ZLFile file = ZLFile.createFileByPath(getLocalFilePath());
            if (file.exists()) {
                final TipsFeedHandler handler = new TipsFeedHandler();
                new ATOMXMLReader(handler, false).readQuietly(file);
                final List<Tip> tips = Collections.unmodifiableList(handler.Tips);
                if (tips.size() > 0) {
                    myTips = tips;
                }
            }
        }
        return myTips;
    }

    public boolean hasNextTip() {
        final List<Tip> tips = getTips();
        if (tips == null) {
            return false;
        }

        final int index = myIndexOption.getValue();
        if (index >= tips.size()) {
            new File(getLocalFilePath()).delete();
            myIndexOption.setValue(0);
            return false;
        }

        return true;
    }

    public Tip getNextTip() {
        final List<Tip> tips = getTips();
        if (tips == null) {
            return null;
        }

        final int index = myIndexOption.getValue();
        if (index >= tips.size()) {
            new File(getLocalFilePath()).delete();
            myIndexOption.setValue(0);
            return null;
        }

        myIndexOption.setValue(index + 1);
        myLastShownOption.setValue(currentTime());
        return tips.get(index);
    }

    private int currentTime() {
        return (int) (new Date().getTime() >> 16);
    }

    public Action requiredAction() {
        if (ShowTipsOption.getValue()) {
            if (hasNextTip()) {
                return myLastShownOption.getValue() + DELAY < currentTime()
                        ? Action.Show : Action.None;
            } else {
                return myDownloadInProgress
                        ? Action.None : Action.Download;
            }
        } else if (!TipsAreInitializedOption.getValue()) {
            return Action.Initialize;
        }
        return Action.None;
    }

    public synchronized void startDownloading() {
        if (requiredAction() != Action.Download) {
            return;
        }

        myDownloadInProgress = true;

        new File(Paths.networkCacheDirectory() + "/tips").mkdirs();
        new Thread(new Runnable() {
            public void run() {
                try {
                    ZLNetworkManager.Instance().downloadToFile(
                            getUrl(), new File(getLocalFilePath())
                    );
                } catch (ZLNetworkException e) {
                    e.printStackTrace();
                } finally {
                    myDownloadInProgress = false;
                }
            }
        }).start();
    }

    public static enum Action {
        Initialize,
        Show,
        Download,
        None
    }
}
