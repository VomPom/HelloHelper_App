package com.xrone.julis.compous.view.HomeFragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.Utils;
import com.xrone.julis.compous.view.application.Feedback_Activity;
import com.xrone.julis.compous.view.person.PersonInfoView;
import com.xrone.julis.compous.view.LoginAndRegister.LoginActivity;

import java.util.Map;

import static com.xrone.julis.compous.model.StringURL.DEFAULT_HEAD_URL;

/**
 * Created by Julis on 17/6/10.
 */

public class PersonFragment extends Fragment implements View.OnClickListener {
    private TextView tvName;
    private RelativeLayout view_user=null;
    private RelativeLayout quit=null;
    private RelativeLayout feedback=null;
    private ImageView sex;
    private TextView idView;
    private SmartImageView headImage;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=null;
        view = inflater.inflate(R.layout.ac_personal,container,false);
        initeView(view);

        return view;
    }
    private void initeView(View view) {
        headImage=(SmartImageView)view.findViewById(R.id.person_head);
        idView=(TextView)view.findViewById(R.id.person_id);
        tvName = (TextView)view.findViewById(R.id.person_name);
        sex = (ImageView) view.findViewById(R.id.person_sex);
        view_user = (RelativeLayout) view.findViewById(R.id.person_information);
        quit=(RelativeLayout)view.findViewById(R.id.person_quit);
        quit.setOnClickListener(this);
        feedback=(RelativeLayout)view.findViewById(R.id.person_feedback);
        feedback.setOnClickListener(this);
        view_user.setOnClickListener(this);
        Map<String, String> userInfo = Utils.getUserInfo(getActivity().getBaseContext());
        String head_URL=userInfo.get("head_url");
        String id=userInfo.get("id");
        String sexT =userInfo.get("sex");
        System.out.println("head_URL"+head_URL);
        if (userInfo != null&&userInfo.size()!=0) {
            if(head_URL!=null){
            headImage.setImageUrl(head_URL,R.drawable.person_default_head,R.drawable.person_default_head);}
            else
                headImage.setImageUrl(DEFAULT_HEAD_URL,R.drawable.person_default_head,R.drawable.person_default_head);
            idView.setText("ID:"+id);
            tvName.setText(Hello.username);
            System.out.println("sex"+Hello.sex);
            if (Hello.sex.equals("1")) {
                sex.setImageDrawable(getResources().getDrawable(R.drawable.icon_boy));
            } else {
                sex.setImageDrawable(getResources().getDrawable(R.drawable.icon_girl));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_information:
                if(tvName.getText().equals("null")){
                    Intent intent =new Intent(getActivity(),LoginActivity.class);
                    intent.putExtra("order","out");
                    startActivity(intent);
                }
                else{
                     Intent intent =new Intent(getActivity(),PersonInfoView.class);
                      startActivity(intent);
                }
                break;

            case R.id.person_quit:
                SharedPreferences sp=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.clear();
                editor.commit();
                Hello.isLogin=false;
                Toast.makeText(getActivity(),"退出登录成功！\n" +
                        "Exit login successful!",Toast.LENGTH_SHORT).show();
                headImage.setImageUrl(DEFAULT_HEAD_URL,R.drawable.person_default_head,R.drawable.person_default_head);
                idView.setText("ID:000000");
                tvName.setText("null");
                break;
            case R.id.person_feedback:
                Intent intent =new Intent(getActivity(),Feedback_Activity.class);
                startActivity(intent);
                break;
        }

    }
}






