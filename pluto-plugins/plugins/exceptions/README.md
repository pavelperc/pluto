## Integrate Exceptions Plugin in your application


### Add Gradle Dependencies
Pluto Exceptions is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.pavelperc.pluto.plugins/exceptions). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation "com.pavelperc.pluto.plugins:exceptions:$plutoVersion"
  releaseImplementation "com.pavelperc.pluto.plugins:exceptions-no-op:$plutoVersion"
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoExceptionsPlugin())
  .install()
```
<br>

###  Set Global Exception Handler

To intercept uncaught exceptions in your app, attach `UncaughtExceptionHandler` to PlutoExceptions
```kotlin
PlutoExceptions.setExceptionHandler { thread, throwable ->
    Log.d("exception_demo", "uncaught exception handled on thread: " + thread.name, throwable)
}
```

To intercept & report potential ANRs in your app, attach `UncaughtANRHandler` to PlutoExceptions
```kotlin
PlutoExceptions.setANRHandler { thread, exception ->
    Log.d("anr_demo", "potential ANR detected on thread: " + thread.name, exception)
}
```

You can also modify the Main thread response time, after which the above callback will be triggered.
```kotlin
PlutoExceptions.mainThreadResponseThreshold = 10_000
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Exceptions plugin installed.

<br>

### Open Plugin view programmatically
To open Exceptions plugin screen via code, use this
```kotlin
Pluto.open(PlutoExceptionsPlugin.ID)
```
