# Android HID Keyboard

[![Android Build](https://github.com/Sucareto/Android_HID_Keyboard/actions/workflows/android-build.yml/badge.svg)](https://github.com/Sucareto/Android_HID_Keyboard/actions/workflows/android-build.yml)

将你的 Android 手机变成即插即用的 USB 键盘 & 鼠标！  
可用于控制 PC，智能电视，手机等支持 USB 输入的设备，受控端不需要权限或安装软件  
鼠标支持 X/Y 轴相对移动，滚轮按下、滑动，左右键  
键盘支持常用的按键，最高支持控制键+6个普通按键的同时输入  
原理可参考[How does it work?](https://github.com/tejado/android-usb-gadget#how-does-it-work)

### 使用方法：

* Android 设备获取 root 权限
* 安装[USB Gadget Tool](https://github.com/tejado/android-usb-gadget)，并检查是否支持
* 在 USB Gadget Tool 内添加 Mouse & Keyboard 功能并启用
* 安装运行[本软件](https://github.com/Sucareto/Android_HID_Keyboard/releases/latest)，授予 root 权限后，用 USB 数据线将
  Android 设备连接到受控端即可按键输入

### 界面预览：

* 键盘主界面：  
  ![Screenshot](Screenshots/Screenshot_1.5_0.jpg)
* 键盘按下 Fn 后：  
  ![Screenshot](Screenshots/Screenshot_1.5_1.jpg)
* 鼠标主界面：  
  ![Screenshot](Screenshots/Screenshot_1.5_2.jpg)

### 已测试设备：

* OnePlus 7 Pro (guacamole) Oxygen OS 11.0.9.1.GM21BA
* OnePlus 13T (pagani) ColorOS 15.0 PKX110_15.0.2.107(CN01)

### 项目现状：

* 键盘与鼠标基础功能已实现，并已改进按键状态管理，降低重复触发和卡键风险。
* HID 报告写入改为集中维护按键状态，控制键和鼠标按键使用位运算维护，退出界面时会主动释放所有按键。
* HID 设备文件同时打开输入流和输出流，后续可以基于读取能力扩展 LED 状态、设备状态检测等功能。
* 创建 HID 设备操作目前仍依赖 USB Gadget Tool；后续可在本程序内新增 HID Gadget 配置页面。

### 感谢：

* [USB Gadget Tool](https://github.com/tejado/android-usb-gadget)：启用了 Android 的 HID 设备功能
* [android-keyboard-gadget](https://github.com/pelya/android-keyboard-gadget)：提供了发送按键操作示例
* [libsu](https://github.com/topjohnwu/libsu)：提供了 root 操作相关库
* [sjy0079](https://github.com/sjy0079)：提供了编码思路，指导了代码编写
* [メリーゴーランド](https://twitter.com/adashima_staff/status/1321866890294210560)：写代码时听的歌
* [一加手机型号汇总](https://github.com/KHwang9883/MobileModels/blob/master/brands/oneplus.md)：手机代号列表
