package com.xrone.julis.compous.HomeFragment.communication.Presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xrone.julis.compous.StringData.NetURL;
import com.xrone.julis.compous.HomeFragment.communication.util.FormatUtils;
import com.xrone.julis.compous.HomeFragment.communication.util.HandlerUtils;
import com.xrone.julis.compous.HomeFragment.communication.Model.Author;
import com.xrone.julis.compous.HomeFragment.communication.Model.Topic;
import com.xrone.julis.compous.HomeFragment.communication.adapter.TopicListAdapter;
import com.xrone.julis.compous.HomeFragment.communication.listener.IMainPresenter;
import com.xrone.julis.compous.HomeFragment.communication.view.IMainView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPresenter implements IMainPresenter {

    private static final int PAGE_LIMIT = 20;

    private final Activity activity;
    private final IMainView mainView;
    private TopicListAdapter adapter;
//    private Tab tab = Tab.all;
//    private Call<Result.Data<List<Topic>>> refreshCall = null;
//    private Call<Result.Data<List<Topic>>> loadMoreCall = null;

    public MainPresenter(@NonNull Activity activity, @NonNull IMainView mainView,TopicListAdapter adapter) {
        this.activity = activity;
        this.mainView = mainView;
        this.adapter=adapter;
    }
//
//    private void cancelRefreshCall() {
//        if (refreshCall != null) {
//            if (!refreshCall.isCanceled()) {
//                refreshCall.cancel();
//            }
//            refreshCall = null;
//        }
//    }
//
//    private void cancelLoadMoreCall() {
//        if (loadMoreCall != null) {
//            if (!loadMoreCall.isCanceled()) {
//                loadMoreCall.cancel();
//            }
//            loadMoreCall = null;
//        }
//    }

//    @Override
//    public void switchTab(@NonNull Tab tab) {
//        if (this.tab != tab) {
//            this.tab = tab;
////            cancelRefreshCall();
////            cancelLoadMoreCall();
//            mainView.onSwitchTabOk(tab);
//        }
//    }

    @Override
    public void refreshTopicListAsyncTask() {

        RequestQueue queue=Volley.newRequestQueue(activity);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NetURL.TOPIC_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {


                    List<Topic> topics=new ArrayList<>();
                    JSONObject jsonObject   = null;


                    try {

                        jsonObject = new JSONObject(s);
                        JSONArray trans_result = jsonObject
                                .getJSONArray("data");


                        for (int i = 0; i < trans_result.length(); i++) {
                            JSONObject jo = trans_result.optJSONObject(i);
                            Topic topic=new Topic();
                            topic.setContent(jo.getString("content"));
                            topic.setGood(jo.getString("good").equals("0")?false:true);
                            topic.setTop(jo.getString("top").equals("0")?false:true);
                            topic.setAuthorId(jo.getString("username"));
                            topic.setTitle(jo.getString("title"));


                            int replyCount=Integer.parseInt(jo.getString("reply_count"));
                            topic.setReplyCount(replyCount);
                            int visitCount=
                                    replyCount==0 ?
                                            (int) (Math.random()*20):
                                            (int) (replyCount * Math.random() * 10);

                            topic.setVisitCount(visitCount);


                            topic.setId(jo.getString("id"));topic.setId(jo.getString("id"));
                            topic.setType(jo.getString("tab"));
                            Author author=new Author();
                            author.setLoginName(jo.getString("username"));
                            author.setAvatarUrl(NetURL.WEBSITE+ NetURL.DIRECTIONARY+ NetURL.USER_HEAD_DIR+jo.getString("head_url"));
                            topic.setAuthor(author);
                            topic.setCreateAt( FormatUtils.getDateTime(jo.getString("create_at")));
                            topic.setLastReplyAt(FormatUtils.getDateTime(jo.getString("last_reply_at")));
                            topics.add(topic);
                        }

                        handleTopicList(topics, topicList -> mainView.onRefreshTopicListOk(topicList));

                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, volleyError -> {

            });
        queue.add(stringRequest);
    }

    @Override
    public void loadMoreTopicListAsyncTask(final int page) {
        Log.e("apge", String.valueOf(page));
        RequestQueue queue=Volley.newRequestQueue(activity);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NetURL.TOPIC_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("LoadMore",s);
                List<Topic> topics=new ArrayList<>();
                JSONObject jsonObject   = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray trans_result = jsonObject
                            .getJSONArray("data");

                    Gson gson=new Gson();

                    Topic topic2=gson.fromJson(s,Topic.class);
                    Log.e("tt",topic2.toString());

                    for (int i = 0; i < trans_result.length(); i++) {

                        JSONObject jo = trans_result.optJSONObject(i);
                        java.lang.reflect.Type type = new TypeToken<Topic>() {}.getType();


                        Topic topic=new Topic();
                        topic.setContent(jo.getString("content"));
                        topic.setGood(jo.getString("good").equals("0")?false:true);
                        topic.setTop(jo.getString("top").equals("0")?false:true);
                        topic.setAuthorId(jo.getString("username"));
                        topic.setTitle(jo.getString("title"));
                        int replyCount=Integer.parseInt(jo.getString("reply_count"));
                        topic.setReplyCount(replyCount);
                        int visitCount=
                                replyCount==0 ?
                                        (int) (Math.random()*20):
                                        (int) (replyCount * Math.random() * 10);

                        topic.setVisitCount(visitCount);


                        topic.setId(jo.getString("id"));
                        topic.setType(jo.getString("tab"));
                        Author author=new Author();
                        author.setLoginName(jo.getString("username"));
                        author.setAvatarUrl(NetURL.WEBSITE+ NetURL.DIRECTIONARY+ NetURL.USER_HEAD_DIR+jo.getString("head_url"));
                        topic.setAuthor(author);

                        DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss");
                        String createtime=jo.getString("create_at");
                        DateTime createTime = DateTime.parse(createtime, format);
                        topic.setCreateAt(createTime);
                        String LastReplytime=jo.getString("last_reply_at");
                        DateTime LastReplyTime = DateTime.parse(LastReplytime, format);
                        topic.setLastReplyAt(LastReplyTime);
                        topics.add(topic);
                    }

                    mainView.onLoadMoreTopicListOk(topics);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mainView.onLoadMoreTopicListError(volleyError.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("page", String.valueOf(page));
                return map;
            }
        };
        queue.add(stringRequest);
//        if (loadMoreCall == null) {
//            final Call<Result.Data<List<Topic>>> call = ApiClient.service.getTopicList(tab, page, PAGE_LIMIT, ApiDefine.MD_RENDER);
//            loadMoreCall = call;
//            loadMoreCall.enqueue(new ForegroundCallback<Result.Data<List<Topic>>>(activity) {
//
//                @Override
//                public boolean onResultOk(int code, Headers headers, Result.Data<List<Topic>> result) {
//                    handleTopicList(result.getData(), new OnHandleTopicListFinishListener() {
//
//                        @Override
//                        public void onHandleTopicListFinishListener(@NonNull List<Topic> topicList) {
//                            if (call == loadMoreCall) {

//                                onFinish();
//                            }
//                        }
//
//                    });
//                    return true;
//                }
//
//                @Override
//                public boolean onResultError(int code, Headers headers, Result.Error error) {
//                    mainView.onLoadMoreTopicListError(error.getErrorMessage());
//                    return false;
//                }
//
//                @Override
//                public boolean onCallException(Throwable t, Result.Error error) {
//                    mainView.onLoadMoreTopicListError(error.getErrorMessage());
//                    return false;
//                }
//
//                @Override
//                public boolean onCallCancel() {
//                    return true;
//                }
//
//                @Override
//                public void onFinish() {
//                    loadMoreCall = null;
//                }
//
//            });
//        }
    }

    @Override
    public void getUserAsyncTask() {
//        final String accessToken = LoginShared.getAccessToken(activity);
//        if (!TextUtils.isEmpty(accessToken)) {
//            ApiClient.service.getUser(LoginShared.getLoginName(activity)).enqueue(new ForegroundCallback<Result.Data<User>>(activity) {
//
//                @Override
//                public boolean onResultOk(int code, Headers headers, Result.Data<User> result) {
//                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(getActivity()))) {
//                        LoginShared.update(getActivity(), result.getData());
//                        mainView.updateUserInfoViews();
//                    }
//                    return false;
//                }
//
//            });
//        }
    }

    @Override
    public void getMessageCountAsyncTask() {
//        final String accessToken = LoginShared.getAccessToken(activity);
//        if (!TextUtils.isEmpty(accessToken)) {
//            ApiClient.service.getMessageCount(accessToken).enqueue(new ForegroundCallback<Result.Data<Integer>>(activity) {
//
//                @Override
//                public boolean onResultOk(int code, Headers headers, Result.Data<Integer> result) {
//                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(getActivity()))) {
//                        mainView.updateMessageCountViews(result.getData());
//                    }
//                    return false;
//                }
//
//            });
//        }
    }

    private void handleTopicList(@NonNull final List<Topic> topicList, @NonNull final OnHandleTopicListFinishListener listener) {
        if (topicList.isEmpty()) {
            listener.onHandleTopicListFinishListener(topicList);
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    for (Topic topic : topicList) {
                        topic.markSureHandleContent();
                    }
                    HandlerUtils.handler.post(new Runnable() {

                        @Override
                        public void run() {
                            listener.onHandleTopicListFinishListener(topicList);
                        }

                    });
                }

            }).start();
        }
    }

    private interface OnHandleTopicListFinishListener {

        void onHandleTopicListFinishListener(@NonNull List<Topic> topicList);

    }

}
