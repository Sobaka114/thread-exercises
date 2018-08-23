import java.util.concurrent.atomic.AtomicInteger;

public class ThreadExercises {
    private volatile static AtomicInteger i = new AtomicInteger(0);

    static class Incrementer implements Runnable {
        @Override
        public void run() {
            long id = Thread.currentThread().getId();
            for(int j = 0; j < 25; j++) {
                synchronized (i) {
                    try {
                        System.out.println("Thread = " + id + " j = " + j + " i = " + i);
                        Thread.sleep(5);
                        i.getAndIncrement();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class PingPong implements Runnable {
        @Override
        public void run() {
            while (i.get() < 100) {
                try {
                    turn();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void turn() throws InterruptedException {
            long id = Thread.currentThread().getId();
            synchronized (i) {
                if (i.get() % 2 == 0) {
                    System.out.println("Thread = " + id + " ping");
                    Thread.sleep(5);
                    i.notifyAll();
                } else {
                    System.out.println("Thread = " + id + " pong");
                    Thread.sleep(5);
                    i.notifyAll();
                }
                i.getAndIncrement();
            }
        }


    }

    public static void main(String[] args) throws InterruptedException {
        Thread th1 = new Thread(new Incrementer());
        Thread th2 = new Thread(new Incrementer());
        Thread th3 = new Thread(new Incrementer());
        Thread th4 = new Thread(new Incrementer());

        th1.start();
        th2.start();
        th3.start();
        th4.start();
        th3.join();
        th2.join();
        th1.join();
        th4.join();

        System.out.println("Incrementer finished. PingPong start");


        i.set(0);
        th1 = new Thread(new PingPong());
        th2 = new Thread(new PingPong());
        th1.start();
        th2.start();

    }
}
