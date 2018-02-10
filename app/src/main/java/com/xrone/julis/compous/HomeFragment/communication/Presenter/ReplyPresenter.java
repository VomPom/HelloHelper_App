package com.xrone.julis.compous.HomeFragment.communication.Presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.xrone.julis.compous.HomeFragment.communication.Model.Reply;
import com.xrone.julis.compous.HomeFragment.communication.view.IReplyView;


public class ReplyPresenter implements IReplyPresenter {

    private final Activity activity;
    private final IReplyView replyView;

    public ReplyPresenter(@NonNull Activity activity, @NonNull IReplyView replyView) {
        this.activity = activity;
        this.replyView = replyView;
    }

    @Override
    public void upReplyAsyncTask(@NonNull final Reply reply) {
//        ApiClient.service.upReply(reply.getId(), LoginShared.getAccessToken(activity)).enqueue(new DefaultCallback<Result.UpReply>(activity) {
//
//            @Override
//            public boolean onResultOk(int code, Headers headers, Result.UpReply result) {
//                if (result.getAction() == Reply.UpAction.up) {
//                    reply.getUpList().add(LoginShared.getId(getActivity()));
//                } else if (result.getAction() == Reply.UpAction.down) {
//                    reply.getUpList().remove(LoginShared.getId(getActivity()));
//                }
//                replyView.onUpReplyOk(reply);
//                return false;
//            }
//
//        });
    }

}
