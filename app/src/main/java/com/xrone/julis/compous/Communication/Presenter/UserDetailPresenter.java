package com.xrone.julis.compous.Communication.Presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.StringData.NetURL;

import com.xrone.julis.compous.Communication.Model.Author;
import com.xrone.julis.compous.Communication.Model.Topic;
import com.xrone.julis.compous.Communication.Model.User;
import com.xrone.julis.compous.Communication.util.FormatUtils;
import com.xrone.julis.compous.Communication.view.IUserDetailView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserDetailPresenter implements ITopicHeaderPresenter.IUserDetailPresenter {

    private final Activity activity;
    private final IUserDetailView userDetailView;
    private final String TAG="UserDetailPresenter";
    private boolean loading = false;

    public UserDetailPresenter(@NonNull Activity activity, @NonNull IUserDetailView userDetailView) {
        this.activity = activity;
        this.userDetailView = userDetailView;
    }

    @Override
    public void getUserAsyncTask(@NonNull String loginName) {
        userDetailView.onGetUserStart();

        Volley.newRequestQueue(activity)
            .add(new StringRequest(Request.Method.POST, NetURL.PERSONAL_DEATL_URL,
                s->{
                Log.e(TAG,s);
                    JSONObject jsonObject   = null;
                    List<Topic> topicSimples=new ArrayList<>();
                    List<Topic> reply_topics=new ArrayList<>();

                    User user=new User();
                    try {

                        jsonObject = new JSONObject(s);
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        JSONArray recently_replies = dataJson.getJSONArray("recent_replyies");
                        for (int i = 0; i < recently_replies.length(); i++) {

                            JSONObject jo = recently_replies.optJSONObject(i);
                            Topic replytopic=new Topic();
                            replytopic.setTitle(jo.getString("title"));
                            replytopic.setLastReplyAt(FormatUtils.getDateTime(jo.getString("last_reply_at")));
                            replytopic.setId(jo.getString("id"));
                            replytopic.setType(jo.getString("tab"));
                            replytopic.setContent(jo.getString("content"));

                            Author author=new Author();
                            author.setAvatarUrl(jo.getString("head_url"));
                            author.setLoginName(jo.getString("username"));
                            replytopic.setAuthor(author);
                            reply_topics.add(replytopic);
                        }



                        JSONArray recently_topics = dataJson.getJSONArray("recent_topics");
                        for (int i = 0; i < recently_topics.length(); i++) {
                            JSONObject jo = recently_topics.optJSONObject(i);
                            Topic topicSimple=new Topic();
                            topicSimple.setTitle(jo.getString("title"));
                            topicSimple.setLastReplyAt(FormatUtils.getDateTime(jo.getString("create_at")));
                            topicSimple.setId(jo.getString("id"));
                            topicSimple.setType(jo.getString("tab"));
                            topicSimple.setContent(jo.getString("content"));
                            topicSimples.add(topicSimple);
                        }

                        user.setRecentReplyList(reply_topics);
                        user.setRecentTopicList(topicSimples);

                        userDetailView.onGetUserOk(user);



                } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG+"Error",e.getMessage());
                    }
                },
                volleyError -> {
                     Log.e(TAG+"Error",volleyError.toString());
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("loginName",loginName);
                return map;
            }
        });

    }

}
