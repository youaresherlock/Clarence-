package com.gougoucompany.clarence.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.entity.MyUser;
import com.gougoucompany.clarence.smartbutler.ui.CourierActivity;
import com.gougoucompany.clarence.smartbutler.ui.LoginActivity;
import com.gougoucompany.clarence.smartbutler.ui.PhoneActivity;
import com.gougoucompany.clarence.smartbutler.utils.L;
import com.gougoucompany.clarence.smartbutler.utils.ShareUtils;
import com.gougoucompany.clarence.smartbutler.utils.StaticClass;
import com.gougoucompany.clarence.smartbutler.utils.UtilTools;
import com.gougoucompany.clarence.smartbutler.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.fragment
 * 文件名:   UserFragment
 * 创建时间: 2018/4/27 10:56
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     个人中心
 */

public class UserFragment extends Fragment implements View.OnClickListener {

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int IMAGE_REQUEST_CODE = 2;
    public static final int RESULT_REQUEST_CODE = 3;
    private File tempFile = null;

    private Button btn_exit_user;
    private TextView edit_user;

    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;
    //更新按钮
    private Button btn_update_ok;
    //圆形头像
    private CircleImageView  profile_image ;

    private CustomDialog dialog;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;


    //快递查询
    private TextView tv_courier;
    //归属地查询
    private TextView tv_phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }


    //初始化View
    private void findView(View view){
        btn_exit_user = (Button) view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user = (TextView) view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);

        tv_courier = (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);

        et_username = (EditText) view.findViewById(R.id.et_username);
        et_sex = (EditText) view.findViewById(R.id.et_sex);
        et_age = (EditText) view.findViewById(R.id.et_age);
        et_desc = (EditText) view.findViewById(R.id.et_desc);

        btn_update_ok = (Button) view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);

        UtilTools.getImageToShare(getActivity(), profile_image);

        dialog = new CustomDialog(getActivity(), 0, 0, R.layout.dialog_photo, R.style.Theme_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        //屏幕外点击无效 也就是说点击不能退出
        dialog.setCancelable(false);
        //dialog算是一个view 因此可以通过这个来找到这三个按钮
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        //进入APP的个人中心Fragment后要将TextView控件设置默认值，然后默认不可编辑
        //默认是不可点击的/不可输入的
        setEnable(false);

        /*设置具体的值
        * 每当你应用的用户注册或是第一次登陆成功，都会在本地磁盘中有一个
        * 缓存的用户对象，这样，你可以通过获取这个缓存的用户对象俩进行登录。
        * 在扩展了用户类的情况下获取当前登录用户，可以使用如下的示例代码
        * MyUser userInfo = BmobUser.getCurrentUser(MyIser.class);*/
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        //getAge()方法返回一个int类型的数值，因此要转换成字符串
        et_age.setText(userInfo.getAge() + "");
        et_desc.setText(userInfo.getDesc());
        //isSex()方法返回一个boolean值 true表示男否则为女
        et_sex.setText(userInfo.isSex() ? "男" : "女");

    }

    private void setEnable(boolean is){
        et_username.setEnabled(is);
        et_sex.setEnabled(is);
        et_age.setEnabled(is);
        et_desc.setEnabled(is);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //退出登录
            case R.id.btn_exit_user:
                //清楚缓存用户对象
                MyUser.logOut();
                //现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            //编辑资料
            case R.id.edit_user:
                //设置为可编辑，并且确认按钮显示出来
                setEnable(true);
                btn_update_ok.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_update_ok:
                //1.拿到输入框的值
                String username = et_username.getText().toString();
                String age = et_age.getText().toString();
                String sex = et_sex.getText().toString();
                String desc = et_desc.getText().toString();

                //2.判断前三个是否为空，第四个为空可以设置为默认值
                if(!TextUtils.isEmpty(username) & !TextUtils.isEmpty(age)
                        & !TextUtils.isEmpty(sex)){
                    //3.更新属性
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    if(sex.equals("男")){
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }
                    //简介
                    if(!TextUtils.isEmpty(desc)){
                        user.setDesc(desc);
                    } else {
                        user.setDesc("这个人很懒，什么都没有留下!");
                    }

                    BmobUser bmobUser =  BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                //修改成功
                                setEnable(false);
                                btn_update_ok.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
        }
    }

    //跳转到相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    //拍照后存储并显示图片
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //启动系统相机
        //判断内存卡是否可用，可用的话就进行存储 File file = new File(path, filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME))); //更改系统默认存储路径
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode != getActivity().RESULT_CANCELED){
           switch(requestCode){
               //相册的数据
               case IMAGE_REQUEST_CODE:
                   startPhotoZoom(data.getData());
                   break;
               //相机的数据
               case CAMERA_REQUEST_CODE:
                   tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                   startPhotoZoom(Uri.fromFile(tempFile));
                   break;
               case RESULT_REQUEST_CODE:
                   //有可能点击取消
                   if(data != null){
                       //拿到图片设置
                       setImageToView(data);
                       //既然已经设置了图片，我们原先的就应该删除
                       if(tempFile != null){
                           tempFile.delete();
                       }
                   }
                   break;
           }
       }
    }

    //裁剪
    private void startPhotoZoom(Uri uri){
        if(uri == null){
          L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data){
        Bundle bundle = data.getExtras();
        if(bundle != null){
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilTools.putImgToShare(getActivity(), profile_image);
    }
}





















































































































