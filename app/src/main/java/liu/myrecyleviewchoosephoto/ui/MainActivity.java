package liu.myrecyleviewchoosephoto.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import liu.myrecyleviewchoosephoto.R;
import liu.myrecyleviewchoosephoto.adapter.PhotoWallAdapter;
import liu.myrecyleviewchoosephoto.util.OtherUtils;
import liu.myrecyleviewchoosephoto.view.PopuChoooseWindow;

public class MainActivity extends AppCompatActivity implements PopuChoooseWindow.OnFeedBackChooseListener, PhotoWallAdapter.OnItemClickListener {

    private static final int    REQUEST_CAMERA   = 1;
    private static final String TAG              = "vivi";
    private static final int    REQUEST_ALBUM_OK = 2;
    @Bind(R.id.recyView)
    RecyclerView mRecyView;
    @Bind(R.id.btnGoChoose)
    Button       mBtnGoChoose;
    @Bind(R.id.ivDispaly)
    ImageView    mIvDispaly;
    private File mTmpFile;
    private ArrayList<String> mDatas = new ArrayList<>();
    /**
     * 设置最大数量
     */
    private int mMaxNum=6;
    private PhotoWallAdapter mPhotoWallAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecycleView();
    }

    private void initRecycleView() {


        mPhotoWallAdapter = new PhotoWallAdapter(MainActivity.this,mDatas,mMaxNum);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,4);

        mRecyView.setLayoutManager(gridLayoutManager);
        mRecyView.setAdapter(mPhotoWallAdapter);
        mPhotoWallAdapter.setOnItemClickListener(this);

    }


    /**
     * 打开相机
     */
    @Override
    public void onCameraItemClick() {
        showCamera();
    }


    /**
     * 打开相册
     */
    @Override
    public void onAlbumItemClick() {

        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, REQUEST_ALBUM_OK);
    }

    @OnClick(R.id.btnGoChoose)
    public void onClick() {
        showChoose();

    }

    private void showChoose() {
        PopuChoooseWindow popuChoooseWindow = new PopuChoooseWindow(this);

        popuChoooseWindow.showAtLocation(mRecyView, Gravity.BOTTOM, 0, 0);

        lightOff();

        popuChoooseWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        popuChoooseWindow.setOnFeedBackChooseListener(this);

    }

    private void lightOff() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.3f;
        getWindow().setAttributes(layoutParams);

    }

    private void lightOn() {

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 1.0f;
        getWindow().setAttributes(layoutParams);
    }

    /**
     * 选择相机
     */

    private void showCamera() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = OtherUtils.createFile(getApplicationContext());

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getApplicationContext(), R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CAMERA:
                //请求相机
                if (mTmpFile != null) {
                    Log.d(TAG, "onActivityResult: 请求相机： " + mTmpFile.getAbsolutePath());

                    Picasso.with(this).load(mTmpFile).centerCrop().resize(OtherUtils.dip2px(this,100),OtherUtils.dip2px(this,100))
                            .error(R.mipmap.pictures_no).into(mIvDispaly);
                }
                mDatas.add(mTmpFile.getAbsolutePath());
                break;
            case REQUEST_ALBUM_OK:
                Log.d(TAG, "onActivityResult:相册 " + data.getData().toString());
        ContentResolver resolver = getContentResolver();
//
//                try {
//                    InputStream inputStream = resolver.openInputStream(data.getData());
//
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                    mIvDispaly.setImageBitmap(bitmap);
//
//                } catch (FileNotFoundException e) {
//
//
//                    e.printStackTrace();
//                }

                 Cursor query = resolver.query(data.getData(), null, null, null, null);

                String str = null;
                while (query.moveToNext()) {
                    Log.d(TAG, "onActivityResult:数量 " + query.getCount());
                    str =query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.d(TAG, "onActivityResult: 列名" + query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA)));
                }
                query.close();
                Picasso.with(this).load(new File(str)).centerCrop().resize(OtherUtils.dip2px(this,100),OtherUtils.dip2px(this,100))
                        .error(R.mipmap.pictures_no).into(mIvDispaly);
                mDatas.add(str);

                break;

        }

        mPhotoWallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, int position) {

        if(mDatas.size()<mMaxNum || mDatas==null){
            showChoose();
        }else{
            Toast.makeText(MainActivity.this, "数量够了", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemLongClick(View v, int position) {

        mPhotoWallAdapter.setIsDelete(true);
        mPhotoWallAdapter.notifyDataSetChanged();
    }
}
