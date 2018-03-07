package cordovawechattimelineshare;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.widget.Toast;
import android.support.v4.content.FileProvider;
import android.provider.MediaStore;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * Created by Glan.Wang on 15/6/16.
 */
public class ShareUtils {
    /**
     * 不实用微信的SDK分享图片到好友
     * @param context
     * @param path
     */
    public static void sharePicToFriendNoSDK(Context context, String path) {
        if(!isInstallWeChart(context)){
            Toast.makeText(context,"您没有安装微信",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        // intent.setFlags(0x3000001);
        File f = new File(path);
        if(f.exists()){
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        } else {
            Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show();
            return;
        }
        context.startActivity(intent);
    }


    /**
     * 分享9图到朋友圈
     *
     * @param context
     * @param Kdescription 9图上边输入框中的文案
     * @param paths        本地图片的路径
     */
    public static void share9PicsToWXCircle(Context context, String Kdescription, List<String> paths){
        if (!isInstallWeChart(context)) {
            Toast.makeText(context,"您没有安装微信",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        ArrayList<Uri> imageList = new ArrayList<Uri>();
        for (String picPath : paths) {
            File f = new File(picPath);
            if (f.exists()) {
              try{
				  String imgpath=MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), f.getName(), null);
                  if(paths.size()==1){
                      //单张图片谁用fileprovide转换
                      Uri photoUri = FileProvider.getUriForFile(
                              context,
                              context.getPackageName() + ".imageProvider",
                              f);
                      imageList.add(photoUri);
                  }else{
                      //通过cordova fileTransfer插件下载的图片不能在相册中看到,并且是file(android7以及以上版本已经禁止使用此头传递)开头的,可以通过MediaStore的api将图片插入到相册中
                      //并返回一个content://开头的path,这样android7以及以上版本可以传递
                      imageList.add(Uri.parse(imgpath));
                  }
              }catch(Exception e){
                Toast.makeText(context,"网络错误,请稍后再试",Toast.LENGTH_SHORT).show();
                return;
              }
            }
        }
        if(imageList.size() == 0){
            Toast.makeText(context,"图片不存在",Toast.LENGTH_SHORT).show();
            return;
        }
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if(paths.size()==1){
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM,imageList.get(0));
        }else{
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(Intent.EXTRA_STREAM,imageList);
        }
        intent.putExtra("Kdescription", Kdescription); //微信分享页面，图片上边的描述
        context.startActivity(intent);
    }

    /**不实用微信sdk检查是否安装微信
     * @param context
     * @return
     */
    public static boolean isInstallWeChart(Context context){
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.tencent.mm", 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }


}