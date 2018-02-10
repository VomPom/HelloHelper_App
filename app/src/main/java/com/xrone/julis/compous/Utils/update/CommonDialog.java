package com.xrone.julis.compous.Utils.update;

import android.app.Dialog;
import android.content.Context;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xrone.julis.compous.R;

/**
 * Created by Julis on 2017/10/11.
 */

public class CommonDialog extends Dialog {


    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }



    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonContent;
        private String negativeButtonContent;
        private OnClickListener positiveButtonListener;
        private OnClickListener negativeButtonListener;
        private View contentView;

        /**
         * 建造器的构造方法：
         * @param context
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 利用字符串设置title
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 利用资源id设置title
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(int textId, OnClickListener listener) {
            this.positiveButtonContent = (String) context.getText(textId);
            this.positiveButtonListener = listener;
            return this;
        }
        public Builder setNegativeButton(int textId, OnClickListener listener) {
            this.negativeButtonContent = (String) context.getText(textId);
            this.negativeButtonListener = listener;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public CommonDialog create() {
            /**
             * 利用我们刚才自定义的样式初始化Dialog
             */
            final CommonDialog dialog = new CommonDialog(context,
                    R.style.Dialog);
            /**
             * 下面就初始化Dialog的布局页面
             */
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogLayoutView = inflater.inflate(R.layout.lib_update_app_dialog, null);
            dialog.addContentView(dialogLayoutView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


            ((TextView) dialogLayoutView.findViewById(R.id.tv_dialog_title)).setText(title);
            ((TextView) dialogLayoutView.findViewById(R.id.tv_update_info)).setText(message);


            ((Button) dialog.findViewById(R.id.tv_dialog_pos)).setOnClickListener(v ->
                    positiveButtonListener.onClick(dialog, -1));

            ((ImageView) dialogLayoutView
                            .findViewById(R.id.tv_dialog_neg))
                            .setOnClickListener(v -> negativeButtonListener.onClick(dialog, -2));

            /**
             * 将初始化完整的布局添加到dialog中
             */
            dialog.setContentView(dialogLayoutView);
            /**
             * 禁止点击Dialog以外的区域时Dialog消失
             */
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }
}
