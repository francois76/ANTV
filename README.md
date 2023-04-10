# ANTV

This android project aims to make livestreams and replays of french "assemblée nationale" readable
on android phone

## Requiements

Android 8.0 or higher (some features requires android 12 or higher)

## Roadmap

* fixing bugs
* adding a credit screen for the use of assets from "assemblée nationale"
* adding a proper way to go back to current reading video from the homescreen
* adding an history
* Adding support for android auto
* Adding support for android TV
* adding integration with Eliasse
*

## Setting up your development environment

To setup the environment, please consult
these [instructions](https://github.com/JetBrains/compose-multiplatform-template#setting-up-your-development-environment)
.

## How to run

Choose a run configuration for an appropriate target in IDE and run it.

![run-configurations.png](screenshots/run-configurations.png)

## Run on desktop via Gradle

`./gradlew desktopApp:run`

### Building native desktop distribution

```
./gradlew :desktop:packageDistributionForCurrentOS
# outputs are written to desktop/build/compose/binaries
```

### Running Android application

- Get
  a [Google Maps API key](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
- Add to `local.properties` file located in the root directory (create if it doesn't exist):
    - `MAPS_API_KEY=YOUR_KEY` where `YOUR_KEY` is your key from previous step;
    - `sdk.dir=YOUR_SDK_PATH` where `YOUR_SDK_PATH` is a path to Android SDK in your system.
- Open project in IntelliJ IDEA or Android Studio and run `androidApp` configuration.

