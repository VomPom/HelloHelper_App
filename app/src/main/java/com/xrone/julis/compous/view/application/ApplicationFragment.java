package com.xrone.julis.compous.view.application;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.view.application.article.ArticleAcvitity;
import com.xrone.julis.compous.view.application.Express.ExpressActivity;
import com.xrone.julis.compous.view.application.about.AboutUsActivity;
import com.xrone.julis.compous.view.application.exchangeRate.CurrencyConverterActivity;
import com.xrone.julis.compous.view.application.map.MapActivity;
import com.xrone.julis.compous.view.application.translate.Translate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Julis on 17/6/11.
 */

public class ApplicationFragment extends Fragment{

    @BindView(R.id.btn_express)  ImageView btn_express;
    @BindView(R.id.btn_translate)  ImageView btn_translate;
    @BindView(R.id.btn_map)  ImageView btn_map;
    @BindView(R.id.about_us) ImageView btn_about_us;
    @BindView(R.id.btn_exchangerate) ImageView btn_exchangerate;
    @BindView(R.id.btn_atricle) ImageView btn_article;
    @BindView(R.id.btn_test_Commucation) ImageView btn_test_Commucation;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ac_application,container,false);
        ButterKnife.bind(
                ApplicationFragment.this,view);
        return view;
    }



    @OnClick({
            R.id.btn_express,
            R.id.btn_translate,
            R.id.btn_map,
            R.id.btn_exchangerate,
            R.id.btn_atricle,
            R.id.about_us,
            R.id.btn_test_Commucation
    })
    public void onClick(ImageView view){
        Intent intent= null;
        Context context=getActivity();
        switch (view.getId()){
            case R.id.btn_express:
                intent= new Intent(context,ExpressActivity.class);
                break;
            case R.id.btn_translate:
                intent= new Intent(context,Translate.class);
                intent.putExtra("text","home");
                break;
            case R.id.btn_map:
                intent= new Intent(context,MapActivity.class);
                break;
            case R.id.btn_exchangerate:
                intent= new Intent(context,CurrencyConverterActivity.class);
                break;
            case R.id.btn_atricle:
                intent= new Intent(context,ArticleAcvitity.class);
                break;
            case R.id.about_us:
                intent= new Intent(context,AboutUsActivity.class);
                break;
            case R.id.btn_test_Commucation:
               // intent= new Intent(context,CommunicationActivity.class);
                break;
        }
        startActivity(intent);
    }

}




