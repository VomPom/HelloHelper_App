package com.xrone.julis.compous.Utils;


import android.util.Log;

import com.android.volley.Response;
import com.xrone.julis.compous.model.TranslateResultModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Julis on 2017/10/28.
 */

public abstract class TransLatorCallback implements Response.Listener<String>{

    TranslateResultModel resultModel = new TranslateResultModel();
    @Override
    public void onResponse(String s) {
        Log.e("source",s);
        JSONObject jsonObject   = null;
        try {
            jsonObject = new JSONObject(s);
            JSONArray trans_result = jsonObject
                    .getJSONArray("trans_result");
            resultModel.setFrom(jsonObject.getString("from"));
            resultModel.setTo(jsonObject.getString("to"));
            for (int i = 0; i < trans_result.length(); i++) {
                JSONObject jo = trans_result.optJSONObject(i);
                resultModel.setDst(jo.getString("dst"));
                resultModel.setSrc(jo.getString("src"));
            }
            setPropety(resultModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public abstract void setPropety(TranslateResultModel resultModel);

}