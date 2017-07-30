# MAC Environment for Scala Development## Installation date and OS info

```sh
$ date

Tue 25 Jul 2017 22:25:18 AEST

$ sw_vers

ProductName:	Mac OS X
ProductVersion:	10.12.5
BuildVersion:	16F73
```
## Homebrew```sh$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"```## Oracle Java 8```sh$ brew update$ brew cask install java

Downloading http://download.oracle.com/otn-pub/java/jdk/8u141-b15/336fa29ff2bb4ef291e347e091f7f4a7/jdk-8u141-macosx-x64.dmg```- After installation:> /usr/bin/java> /System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java```sh$ java -version
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)```- Set environmentEdit .bash_profile file```sh
$ vim ~/.bash_profile
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_141.jdk/Contents/Homeexport PATH=$JAVA_HOME/bin:$PATH```## Scala```sh$ brew install scala```
Scala 2.12.2 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_141)## SBT```sh$ brew install sbt

Downloading https://github.com/sbt/sbt/releases/download/v0.13.15/sbt-0.13.15.tgz

/usr/local/Cellar/sbt/0.13.15: 378 files, 63.3MB```Operations in project:```sh$ sbt 
$ clean
$ compile$ test$ testOnly partFileName*$ run
$ publish```## Git```sh$ brew install git

Downloading https://homebrew.bintray.com/bottles/git-2.13.3.sierra.bottle.1.tar.gz

/usr/local/Cellar/git/2.13.3: 1,472 files, 33.2MB```## Intellij
[Download](https://www.jetbrains.com/idea/)Don't suggest develper to use Scala-Eclipse. ## Mysql[MySQL Community Server 5.7.19](https://dev.mysql.com/downloads/mysql/)Add /usr/local/mysql/bin to your PATH environment variable[MySQL Workbench 6.3.9](https://dev.mysql.com/downloads/workbench/)## How to delete all ".DS_Store"```sh$ find . -name ".DS_Store" -exec rm {} \;```