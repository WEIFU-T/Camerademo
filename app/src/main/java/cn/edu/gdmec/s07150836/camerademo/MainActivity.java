package cn.edu.gdmec.s07150836.camerademo;


import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private ImageView imageView;
    private File file;
    private Camera camera;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void takePhoto(View v){

        camera.takePicture(null,null,prictureCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            camera.release();
            camera=null;
            e.printStackTrace();
        }
        camera.startPreview();


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    Camera.PictureCallback prictureCallback = new Camera.PictureCallback(){


        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if(data!=null){
                savaPicture(data);
            }
        }
    };
    public void savaPicture(byte[] data) {
        try{
            String imageID = System.currentTimeMillis() + "";

            String pathName = android.os.Environment.getExternalStorageDirectory().getPath() + "/";
            File file = new File(pathName);

            if (!file.exists()) {
                file.mkdirs();
            }
            pathName += imageID + ".jpeg";
            file = new File(pathName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            Toast.makeText(this, "已经存在路径："+pathName,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
