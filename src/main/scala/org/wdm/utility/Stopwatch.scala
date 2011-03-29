package org.wdm.utility

/**
 * A quick stopwatch implementation used to time
 * the execution of blocks of code.
 */
class Stopwatch {

    case class WatchTime(var start:Long, var stop:Long) {
        def this(current:Long) = this(current, current)
    }
    
    private var time:WatchTime = new WatchTime(0L)

    def start = time = new WatchTime(System.currentTimeMillis)
    def stop  = time.stop = System.currentTimeMillis
    def reset = time = new WatchTime(0L)
    def restart = time = new WatchTime(System.currentTimeMillis)
    def elapsed = time.stop - time.start
    override def toString = elapsed/ 1000.0 + "s"
}

object Stopwatch {

    /**
     * Creates a new Stopwatch, starts it, and returns the instance
     */
    def startNew:Stopwatch = {
        val watch = new Stopwatch()
        watch.start
        watch
    }
}
