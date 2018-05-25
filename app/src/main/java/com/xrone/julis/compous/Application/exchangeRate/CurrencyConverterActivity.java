package com.xrone.julis.compous.Application.exchangeRate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Application.exchangeRate.Data.Currency_id_name;
import com.xrone.julis.compous.Application.exchangeRate.Data.Global_Data;
import com.xrone.julis.compous.Application.exchangeRate.HttpService.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;



public class CurrencyConverterActivity extends Activity {


    RelativeLayout from_layout, to_layout               ;
    ImageView      from_image, to_image,btn_exchange_country ;
    EditText       from_edittext                        ;
    TextView       result_textview                      ;
    TextView       from_country_name, to_country_name   ;
                             ;
    Button         button_convert                       ;
    Boolean        flag_check_first_item=false          ;
    Boolean        flag_check_second_item=false         ;
    public static  String url_Result = null             ;
    public         JSONObject jsonObj_result=null       ;
    String         final_Result = null                  ;
    String         temp = null                          ;
    String         from_amount = null                   ;
    String         first_country_short                  ;
    String         second_country_short                 ;

    private        ProgressDialog pDialog               ;
	;
    public static ArrayList<Currency_id_name> currences_names  ;


    public static String sDefSystemLanguage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_fragment_currency_converter);
        from_layout         =   (RelativeLayout)findViewById(R.id.from_field)         ;
        to_layout           =   (RelativeLayout)findViewById(R.id.to_field)           ;
        from_image          =   (ImageView)findViewById(R.id.first_country_image)     ;
        to_image            =   (ImageView)findViewById(R.id.second_country_flag)     ;
        from_edittext       =   (EditText)findViewById(R.id.first_country_edittext)   ;

        from_country_name   =   (TextView)findViewById(R.id.first_country_name)       ;
        to_country_name     =   (TextView)findViewById(R.id.second_country_name)      ;
        result_textview     =   (TextView)findViewById(R.id.text_result)              ;
        button_convert      =   (Button)findViewById(R.id.button_convert)             ;
        btn_exchange_country=(ImageView)findViewById(R.id.btn_exchange_country);


        getAPIVerison();
        sDefSystemLanguage = Locale.getDefault().getLanguage();
        currences_names= new ArrayList<>();

        pDialog = new ProgressDialog(CurrencyConverterActivity.this);

        first_country_short = "CNY";
        second_country_short= "USD";



        from_layout.setOnClickListener(view -> {
            flag_check_first_item=true;
            flag_check_second_item=false;
            Intent intent = new Intent(getBaseContext(), Activity_conversion_listview.class);
            startActivity(intent);
        });


        to_layout.setOnClickListener(view -> {

            flag_check_second_item=true;
            flag_check_first_item=false;
            Intent intent = new Intent(getBaseContext(), Activity_conversion_listview.class);
            startActivity(intent);

        });


        button_convert.setOnClickListener(view -> excuteQuery());

        btn_exchange_country.setOnClickListener(v -> swapCountryData());
    }

    /**
     * 执行交换查询数据
     */
    public void swapCountryData(){
        String temp=first_country_short;
        first_country_short=second_country_short;
        second_country_short=temp;

        Drawable tempDrawable=from_image.getDrawable();
        from_image.setImageDrawable(to_image.getDrawable());
        to_image.setImageDrawable(tempDrawable);


        String tempName=from_country_name.getText().toString();
        from_country_name.setText(to_country_name.getText().toString());
        to_country_name.setText(tempName);

        excuteQuery();
    }


    /**
     * 查询汇率
     */
    public void excuteQuery(){

            if(from_edittext.getText().toString().equals("")){

                Snackbar.make(button_convert, getString(R.string.app_expreess_tip_noInput), Snackbar.LENGTH_SHORT)
                        .show();
            }else {
                from_amount = from_edittext.getText().toString();
                String get_cc_link = getResources().getString(R.string.Free_CC_link);
                url_Result = get_cc_link + first_country_short + "_" + second_country_short;
                Log.e("urlResult", url_Result);
                if (isNetworkAvailable()) {
                    new GetExchangeRates1().execute();
                } else {
                    MyAlert.showErroNetworking(CurrencyConverterActivity.this);
                }
            }
    }


    /**
     * 获取安卓的版本
     * @return
     */

    public static float getAPIVerison() {
        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "error retriving api version" + e.getMessage());
        }

        Global_Data.android_version=f.floatValue();

        return f.floatValue();
    }

    /**
     * 检测网络是否可用
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * 子线程执行查询操作
     */
    private class GetExchangeRates1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          pDialog.setMessage("Please wait...");
          pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String json_Result = sh.makeServiceCall(url_Result, ServiceHandler.GET);
            try {
                jsonObj_result = new JSONObject(json_Result);
                final_Result  = jsonObj_result.getJSONObject("results").toString();
            }catch (JSONException e) {

                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            add_country_Result();


        }

    }


    /**
     * 处理子线程返回的数据结果
     */

    public void add_country_Result()  {
        final_Result=final_Result.replace("{","");
        final_Result=final_Result.replace("}","");
        final_Result=final_Result.replace("\"","");

        StringTokenizer stok= new StringTokenizer(final_Result,",");

        while(stok.hasMoreElements()) {
            temp= stok.nextElement().toString();
            if(temp.indexOf("val") != -1){
                String split[]= temp.split(":");
                NumberFormat f = NumberFormat.getInstance(Locale.US);

                double temp_Amount2 = Double.parseDouble(split[2]);
               DecimalFormat df = new DecimalFormat("#.#########");
                temp_Amount2 = Double.valueOf(f.format(temp_Amount2));

                double temp_Amount1 = Double.parseDouble(from_amount);
                DecimalFormat df1 = new DecimalFormat("#.#########",new DecimalFormatSymbols(Locale.US));
                temp_Amount1 = Double.valueOf(df1.format(temp_Amount1));

                double result = temp_Amount1 * temp_Amount2;
                DecimalFormat df2 = new DecimalFormat("#.#########");
                result = Double.valueOf(df1.format(result));



                f.setGroupingUsed(false);
                String refinedNumber = f.format(result);

                result_textview.setText(refinedNumber);


            }
            temp= stok.nextElement().toString();


            if(temp.indexOf("val") != -1){
                String split[]= temp.split(":");

                NumberFormat f = NumberFormat.getInstance(Locale.US);

                double temp_Amount2 = Double.parseDouble(split[1]);
              DecimalFormat df = new DecimalFormat("#.#########");
                temp_Amount2 = Double.valueOf(f.format(temp_Amount2));

                double temp_Amount1 = Double.parseDouble(from_amount);
               DecimalFormat df1 = new DecimalFormat("#.#########",new DecimalFormatSymbols(Locale.US));
                temp_Amount1 = Double.valueOf(df1.format(temp_Amount1));

                double result = temp_Amount1 * temp_Amount2;
                DecimalFormat df2 = new DecimalFormat("#.#########");
                result = Double.valueOf(df1.format(result));



                f.setGroupingUsed(false);
                String refinedNumber = f.format(result);

                result_textview.setText(refinedNumber);
            }

            temp= stok.nextElement().toString();


            if(temp.indexOf("val") != -1){

                String split[]= temp.split(":");

                NumberFormat f = NumberFormat.getInstance(Locale.US);

                double temp_Amount2 = Double.parseDouble(split[1]);
              DecimalFormat df = new DecimalFormat("#.#########");
                temp_Amount2 = Double.valueOf(f.format(temp_Amount2));

                double temp_Amount1 = Double.parseDouble(from_amount);
                DecimalFormat df1 = new DecimalFormat("#.#########",new DecimalFormatSymbols(Locale.US));
                temp_Amount1 = Double.valueOf(df1.format(temp_Amount1));

                double result = temp_Amount1 * temp_Amount2;
               DecimalFormat df2 = new DecimalFormat("#.#########");
                result = Double.valueOf(df1.format(result));



                f.setGroupingUsed(false);
                String refinedNumber = f.format(result);

                result_textview.setText(refinedNumber);

            }

            temp= stok.nextElement().toString();


            if(temp.indexOf("val") != -1){
                String split[]= temp.split(":");

                NumberFormat f = NumberFormat.getInstance(Locale.US);

                double temp_Amount2 = Double.parseDouble(split[1]);

               DecimalFormat df = new DecimalFormat("#.#########");
                temp_Amount2 = Double.valueOf(f.format(temp_Amount2));

                double temp_Amount1 = Double.parseDouble(from_amount);
                DecimalFormat df1 = new DecimalFormat("#.#########",new DecimalFormatSymbols(Locale.US));
                temp_Amount1 = Double.valueOf(df1.format(temp_Amount1));

                double result = temp_Amount1 * temp_Amount2;
                DecimalFormat df2 = new DecimalFormat("#.#########");
                result = Double.valueOf(df1.format(result));

             //   String Result = String.valueOf(result);



                f.setGroupingUsed(false);
                String refinedNumber = f.format(result);

                result_textview.setText(refinedNumber);

            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if(flag_check_first_item) {
            if (Global_Data.global_image_id!=0 && !Global_Data.global_country_name.isEmpty()) {
                from_image.setImageResource(Global_Data.global_image_id);
                from_country_name.setText(Global_Data.global_country_name);
                first_country_short=Global_Data.country_id;
                Log.d("Key",first_country_short);
            }
            else {

            }
        }

        if(flag_check_second_item) {
            if (Global_Data.global_image_id!=0 && !Global_Data.global_country_name.isEmpty()) {
                to_image.setImageResource(Global_Data.global_image_id);
                to_country_name.setText(Global_Data.global_country_name);
                second_country_short=Global_Data.country_id;

            }



        }

    }
}
