1.在CMTest.java文件中把字段key替换成自己的密钥
2.
/**************
在需要生成加密短信的地方加入下面代码
*************/
CMTest t = new CMTest();
String cmd = "M0MjOUhZMfgjRlCjMd1mYg==";	
String cmdScret = t.getDynamicCmd(cmd);	//加密生成的短信代码