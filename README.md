## Introduction
Tchaikovsky is a Java library for discovering and controlling [AllPlay™](https://www.qualcomm.com/products/allplay)-compatible speakers (e.g. the Panasonic ALL series). 
The implementation is based on the [AllJoyn framework](https://allseenalliance.org/framework).
Please note that while the AllJoyn framework is able to connect to a variety of compatible devices, the sole purpose of the Tchaikovsky library is to control AllPlay devices.

## Native library
All the AllJoyn functionality is available through a native C++ library. When using Tchaikovsky, you need to have this library in path specified by java.library.path, else it will not work.
There are two options for getting hold of the native library: Either extract it from a precompiled SDK, or download the source code and compile it yourself. Both source code and SDKs are available at [https://allseenalliance.org/framework/download](https://allseenalliance.org/framework/download). 
Tchaikovsky has been successfully tested with the dll extracted from the Windows 15.04.00b SDK as well as with the Standard Core Source 16.04.00 compiled on a Raspberry Pi 2.

## Usage

First, we create a connection to the AllJoyn bus by creating a new instance of Tchaikovsky and calling the `connect()` method:

```
AllPlay allPlay = new AllPlay();
allPlay.connect();
```

Now, we register a listener to be notified about discovered speakers and start the discovery process:

```
allPlay.addSpeakerAnnouncedListener(new SpeakerAnnouncedListener() {
    @Override
    public void onSpeakerAnnounced(Speaker speaker) {
    }
});
allPlay.discoverSpeakers();
```

The `onSpeakerAnnounced()` method will be called whenever a Speaker is discovered. In order to be able to communicate with a discovered speaker, we register a listener (to be notified about speaker changes) and connect to the speaker:

```
speaker.addSpeakerChangedListener(mySpeakerChangedListener);
speaker.connect();
```

The `mySpeakerChangedListener` implements the `SpeakerChangedListener` interface. This interface has various method which are called if the state of the speaker changes.

To control the speaker, the speaker object has various method which can be called, e.g.

```
speaker.getPlaylist()
speaker.playItem("http://myfavouritewebradio.online")
speaker.volume().adjustVolume(10)
```

and many more. Just have a look at the Speaker interface.
