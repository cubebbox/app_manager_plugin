package com.letoai.app_manager_plugin;

import android.content.Context;
import android.os.BatteryManager;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * CommonPlugin
 */
public class AppManagerPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context context;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();

        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "app_manager_plugin");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getInstalledAppList")) {
            try {
                List<HashMap> list = new AppManagerUtils().loadAllAppsByBatch(context);

//                List<HashMap> list = new ArrayList<>();
//                HashMap hashMap = new HashMap();
//                hashMap.put("name", "luo");
//                list.add(hashMap);
                System.out.println(list);

                result.success(list);
            } catch (Exception e) {
                e.printStackTrace();
                result.success(new ArrayList<>());
            }
        } else if (call.method.equals("getFileList")) {
            try {
                System.out.println("diaoyong");
                File[] dirs = FileUtils.getFileList(null);
                List<HashMap> list = new ArrayList<>();
                result.success(list);
            } catch (Exception e) {
                e.printStackTrace();
                result.success(new ArrayList<>());
            }
        } else if (call.method.equals("getStorage")) {
            try {
                HashMap<String, Object> hashMap = new AppManagerUtils().getStorage();

                result.success(hashMap);
            } catch (Exception e) {
                e.printStackTrace();
                result.success(new ArrayList<>());
            }
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }


}
