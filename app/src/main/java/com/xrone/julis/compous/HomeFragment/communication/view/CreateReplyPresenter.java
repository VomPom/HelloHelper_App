package com.xrone.julis.compous.HomeFragment.communication.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.StringData.NetURL;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.HomeFragment.communication.Model.Author;
import com.xrone.julis.compous.HomeFragment.communication.Model.Reply;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateReplyPresenter implements ICreateReplyPresenter {

    private final Activity activity;
    private final ICreateReplyView createReplyView;
    private ProgressDialog progressDialog;
    public CreateReplyPresenter(@NonNull Activity activity, @NonNull ICreateReplyView createReplyView) {
        this.activity = activity;
        this.createReplyView = createReplyView;
    }

    @Override
    public void createReplyAsyncTask(@NonNull final String topicId, final String content, final String targetId) {
        progressDialog=new ProgressDialog(activity);
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        RequestQueue queue= Volley.newRequestQueue(activity);
        final StringRequest request=new StringRequest(Request.Method.POST, NetURL.ADDACOMMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("callbakc",s);
                Toast.makeText(activity,"Success.",Toast.LENGTH_LONG).show();
                Reply reply = new Reply();
                    reply.setId(targetId);
                    Author author = new Author();
                    author.setLoginName(Hello.username);
                    author.setAvatarUrl(Hello.head_url);
                    reply.setAuthor(author);
                    reply.setContent(content);
                  //  reply.setContentFromLocal(finalContent); // 这里要使用本地的访问器
                    reply.setCreateAt(new DateTime());
                    reply.setUpList(new ArrayList<String>());
                if(targetId==null){
                    reply.setReplyId("0");
                }else{
                    reply.setReplyId(targetId);
                }

                    createReplyView.onReplyTopicOk(reply);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MyAlert.showErrorMessage(activity,"Error!","评论失败!");
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put("topic_id",topicId);
                map.put("content",content);
                map.put("author_id", Hello.id);
                if(targetId==null){
                    map.put("reply_id","0");
                }else{
                    map.put("reply_id",targetId);
                }
                return map;
            }
        };
        queue.add(request);



//        if (TextUtils.isEmpty(content)) {
//            createReplyView.onContentError(activity.getString(R.string.content_empty_error_tip));
//        } else {
//            final String finalContent;
//            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
//                finalContent = content + "\n\n" + SettingShared.getTopicSignContent(activity);
//            } else {
//                finalContent = content;
//            }
//            createReplyView.onReplyTopicStart();
//            ApiClient.service.createReply(topicId, LoginShared.getAccessToken(activity), finalContent, targetId).enqueue(new DefaultCallback<Result.ReplyTopic>(activity) {
//
//                @Override
//                public boolean onResultOk(int code, Headers headers, Result.ReplyTopic result) {
//                    Reply reply = new Reply();
//                    reply.setId(result.getReplyId());
//                    Author author = new Author();
//                    author.setLoginName(LoginShared.getLoginName(getActivity()));
//                    author.setAvatarUrl(LoginShared.getAvatarUrl(getActivity()));
//                    reply.setAuthor(author);
//                    reply.setContentFromLocal(finalContent); // 这里要使用本地的访问器
//                    reply.setCreateAt(new DateTime());
//                    reply.setUpList(new ArrayList<String>());
//                    reply.setReplyId(targetId);
//                    createReplyView.onReplyTopicOk(reply);
//                    return false;
//                }
//
//                @Override
//                public void onFinish() {
//                    createReplyView.onReplyTopicFinish();
//                }
//
//            });
//        }
    }

}
