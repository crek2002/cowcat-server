package com.example.lyyserverdemo.lyyframework.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.lyyserverdemo.R;

/**
 * @author Yingyong Lao
 * 创建时间 2022/8/14 18:11
 * @version 1.0
 */
public class LyyDialog {
    private Dialog mDialog;
    private View mdialogView;
    private Context mContext;
    private TextView titleTv;
    private TextView contentTv;
    private TextView cancelTv;
    private TextView confirmTv;
    private OnClickListener mOnClickListener;

    public LyyDialog(Context context){
        this.mContext=context;
        mDialog=new Dialog(context,R.style.LYY_DIALOG);
        mDialog.setCancelable(false);
        mdialogView= LayoutInflater.from(context).inflate(R.layout.lyy_dialog_layout,null);
        mDialog.setContentView(mdialogView);
        titleTv=mdialogView.findViewById(R.id.titleTv);
        contentTv=mdialogView.findViewById(R.id.contentTv);
        cancelTv=mdialogView.findViewById(R.id.cancelTv);
        confirmTv=mdialogView.findViewById(R.id.confirmTv);
        initEvents();
    }

    private void initEvents() {
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener!=null){
                    mOnClickListener.onCancel(mDialog);
                }else {
                    if (mDialog!=null){
                        mDialog.dismiss();
                    }
                }
            }
        });
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener!=null){
                    mOnClickListener.onConfirm(mDialog);
                }
            }
        });
    }

    public void setTitle(CharSequence title){
        if (titleTv!=null&&title!=null){
            titleTv.setText(title);
        }
    }

    public void setContent(CharSequence content){
        if (contentTv!=null&&content!=null){
            contentTv.setText(content);
        }
    }

    public void setNegativeText(CharSequence negativeText){
        if (cancelTv!=null&&negativeText!=null){
            cancelTv.setText(negativeText);
        }
    }

    public void setPositiveText(CharSequence positiveText){
        if (confirmTv!=null&&positiveText!=null){
            confirmTv.setText(positiveText);
        }
    }

   public void show(){
        if (mDialog!=null&&!mDialog.isShowing()){
            mDialog.show();
        }
   }

   public void dismiss(){
        if (mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
        }
   }

   public void setOnClickListener(OnClickListener listener){
        mOnClickListener=listener;
   }

   public static abstract class OnClickListener{
        public abstract void onCancel(Dialog lyyDialog);
        public abstract void onConfirm(Dialog lyyDialog);
   }
}
