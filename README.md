# robocode

This is a sample robocode repository with a really simple [robot](src/main/kotlin/robots/SarahRobot.kt) built in Kotlin.

You can import this robot into robocode by:
1. Cloning this repo
2. Running `gradle build` from this directory
3. Open the robocode application (download and install instructions [here](http://robowiki.net/wiki/Robocode/Download_And_Install)
4. Click `Robots` -> `Import robot or team`
3. Select the jar file created by the gradle build (should be in `/build/libs` in this directory)

Then when you start a battle, you should see `SarahRobot` under the `robots` package!
