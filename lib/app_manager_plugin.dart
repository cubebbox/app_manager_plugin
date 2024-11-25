
import 'app_manager_plugin_platform_interface.dart';

class AppManagerPlugin {
  Future<List<dynamic>> getInstalledAppList() async {
    List<dynamic> list = await AppManagerPluginPlatform.instance.getInstalledAppList();
    return list;
  }

  Future<List<dynamic>> getFileList() async {
    List<dynamic> list = await AppManagerPluginPlatform.instance.getFileList();
    return list;
  }

  dynamic getStorage() async {
    dynamic storage = await AppManagerPluginPlatform.instance.getStorage();
    return storage;
  }
}
