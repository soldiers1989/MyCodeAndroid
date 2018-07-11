package com.hxjr.p2p.ad5.ui.mine.setting;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dm.photo.CropperImage;
import com.dm.photo.IntentEvent;
import com.dm.photo.cropper.CropImageView;
import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.PhotoUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;


public class TakePhotoNewActivity extends BaseActivity implements CameraSurfaceView.OnCameraStatusListener {

    private Button takePic;
    private CameraSurfaceView mCameraSurfaceView;
    private View rl_photo;
    private View rl_selected;
    private View crop_layout;
    private CropImageView cropImageView;
    private ImageView selected_photo;
    private String index;
    public static final String PATH = Environment.getExternalStorageDirectory().toString() + "/AndroidMedia/";
    public static final int SELECT_PHOTO = 2;
    private Uri uri;
    private String filename;
    private long dateTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //        // 全屏显示
        //        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams
        // .FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_photo_new);
        //        if (true!= CameraPermissionCompat.cameraIsCanUse()) {
        //            CameraPermissionCompat.checkCameraPermission(this,new OnCameraPermissionListener);
        //        }
        rl_photo = findViewById(R.id.rl_photo);
        rl_selected = findViewById(R.id.rl_selected);
        crop_layout = findViewById(R.id.crop_layout);
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        //        resetChoicePhoto = (Button) findViewById(R.id.resetChoicePhoto);
        //        affirmPhoto = (Button) findViewById(R.id.affirmPhoto);
        takePic = (Button) findViewById(R.id.takePic);
        selected_photo = (ImageView) findViewById(R.id.selected_photo);
        index = getIntent().getStringExtra("index");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0000);
        } else {
            // 有权限了，去放肆吧。
            mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
            mCameraSurfaceView.setOnCameraStatusListener(this);
            takePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkClick(view.getId()))
                        mCameraSurfaceView.takePicture();
                    takePic.setClickable(false);
                }
            });
        }
    }

    @Override
    public void onCameraStopped(Bitmap bm) {
        rl_photo.setVisibility(View.GONE);
        rl_selected.setVisibility(View.VISIBLE);
        dateTaken = System.currentTimeMillis();
        filename = DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString() + ".jpg";
        uri = insertImage(PATH, filename, bm,
                null);
        takePic.setClickable(true);
    }

    /**
     * 存储图像并将信息添加入媒体数据库
     */
    private Uri insertImage(String directory,
                            String filename, Bitmap source, byte[] jpegData) {
        show();
        OutputStream outputStream = null;
        //        filePath = directory + filename;
        Uri uri;
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(directory, filename);
            if (file.createNewFile()) {
                outputStream = new FileOutputStream(file);
                if (source != null) {
                    source = zoomImage(source, 700, 500);
                    source.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
                } else {
                    outputStream.write(jpegData);
                }
            }
            selected_photo.setImageBitmap(source);
            uri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Throwable t) {
                }
            }
        }
        dismiss();
        return uri;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        if (bgimage.getWidth() < bgimage.getHeight()) {
            bgimage = PhotoUtils.rotate(bgimage, -90);
        }
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 从相册中获取图片
     */
    public void select_photo(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    /**
     * 打开相册的方法
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImgeOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 4.4以下系统处理图片的方法
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }


    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.e(TAG, "handleImgeOnKitKat   1");
        if (DocumentsContract.isDocumentUri(this, uri)) {
            Log.e(TAG, "handleImgeOnKitKat   2");
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                Log.e(TAG, "handleImgeOnKitKat   3");
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Log.e(TAG, "handleImgeOnKitKat   4");
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long
                        .valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            Log.e(TAG, "handleImgeOnKitKat   5");
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取图片路径即可
            Log.e(TAG, "handleImgeOnKitKat   6");
            imagePath = uri.getPath();
        }
        Log.e(TAG, "handleImgeOnKitKat   7" + imagePath);
        //根据图片路径显示图片
        displayImage(imagePath);
    }

    /**
     * 通过uri和selection来获取真实的图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 根据图片路径显示图片的方法
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //            mCropImageView.setImageBitmap(bitmap);
            //            showCropperLayout();
            cropImageView.setImageBitmap(bitmap);
            crop_layout.setVisibility(View.VISIBLE);
            rl_selected.setVisibility(View.GONE);
            rl_photo.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_LONG).show();
        }
    }

    public void resetPhoto(View view) {
        rl_photo.setVisibility(View.VISIBLE);
        rl_selected.setVisibility(View.GONE);
    }

    public void submitPhoto(View view) {
        Intent intent = new Intent();
        intent.setData(uri);
        DMLog.e(uri.toString());
        intent.putExtra("index", index);
        intent.putExtra("path", PATH + filename);
        IntentEvent ie = new IntentEvent();
        ie.setIntent(intent);
        EventBus.getDefault().post(ie);
        finish();
    }

    public void affirmPhoto(View view) {
        // 获取截图并旋转90度
        CropperImage cropperImage = cropImageView.getCroppedImage();
        Log.e(TAG, cropperImage.getX() + "," + cropperImage.getY());
        Log.e(TAG, cropperImage.getWidth() + "," + cropperImage.getHeight());
        // Bitmap bitmap = Utils.rotate(cropperImage.getBitmap(), -90);
        Bitmap bitmap = cropperImage.getBitmap();
        // 系统时间
        long dateTaken = System.currentTimeMillis();
        // 图像名称
        String filename = DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString() + ".jpg";
        Uri uri = insertImage(PATH, filename, bitmap,
                null);
        cropperImage.getBitmap().recycle();
        cropperImage.setBitmap(null);
        // Intent intent = new Intent(this, ShowCropperedActivity.class);
        Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra("index", index);
        intent.putExtra("path", PATH + filename);
        intent.putExtra("width", bitmap.getWidth());
        intent.putExtra("height", bitmap.getHeight());
        // intent.putExtra("cropperImage", cropperImage);
        // startActivity(intent);
        IntentEvent ie = new IntentEvent();
        ie.setIntent(intent);
        DMLog.e(filename);
        EventBus.getDefault().post(ie);
        bitmap.recycle();
        finish();
    }

    public void resetChoicePhoto(View view) {
        rl_photo.setVisibility(View.VISIBLE);
        crop_layout.setVisibility(View.GONE);
        rl_selected.setVisibility(View.GONE);
    }
    public void onBack(View view){
        finish();
    }
}
