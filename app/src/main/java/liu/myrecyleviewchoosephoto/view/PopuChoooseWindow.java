package liu.myrecyleviewchoosephoto.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import liu.myrecyleviewchoosephoto.R;


/**
 * @Description: 描述
 * @AUTHOR 刘楠  Create By 2016/8/12 0012 17:32
 */
public class PopuChoooseWindow extends PopupWindow implements View.OnClickListener {


    Context mContext;

    View mContentView;

    /**
     * 相机
     */
    TextView mTvCamera;
    /**
     * 相册
     */
    TextView mTvAlbum;
    /**
     * 取消
     */
    TextView mTvCancel;

    public PopuChoooseWindow(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.popu_choose_pic, null);

        setContentView(mContentView);


        setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        //设置焦点
        setFocusable(true);

        setBackgroundDrawable(new ColorDrawable());


        setOutsideTouchable(true);

        setTouchable(true);

        setAnimationStyle(R.style.popuStyle);


        initView(mContentView);
        initListener();

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                return false;
            }
        });
    }

    private void initView(View contentView) {

        mTvCamera = (TextView) contentView.findViewById(R.id.tvCamera);
        mTvAlbum = (TextView) contentView.findViewById(R.id.tvAlbum);
        mTvCancel = (TextView) contentView.findViewById(R.id.tvCancel);
    }

    private void initListener() {
        mTvCamera.setOnClickListener(this);
        mTvAlbum.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);

    }


    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.tvCamera:
                //相机
                if(mOnFeedBackChooseListener!=null){
                    mOnFeedBackChooseListener.onCameraItemClick();
                }
                dismiss();
                break;
            case R.id.tvAlbum:
                //相册
                if(mOnFeedBackChooseListener!=null){
                    mOnFeedBackChooseListener.onAlbumItemClick();
                }
                dismiss();
                break;
            case R.id.tvCancel:
                dismiss();
                break;
        }
    }

    OnFeedBackChooseListener mOnFeedBackChooseListener;

    public void setOnFeedBackChooseListener(OnFeedBackChooseListener onFeedBackChooseListener){
        this.mOnFeedBackChooseListener = onFeedBackChooseListener;
    }

    public interface  OnFeedBackChooseListener{
        void onCameraItemClick();
        void onAlbumItemClick();
    }
}
