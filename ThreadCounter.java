class ThreadCounter {
    private volatile Integer activeThreadCount;
    private int maxThreads;
    public ThreadCounter(int max) {
        maxThreads = max;
        activeThreadCount = 0;
    }

    public synchronized boolean acquire(){
        if(activeThreadCount < maxThreads) {
            activeThreadCount++;
            return true;
        }
        return false;
    }

    public synchronized void release() {
        activeThreadCount--;
    }
}