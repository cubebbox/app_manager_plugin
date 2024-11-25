import 'package:app_manager_plugin/common_plugin.dart';
import 'package:app_manager_plugin/common_plugin_method_channel.dart';
import 'package:app_manager_plugin/common_plugin_platform_interface.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockCommonPluginPlatform
    with MockPlatformInterfaceMixin
     {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<List<dynamic>> getInstalledAppList() {
    // TODO: implement getInstalledAppList
    throw UnimplementedError();
  }
}

void main() {
  final AppManagerPluginPlatform initialPlatform = AppManagerPluginPlatform.instance;

  test('$MethodChannelAppManagerPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelAppManagerPlugin>());
  });

  test('getPlatformVersion', () async {
    AppManagerPlugin commonPlugin = AppManagerPlugin();
    MockCommonPluginPlatform fakePlatform = MockCommonPluginPlatform();
    // CommonPluginPlatform.instance = fakePlatform;

    // expect(await commonPlugin.getPlatformVersion(), '42');
  });
}
