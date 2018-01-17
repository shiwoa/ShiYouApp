package com.jiaoyu.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class PublicListTypeEntityCallback extends Callback<PublicListTypeEntity>
{
    @Override
    public PublicListTypeEntity parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        PublicListTypeEntity publicListEntity = new Gson().fromJson(string, PublicListTypeEntity.class);
        return publicListEntity;
    }


}
