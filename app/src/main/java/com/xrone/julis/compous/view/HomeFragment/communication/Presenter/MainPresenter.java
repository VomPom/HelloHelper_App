package com.xrone.julis.compous.view.HomeFragment.communication.Presenter;

import android.app.Activity;
import android.support.annotation.NonNull;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.StringData.AppURL;
import com.xrone.julis.compous.view.HomeFragment.communication.util.HandlerUtils;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.Author;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.Tab;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.Topic;
import com.xrone.julis.compous.view.HomeFragment.communication.adapter.TopicListAdapter;
import com.xrone.julis.compous.view.HomeFragment.communication.listener.IMainPresenter;
import com.xrone.julis.compous.view.HomeFragment.communication.view.IMainView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements IMainPresenter {

    private static final int PAGE_LIMIT = 20;

    private final Activity activity;
    private final IMainView mainView;
    private TopicListAdapter adapter;
    private Tab tab = Tab.all;
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

    @Override
    public void switchTab(@NonNull Tab tab) {
        if (this.tab != tab) {
            this.tab = tab;
//            cancelRefreshCall();
//            cancelLoadMoreCall();
            mainView.onSwitchTabOk(tab);
        }
    }

    @Override
    public void refreshTopicListAsyncTask() {

        RequestQueue queue=Volley.newRequestQueue(activity);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppURL.TOPIC_URL, new Response.Listener<String>() {
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
                            topic.setReplyCount(Integer.parseInt(jo.getString("reply_count")));
                            topic.setVisitCount(Integer.parseInt(jo.getString("visit_count")));
                            topic.setId(jo.getString("id"));
                            Author author=new Author();
                            author.setLoginName(jo.getString("username"));
                            author.setAvatarUrl(jo.getString("head_url"));
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

                        handleTopicList(topics, new OnHandleTopicListFinishListener() {
                            @Override
                            public void onHandleTopicListFinishListener(@NonNull List<Topic> topicList) {
                                mainView.onRefreshTopicListOk(topicList);
                            }
                        });

                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(stringRequest);

//        if (refreshCall == null) {
//            final Call<Result.Data<List<Topic>>> call = ApiClient.service.getTopicList(tab, 1, PAGE_LIMIT, ApiDefine.MD_RENDER);
//            refreshCall = call;
//            refreshCall.enqueue(new ForegroundCallback<Result.Data<List<Topic>>>(activity) {
//
//                @Override
//                public boolean onResultOk(int code, Headers headers, Result.Data<List<Topic>> result) {
//                    handleTopicList(result.getData(), new OnHandleTopicListFinishListener() {
//
//                        @Override
//                        public void onHandleTopicListFinishListener(@NonNull List<Topic> topicList) {
//                            if (call == refreshCall) {
//                                cancelLoadMoreCall();
//                                mainView.onRefreshTopicListOk(topicList);
//                                onFinish();
//                            }
//                        }
//
//                    });
//                    Log.e("TAG>",result.getData().toString()) ;
//                    return true;
//                }
//
//                @Override
//                public boolean onResultError(int code, Headers headers, Result.Error error) {
//                    mainView.onRefreshTopicListError(error.getErrorMessage());
//                    return false;
//                }
//
//                @Override
//                public boolean onCallException(Throwable t, Result.Error error) {
//                    mainView.onRefreshTopicListError(error.getErrorMessage());
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
//                    refreshCall = null;
//                }
//
//            });
//        }
    }

    @Override
    public void loadMoreTopicListAsyncTask(int page) {
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
//                                mainView.onLoadMoreTopicListOk(topicList);
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
