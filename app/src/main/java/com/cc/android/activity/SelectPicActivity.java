package com.cc.android.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.cc.android.R;
import com.cc.android.base.BaseActivity;
import com.cc.android.entity.RspOk;
import com.cc.android.entity.RspResult;
import com.cc.android.net.Api;
import com.cc.android.net.NetUtils;
import com.cc.android.utils.UploadUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author spring sky<br>
 * Email :vipa1888@163.com<br>
 * QQ: 840950105<br>
 * @version 创建时间：2012-11-22 上午9:20:03
 * 说明：主要用于选择文件操作
 */

public class SelectPicActivity extends BaseActivity implements OnClickListener ,UploadUtil.OnUploadProcessListener {

    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

    /***
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "photo_path";

    private static final String TAG = "SelectPicActivity";

    private Button takePhotoBtn,cancelBtn,btn_upload;
    private ImageView img;
    private EditText message;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    btn_upload.setEnabled(true);
                    Bundle bundle = msg.getData();
                    com.cc.android.widget.Toast.show(self,bundle.get("msg").toString());
                    break;
                case 1:
                    btn_upload.setEnabled(true);
                    com.cc.android.widget.Toast.show(self,"上报成功");
                    Intent intent = new Intent(self, Location_Activity.class);
                    startActivity(intent);
                    leftToright();
                    break;
            }
        }
    };

    /**获取到的图片路径*/
    private String picPath;

    private Intent lastIntent ;

    private Uri photoUri;
    private File photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pic);
        initView();
    }
    /**
     * 初始化加载View
     */
    private void initView() {
//        dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
//        dialogLayout.setOnClickListener(this);
//        takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
//        takePhotoBtn.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.picture);
        img.setOnClickListener(this);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        message = (EditText) findViewById(R.id.message);
        cancelBtn.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        lastIntent = getIntent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.dialog_layout:
//                finish();
//                break;
            case R.id.btn_upload:
                btn_upload.setEnabled(false);
                uploadMessage();
                break;
            case R.id.picture:
                pickPhoto();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if(SDState.equals(Environment.MEDIA_MOUNTED))
        {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            /**-----------------*/
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        }else{
            Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_PIC_BY_PICK_PHOTO:
                    Uri photoUri = data.getData();
                    Bitmap mBitmap = null;
                    try {
                        mBitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(photoUri));
                        String[] proj = {MediaStore.Images.Media.DATA};
                        // 好像是Android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = getContentResolver().query(photoUri, proj, null, null, null);
                        // 按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        // 最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        photoFile = new File(path);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(mBitmap);
                    break;
            }
        }
    }

    /**
     * 选择图片后，获取图片的路径
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode,Intent data)
    {
        if(requestCode == SELECT_PIC_BY_PICK_PHOTO )  //从相册取图片，有些手机有异常情况，请注意
        {
            if(data == null)
            {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if(photoUri == null )
            {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
        if(cursor != null )
        {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
        Log.i(TAG, "imagePath = "+picPath);
        if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
        {
            lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            setResult(Activity.RESULT_OK, lastIntent);
            finish();
        }else{
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }
    public void uploadMessage(){
        String msg = message.getText().toString();
        if(msg.equals("")){
            com.cc.android.widget.Toast.show(self,"请输入上报的消息");
            btn_upload.setEnabled(true);
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("Msg_From", Api.getToken().get("token"));
        params.put("Msg_Title", msg);
        params.put("Msg_Content", msg);
        if(photoFile != null){
            String reqUrl = NetUtils.getBaseUrl()+"Message/AddMessage";
            UploadUtil.getInstance().setOnUploadProcessListener(this);
            UploadUtil.getInstance().uploadFile(photoFile,"img1",reqUrl,params);
        }else{
            Api.uploadMessage(this, msg, new NetUtils.NetCallBack<RspOk>() {
                @Override
                public void success(RspOk rspData) {
                    btn_upload.setEnabled(true);
                    com.cc.android.widget.Toast.show(self,"上报成功");
                    Intent intent = new Intent(self, Location_Activity.class);
                    startActivity(intent);
                    leftToright();
                }
                @Override
                public void failed(String msg) {
                    btn_upload.setEnabled(true);
                    com.cc.android.widget.Toast.show(self,"上报失败");
                }
            });
        }

    }

    @Override
    public void onUploadDone(int responseCode, String message) {
        Gson gson = new Gson();
        RspResult result= gson.fromJson(message,RspResult.class);
        Message ms = new Message();
        if(result.getCode() == 200){
            ms.what = 1;
            handler.sendMessage(ms);
        }else{
            ms.what = 0;
            Bundle bundle = new Bundle();
            bundle.putString("msg",result.getMessage());
            ms.setData(bundle);
            handler.sendMessage(ms);
        }
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        System.out.println("onUploadProcess");
    }

    @Override
    public void initUpload(int fileSize) {
        System.out.println("initUpload");
    }
}
