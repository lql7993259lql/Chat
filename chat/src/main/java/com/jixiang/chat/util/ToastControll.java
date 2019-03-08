package com.jixiang.chat.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wangda on 2017/6/10.
 */

public class ToastControll {
    public static Toast toast;
    public static void showToast(String text, Context context){
        try{
            if(toast == null){
                toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
            }else{
                toast.setText(text);
                toast.setDuration(Toast.LENGTH_SHORT);
            }
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void cancelToast(){
        try{
            if(toast != null){
                toast.cancel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
