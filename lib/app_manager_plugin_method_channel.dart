import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'app_manager_plugin_platform_interface.dart';


/// An implementation of [AppManagerPluginPlatform] that uses method channels.
class MethodChannelAppManagerPlugin extends AppManagerPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = MethodChannel('app_manager_plugin');

  Future<List<dynamic>> getInstalledAppList() async {
    List<dynamic> list = await methodChannel.invokeMethod('getInstalledAppList');

    for (var item in list) {
      try {
        String icon = item['icon'];
        icon = icon.replaceAll("\n", "");
        item['icon'] = base64Decode(icon);
      } catch (e) {
        print(e);
      }
    }

    return list;
  }

  Future<List<dynamic>> getFileList() async {
    List<dynamic> list = await methodChannel.invokeMethod('getFileList');

    // for (var item in list) {
    //   try {
    //     String icon = item['icon'];
    //     icon = icon.replaceAll("\n", "");
    //     item['icon'] = base64Decode(icon);
    //   } catch (e) {
    //     print(e);
    //   }
    // }

    return list;
  }

  Future<dynamic> getStorage() async {
    dynamic list = await methodChannel.invokeMethod('getStorage');

    return list;
  }
}
