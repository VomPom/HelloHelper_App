package com.xrone.julis.compous.HomeFragment.communication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;


import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Service.SettingShared;
import com.xrone.julis.compous.HomeFragment.communication.base.StatusBarActivity;
import com.xrone.julis.compous.HomeFragment.communication.listener.NavigationFinishClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends StatusBarActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.switch_setting_language)
    SwitchCompat switchLanguage;

//    @BindView(R.id.switch_theme_dark)
//    SwitchCompat switchThemeDark;
//
//    @BindView(R.id.switch_topic_draft)
//    SwitchCompat switchTopicDraft;
//
//    @BindView(R.id.switch_topic_sign)
//    SwitchCompat switchTopicSign;
//
//    @BindView(R.id.btn_modify_topic_sign)
//    TextView btnModifyTopicSign;
//
//    @BindView(R.id.switch_topic_render_compat)
//    SwitchCompat switchTopicRenderCompat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       // ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
//
        switchLanguage.setChecked( SettingShared.changeLanguage(this));
//        switchThemeDark.setChecked(SettingShared.isEnableThemeDark(this));
//        switchTopicDraft.setChecked(SettingShared.isEnableTopicDraft(this));
//        switchTopicSign.setChecked(SettingShared.isEnableTopicSign(this));
//        btnModifyTopicSign.setEnabled(SettingShared.isEnableTopicSign(this));
//        switchTopicRenderCompat.setChecked(SettingShared.isEnableTopicRenderCompat(this));
    }
//
    @OnClick(R.id.btn_setting_language)
    void onBtnNotificationClick() {
        switchLanguage.toggle();
        SettingShared.changeLanguage(this);

    }

//    @OnClick(R.id.btn_theme_dark)
//    void onBtnThemeDarkClick() {
//        switchThemeDark.toggle();
//        SettingShared.setEnableThemeDark(this, switchThemeDark.isChecked());
//        ThemeUtils.notifyThemeApply(this);
//    }
//
//    @OnClick(R.id.btn_topic_draft)
//    void onBtnTopicDraftClick() {
//        switchTopicDraft.toggle();
//        SettingShared.setEnableTopicDraft(this, switchTopicDraft.isChecked());
//    }
//
//    @OnClick(R.id.btn_topic_sign)
//    void onBtnTopicSignClick() {
//        switchTopicSign.toggle();
//        SettingShared.setEnableTopicSign(this, switchTopicSign.isChecked());
//        btnModifyTopicSign.setEnabled(switchTopicSign.isChecked());
//    }
//
//    @OnClick(R.id.btn_modify_topic_sign)
//    void onBtnModifyTopicSignClick() {
//        startActivity(new Intent(this, ModifyTopicSignActivity.class));
//    }
//
//    @OnClick(R.id.btn_topic_render_compat)
//    void onBtnTopicRenderCompatClick() {
//        switchTopicRenderCompat.toggle();
//        SettingShared.setEnableTopicRenderCompat(this, switchTopicRenderCompat.isChecked());
//    }

}
