import java.util.ArrayList;
import java.util.List;

class ThreadCounter {
    private volatile Integer activeThreadCount;
    private int maxThreads;
    public List<Thread> threads;

    public ThreadCounter(int max) {
        maxThreads = max;
        activeThreadCount = 0;
        threads = new ArrayList<Thread>();
    }

    public synchronized boolean acquire(){
        if(activeThreadCount <= maxThreads) {
            activeThreadCount++;
            return true;
        }
        return false;
    }

    public synchronized void release() {
        activeThreadCount--;
    }

    public synchronized void addThread(Thread thread) {
        threads.add(thread);
    }

    public synchronized void removeThread(Thread thread) {
        threads.remove(thread);
    }

    public synchronized List<Thread> getThreads() {
        return threads;
    }
}