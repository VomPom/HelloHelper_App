package com.xrone.julis.compous.view.HomeFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.StringData.AppURL;
import com.xrone.julis.compous.Utils.HttpUtils;
import com.xrone.julis.compous.adpter.ShareAdapter;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.model.UserInformation;
import com.xrone.julis.compous.view.LoginAndRegister.LoginActivity;
import com.xrone.julis.compous.view.application.translate.Translate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julis on 17/6/11.
 * 首页页面
 */
public class HomeFragmentOLD extends Fragment implements AdapterView.OnItemClickListener {
    private ImageView voice;
    private EditText editText;
    private Button fanyi;
    private FloatingActionButton shareBtn;
    private ListView sharelistview;

    //Banner banner;
    private ShareAdapter shareAdapter;
    private List<UserInformation> informations = new ArrayList<>();

    public static List<?> images=new ArrayList<>();
    private String[] urls;
    private String[] tips ;

    @Override
    public void onStop() {
        super.onStop();
    }

    public Handler getShareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            informations.clear();
            try {
                //获取Json数组里面的值，并加入到Information对象里面去
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String info = object.getString("info");
                    String name = object.getString("name");
                    String head_url = object.getString("head_url");
                    String time = object.getString("time");
                    informations.add(new UserInformation(name, info, time, head_url));
                }
                shareAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("view","view创建");
        initViews();
        initDatas();
    }

    public void initDatas(){

        HttpUtils.getNewsJSON(AppURL.GET_NEWS_URL, getShareHandler);

      //  urls=getResources().getStringArray(R.array.url);
      //  tips=getResources().getStringArray(R.array.title);;
      //  List list = Arrays.asList(urls);
       // List list2 = Arrays.asList(tips);
        //images = new ArrayList(list);
//        banner.setImages(images)
//                .setBannerTitles(list2)
//                .setImageLoader(new GlideImageLoader())
//                .start();
    }

    public void initViews(){
        shareBtn = (FloatingActionButton) getActivity().findViewById(R.id.share_floatBtn);
        voice = (ImageView) getActivity().findViewById(R.id.home_iv_voice);
        editText = (EditText) getActivity().findViewById(R.id.home_ed_text);
        fanyi = (Button) getActivity().findViewById(R.id.home_btn_fanyi);
     //   banner = (Banner)getActivity().findViewById(R.id.banner);

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Translate.class);
                intent.putExtra("text", "order");
                startActivity(intent);
            }
        });


        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Hello.isLogin == true) {
                    Intent intent = new Intent(getActivity(), ShareInformation.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("order","share");
                    startActivity(intent);
                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode()==KeyEvent.KEYCODE_ENTER);
            }
        });


        editText.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                boolean flag=false;
                if(keyCode == KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_UP){
                    if (editText.getText().toString().equals("")) {
                        flag=true;
                        Toast.makeText(getActivity(), "请输入要翻译的内容！\n" +
                                "Please enter what you want to translate!", Toast.LENGTH_SHORT).show();


                    }else {
                        Intent intent = new Intent(getActivity(), Translate.class);
                        intent.putExtra("text", editText.getText().toString());
                        editText.setSelection(editText.getText().toString().length());
                        startActivity(intent);
                    }
                }
                return  flag;
            }
        });


        fanyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "请输入要翻译的内容！\n" +
                            "Please enter what you want to translate!", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), Translate.class);
                    intent.putExtra("text", editText.getText().toString());
                    editText.setSelection(editText.getText().toString().length());
                    startActivity(intent);
                }
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_activity, container, false);
        sharelistview = (ListView) view.findViewById(R.id.lv_home_share);
        shareAdapter = new ShareAdapter(this.getActivity(), informations);
        sharelistview.setAdapter(shareAdapter);
        sharelistview.setOnItemClickListener(this);

        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("tag","被电击了");
    }
}














