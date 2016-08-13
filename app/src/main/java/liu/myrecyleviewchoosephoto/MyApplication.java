package liu.myrecyleviewchoosephoto;

import android.app.Application;

import liu.myrecyleviewchoosephoto.util.UiUtils;

/**
 * Created by 刘楠 on 2016/8/13 0013.17:31
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UiUtils.init(getApplicationContext());
    }
}
