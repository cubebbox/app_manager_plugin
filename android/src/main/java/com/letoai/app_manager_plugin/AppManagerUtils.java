package com.letoai.app_manager_plugin;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.flutter.plugin.common.MethodChannel;

public class AppManagerUtils {
    private PackageManager packageManager;
    private int mIconDpi;

    public List<HashMap> loadAllAppsByBatch(Context context, MethodChannel.Result result) {

        packageManager = context.getPackageManager();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        packageManager = context.getPackageManager();
        mIconDpi = activityManager.getLauncherLargeIconDensity();

        new Thread() {
            @Override
            public void run() {
                super.run();
                List<ResolveInfo> apps = null;
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                apps = context.getPackageManager().queryIntentActivities(mainIntent, 0);

                List<HashMap> list = new ArrayList<>();
                for (int i = 0; i < apps.size(); i++) {

                    String packageName = apps.get(i).activityInfo.applicationInfo.packageName;
                    String title = apps.get(i).loadLabel(context.getPackageManager()).toString();

//            if (title == null) {
//                title = apps.get(i).activityInfo.name;
//            }

                    ActivityInfo info = apps.get(i).activityInfo;

                    Drawable icon = getFullResIcon(info);
//            AppInfo appInfo = new AppInfo();
//            appInfo.setName(title);
//            appInfo.setPackageName(packageName);
                    HashMap hashMap = new HashMap();
                    hashMap.put("name", title);
                    hashMap.put("packageName", packageName);
//            hashMap.put("icon", "data:image/png;base64," + drawableToBase64(icon));
                    hashMap.put("icon", drawableToBase64(icon));
//            hashMap.put()

//            appInfo.setVersionName();
//            appInfo.setVersionCode();
//            appInfo.setInstalledTimestamp();
//            long totalSize = 0;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                totalSize = getAppSize(context, packageName);
//            }

//            System.out.println(title + "  " + packageName + "   ");
//            datas.add(new PakageMod(packageName,title,icon));
                    list.add(hashMap);
                }

                result.success(list);
            }
        }.start();


        return null;
    }

    public String drawableToBase64(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        String base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return base64String;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public Drawable getFullResIcon(ActivityInfo info) {
        Resources resources;
        try {
            resources = packageManager.getResourcesForApplication(info.applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            resources = null;
        }
        if (resources != null) {
            int iconId = info.getIconResource();
            if (iconId != 0) {
                return getFullResIcon(resources, iconId);
            }
        }
        return getFullResDefaultActivityIcon();
    }

    public Drawable getFullResDefaultActivityIcon() {
        return getFullResIcon(Resources.getSystem(), android.R.mipmap.sym_def_app_icon);
    }

    public Drawable getFullResIcon(Resources resources, int iconId) {
        Drawable d;
        try {
            // requires API level 15 (current min is 14):
            d = resources.getDrawableForDensity(iconId, mIconDpi);
        } catch (Resources.NotFoundException e) {
            d = null;
        }

        return (d != null) ? d : getFullResDefaultActivityIcon();
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static long getAppSize(final Context context, String pkgName) {
//        StorageStatsManager storageStatsManager = (StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
//        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
//        List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
//
//        long totalSize = 0;
//        for (StorageVolume item : storageVolumes) {
//            String uuidStr = item.getUuid();
//            UUID uuid;
//            if (uuidStr == null) {
//                uuid = StorageManager.UUID_DEFAULT;
//            } else {
//                uuid = UUID.fromString(uuidStr);
//            }
//            int uid = getUid(context, pkgName);
//            //通过包名获取uid
//            StorageStats storageStats = null;
//            try {
//                storageStats = storageStatsManager.queryStatsForUid(uuid, uid);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            synchronized (AppInfo.class) {
//                //缓存大小
//                storageStats.getCacheBytes();
//                //数据大小
//                storageStats.getDataBytes();
//                //应用大小
//                storageStats.getAppBytes();
//                //应用的总大小
//                totalSize = storageStats.getCacheBytes() + storageStats.getDataBytes() + storageStats.getAppBytes();
//            }
//        }
//        return totalSize;
//    }
//
//    public static int getUid(Context context, String pakName) {
//        try {
//            return context.getPackageManager().getApplicationInfo(pakName, PackageManager.GET_META_DATA).uid;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }


    public HashMap<String, Object> getStorage() {

        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());

        //存储块总数量
        long blockCount = statFs.getBlockCount();
        //块大小
        long blockSize = statFs.getBlockSize();
        //可用块数量
        long availableCount = statFs.getAvailableBlocks();
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        long freeBlocks = statFs.getFreeBlocks();
        //这两个方法是直接输出总内存和可用空间，也有getFreeBytes
        //API level 18（JELLY_BEAN_MR2）引入
        long totalSize = statFs.getTotalBytes();
        long availableSize = statFs.getAvailableBytes();

//        Log.d("statfs", "total = " + getUnit(totalSize));
//        Log.d("statfs", "availableSize = " + getUnit(availableSize));
//
//        //这里可以看出 available 是小于 free ,free 包括保留块。
//        Log.d("statfs", "total = " + getUnit(blockSize * blockCount));
//        Log.d("statfs", "available = " + getUnit(blockSize * availableCount));
//        Log.d("statfs", "free = " + getUnit(blockSize * freeBlocks));

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("total", blockSize * blockCount);
        hashMap.put("totalUnit", getUnit(blockSize * blockCount));
        hashMap.put("available", blockSize * availableCount);
        hashMap.put("availableUnit", getUnit(blockSize * availableCount));
        hashMap.put("free", blockSize * freeBlocks);
        hashMap.put("freeUnit", getUnit(blockSize * freeBlocks));

        return hashMap;
    }

    private String[] units = {"B", "KB", "MB", "GB", "TB"};

    /**
     * 单位转换
     */
    private String getUnit(float size) {

        int index = 0;
        while (size > 1024 && index < 4) {

            size = size / 1024;
            index++;
        }
        return String.format(Locale.getDefault(), " %.2f %s", size, units[index]);
    }


}
