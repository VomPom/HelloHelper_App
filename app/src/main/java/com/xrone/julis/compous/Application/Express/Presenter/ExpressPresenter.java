package com.xrone.julis.compous.Application.Express.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.StringData.NetURL;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Application.Express.View.IExpressView;
import com.xrone.julis.compous.Application.translate.TranslateActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julis on 2017/12/23.
 */

public class ExpressPresenter implements IExpressPresenter {
    private final Activity activity;
    private final IExpressView expressView;
    private final String TAG="Express";
    private double deslatitude;
    private double deslongitude;

    public ExpressPresenter(Activity activity, IExpressView expressView) {
        this.activity = activity;
        this.expressView = expressView;
    }

    @Override
    public void sendAndgetExpressInformation(@NonNull String message) {

        StringRequest findRequest =new StringRequest(Request.Method.POST, NetURL.GET_EXPRESS_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                JSONObject   jsonObject = new JSONObject(s);
                JSONArray dataJson = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataJson.length(); i++) {
                        JSONObject object = dataJson.getJSONObject(i);
                        deslatitude= Double.parseDouble(object.getString("altitude"));
                        deslongitude=Double.parseDouble(object.getString("longitude"));
                        expressView.startLocation(deslatitude,deslongitude);
                   }
                } catch (JSONException e) {
                    MyAlert.AlertWithOK(activity,"Sorry！Not Found！",
                            "Please check whether the message is the Express notification information\n" +
                                    "If it is, we will complete it.\nThanks for your cooperation.",
                            (dialog, which) -> {
                                Intent intent=new Intent(activity, TranslateActivity.class);
                                intent.putExtra("text",message);
                                activity.startActivity(intent);
                            });
                    e.printStackTrace();
                }
            }
        }, volleyError -> {
                MyAlert.showErrorMessage(activity,"Sorry！Not Found！","Something wrong.");
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("message", message);
                return map;
            }
        };

        RequestQueue mQueue= Volley.newRequestQueue(activity);
        mQueue.add(findRequest);

    }


}
