
package stationarylines;

public class StopwatchThread{
    
    private long startTime;
    private boolean started;

    public void startStopwatchThread()
    {
        this.startTime = System.currentTimeMillis();
    }

    public long getTime()
    {
        long milliTime = System.currentTimeMillis() - this.startTime;
        return milliTime;
    }

    public void stopStopwatchThread()
    {
        this.started = false;
    }
}
