import 'dart:convert';

import 'package:app_manager_plugin/app_manager_plugin.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String batteryLevel = "";
  List<dynamic> list = [];
  dynamic storage;

  void getInstalledAppList() async {
    list = await AppManagerPlugin().getInstalledAppList();

    // print(list);
    setState(() {
      // batteryLevel = level;
    });
  }

  void getFileList() async {
    await AppManagerPlugin().getFileList();

    // print(list);
    setState(() {
      // batteryLevel = level;
    });
  }

  void getStorage() async {
    storage = await AppManagerPlugin().getStorage();

    // print(list);
    setState(() {
      // batteryLevel = level;
    });
  }

  @override
  void initState() {
    super.initState();
  }

  Uint8List decodeBase64(String base64String) {
    return base64Decode(base64String);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              ElevatedButton(
                  onPressed: () {
                    getInstalledAppList();
                  },
                  child: Text("获取应用列表$batteryLevel")),
              SizedBox(
                height: 200,
                width: double.infinity,
                child: ListView.builder(
                  itemBuilder: (BuildContext context, int index) {
                    return Row(
                      children: [
                        SizedBox(height: 50, width: 50, child: Image.memory(list[index]['icon'])),
                        Text("${list[index]['name']}"),
                        Text("${list[index]['packageName']}"),
                      ],
                    );
                  },
                  itemCount: list.length,
                ),
              ),
              ElevatedButton(
                  onPressed: () {
                    getFileList();
                  },
                  child: Text("获取文件列表$batteryLevel")),
              ElevatedButton(
                  onPressed: () {
                    getStorage();
                  },
                  child: Text("存储空间$batteryLevel")),
              if (storage != null) Text("${storage['availableUnit']}/${storage['totalUnit']}")
            ],
          ),
        ),
      ),
    );
  }
}
