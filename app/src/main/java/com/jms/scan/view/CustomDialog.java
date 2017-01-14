package com.jms.scan.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jms.scan.R;

/**
 * Created by alpha on 2017/1/14.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String name;
        private String num;
        private String positiveButtonText;
        private String negativeButtonText;
        private String centerButtonText;
        private View layout;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private DialogInterface.OnClickListener centerButtonClickListener;

        public Builder(Context context) {
            this.context=context;
        }

        public Builder setName(String name) {
            this.name=name;
            return this;
        }

        public Builder setNum(String num){
            this.num = num;
            return this;
        }

        public String getNum(){
            return  ((EditText)layout.findViewById(R.id.et_snum)).getText().toString();
        }


        public Builder setTitle(String title) {
            this.title=title;
            return this;
        }



        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText=positiveButtonText;
            this.positiveButtonClickListener=listener;
            return this;
        }


        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText=negativeButtonText;
            this.negativeButtonClickListener=listener;
            return this;
        }


        public Builder setCenterButton(String centerButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.centerButtonText=centerButtonText;
            this.centerButtonClickListener=listener;
            return this;
        }


        public CustomDialog create() {
            LayoutInflater inflater=(LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog=new CustomDialog(context, R.style.Dialog);
            layout=inflater.inflate(R.layout.dialog_normal_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }

            if (centerButtonText != null) {
                ((Button) layout.findViewById(R.id.centerButton))
                        .setText(centerButtonText);
                if (centerButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.centerButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    centerButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEUTRAL);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.centerButton).setVisibility(
                        View.GONE);
            }

            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {

                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }

            if (name != null) {
                ((TextView) layout.findViewById(R.id.tv_sname)).setText(name);
            }

            if(num != null){
                ((EditText)layout.findViewById(R.id.et_snum)).setText(num);
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }




}
