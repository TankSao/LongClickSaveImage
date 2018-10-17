package com.example.administrator.longclicksaveimagedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.longclicksaveimagedemo.utils.DensityUtil;
import com.example.administrator.longclicksaveimagedemo.utils.DialogCircle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView save;
    String path =  Environment.getExternalStorageDirectory().getPath()
            + "/mingpian.png";
    TextView qd;
    TextView qx;
    TextView tv;
    DialogCircle newDialog;
    int width, height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAllPower();
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        save = (ImageView) findViewById(R.id.save);
        save.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                popSave();
                return false;
            }
        });
    }

    private void popSave() {
        View view = View.inflate(this, R.layout.pop_save, null);
        tv = view.findViewById(R.id.tv);
        qd = view.findViewById(R.id.qd);
        qx = view.findViewById(R.id.qx);
        newDialog = new DialogCircle(this, DensityUtil.dip2px(this, width / 4), DensityUtil.dip2px(this, height / 8), view,
                R.style.dialog);
        newDialog.setCancelable(false);
        tv.setText("下载路径："+path);
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBitmap(save,path);
                newDialog.dismiss();
            }
        });
        newDialog.show();
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newDialog.dismiss();
            }
        });
    }
    public void saveBitmap(View view, String filePath) {

        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        //存储
        FileOutputStream outStream = null;
        File file = new File(filePath);
        if (file.isDirectory()) {//如果是目录不允许保存
            Toast.makeText(MainActivity.this, "该路径为目录路径", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            Toast.makeText(MainActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage() + "#");
            Toast.makeText(MainActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                bitmap.recycle();
                if (outStream != null) {
                    outStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //申请权限，需要使用之前申请
    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET}, 1);
            }
        }
    }
}
