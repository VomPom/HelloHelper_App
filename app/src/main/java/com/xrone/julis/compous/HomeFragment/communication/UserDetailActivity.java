package com.xrone.julis.compous.HomeFragment.communication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.HomeFragment.communication.Model.User;
import com.xrone.julis.compous.HomeFragment.communication.Presenter.UserDetailPresenter;
import com.xrone.julis.compous.HomeFragment.communication.adapter.UserDetailPagerAdapter;
import com.xrone.julis.compous.HomeFragment.communication.base.StatusBarActivity;
import com.xrone.julis.compous.HomeFragment.communication.listener.NavigationFinishClickListener;
import com.xrone.julis.compous.HomeFragment.communication.util.ToastUtils;
import com.xrone.julis.compous.HomeFragment.communication.view.IUserDetailPresenter;
import com.xrone.julis.compous.HomeFragment.communication.view.IUserDetailView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetailActivity extends StatusBarActivity implements IUserDetailView {

    private static final String EXTRA_LOGIN_NAME = "loginName";
    private static final String EXTRA_AVATAR_URL = "avatarUrl";
    private static final String NAME_IMG_AVATAR = "imgAvatar";
    private static final String TAG = "UserDetailActivity";

    public static void startWithTransitionAnimation(@NonNull Activity activity, String loginName, @NonNull ImageView imgAvatar, String avatarUrl) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra(EXTRA_LOGIN_NAME, loginName);
        intent.putExtra(EXTRA_AVATAR_URL, avatarUrl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imgAvatar, NAME_IMG_AVATAR);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void start(@NonNull Activity activity, String loginName) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra(EXTRA_LOGIN_NAME, loginName);
        activity.startActivity(intent);
    }

    public static void start(@NonNull Context context, String loginName) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_LOGIN_NAME, loginName);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.tv_login_name)
    TextView tvLoginName;

    @BindView(R.id.tv_github_username)
    TextView tvGithubUsername;

    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;

    @BindView(R.id.tv_score)
    TextView tvScore;

//    @BindView(R.id.progress_wheel)
//    ProgressWheel progressWheel;

    private UserDetailPagerAdapter adapter;

    private IUserDetailPresenter userDetailPresenter;

    private String loginName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       // ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_user_detail);
        ButterKnife.bind(this);

        ViewCompat.setTransitionName(imgAvatar, NAME_IMG_AVATAR);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));


        adapter = new UserDetailPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(viewPager);

        loginName = getIntent().getStringExtra(EXTRA_LOGIN_NAME);
        tvLoginName.setText(loginName);

        String avatarUrl = getIntent().getStringExtra(EXTRA_AVATAR_URL);
        Log.e(TAG,avatarUrl);
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(this).
                    load(avatarUrl)
                    .into(imgAvatar);
        }

        userDetailPresenter = new UserDetailPresenter(this, this);
        userDetailPresenter.getUserAsyncTask(loginName);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {}

    @OnClick(R.id.img_avatar)
    void onBtnAvatarClick() {

        userDetailPresenter.getUserAsyncTask(loginName);
    }

    @OnClick(R.id.tv_github_username)
    void onBtnGithubUsernameClick() {

    }

    /**
     * 个人信息栏
     * @param user
     */
    @Override
    public void onGetUserOk(@NonNull User user) {
        //Glide.with(this).load(user.getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);

//         tvCreateTime.setText(getString(R.string.register_time__, user.getCreateAt().toString("yyyy-MM-dd")));
        //tvScore.setText(getString(R.string.score__, 20));
        adapter.update(user);

    }



    @Override
    public void onGetUserError(@NonNull String message) {
        ToastUtils.with(this).show(message);
    }

    @Override
    public void onGetUserStart() {
       // progressWheel.spin();
    }

    @Override
    public void onGetUserFinish() {
        //progressWheel.stopSpinning();
    }

}