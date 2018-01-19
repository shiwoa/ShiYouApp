package com.mouee.fbreader.interfaces;

import android.app.Activity;

public interface Purchase {
    void tipPurchase(Activity activity, int limit, String bookID);

    void performPay();

    void registerReceiver(Activity activity);

    void unregisterReceiver(Activity activity);
}
