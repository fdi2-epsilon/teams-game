<img align="left" alt="logo" title="A chicken with a pulley!" src="/android-app/src/main/res/mipmap-xxhdpi/ic_launcher.png"/>

#### A simple quiz game... starring our buddies.
*Our co-workers are the stars here: a quest series for each team and their problems!*
<br><br><br>

## What is this?
Codename `Enigma` is a framework for quiz collections produced at the teams software development course at [UNIPV](https://www.unipv.eu).

We have plans for an Android and a Desktop application, including also multiplayer support in the next releases.  
Check out [the docs](https://github.com/fdi2-epsilon/teams-game/wiki) if you want learn more and contribute.

## Installation instructions, *for dummies*
> I will assume that you are using Windows x64 here, since 4/5 of the team actually uses it *(ME INCLUDED, VEERY BAD)*

- **Get what you need**
  1. Install Git, download it from [here][GIT];
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
