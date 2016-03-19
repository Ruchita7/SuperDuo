#Super Duo

##Overview

This project productionized  two apps :

**Alexandria:** A book list and barcode scanner app which allows to manually enter or scan ISBN numbers.

![Screenshot1](https://cloud.githubusercontent.com/assets/15085932/13896919/bab662ec-edc4-11e5-8d22-f1b142716fdf.png)
![Screenshot2] (https://cloud.githubusercontent.com/assets/15085932/13896920/bab799f0-edc4-11e5-9d1e-dbc85363150e.png)

**Football Scores:** An app that tracks current and future football matches of popular football leagues. Widgets are provided to add to the user experiences

![Screenshot1](https://cloud.githubusercontent.com/assets/15085932/13896917/bab06108-edc4-11e5-9835-4caddeb011f0.png)
![Screenshot2](https://cloud.githubusercontent.com/assets/15085932/13896918/bab19f32-edc4-11e5-80bd-e1ae8787b2bd.png)

Error Cases handled were :

* Diagnose existing issues with an app.

* Make an app accessible to sight-impaired users.

* Allow your app to be localized for distribution in other countries.
 
* Handle error cases in app.

* Add a widget to your app experience.

* Leverage the functionality of a library in your app.

For further details see [Guidelines](https://docs.google.com/document/d/1dF3tFXEllEnFvW0rnMcPzq4pYanE4mn0axzhsQ70r8g/pub?embedded=true)

##Prerequisites

 * The app is built with compileSdkVersion 21 and requires [JDK 7](http://oracle.com/technetwork/java/javase/downloads/index.html) or higher
 
* Android Studio

**Additional Requirements for Alaxandaria**

 * Used libraries like [Butterknife](http://jakewharton.github.io/butterknife/),[Zxing](https://github.com/zxing/zxing) for scanning ISBN numbers, [Glide](https://github.com/bumptech/glide) for image loading and caching, dependencies for which are added in `SuperDuo/superduo/Alexandria_Code/alexandria/build.gradle` like :
   
    `compile 'com.journeyapps:zxing-android-embedded:3.0.2@aar'`

    `compile 'com.google.zxing:core:3.2.0'`
    
    `compile 'com.jakewharton:butterknife:7.0.1'`
    
    `compile 'com.github.bumptech.glide:glide:3.6.1'`
   

**Additional Requirements for Football Scores**

 * To get football match scores/schedule, you will need API key from [Football-data.org](http://api.football-data.org/register).
   
 In your request for a key, state that your usage will be foreducational/non-commercial use. You will also need to provide some personal information to complete the request. Once you submit your request, you should receive your key via email shortly after.

* Once you obtain your key, in your `SuperDuo/superduo/Football_Scores-Starting/Football_Scores-master/app/src/main/res/values/strings.xml`,

    `<string name="api_key" translatable="false"></string>`

* Used Butterknife library as well.

##Instructions

###Get the source codes

Get the source code of the library and example app, by cloning git repository or downloading archives.

 * If you use **git**, execute the following command in your workspace directory.
 
    `$ git clone https://github.com/Ruchita7/SuperDuo.git`
    
* If you are using Windows, try it on GitBash or Cygwin or something that supports git.
 
###Import the project to Android Studio
 
Once the project is cloned to disk you can import into Android Studio:

 * From the toolbar select **File > Import Project**, or Import Non-Android Studio project from the Welcome Quick Start.

 *  Select the directory that is cloned. If you can't see your cloned directory, click "Refresh" icon and find it.

 *  Android Studio will import the project and build it. This might take minutes to complete. Even when the project window is opened, wait until the Gradle tasks are finished and indexed.

 *  Connect your devices to your machine and select app from the select Run/Debug Configuration drop down.Click the Run button

###Build and install using Gradle

If you just want to install the app to your device, you don't have to import project to Android Studio.

 •  After cloning the project, make sure **ANDROID_HOME** environment variable is set to point to your Android SDK. See [Getting Started with Gradle](https://guides.codepath.com/android/Getting-Started-with-Gradle).

 •  Connect an Android device to your computer or start an Android emulator.

 •  Compile the sample and install it. Run gradlew installDebug. Or if you on a Windows computer, use **gradlew.bat** instead.
 
###Contributing

Please follow the **"fork-and-pull"** Git workflow while contributing to this project

 **Fork** the repo on GitHub

 **Commit** changes to a branch in your fork

 **Pull request "upstream"** with your changes

 **Merge** changes in to "upstream" repo

**NOTE:** Be sure to merge the latest from **"upstream"** before making a pull request!
 
###FAQs

**The project can no longer be compiled/imported if I move it to another directory**

I typically include the dependencies in the repo and have them already linked, so if you move the project you'll need to also update the path to the dependency. To do this, modify the project.properties file of the project

**I'm getting an error saying that there are multiple versions of a jar**

To fix this, make sure that all the jars you are using conform to the same version. You can do this by just replacing all the problematic jars with the version you would like to use.
 

