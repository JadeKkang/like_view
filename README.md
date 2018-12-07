# 效果展示
![](https://github.com/JadeKkang/like_view/blob/master/images/likeView.gif)
# 使用
    1.在项目gradle中添加 
      allprojects {
        repositories {
           ...
           maven { url 'https://jitpack.io' }
         } 
       }
     2.添加依赖 
        {
	  implementation 'com.github.JadeKkang:like_view:v1.0'
	 }
     3.xml中使用 
     <com.example.library.LikeView
       android:layout_width="wrap_content" 
       android:layout_height="wrap_content"
       app:circleColor="@color/colorAccent"
       app:bitmap="@mipmap/heart"/> 
# 自定义属性
| 属性 | 值 | 描述 | 
| ------------- |:-------------:| -----:| 
| circleColor |#FF4081| 点击之后出现圆形的颜色 | 
| bitmap | @mipmap/heart | 显示的图片（如 心形图片） | 
# 预留方法

	1.setIvResore(int ivResore)设置图片资源

	2.setCircleColor(int circleColor)设置点击之后出现圆形的颜色

	3.setDotNum(int dotNum,int[] dotColors)设置周边爆炸效果圆点数量和颜色值




 


