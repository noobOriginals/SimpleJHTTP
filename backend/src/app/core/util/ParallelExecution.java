package app.core.util;

public class ParallelExecution implements Runnable {
    private MethodCallback<Void> callback;
    private Thread thread;
    private boolean isDone = true;

    public ParallelExecution(MethodCallback<Void> callback) {
        this.callback = callback;
    }

    public void execute() {
        thread = new Thread(this);
        thread.start();
    }
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void run() {
        isDone = false;
        callback.runMethod();
        isDone = true;
    }
}
