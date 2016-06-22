# Synchronized Audio Player
[![Build Status](https://travis-ci.org/NoahAndrews/SynchronizedMediaPlayer.svg?branch=master)](https://travis-ci.org/NoahAndrews/SynchronizedMediaPlayer)

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

