## Integrate Shared Preferences Plugin in your application


### Add Gradle Dependencies
Pluto Shared Preferences is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.pavelperc.pluto.plugins/preferences). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation "com.pavelperc.pluto.plugins:preferences:$plutoVersion"
  releaseImplementation "com.pavelperc.pluto.plugins:preferences-no-op:$plutoVersion"
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoSharePreferencesPlugin())
  .install()
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Shared Preferences plugin installed.

<br>

### Open Plugin view programmatically
To open Shared Preferences screen via code, use this
```kotlin
Pluto.open(PlutoSharePreferencesPlugin.ID)
```
