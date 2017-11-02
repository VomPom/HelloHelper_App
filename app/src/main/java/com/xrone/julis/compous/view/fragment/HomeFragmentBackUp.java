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

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.HttpUtils;
import com.xrone.julis.compous.adpter.ShareAdapter;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.model.StringURL;
import com.xrone.julis.compous.model.UserInformation;
import com.xrone.julis.compous.view.Home.ShareInformation;
import com.xrone.julis.compous.view.LoginAndRegister.LoginActivity;
import com.xrone.julis.compous.view.application.BDTranslator;

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
public class HomeFragmentBackUp extends Fragment implements AdapterView.OnItemClickListener {
    private ImageView voice;
    private EditText editText;
    private Button fanyi;
    private LinearLayout viewGroup;
    private View header;
    private int currentItem=1;
    private ViewPager guidePages;
    private FloatingActionButton shareBtn;
    private ListView sharelistview;
    private ScheduledExecutorService scheduledExecutorService;
    private ImageView[] imageViews;
    private static int[] HeaderimageViews={R.drawable.home_headerview_1,R.drawable.home_headerview_2,R.drawable.home_headerview_3};;
    private ArrayList<View> viewList;
    private ShareAdapter shareAdapter;
    private List<UserInformation> informations = new ArrayList<UserInformation>();

    @Override
    public void onStop() {
        System.out.println("停止了");
        scheduledExecutorService.shutdown();
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


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            guidePages.setCurrentItem(currentItem);
        }
    };

    public HomeFragmentBackUp() {
        viewList = new ArrayList<View>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shareBtn = (FloatingActionButton) getActivity().findViewById(R.id.share_floatBtn);
        voice = (ImageView) getActivity().findViewById(R.id.home_iv_voice);
        editText = (EditText) getActivity().findViewById(R.id.home_ed_text);
        fanyi = (Button) getActivity().findViewById(R.id.home_btn_fanyi);
        guidePages.setOnPageChangeListener(new NavigationPageChangeListener());
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
        header = LayoutInflater.from(getActivity()).inflate(R.layout.viewpage_header,
                null);
        guidePages = (ViewPager) header.findViewById(R.id.guidePages);
        viewGroup = (LinearLayout) header.findViewById(R.id.viewGroup);

        sharelistview.addHeaderView(header);

        System.out.println("url"+StringURL.GET_NEWS_URL);
        fillGuanggao();
        onStart();
        return view;
    }

    /**
     * 滚动图被点击
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Home 被点击");
      //  UserInformation information = informations.get(position);
    }


    public void fillGuanggao() {
        for (int i = 0; i < 3; i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setBackgroundResource(HeaderimageViews[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            viewList.add(iv);
        }

        guidePages.setAdapter(new MyViewPagerAdapter(viewList));
        imageViews = new ImageView[3];
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            imageView.setPadding(5, 0, 5, 0);
            imageViews[i] = imageView;
            if (i == 0)
                imageViews[i].setImageDrawable(getResources().getDrawable(
                        R.drawable.page_focused));
            else
                imageViews[i].setImageDrawable(getResources().getDrawable(
                        R.drawable.page_unfocused));

            viewGroup.addView(imageViews[i]);
        }
    }
    // ==============
    class NavigationPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
           // System.out.println("图片在轮播");
            currentItem = arg0;
            for (int i = 0; i < HeaderimageViews.length; i++) {
                if (arg0 == i) {
                    imageViews[i].setImageResource(R.drawable.page_focused);
                } else {
                    imageViews[i].setImageResource(R.drawable.page_unfocused);
                }
            }
        }

    }

    /**
     *
     */

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup parent = (ViewGroup)mListViews.get(position).getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    public void onStart() {
        // TODO Auto-generated method stub
        // 用一个定时器 来完成图片切换
        // Timer 与 ScheduledExecutorService 实现定时器的效果
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 通过定时器 来完成 每2秒钟切换一个图片
        // 经过指定的时间后，执行所指定的任务
        // scheduleAtFixedRate(command, initialDelay, period, unit)
        // command 所要执行的任务
        // initialDelay 第一次启动时 延迟启动时间
        // period 每间隔多次时间来重新启动任务
        // unit 时间单位
        scheduledExecutorService.scheduleAtFixedRate(new ViewPagerTask(), 1, 5,
                TimeUnit.SECONDS);
        super.onStart();

    }

    // 用来完成图片切换的任务
    private class ViewPagerTask implements Runnable {

        public void run() {
            // 实现我们的操作
            // 改变当前页面
            currentItem = (currentItem + 1) % imageViews.length;
            // Handler来实现图片切换
            handler.obtainMessage().sendToTarget();
        }
    }

    @Override
    public void onDetach() {
        scheduledExecutorService.shutdown();
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        // 停止图片切换
        System.out.println("停止切换");
        scheduledExecutorService.shutdown();
        super.onDestroyView();
    }

}














