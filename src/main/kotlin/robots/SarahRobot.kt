package robots

import robocode.*

class SarahRobot : Robot() {
    override fun run() {
        while (true) {
            turnRight(5.0)
        }
    }

    override fun onScannedRobot(event: ScannedRobotEvent?) {
        fire(1.0)
    }
}