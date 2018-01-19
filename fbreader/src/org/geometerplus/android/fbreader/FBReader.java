package org.geometerplus.android.fbreader;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.linthink.reader.ReaderConfig;
import com.linthink.slidingmenu.SlidingMenu;
import com.mouee.fbreader.activity.TextSearchActivity;
import com.mouee.fbreader.interfaces.Purchase;
import com.mouee.fbreader.interfaces.Refresh;
import com.mouee.fbreader.util.BrightnessUtil;

import org.geometerplus.android.fbreader.api.ApiListener;
import org.geometerplus.android.fbreader.api.ApiServerImplementation;
import org.geometerplus.android.fbreader.api.PluginApi;
import org.geometerplus.android.fbreader.library.SQLiteBooksDatabase;
import org.geometerplus.android.fbreader.preferences.WallpaperPreference;
import org.geometerplus.android.util.UIUtil;
import org.geometerplus.fbreader.bookmodel.BookModel;
import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.ClearFindResultsAction;
import org.geometerplus.fbreader.fbreader.ColorProfile;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.fbreader.TurnPageAction;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.fbreader.library.Bookmark;
import org.geometerplus.fbreader.library.Library;
import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.library.ZLibrary;
import org.geometerplus.zlibrary.core.options.ZLIntegerRangeOption;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.text.hyphenation.ZLTextHyphenator;
import org.geometerplus.zlibrary.text.model.ZLTextModel;
import org.geometerplus.zlibrary.text.view.ZLTextView;
import org.geometerplus.zlibrary.text.view.style.ZLTextStyleCollection;
import org.geometerplus.zlibrary.ui.android.R;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidActivity;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidLibrary;
import org.geometerplus.zlibrary.ui.android.view.AndroidFontUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FBReader extends ZLAndroidActivity {

    public static final String ACTION_TIP_PURCHASE = "tip_purchase";
    public static final String EXTRA_PAGE_LIMIT = "pageLimit";
    public static final int REQUEST_PREFERENCES = 1;
    public static final int REQUEST_BOOK_INFO = 2;
    public static final int REQUEST_CANCEL_MENU = 3;
    public static final int RESULT_DO_NOTHING = RESULT_FIRST_USER;
    public static final int RESULT_REPAINT = RESULT_FIRST_USER + 1;
    public static final int RESULT_RELOAD_BOOK = RESULT_FIRST_USER + 2;
    public static final int MSG_WHAT_SWITCH = 10;
    public static final String ACTION_UPDATE_BOOKMARK_BUTTON = "updateBookmarkButton";
    public static final String ACTION_CLOSE = "close";
    public static final String ACTION_MENU = "mouee.dajia.intent.action.MENU";
    public static final String INTENT_EXTRA_BOOKMARK_ADDED = "bookmarkAdded";
    // 文本搜索
    public static final String EXTRA_SEARCH_RESULT_INDEX = "searchResultIndex";
    public static final String ACTION_GO_TO_SEARCH_ITEM = "goToSearchItem";
    // 分享
    private static final String FILE_NAME = "/app_icon.jpg";
    private static final String PLUGIN_ACTION_PREFIX = "___";
    // 屏幕亮度
    private static final String SCREEN_BRIGHTNESS_KEY = "screenBrightness";
    private static final String CURRENT_PAGE_KEY = "currentPage";
    private static final String FORWARD_PAGE_KEY = "forwardPage";
    //引导图接口
//	public static Guide guide = new Guide() {
//		@Override
//		public View showGuide(View contenView) {
//			return contenView;
//		}
//	};
    // 购买
    public static Purchase purchase;
    public static String TEST_IMAGE;
    public static String bookPath = "";//数据本地地址
    public static int probationRate;//限制的阅读进度
    public static int locktime;//无操作时亮屏时间
    public static String bookID = "";//资源id
    public static FBReader inst;
    public static SlidingMenu menu;
    private final List<PluginApi.ActionInfo> myPluginActions = new LinkedList<PluginApi.ActionInfo>();
    private final BroadcastReceiver myPluginInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ArrayList<PluginApi.ActionInfo> actions = getResultExtras(
                    true).getParcelableArrayList(
                    PluginApi.PluginInfo.KEY);
            if (actions != null) {
                synchronized (myPluginActions) {
                    final FBReaderApp fbReader = (FBReaderApp) FBReaderApp
                            .Instance();
                    int index = 0;
                    while (index < myPluginActions.size()) {
                        fbReader.removeAction(PLUGIN_ACTION_PREFIX + index++);
                    }
                    myPluginActions.addAll(actions);
                    index = 0;
                    for (PluginApi.ActionInfo info : myPluginActions) {
                        fbReader.addAction(PLUGIN_ACTION_PREFIX + index++,
                                new RunPluginAction(FBReader.this, fbReader,
                                        info.getId()));
                    }
                }
            }
        }
    };
    public boolean isSelectionShow = false;
    private FBReaderApp fbReader;
    private boolean bShowPreferences = false;
    private int myFullScreenFlag;
    private View menuTop;
    private View menuProgress;
    private View menuOptions;
    private boolean isMenuShown = false;
    private boolean isSubMenuShown = false;
    private SeekBar slider;
    private TextView bookPositionTextView;
    // 书签和目录
    private boolean isTOCBookmarksAdded = false;
    private MyHandler handler = new MyHandler(this);
    private boolean bookmarkAdded = false;
    private ImageButton bookmarkButton;
    private UpdateReceiver bookmarkButtonUpdateReceiver;
    private SeekBar brightnessControl;
    private ToggleButton autoBrightnessToggle;
    private Button btn_purchase;
    private ImageView spacing_left;
    private ImageView spacing_middle;
    private Button spacing_original;
    private ImageView spacing_right;
    private boolean isCurrent = true;

    public MyHandler getHandler() {
        return handler;
    }

    @Override
    protected ZLFile fileFromIntent(Intent intent) {
        String filePath = bookPath;
        return filePath != null ? ZLFile.createFileByPath(filePath) : null;
    }

    @Override
    protected Runnable getPostponedInitAction() {
        return new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /* new TipRunner().start(); */
                        DictionaryUtil.init(FBReader.this);
                    }
                });
            }
        };
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        fbReader = (FBReaderApp) FBReaderApp.Instance();

        final ZLAndroidLibrary zlibrary = (ZLAndroidLibrary) ZLibrary.Instance();

        myFullScreenFlag = zlibrary.ShowStatusBarOption.getValue() ? 0
                : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                myFullScreenFlag);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (fbReader.getPopupById(TextSearchPopup.ID) == null) {
            new TextSearchPopup(fbReader);
        }
        if (fbReader.getPopupById(NavigationPopup.ID) == null) {
            new NavigationPopup(fbReader);
        }
        if (fbReader.getPopupById(SelectionPopup.ID) == null) {
            new SelectionPopup(fbReader);
        }
        if (fbReader.getPopupById(TopMenuPopup.ID) == null) {
            new TopMenuPopup(fbReader);
        }
        fbReader.addAction(ActionCode.SHOW_TOC, new ShowTOCAction(this,
                fbReader));

        fbReader.addAction(ActionCode.SHOW_BOOKMARKS,
                new ShowBookmarksMoueeAction(this, fbReader));
        fbReader.addAction(ActionCode.SHOW_MENU, new ShowMenuAction(this,
                fbReader));
        fbReader.addAction(ActionCode.SHOW_NAVIGATION,
                new ShowNavigationAction(this, fbReader));

        fbReader.addAction(ActionCode.SEARCH, new SearchAction(this, fbReader));
        fbReader.addAction(ActionCode.SHARE_BOOK, new ShareBookAction(this,
                fbReader));

        fbReader.addAction(ActionCode.SELECTION_SHOW_PANEL,
                new SelectionShowPanelAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_HIDE_PANEL,
                new SelectionHidePanelAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_BAIKE,
                new SelectionBaikeAction(this, fbReader));

        fbReader.addAction(ActionCode.SELECTION_DRAW_YELLOW_LINE,
                new SelectionDrawYellowLineAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_DRAW_GREEN_LINE,
                new SelectionDrawGreenLineAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_DRAW_BLUE_LINE,
                new SelectionDrawBlueLineAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_DRAW_PURPLE_LINE,
                new SelectionDrawPurpleLineAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_DELETE_LINE,
                new SelectionDeleteAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_SHARE,
                new SelectionShareAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_SEARCH,
                new SelectionSearchAction(this, fbReader));
        fbReader.addAction(ActionCode.SELECTION_BOOKMARK,
                new SelectionBookmarkAction(this, fbReader));

        fbReader.addAction(ActionCode.PROCESS_HYPERLINK,
                new ProcessHyperlinkAction(this, fbReader));

        fbReader.addAction(ActionCode.SHOW_CANCEL_MENU,
                new ShowCancelMenuAction(this, fbReader));
        // 从FBReaderApp的构造函数移至此
        fbReader.addAction(ActionCode.CLEAR_FIND_RESULTS,
                new ClearFindResultsAction(this, fbReader));
        // 关于菜单
        fbReader.addAction(ActionCode.TOGGLE_MENU, new ToggleMenuAction(this,
                fbReader));
        initMenu();
        // 将翻页Action的初始化移至此
        fbReader.addAction(ActionCode.TURN_PAGE_FORWARD, new TurnPageAction(
                fbReader, true, this));
        fbReader.addAction(ActionCode.TURN_PAGE_BACK, new TurnPageAction(
                fbReader, false, this));
        // 分享
        new Thread() {
            public void run() {
                initImagePath();
            }
        }.start();

        bookmarkButton = (ImageButton) findViewById(R.id.btn_bookmark);

        // configure the SlidingMenu 目录书签菜单
        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_frame);

        if (icicle != null) {
            isTOCBookmarksAdded = icicle.getBoolean("tocBookmarksAdded");
        }

        if (purchase != null)
            purchase.registerReceiver(this);
    }

    private void refreshTOCBookmarksFragment() {
        FragmentManager manager = getSupportFragmentManager();
        if (isTOCBookmarksAdded) {
            Fragment parentFragment = manager.findFragmentById(R.id.menu_frame);
            Refresh fragment = (Refresh) parentFragment
                    .getChildFragmentManager().findFragmentById(
                            R.id.realtabcontent);
            fragment.refresh();
        } else {
            manager.beginTransaction()
                    .replace(R.id.menu_frame, new TOCBookmarksFragment())
                    .commit();
            isTOCBookmarksAdded = true;
        }
    }

    private void updateBookmarkButton(boolean flag) {
        int id = 0;
        if (flag) {
            id = R.drawable.bookmark_exist;
        } else {
            id = R.drawable.bookmark_normal;
        }
        bookmarkButton.setImageResource(id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("tocBookmarksAdded", isTOCBookmarksAdded);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        final Uri data = intent.getData();
        final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            super.onNewIntent(intent);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())
                && data != null && "fbreader-action".equals(data.getScheme())) {
            fbReader.runAction(data.getEncodedSchemeSpecificPart(),
                    data.getFragment());
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String pattern = intent.getStringExtra(SearchManager.QUERY);
            final Runnable runnable = new Runnable() {
                public void run() {
                    final TextSearchPopup popup = (TextSearchPopup) fbReader
                            .getPopupById(TextSearchPopup.ID);
                    popup.initPosition();
                    fbReader.TextSearchPatternOption.setValue(pattern);
                    if (fbReader.getTextView().search(pattern, true, false,
                            false, false) != 0) {
                        startActivity(new Intent(FBReader.this,
                                TextSearchActivity.class));
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                UIUtil.showErrorMessage(FBReader.this,
                                        "textNotFound");
                                popup.StartPosition = null;
                            }
                        });
                    }
                }
            };
            UIUtil.wait("search", runnable, this);
        } else if (ACTION_GO_TO_SEARCH_ITEM.equals(intent.getAction())) {
            int index = intent.getIntExtra(EXTRA_SEARCH_RESULT_INDEX, 0);
            fbReader.getTextView().gotoSearchItem(index);
            final TextSearchPopup popup = (TextSearchPopup) fbReader
                    .getPopupById(TextSearchPopup.ID);
            popup.initPosition();
            fbReader.showPopup(popup.getId());
        } else {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        initPluginActions();

        final ZLAndroidLibrary zlibrary = (ZLAndroidLibrary) ZLibrary
                .Instance();

        final int fullScreenFlag = zlibrary.ShowStatusBarOption.getValue() ? 0
                : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (fullScreenFlag != myFullScreenFlag) {
            finish();
            startActivity(new Intent(this, getClass()));
        }

        final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
        final RelativeLayout root = (RelativeLayout) findViewById(R.id.root_view);

        ((PopupPanel) fbReader.getPopupById(TextSearchPopup.ID)).setPanelInfo(
                this, root);
        ((PopupPanel) fbReader.getPopupById(NavigationPopup.ID)).setPanelInfo(
                this, root);
        ((PopupPanel) fbReader.getPopupById(TopMenuPopup.ID)).setPanelInfo(
                this, root);

        ((PopupPanel) fbReader.getPopupById(SelectionPopup.ID)).setPanelInfo(
                this, root);

        if (isMenuShown) {
            updateMenuProgress();
        }

        // 登记更新书签按钮的广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE_BOOKMARK_BUTTON);
        filter.addAction(ACTION_CLOSE);
        filter.addAction(ACTION_MENU);
        filter.addAction(ACTION_TIP_PURCHASE);
        bookmarkButtonUpdateReceiver = new UpdateReceiver();
        this.registerReceiver(bookmarkButtonUpdateReceiver, filter);

    }

    private void initPluginActions() {
        final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
        synchronized (myPluginActions) {
            int index = 0;
            while (index < myPluginActions.size()) {
                fbReader.removeAction(PLUGIN_ACTION_PREFIX + index++);
            }
            myPluginActions.clear();
        }

        sendOrderedBroadcast(new Intent(PluginApi.ACTION_REGISTER), null,
                myPluginInfoReceiver, null, RESULT_OK, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        inst = this;
        PopupPanel.restoreVisibilities(FBReaderApp.Instance());
        ApiServerImplementation.sendEvent(this,
                ApiListener.EVENT_READ_MODE_OPENED);
        // 设置亮度调节滑块的位置
        float brightness = getPreferences(Context.MODE_PRIVATE).getFloat(
                SCREEN_BRIGHTNESS_KEY, 0.0f);
        if (brightness == 0.0f) {
            brightness = BrightnessUtil.getSystemBrightness(this);
        }
        brightnessControl.setProgress(brightnessValueToProgress(brightness));
        // 初始屏幕亮度
        if (!BrightnessUtil.isAutoBrightness(this)
                && brightness != BrightnessUtil.getSystemBrightness(this)) {
            BrightnessUtil.setBrightness(this, brightness);
        }
        if (probationRate <= 0 && btn_purchase != null && btn_purchase.getVisibility() == View.VISIBLE) {
            btn_purchase.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        float brightnessValue = progressToBrightnessValue(brightnessControl
                .getProgress());
        getPreferences(Context.MODE_PRIVATE).edit()
                .putFloat(SCREEN_BRIGHTNESS_KEY, brightnessValue).commit();
        super.onPause();
    }

    @Override
    public void onStop() {
        ApiServerImplementation.sendEvent(this,
                ApiListener.EVENT_READ_MODE_CLOSED);
        PopupPanel.removeAllWindows(FBReaderApp.Instance(), this);

        if (bookmarkButtonUpdateReceiver != null) {
            this.unregisterReceiver(bookmarkButtonUpdateReceiver);
            bookmarkButtonUpdateReceiver = null;
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ZLTextModel model = fbReader.getTextView().getModel();
        if (model != null) {
            model.removeSavedMarks();
        }
        getSharedPreferences(TextSearchActivity.SP_NAME_CONFIG, MODE_PRIVATE)
                .edit().clear().commit();
        if (purchase != null)
            purchase.unregisterReceiver(this);
        super.onDestroy();
    }

    @Override
    protected FBReaderApp createApplication() {
        if (SQLiteBooksDatabase.Instance() == null) {
            new SQLiteBooksDatabase(this, "READER");
        }
        return new FBReaderApp();
    }

    public void showSelectionPanel() {
        final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
        final ZLTextView view = fbReader.getTextView();
        ((SelectionPopup) fbReader.getPopupById(SelectionPopup.ID)).move(
                view.getSelectionStartY(), view.getSelectionEndY());
        fbReader.showPopup(SelectionPopup.ID);
        isSelectionShow = true;
    }

    public void hideSelectionPanel() {
        isSelectionShow = false;
        final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
        fbReader.hideActivePopup();
    }

    public void onlyHideSelectionPanel() {
        final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
        fbReader.hideActivePopup();
    }

    private void onPreferencesUpdate(int resultCode) {
        final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
        switch (resultCode) {
            case RESULT_DO_NOTHING:
                break;
            case RESULT_REPAINT: {
                AndroidFontUtil.clearFontCache();
                final BookModel model = fbReader.Model;
                if (model != null) {
                    final Book book = model.Book;
                    if (book != null) {
                        book.reloadInfoFromDatabase();
                        ZLTextHyphenator.Instance().load(book.getLanguage());
                    }
                }
                fbReader.clearTextCaches();
                fbReader.getViewWidget().repaint();
                break;
            }
            case RESULT_RELOAD_BOOK:
                fbReader.reloadBook();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PREFERENCES:
            case REQUEST_BOOK_INFO:
                onPreferencesUpdate(resultCode);
                break;
            case REQUEST_CANCEL_MENU:
                ((FBReaderApp) FBReaderApp.Instance())
                        .runCancelAction(resultCode - 1);
                break;
        }
    }

    public void navigate() {
        ((NavigationPopup) FBReaderApp.Instance().getPopupById(
                NavigationPopup.ID)).runNavigation();
    }

    private String makeProgressText(int page, int pagesNumber) {
        return String.valueOf(page) + "/" + pagesNumber;
    }

    // 菜单操作
    public void openProgressMenu(View view) {
        closeMenu();

        isSubMenuShown = true;
        setupMenuProgress();
        fadeIn(menuProgress);
    }

    public void openOptionsMenu(View view) {
        closeMenu();

        setupBrightnessMenu();
        isSubMenuShown = true;
        fadeIn(menuOptions);
    }

    private void setupBrightnessMenu() {
        // 设置自动调节状态
        boolean isAutoBrightness = BrightnessUtil.isAutoBrightness(this);
        autoBrightnessToggle.setChecked(isAutoBrightness);
        if (isAutoBrightness) {
            autoBrightnessToggle.setTextColor(getResources().getColor(
                    R.color.red));
        } else {
            autoBrightnessToggle.setTextColor(getResources().getColor(
                    android.R.color.darker_gray));
        }
        brightnessControl.setEnabled(!isAutoBrightness);
    }

    /**
     * 滑动条的亮度调节范围为0.1～0.9
     */
    private int brightnessValueToProgress(float brightness) {
        if (brightness <= 0.1f) {
            return 0;
        } else if (brightness >= 0.9f) {
            return 100;
        } else {
            return (int) ((brightness - 0.1f) / 0.8f * 100);
        }
    }

    /**
     * 滑动条的亮度调节范围为0.1～0.9
     */
    private float progressToBrightnessValue(int progress) {
        return 0.1f + (progress / 100.0f) * 0.8f;
    }

    private void initOptionsMenu() {
        initWallpaperMenu();
        initBrightnessMenu();
    }

    private void initBrightnessMenu() {
        brightnessControl = (SeekBar) menuOptions
                .findViewById(R.id.sb_brightness_control);
        brightnessControl
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        if (fromUser) {
                            BrightnessUtil.setBrightness(FBReader.this,
                                    progressToBrightnessValue(progress));
                        }
                    }
                });

        autoBrightnessToggle = (ToggleButton) menuOptions
                .findViewById(R.id.tb_auto_brightness);
    }

    private void initWallpaperMenu() {
        final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
        final ColorProfile profile = fbReader.getColorProfile();
        String initialWallpaperValue = profile.WallpaperOption.getValue();

        ZLResource resource = ZLResource.resource("Preferences").getResource(
                "colors");
        final WallpaperPreference wallpaperPreference = new WallpaperPreference(
                this, profile, resource, "background");

        final LinearLayout llWallpaper = (LinearLayout) menuOptions
                .findViewById(R.id.ll_wallpaper);

        final CharSequence[] entries = wallpaperPreference.getEntries();
        final CharSequence[] entryValues = wallpaperPreference.getEntryValues();

        View imgNight = menuOptions.findViewById(R.id.imgNight);
        imgNight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                toggleNightMode(arg0);
            }
        });

        String colorProfileName = fbReader.getColorProfileName();
        if (colorProfileName.equals(ColorProfile.DAY)) {
            imgNight.setBackgroundResource(R.drawable.night_s);
        } else {
            imgNight.setBackgroundResource(R.drawable.night_n);
        }

        for (int i = 0; i < entries.length; i++) {
            View view = llWallpaper.getChildAt(i);
            view.setTag(entryValues[i]);
            if (initialWallpaperValue.equals(entryValues[i])) {
                view.setSelected(true);
            }
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        return;
                    }
                    for (int i = 0; i < entries.length; i++) {
                        llWallpaper.getChildAt(i).setSelected(false);
                    }
                    v.setSelected(true);
                    String value = (String) v.getTag();
                    profile.WallpaperOption.setValue(value);
                    onPreferencesUpdate(RESULT_REPAINT);
                }
            });
        }
    }

    private void closeMenu() {
        isMenuShown = false;
        fadeOut(menuTop);
        fadeOut(menuProgress);
    }

    public void toggleNightMode(View view) {
        ImageView button = (ImageView) view;
        String colorProfileName = fbReader.getColorProfileName();
        if (colorProfileName.equals(ColorProfile.DAY)) {
            fbReader.runAction(ActionCode.SWITCH_TO_NIGHT_PROFILE);
            button.setBackgroundResource(R.drawable.night_n);
        } else {
            fbReader.runAction(ActionCode.SWITCH_TO_DAY_PROFILE);
            button.setBackgroundResource(R.drawable.night_s);
        }
    }

    public void goToBookshelf(View view) {
        finish();
    }

    public void increaseFontSize(View view) {
        fbReader.runAction(ActionCode.INCREASE_FONT);
    }

    public void decreaseFontSize(View view) {
        fbReader.runAction(ActionCode.DECREASE_FONT);
    }

    public void smallLineSpace(View view) {
        defaultSpacing();
        view.setSelected(true);
        fbReader.runAction(ActionCode.SPACE_SMALL);
    }

    public void middleLineSpace(View view) {
        defaultSpacing();
        view.setSelected(true);
        fbReader.runAction(ActionCode.SPACE_MIDDLE);
    }

    public void bigLineSpace(View view) {
        defaultSpacing();
        view.setSelected(true);
        fbReader.runAction(ActionCode.SPACE_BIG);
    }

    public void oriLineSpace(View view) {
        defaultSpacing();
        spacing_original.setTextColor(getResources().getColor(
                R.color.original_selected_color));
        fbReader.runAction(ActionCode.SPACE_ORIGINAL);
    }

    public void showTOC(View view) {
        toggleMenu();
        refreshTOCBookmarksFragment();
        menu.showMenu();
    }

    public void toggleBookmark(View view) {
        bookmarkAdded = !bookmarkAdded;
        ImageButton button = (ImageButton) view;
        if (bookmarkAdded) {
            Bookmark bookmark = BookmarksFragment.addBookmarkOutside();
            if (bookmark != null) {
                bookmark.save();
            }
            button.setImageResource(R.drawable.bookmark_exist);
        } else {
            int startIndex = fbReader.getTextView().getStartCursor()
                    .getParagraphIndex();
            long bookId = fbReader.Model.Book.getId();
            Bookmark bookmark = Library.Instance().getBookmark(bookId, startIndex);
            bookmark.delete();
            button.setImageResource(R.drawable.bookmark_normal);
        }
    }

    public void search(View view) {
        toggleMenu();
        startActivity(new Intent(this, TextSearchActivity.class));
        if (ReaderConfig.instance().textSearchAcitivtySlideInUp()) {
            overridePendingTransition(R.anim.slide_in_up, 0);
        }
    }

    public void shareBook(View view) {

        final Dialog shareDialog = new Dialog(this,
                R.style.Translucent_NoTitle);
        View view_dialog = LayoutInflater.from(this).inflate(
                R.layout.dialog_share_layout, null);
        shareDialog.setContentView(view_dialog);
        shareDialog.setCanceledOnTouchOutside(true);
        Window window = shareDialog.getWindow();
        android.view.WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        shareDialog.show();
        Button cancelBtn = (Button) view_dialog.findViewById(R.id.button1);
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
            }
        });

    }

    public void onAutoBrightnessToggleClicked(View view) {
        ToggleButton button = (ToggleButton) view;
        boolean on = button.isChecked();
        brightnessControl.setEnabled(!on);
        BrightnessUtil.toggleAutoBrightness(this, on);
        if (on) {
            BrightnessUtil.setBrightness(this,
                    BrightnessUtil.getSystemBrightness(this));
            button.setTextColor(getResources().getColor(R.color.red));
        } else {
            BrightnessUtil.setBrightness(this,
                    progressToBrightnessValue(brightnessControl.getProgress()));
            button.setTextColor(getResources().getColor(
                    android.R.color.darker_gray));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (bShowPreferences) {
            bShowPreferences = false;
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    public void toggleMenu() {
        if (isSubMenuShown) {
            isSubMenuShown = false;
            if (menuProgress.getVisibility() == View.VISIBLE) {
                fadeOut(menuProgress);
            }
            if (menuOptions.getVisibility() == View.VISIBLE) {
                fadeOut(menuOptions);
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            return;
        }
        isMenuShown = !isMenuShown;
        if (isMenuShown) {
            // 不显示statusBar
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) menuTop
                    .getLayoutParams();
            // 设置顶部菜单距离顶部距离
            menuTop.setLayoutParams(layoutParams);
            setupMenuProgress();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    fadeIn(menuTop);
                    fadeIn(menuProgress);
                }
            }, getResources()
                    .getInteger(android.R.integer.config_shortAnimTime));
        } else {
            hideStatusBar();
            fadeOut(menuTop);
            fadeOut(menuProgress);
        }
    }

    private void fadeIn(final View view) {
        Animation animation = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        animation.setDuration(getResources().getInteger(
                android.R.integer.config_shortAnimTime));
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(animation);
    }

    private void fadeOut(final View view) {
        Animation animation = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        animation.setDuration(getResources().getInteger(
                android.R.integer.config_shortAnimTime));
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }
        });
        view.startAnimation(animation);
    }

    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void setupMenuProgress() {
        final ZLTextView textView = fbReader.getTextView();
        final ZLTextView.PagePosition pagePosition = textView.pagePosition();
        if (slider.getMax() != pagePosition.Total - 1
                || slider.getProgress() != pagePosition.Current - 1) {
            slider.setMax(pagePosition.Total - 1);
            slider.setProgress(pagePosition.Current - 1);
            bookPositionTextView.setText(makeProgressText(pagePosition.Current,
                    pagePosition.Total));
        }
    }

    public void updateMenuProgress() {
        setupMenuProgress();
    }

    private void initMenu() {
        menuTop = findViewById(R.id.menu_top);
        btn_purchase = (Button) menuTop.findViewById(R.id.btn_purchase);
        if (probationRate <= 0) {
            btn_purchase.setVisibility(View.GONE);
        }
        btn_purchase.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int limit = fbReader.getPageUpperLimit();
                tipPurchase(limit);
            }
        });

        menuProgress = findViewById(R.id.menu_progress);
        // 防止进行翻页操作
        menuProgress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        menuOptions = findViewById(R.id.menu_options);
        menuOptions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        ZLIntegerRangeOption lineSpaceOption = ZLTextStyleCollection.Instance()
                .getBaseStyle().LineSpaceOption;
        int value = lineSpaceOption.getValue();
        spacing_left = (ImageView) menuOptions.findViewById(R.id.spacing_left);
        spacing_middle = (ImageView) menuOptions
                .findViewById(R.id.spacing_middle);
        spacing_right = (ImageView) menuOptions
                .findViewById(R.id.spacing_right);
        spacing_original = (Button) menuOptions
                .findViewById(R.id.spacing_original);
        defaultSpacing();

        if (value == 10) {
            spacing_left.setSelected(true);
        } else if (value == 13) {
            spacing_middle.setSelected(true);
        } else if (value == 20) {
            spacing_right.setSelected(true);

        }
        initOptionsMenu();
        slider = (SeekBar) menuProgress.findViewById(R.id.book_position_slider);
        bookPositionTextView = (TextView) menuProgress
                .findViewById(R.id.book_position_text);
        final ImageButton go_page_btn = (ImageButton) findViewById(R.id.go_page_btn);
        int forwardpage = getPreferences(Context.MODE_PRIVATE).getInt(
                FORWARD_PAGE_KEY + bookID, 1);
        int currentPage = getPreferences(Context.MODE_PRIVATE).getInt(
                CURRENT_PAGE_KEY + bookID, 1);
        if (currentPage > forwardpage) {
            go_page_btn.setBackgroundResource(R.drawable.page_back);
        } else {
            go_page_btn.setBackgroundResource(R.drawable.page_forward);
        }
        go_page_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int page = 1;
                if (isCurrent) {
                    isCurrent = false;
                    page = getPreferences(Context.MODE_PRIVATE).getInt(
                            FORWARD_PAGE_KEY + bookID, 1);
                    int currentPage = getPreferences(Context.MODE_PRIVATE).getInt(
                            CURRENT_PAGE_KEY + bookID, 1);
                    if (currentPage > page) {
                        go_page_btn.setBackgroundResource(R.drawable.page_back);
                    } else {
                        go_page_btn.setBackgroundResource(R.drawable.page_forward);
                    }
                } else {
                    isCurrent = true;
                    go_page_btn.setBackgroundResource(R.drawable.page_forward);
                    page = getPreferences(Context.MODE_PRIVATE).getInt(
                            CURRENT_PAGE_KEY + bookID, 1);
                    int currentPage = getPreferences(Context.MODE_PRIVATE).getInt(
                            FORWARD_PAGE_KEY + bookID, 1);
                    if (currentPage > page) {
                        go_page_btn.setBackgroundResource(R.drawable.page_back);
                    } else {
                        go_page_btn.setBackgroundResource(R.drawable.page_forward);
                    }
                }
                final ZLTextView view = fbReader.getTextView();
                final int pagesNumber = slider.getMax() + 1;
                if (page == 1) {
                    view.gotoHome();
                    slider.setProgress(0);
                } else {
                    view.gotoPage(page);
                    slider.setProgress(page);
                }
                bookPositionTextView.setText(makeProgressText(page,
                        pagesNumber));
                fbReader.getViewWidget().reset();
                fbReader.getViewWidget().repaint();
            }
        });
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private void gotoPage(int page) {
                final ZLTextView view = fbReader.getTextView();
                if (page == 1) {
                    view.gotoHome();
                } else {
                    view.gotoPage(page);
                }
                //保存当前位置
                isCurrent = true;
                int forwardPage = getPreferences(Context.MODE_PRIVATE).getInt(
                        FORWARD_PAGE_KEY + bookID, 1);
                if (page > forwardPage) {
                    go_page_btn.setBackgroundResource(R.drawable.page_forward);
                } else {
                    go_page_btn.setBackgroundResource(R.drawable.page_back);
                }
                getPreferences(Context.MODE_PRIVATE).edit()
                        .putInt(CURRENT_PAGE_KEY + bookID, page).commit();
                fbReader.getViewWidget().reset();
                fbReader.getViewWidget().repaint();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int page = seekBar.getProgress() + 1;
                int limit = fbReader.getPageUpperLimit();
                if (limit != 0 && page > limit) {
                    bookPositionTextView.setText(makeProgressText(limit,
                            seekBar.getMax() + 1));
                    seekBar.setProgress(limit - 1);
                    gotoPage(limit);
                    tipPurchase(limit);
                } else {
                    gotoPage(page);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int page = seekBar.getProgress() + 1;
                int limit = fbReader.getPageUpperLimit();
                if (limit != 0 && page > limit) {
                    getPreferences(Context.MODE_PRIVATE).edit()
                            .putInt(FORWARD_PAGE_KEY + bookID, limit).commit();
                } else {
                    getPreferences(Context.MODE_PRIVATE).edit()
                            .putInt(FORWARD_PAGE_KEY + bookID, page).commit();
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    final int page = progress + 1;
                    final int pagesNumber = seekBar.getMax() + 1;
                    bookPositionTextView.setText(makeProgressText(page,
                            pagesNumber));
                    bookPositionTextView.setTextColor(Color.RED);
                }
            }
        });
    }

    private void defaultSpacing() {
        spacing_left.setSelected(false);
        spacing_middle.setSelected(false);
        spacing_right.setSelected(false);
        spacing_original.setTextColor(getResources().getColor(
                R.color.original_color));
    }

    private void initImagePath() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    && this.getExternalFilesDir(null).exists()) {
                TEST_IMAGE = this.getExternalFilesDir(null).getAbsolutePath()
                        + FILE_NAME;
            } else {
                TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath()
                        + FILE_NAME;
            }
            File file = new File(TEST_IMAGE);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(getResources(),
                        R.drawable.icon);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            TEST_IMAGE = null;
        }
    }

    @Override
    public boolean onSearchRequested() {
        final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
        final ArrayList<ZLApplication.PopupPanel> activePopupList = fbreader
                .getActivePopup();
        fbreader.hideActivePopup();

        final SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        manager.setOnCancelListener(new SearchManager.OnCancelListener() {
            public void onCancel() {
                if (activePopupList != null) {
                    for (ZLApplication.PopupPanel popup : activePopupList) {
                        fbreader.showPopup(popup.getId());
                    }
                }
                manager.setOnCancelListener(null);
            }
        });
        startSearch(fbreader.TextSearchPatternOption.getValue(), true, null,
                false);
        return true;
    }

    private void tipPurchase(int limit) {

        Toast.makeText(this, "阅读限制", Toast.LENGTH_SHORT).show();
//        if (purchase != null)
//            purchase.tipPurchase(this, limit, bookID);
    }

    static class MyHandler extends Handler {
        WeakReference<FBReader> mActivity;

        public MyHandler(FBReader activity) {
            mActivity = new WeakReference<FBReader>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_SWITCH:
                    mActivity.get().menu.showContent();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_UPDATE_BOOKMARK_BUTTON)) {
                boolean flag = intent.getBooleanExtra(
                        INTENT_EXTRA_BOOKMARK_ADDED, false);
                updateBookmarkButton(flag);
                bookmarkAdded = flag;
            } else if (intent.getAction().equals(ACTION_CLOSE)) {
                if (menu.isMenuShowing()) {
                    menu.showContent();
                } else if (fbReader.hasActivePopup()) {
                    fbReader.hideActivePopup();
                    fbReader.runAction(ActionCode.SELECTION_CLEAR,
                            new Object[]{null});
                } else if (isMenuShown || isSubMenuShown) {
                    toggleMenu();
                } else {
                    fbReader.closeWindow();
                }
            } else if (intent.getAction().equals(ACTION_MENU)) {
                if (fbReader.hasActivePopup()) {
                    fbReader.hideActivePopup();
                    fbReader.runAction(ActionCode.SELECTION_CLEAR,
                            new Object[]{null});
                } else {
                    toggleMenu();
                }
            } else if (intent.getAction().equals(ACTION_TIP_PURCHASE)) {
                int pageLimit = intent.getIntExtra(EXTRA_PAGE_LIMIT, 0);
                tipPurchase(pageLimit);
            }
        }
    }
}
