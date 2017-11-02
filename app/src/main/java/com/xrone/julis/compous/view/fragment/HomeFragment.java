package com.xrone.julis.compous.view.fragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.model.UserInformation;
import com.xrone.julis.compous.view.Home.ShareInformation;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.model.StringURL;
import com.xrone.julis.compous.adpter.ShareAdapter;
import com.xrone.julis.compous.view.application.BDTranslator;
import com.xrone.julis.compous.Utils.HttpUtils;
import com.xrone.julis.compous.view.LoginAndRegister.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * Created by Julis on 17/6/11.
 * 首页页面
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ImageView voice;
    private EditText editText;
    private Button fanyi;
    private FloatingActionButton shareBtn;
    private ListView sharelistview;

    private static int[] HeaderimageViews={R.drawable.home_headerview_1,R.drawable.home_headerview_2,R.drawable.home_headerview_3};;

    private ShareAdapter shareAdapter;
    private List<UserInformation> informations = new ArrayList<>();

    @Override
    public void onStop() {
        System.out.println("停止了");
        super.onStop();
    }

    public Handler getShareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
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
        shareBtn = (FloatingActionButton) getActivity().findViewById(R.id.share_floatBtn);
        voice = (ImageView) getActivity().findViewById(R.id.home_iv_voice);
        editText = (EditText) getActivity().findViewById(R.id.home_ed_text);
        fanyi = (Button) getActivity().findViewById(R.id.home_btn_fanyi);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BDTranslator.class);
                intent.putExtra("text", "order");
                startActivity(intent);

            }
        });

        fanyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "请输入要翻译的内容！\n" +
                            "Please enter what you want to translate!", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), BDTranslator.class);
                    intent.putExtra("text", editText.getText().toString());
                    startActivity(intent);
                }
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_list_share, container, false);
        sharelistview = (ListView) view.findViewById(R.id.lv_home_share);
        shareAdapter = new ShareAdapter(this.getActivity(), informations);
        sharelistview.setAdapter(shareAdapter);
        sharelistview.setOnItemClickListener(this);
        /**
         * 获取消息
         */
        HttpUtils.getNewsJSON(StringURL.GET_NEWS_URL, getShareHandler);
        onStart();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(position+"Home 被点击");
    }


}














