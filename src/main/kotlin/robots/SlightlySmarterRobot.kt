package robots

import robocode.*
import java.awt.Color
import robocode.WinEvent
import robocode.util.Utils.normalRelativeAngleDegrees
import robocode.HitRobotEvent
import robocode.ScannedRobotEvent





class SlightlySmarterRobot : Robot() {
    var count = 0 // Keeps track of how long we've
    // been searching for our target
    var gunTurnAmt = 0.0 // How much to turn our gun when searching
    var trackName: String? = null // Name of the robot we're currently tracking

    /**
     * run:  Tracker's main run function
     */
    override fun run() {
        // Set colors
        setBodyColor(Color(128, 128, 50))
        setGunColor(Color(50, 50, 20))
        setRadarColor(Color(200, 200, 70))
        setScanColor(Color.white)
        setBulletColor(Color.blue)

        // Prepare gun
        trackName = null // Initialize to not tracking anyone
        setAdjustGunForRobotTurn(true) // Keep the gun still when we turn
        gunTurnAmt = 10.0 // Initialize gunTurn to 10

        // Loop forever
        while (true) {
            // turn the Gun (looks for enemy)
            turnGunRight(gunTurnAmt)
            // Keep track of how long we've been looking
            count++
            // If we've haven't seen our target for 2 turns, look left
            if (count > 2) {
                gunTurnAmt = -10.0
            }
            // If we still haven't seen our target for 5 turns, look right
            if (count > 5) {
                gunTurnAmt = 10.0
            }
            // If we *still* haven't seen our target after 10 turns, find another target
            if (count > 11) {
                trackName = null
            }
        }
    }

    /**
     * onScannedRobot:  Here's the good stuff
     */
    override fun onScannedRobot(e: ScannedRobotEvent?) {
        if (e == null) {
            return
        }

        // If we have a target, and this isn't it, return immediately
        // so we can get more ScannedRobotEvents.
        if (trackName != null && e.name != trackName) {
            return
        }

        // If we don't have a target, well, now we do!
        if (trackName == null) {
            trackName = e.name
            out.println("Tracking $trackName")
        }
        // This is our target.  Reset count (see the run method)
        count = 0
        // If our target is too far away, turn and move toward it.
        if (e.distance > 150.0) {
            gunTurnAmt = normalRelativeAngleDegrees(e.bearing + (heading - radarHeading))

            turnGunRight(gunTurnAmt) // Try changing these to setTurnGunRight,
            turnRight(e.bearing) // and see how much Tracker improves...
            // (you'll have to make Tracker an AdvancedRobot)
            ahead(e.distance - 140)
            return
        }

        // Our target is close.
        gunTurnAmt = normalRelativeAngleDegrees(e.bearing + (heading - radarHeading))
        turnGunRight(gunTurnAmt)
        fire(3.0)

        // Our target is too close!  Back up.
        if (e.distance < 100) {
            if (e.bearing > -90 && e.bearing <= 90) {
                back(40.0)
            } else {
                ahead(40.0)
            }
        }
        scan()
    }

    /**
     * onHitRobot:  Set him as our new target
     */
    override fun onHitRobot(e: HitRobotEvent?) {
        // Only print if he's not already our target.
        if (trackName != null && trackName != e!!.name) {
            out.println("Tracking " + e.name + " due to collision")
        }
        // Set the target
        trackName = e!!.name
        // Back up a bit.
        // Note:  We won't get scan events while we're doing this!
        // An AdvancedRobot might use setBack(); execute();
        gunTurnAmt = normalRelativeAngleDegrees(e.bearing + (heading - radarHeading))
        turnGunRight(gunTurnAmt)
        fire(3.0)
        back(50.0)
    }

    /**
     * onWin:  Do a victory dance
     */
    override fun onWin(e: WinEvent?) {
        for (i in 0..49) {
            turnRight(30.0)
            turnLeft(30.0)
        }
    }
}