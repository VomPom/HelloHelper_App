package com.xrone.julis.compous.view.HomeFragment.communication.Presenter;

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
import com.xrone.julis.compous.StringData.AppURL;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.Reply;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.Author;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.TopicWithReply;
import com.xrone.julis.compous.view.HomeFragment.communication.view.ITopicView;

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


public class TopicPresenter implements ITopicPresenter {
    private final String TOPIC_ID="topic_id";
    private final Activity activity;
    private final ITopicView topicView;

    public TopicPresenter(@NonNull Activity activity, @NonNull ITopicView topicView) {
        this.activity = activity;
        this.topicView = topicView;
    }

    @Override
    public void getTopicAsyncTask(@NonNull final String topicId) {
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        StringRequest request=new StringRequest(Request.Method.POST, AppURL.COMMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("ss",s);
                TopicWithReply topic=new TopicWithReply();
                JSONObject jsonObject   = null;
                try {

                    jsonObject = new JSONObject(s);

                    JSONObject dataJson = jsonObject.getJSONObject("data");
                    DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss");
                    topic.setContent(dataJson.getString("content"));
                    topic.setGood(dataJson.getString("good").equals("0")?false:true);
                    topic.setTop(dataJson.getString("top").equals("0")?false:true);
                    topic.setTitle(dataJson.getString("title"));
                    topic.setReplyCount(Integer.parseInt(dataJson.getString("reply_count")));
                    topic.setVisitCount(Integer.parseInt(dataJson.getString("visit_count")));

                    String createtime=dataJson.getString("create_at");
                    DateTime createTime = DateTime.parse(createtime, format);
                    topic.setCreateAt(createTime);
                    JSONObject authorJson = dataJson.getJSONObject("author");
                    Author author = new Author();
                    author.setLoginName(authorJson.getString("username"));
                    author.setAvatarUrl(authorJson.getString("head_url"));
                    topic.setAuthor(author);


                    List<Reply> replies=new ArrayList<>();
                    JSONArray commentsJson = dataJson.getJSONArray("comments");
                    for (int j = 0; j < commentsJson.length(); j++){
                        JSONObject joo = commentsJson.optJSONObject(j);

                        Reply reply=new Reply();
                        Author authorReplay = new Author();
                        authorReplay.setLoginName(joo.getString("author_name"));
                        authorReplay.setAvatarUrl(joo.getString("author_head_url"));
                        reply.setAuthor(authorReplay);

                        reply.setContent(joo.getString("content"));
                        reply.setReplyId(joo.getString("reply_id"));
                        reply.setId(joo.getString("id"));




                        String replyCreatetime=joo.getString("create_at");
                        DateTime replyCreateTime = DateTime.parse(replyCreatetime, format);
                        reply.setCreateAt(replyCreateTime);
                        replies.add(reply);
                    }

                    topic.setReplyList(replies);
                    topicView.onGetTopicOk(topic);
                    topicView.onGetTopicFinish();
                } catch (JSONException e) {
                    Log.e("eee","JsonErroAtTopicPres");
                    topicView.onGetTopicFinish();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                topicView.onGetTopicFinish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put(TOPIC_ID,topicId);
                return map;
            }
        };
        requestQueue.add(request);


//        ApiClient.service.getTopic(topicId, LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER).enqueue(new DefaultCallback<Result.Data<TopicWithReply>>(activity) {
//
//            @Override
//            public boolean onResultOk(int code, Headers headers, Result.Data<TopicWithReply> result) {//       topicView.onGetTopicOk(topic);
//                return false;
//            }
//
//            @Override
//            public void onFinish() {
//                topicView.onGetTopicFinish();
//            }
//
//        });
    }

}
