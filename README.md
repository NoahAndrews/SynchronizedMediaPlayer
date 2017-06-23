# This project is being reworked as an Android app called DolphinPod. 
This version of the project will likely not be revisited in the future.

# Synchronized Audio Player
[![GitHub license](https://img.shields.io/badge/license-AGPL-blue.svg)](https://raw.githubusercontent.com/NoahAndrews/SynchronizedMediaPlayer/master/LICENSE.txt)

[![Build Status](https://img.shields.io/travis/NoahAndrews/SynchronizedMediaPlayer/master.svg?maxAge=2592000)](https://travis-ci.org/NoahAndrews/SynchronizedMediaPlayer)

[![Dependency Status](https://www.versioneye.com/user/projects/577c7698649a6f000d048015//badge.svg?style=flat)](https://www.versioneye.com/user/projects/577c7698649a6f000d048015)

### Overview
The goal of this project is to produce a simple audio player that will
allow two people to listen to an audio file in sync with each other over
the Internet. I've used the source code from [this JavaFX tutorial](https://docs.oracle.com/javafx/2/media/jfxpub-media.htm) as a
starting point. Oracle's license for that code can be found in
[ORACLE_LICENSE.txt.](ORACLE_LICENSE.txt)

### Download
You can download the latest version at [the releases page.](https://github.com/NoahAndrews/SynchronizedMediaPlayer/releases)

### Tech Stack
This software is built using JavaFX for the GUI and media playback
functionality. This is my first ever JavaFX project, so I'm learning as 
I go. Gradle is being used as the build system.

### Protocol
I'm calling the networking protocol I'm developing for this project SAVPP,
for Synchronized Audio/Video Playback Protocol. I plan to implement it
using [Protocol Buffers.](https://developers.google.com/protocol-buffers/) You can find the Java implementation [here.](https://github.com/NoahAndrews/SAVPP-java)

### Other Documents
* The changelog for this software can be found in [CHANGELOG.md](CHANGELOG.md)
* The roadmap for this software can be found in [ROADMAP.md](ROADMAP.md)
* Notes on how the SAVPP protocol will work can be found [here.](https://github.com/NoahAndrews/SAVPP-java/blob/master/NOTES.md)

### Legal Stuff
This software is licensed under the AGPL version 3. Basically, if you
change and distribute this software, you must also distribute your
modified source code, and release it under this same license. In
addition, if you host a server using a modified version of my source code,
you must make those modifications available as well, even if you don't
distribute a binary version. The full license is in [LICENSE.txt.](LICENSE.txt)
The reusable module for the network synchronization
functionality is being released under the much more liberal MIT license.
You can effectively do whatever you want with that code, as long as you
give me proper credit for it and include the license.

Copyright (C) 2016 Noah Andrews

