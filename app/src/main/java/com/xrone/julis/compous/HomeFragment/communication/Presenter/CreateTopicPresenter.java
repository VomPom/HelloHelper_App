package com.xrone.julis.compous.HomeFragment.communication.Presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.StringData.NetURL;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.HomeFragment.communication.view.ICreateTopicView;

import java.util.HashMap;
import java.util.Map;


public class CreateTopicPresenter implements ICreateTopicPresenter {

    private final Activity activity;
    private final ICreateTopicView createTopicView;

    public CreateTopicPresenter(@NonNull Activity activity, @NonNull ICreateTopicView createTopicView) {
        this.activity = activity;
        this.createTopicView = createTopicView;
    }

    @Override
    public void createTopicAsyncTask(@NonNull String tab, String title, String content) {
        createTopicView.onCreateTopicStart();


        RequestQueue queue= Volley.newRequestQueue(activity);

        StringRequest request=new StringRequest(
                Request.Method.POST,
                NetURL.ADDATOPIC_URL,
                s -> {
                    createTopicView.onCreateTopicOk();
                },
                volleyError ->{
                    createTopicView.onCreateTopicFinish();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put("title",title);
                map.put("content",content);
                map.put("author_id", Hello.id);
                map.put("tab",tab);
                return map;
            }
        };
        queue.add(request);

//        if (TextUtils.isEmpty(title) || title.length() < 10) {
//            createTopicView.onTitleError(activity.getString(R.string.title_empty_error_tip));
//        } else if (TextUtils.isEmpty(content)) {
//            createTopicView.onContentError(activity.getString(R.string.content_empty_error_tip));
//        } else {
//            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
//                content += "\n\n" + SettingShared.getTopicSignContent(activity);
//            }
//            createTopicView.onCreateTopicStart();
//            ApiClient.service.createTopic(LoginShared.getAccessToken(activity), tab, title, content).enqueue(new DefaultCallback<Result.CreateTopic>(activity) {
//
//                @Override
//                public boolean onResultOk(int code, Headers headers, Result.CreateTopic result) {
//                    createTopicView.onCreateTopicOk(result.getTopicId());
//                    return false;
//                }
//
//                @Override
//                public void onFinish() {
//                    createTopicView.onCreateTopicFinish();
//                }
//
//            });
//        }
    }

}
