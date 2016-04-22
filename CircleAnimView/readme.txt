android系统：2.2
第三方图片框架：glide-3.7.0.jar
view完整效果：FlyCircleActivity+FlyCircleAnimView+FlyCircleDetailsFragment
主要view：FlyCircleAnimView

FlowView 相关可以不用，对应效果需要两个view，衔接不强。

看效果建议手机分辨率为1080x1920

2016-04-22 10:54 初始化画布的宽度和高度
FlyCircleAnimView init()方法中
	canvaswWidth = context.getResources().getDisplayMetrics().widthPixels;
	canvasHeight = context.getResources().getDisplayMetrics().heightPixels;

CircleAnimXfermodeView init()方法中
	width = context.getResources().getDisplayMetrics().widthPixels;
	height = context.getResources().getDisplayMetrics().heightPixels;