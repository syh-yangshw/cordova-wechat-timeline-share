微信分享到朋友圈多图

-------------

# iOS如何调用分享

```javascript
// when use it with ios app, the paths should be base64 data url

var paths = ['base64 image data', 'base64 image data'];
WechatTimelineShare.shareTimeline(
    {
        urls: paths
    },
    function (msg) {
        console.log(msg);
    },
    function (err) {
        console.log(err);
    }
)
```

# Android 如何调用分享

```javascript
// when use it with android app, the paths should be absolute path

var paths = [
    '/storage/emulated/0/Pictures/wechattimelineshare1.jpeg',
    '/storage/emulated/0/Pictures/wechattimelineshare0.jpeg'
]

WechatTimelineShare.shareTimeline(
    {
        urls: paths,
        message:"测试分享功能"
    },
    function (msg) {
        console.log(msg);
    },
    function (err) {
        console.log(err);
    }
)
```
### android 7以及以上版本会出现android.os.FileUriExposedException问题.
参考[stackoverflow解决FileUriExposedException问题](https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed)
 - 配置fileprovide,但是经过测试,单图是可以微信分享,但是多图分享还是不行.
 - 使用StrictMode,需要自己继承Application类,并注册到AndroidManifest.xml上.
 - 代替startActivity(intent),使用startActivity(Intent.createChooser(intent, "Your title"));,经过测试是可以成功分享.并且是改动最小的方法.但是经过android8实测,仍然会报错.
 所以最终方法是
 - 单张图片分享使用fileprovide装换
 - 多张图片先使用cordova的fileTransfer插件下载到本地外部存储目录,但是相册不会显示刚下载好的图片,然后使用图片导入的方法导入图片到相册,并返回的就是一个content://头的文件信息,这样就可以避免file://头的权限信息
