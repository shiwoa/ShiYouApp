package org.geometerplus.android.fbreader;

import org.geometerplus.fbreader.fbreader.FBReaderApp;

public class ToggleMenuAction extends FBAndroidAction {

    ToggleMenuAction(FBReader baseActivity, FBReaderApp fbreader) {
        super(baseActivity, fbreader);
    }

    @Override
    protected void run(Object... params) {
        BaseActivity.toggleMenu();
    }

}
