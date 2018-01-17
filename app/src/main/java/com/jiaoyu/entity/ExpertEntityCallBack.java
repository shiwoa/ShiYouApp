package com.jiaoyu.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by xiangyao on 2017/10/24.
 */

public abstract class ExpertEntityCallBack extends Callback<ExpertEntity> {
    /**
     * Thread Pool Thread
     *
     * @param response
     * @param id
     */
    @Override
    public ExpertEntity parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        ExpertEntity publicEntity = new Gson().fromJson(string, ExpertEntity.class);
        return publicEntity;
    }

}
