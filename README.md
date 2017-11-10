# QQNumberSimulation
软件地址：http://www.coolapk.com/apk/com.zzz.QQNumberSimulation

注意：本软件需要root权限

软件用途：
❶模拟出任意一个QQ号码，号码会在QQ的账号管理里面显示，可以装B。
❷如果在设备上登录了某个QQ，最近又把该QQ从手机上删除掉了。此时可以使用软件模拟出被删除的QQ添加到您的设备中，并且二次登录的QQ不需要密码验证。
❸如果在QQ账号管理中登录的QQ太多，账号达到上限QQ不能继续添加。可以使用本软件模拟QQ账号，之后再验证密码即可

软件核心代码：
touch /data/data/com.tencent.mobileqq/files/user/u_12345_t
"touch " + dataPath + ver + "/files/user/" + "u_" + QQNumer + "_t"
