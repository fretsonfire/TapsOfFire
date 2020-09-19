package app.tapsoffire.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import android.os.Process;
import android.os.SystemClock;

public class Simply {

    private static final Object WAITSLEEP_OBJECT = new Object();

    public static int elapsedUptimeMillis(long start) {
        return (int)(SystemClock.uptimeMillis() - start);
    }

    public static void setThreadPriority(int priority) {
        try {
            Process.setThreadPriority(priority);
        } catch (SecurityException e) {
            // do nothing
        }
    }

    public static void notify(Object object) {
        if (object != null) {
            synchronized (object) {
                object.notify();
            }
        }
    }

    public static void notifyAll(Object object) {
        if (object != null) {
            synchronized (object) {
                object.notifyAll();
            }
        }
    }

    public static boolean waitNoLock(Object object) {
        if (object == null) {
            return true;
        }

        try {
            object.wait();
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean wait(Object object) {
        if (object == null) {
            return true;
        }

        try {
            synchronized (object) {
                object.wait();
            }
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean wait(Object object, long timeout) {
        if (object == null) {
            return true;
        }

        try {
            synchronized (object) {
                object.wait(timeout);
            }
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean join(Thread thread) {
        if (thread == null) {
            return true;
        }

        try {
            thread.join();
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean waitSleep(int milliseconds) {
        return wait(WAITSLEEP_OBJECT, Math.max(1, milliseconds));
    }

    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }



}
