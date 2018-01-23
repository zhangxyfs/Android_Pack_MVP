# Android_Pack_MVP
### android 可以分包编译的MVP 框架

#### 原理如下：
模块分为：main、Base、pluginXXX

- main 用于实现主体页面结构，将引用其他所有的module。
- Base 为基础module用于存放基础网络请求mvp基础框架、其他的基础的util及所有widget
- pluginXXX 为模块化后的业务代码，plugin需要引用Base，但是不引用其他的pluginXXX，所以plugin之间无法直接调用，可以通过Base里的JumpAct类来跳转到其他plugin中

#### 代码说明：
- 在 gradle.properties中可以设置SELF_TEST=（NO/YES）用于判断是否为PluginXXX独立编译。
- 在 info/lib_version.properties中可以声明所有的第三方库的版本号
- 在 info/pro_base.properties 中声明应用的id和android的版本号等
- 在 info/pro_config.properties 中声明版本迭代时候的版本号和数据库版本号等
- 在 info/pro_plugin.properties 中声明gradle的plugin内容
- 在 info/lib.gradle中声明所有第三方库的引用
- 在 info/paramsBase.gradle 中声明所有引用的名字

#### 调用方式：
看例子：PluginTest即可
