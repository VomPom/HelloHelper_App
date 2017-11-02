package com.xrone.julis.compous.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.view.application.BDTranslator;
import com.xrone.julis.compous.view.application.Express.ExpressActivity;
import com.xrone.julis.compous.view.application.Feedback_Activity;
import com.xrone.julis.compous.view.application.about.AboutUsActivity;
import com.xrone.julis.compous.view.application.exchangeRate.CurrencyConverterActivity;
import com.xrone.julis.compous.view.application.map.MapActivity;

/**
 * Created by Julis on 17/6/11.
 */

public class ApplicationFragment extends Fragment{
    private ImageView btn_express;
    private ImageView btn_translate;
    private ImageView btn_map;
    private ImageView btn_about_us;
    private ImageView btn_exchangerate;
    private ImageView btn_article;
    private ImageView btn_feedback;
   // private ListView  applicationListview;


//      private ApplicationAdapter applicationAdapter;
//      private List<ApplicationInformation> informationList = new ArrayList<ApplicationInformation>();
//       public Handler getAPPlicationInformationHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            String jsonData = (String) msg.obj;
//            try {
//                //获取Json数组里面的值，并加入到Information对象里面去
//                JSONArray jsonArray = new JSONArray(jsonData);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String title = object.getString("title");
//                    String from = object.getString("from");
//                    String pic_url = object.getString("pic_url");
//                    String content_url = object.getString("content_url");
//                    informationList.add(new ApplicationInformation(title,from,pic_url,content_url));
//                }
//                applicationAdapter.notifyDataSetChanged();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ac_application,container,false);
        initView(view);
        return view;
    }
//    public void initData(){
//        HttpUtils.getNewsJSON(StringURL.APPINFORMATION_URL, getAPPlicationInformationHandler);
//
//    }
    private void initView(View view){
//        applicationListview=(ListView)view.findViewById(R.id.application_listview);
//        applicationAdapter = new ApplicationAdapter(this.getActivity(), informationList);
//        applicationListview.setAdapter(applicationAdapter);
//        applicationListview.setOnItemClickListener(this);
        btn_express=(ImageView)view.findViewById(R.id.btn_express);
        btn_translate=(ImageView)view.findViewById(R.id.btn_translate);
        btn_map=(ImageView)view.findViewById(R.id.btn_map);
        btn_about_us=(ImageView)view.findViewById(R.id.about_us);
        btn_exchangerate=(ImageView)view.findViewById(R.id.btn_exchangerate);
        btn_article=(ImageView)view.findViewById(R.id.btn_atricle);
        btn_feedback=(ImageView)view.findViewById(R.id.btn_feedback);
        btn_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(),ExpressActivity.class);
                startActivity(i);
            }});
        btn_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), BDTranslator.class);
                i.putExtra("text","translate");
                startActivity(i);
            }});
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(),MapActivity.class);
                startActivity(i);
            }});
        btn_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(),AboutUsActivity.class);
                startActivity(i);
            }});
        btn_exchangerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(),CurrencyConverterActivity.class);
                startActivity(i);
            }
        });
        btn_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlert.notFinished(getActivity());
            }
        });
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(),Feedback_Activity.class);
                startActivity(i);
            }
        });


    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ApplicationInformation information = informationList.get(position);
//        String CLICK_URL=information.getContent_url();
//        Intent intent = new Intent(getActivity(), BrowerApplicationActivity.class);
//        intent.putExtra("url",CLICK_URL);
//        startActivity(intent);
//    }
}




