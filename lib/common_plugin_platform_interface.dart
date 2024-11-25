import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'common_plugin_method_channel.dart';


abstract class CommonPluginPlatform extends PlatformInterface {
  /// Constructs a CommonPluginPlatform.
  CommonPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static CommonPluginPlatform _instance = MethodChannelCommonPlugin();

  /// The default instance of [CommonPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelCommonPlugin].
  static CommonPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [CommonPluginPlatform] when
  /// they register themselves.
  static set instance(CommonPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<List<dynamic>> getInstalledAppList() async {
    throw UnimplementedError('getInstalledAppList() has not been implemented.');
  }
  Future<List<dynamic>> getFileList() async {
    throw UnimplementedError('getFileList() has not been implemented.');
  }
  Future<dynamic> getStorage() async {
    throw UnimplementedError('getStorage() has not been implemented.');
  }
}
