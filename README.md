## 呼吸控件BreatheView
![控件效果](https://github.com/chengqian0109/images/blob/master/BreathView.gif)
### 1. 编辑项目根目录的 build.gradle 添加仓库支持：
```
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### 2. 添加 Gradle 依赖：
![](https://jitpack.io/v/chengqian0109/BreatheView-master.svg) BreatheView-master 后面的「Tag」指的是左边这个 JitPack 徽章后面的「版本名称」，请自行替换。(https://jitpack.io/#chengqian0109/BreatheView-master)
```
    dependencies {
        implementation 'com.github.chengqian0109:BreatheView-master:Tag'
    }
```
## <font color="red" face="黑体">**[注]：出于对效果的考虑，呼吸圆的颜色必须设置为半透明，否则会导致程序崩溃，请知晓。**</font>
### 3. xml布局文件中的简单实用：
```
    <com.jack.widget.BreatheView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```
### 4. Java源代码中使用：
```
    BreatheView breatheView = new BreatheView(this);
    breatheView.setAnimationDuration(1000)
            .setFirstCircleMinRadiusRes(R.dimen.dp_5)
            .setFirstCircleMaxRadiusRes(R.dimen.dp_10)
            .setFirstCircleColorRes(R.color.colorAccent)
            .setSecondCircleRadiusRes(R.dimen.dp_20)
            .setSecondCircleColorRes(R.color.colorPrimary)
            .setThirdCircleRadiusRes(R.dimen.dp_40)
            .setThirdCircleColorRes(R.color.cyan)
            .setBreatheCircleMaxRadiusRes(R.dimen.dp_60)
            .setBreatheCircleColorRes(R.color.breatheColor)
            .start();

    // 其他
    BreatheView anotherBreatheView = new BreatheView(this);
    anotherBreatheView.setAnimationDuration(1500)
            .setFirstCircleMinRadius(dp2px(7))
            .setFirstCircleMaxRadius(dp2px(14))
            .setFirstCircleColor(Color.parseColor("#9C27B0"))
            .setSecondCircleRadius(dp2px(30))
            .setSecondCircleColor(Color.parseColor("#FFC107"))
            .setThirdCircleRadius(dp2px(50))
            .setThirdCircleColor(Color.parseColor("#4CAF50"))
            .setBreatheCircleMaxRadius(dp2px(100))
            .setBreatheCircleColor(Color.parseColor("#99F39621"))
            .start();
```
