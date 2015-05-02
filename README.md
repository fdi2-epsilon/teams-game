<img align="left" alt="logo" title="A chicken with a pulley!" src="/android-app/src/main/res/mipmap-xxhdpi/ic_launcher.png"/>

#### A simple quiz game... starring our buddies.&nbsp;&nbsp;[![Build Status](https://travis-ci.org/fdi2-epsilon/teams-game.svg?branch=master)](https://travis-ci.org/fdi2-epsilon/teams-game)
*Our co-workers are the stars here: a quest series for each team and their problems!*
<br><br><br>

## What is this?
Codename `Enigma` is a framework for quiz collections produced at the teams software development course at [UNIPV](https://www.unipv.eu).

We have plans for an Android and a Desktop application, including also multiplayer support in the next releases.  
Check out [the docs](https://github.com/fdi2-epsilon/teams-game/wiki) if you want learn more and contribute.

## I want to try it!
Let the built-in Gradle wrapper do everything for you:
```shell
# This will compile the project, assemble game levels and upload the app to your device
> ./gradlew build installDebug

# Upload game levels to your Android thingy using ADB, only tested with the SDK emulator
> ./gradlew uploadLevels
```
If you have problems in running these simple commands, this may be because your environment isn't setup correctly, (i.e. you don't have the latest version of the Android SDK installed), so please continue reading to fix things up.

## Inside the box...
- `android-app` is the Android application;
- `desktop-app` is the Java Desktop application;
- `common`is common code shared between Android and Desktop apps, in here we have things like custom file formats decoders and such;
- `levels` contains subprojects to create *Quest Collections* (i.e. quiz campaigns for the game);
- `build.gradle` and `settings.gradle` are part of the buildscript and define projects and tasks;
- The `gradle` folder, `gradlew` and `gradlew.bat` allow you to run build tasks and fetch dependencies without having Gradle installed;
- `gradle.properties` enables some incubating Gradle features to speed up builds;
- `.travis.yml` is a configuration file for build automation, provided by [Travis CI](https://travis-ci.org);
- Finally`README.md`, `CONTRIBUTING` and `LICENSE` are simply good things to have in a repo.


## Installation instructions, *for dummies*
> I will assume that you are using Windows x64 here, since 4/5 of the team actually uses it *(ME INCLUDED, VEERY BAD)*

- **Get what you need**
  1. Install Git, download it from [here][GIT], you can also use Subversion of you want, a guide is on the wiki;
  2. [Download][JDK7] and install JDK 7;
  3. [Get the Android][ASDK] SDK. You can also get it with [Android Studio] of you don't have any other IDE installed.
  4. If you decided to get only the SDK from step **3**, you will also need an IDE like [IntelliJ IDEA] Community,
     [Eclipse] or [NetBeans]; if you're an h4x0r you can also use vim or emacs :smiley:.
- **Link everything up**
  5. Find and fire up the Android SDK manager and take note of the path to where it is installed like here:  
     ![C:\dev\android-sdk-windows](/../gh-pages/images/guide_sdkpath.png?raw=true "SDK Path")

  6. Find the Git installation path, usually in `C:\Program Files\Git\bin` and take note;ù

  7. Run the following commands in a prompt to let the system know the tools location:
     ```shell
     setx JAVA_HOME "C:\Program Files\Java\jdk1.7.0_75\bin"
     setx ANDROID_HOME "{{the path you have found in step 5}}"
     setx PATH "%PATH%;{{path found in step 6}}"
     ```
  
  8. Link Git with your GitHub account, like this:
     ```shell
     git config —global user.name "{{Your full name}}"
     git config —global user.email "{{Your GitHub account email}}"
     ```
     
  9. Install some Android SDK packages, here's what I have installed:  
     ![Many packages](/../gh-pages/images/guide_packages.png?raw=true "SDK Packages")

Now you shuld be ready to work as usual on the project. I feel that this guide isn't complete yet...
please let me know if you are struck on something and I will consider to add a solution here.

[GIT]:http://git-scm.com/download/win
[JDK7]:http://download.oracle.com/otn-pub/java/jdk/7u75-b13/jdk-7u75-windows-x64.exe
[ASDK]:https://developer.android.com/sdk/index.html#Other

[Android Studio]:https://developer.android.com/sdk/index.html
[IntelliJ IDEA]:https://www.jetbrains.com/idea/
[Eclipse]:https://eclipse.org
[NetBeans]:https://netbeans.org

## Contributors
This project is actually mantained by
[donmarcolino](https://github.com/donmarcolino),
[franckneymar](https://github.com/franckneymar),
[lucad93](https://github.com/lucad93),
[lczx](https://github.com/lczx) and
[Vik28](https://github.com/Vik28).
