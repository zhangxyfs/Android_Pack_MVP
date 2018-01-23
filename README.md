# Android_Pack_MVP
### android 可以分包编译的MVP 框架
#### 原理如下：
模块分为：main、Base、pluginXXX

- main 用于实现主体页面结构，将引用其他所有的module。
- Base 为基础module用于存放基础网络请求mvp基础框架、其他的基础的util及所有widget
- pluginXXX 为模块化后的业务代码，plugin需要引用Base，但是不引用其他的pluginXXX，所以plugin之间无法直接调用，可以通过Base里的JumpAct类来跳转到其他plugin中


