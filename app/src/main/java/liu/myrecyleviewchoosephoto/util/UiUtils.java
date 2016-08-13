package liu.myrecyleviewchoosephoto.util;

import android.content.Context;
import android.os.Handler;

/**
 * Created by 刘楠 on 2016/6/15 0015 15:39.
 */
public class UiUtils {

    private static Handler mHandler;
    private static Context mContext;
    public static void init(Context applicationContext) {
        mHandler = new Handler();
        mContext=applicationContext;
    }

    /**
     * 提交任务
     * @param task 线程
     */
    public static void post(Runnable task){
        mHandler.post(task);
    }

    /**
     * 延时提交任务
     * @param task 线程
     * @param delay 延时时间，毫秒
     */
    public static void post(Runnable task,long delay){

        mHandler.postDelayed(task,delay);

    }

    public static Context getContext(){
        return mContext;
    }

    /**
     * 移除任务
     * @param task 线程
     *
     */
    public static void remove(Runnable task){

        mHandler.removeCallbacks(task);
        task=null;

    }




}
