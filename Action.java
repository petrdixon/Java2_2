package Lesson5;

public class Action {
    static final int size = 10000000;
    static final int h = size / 2;
    float[] arr = new float[size];

    public void createFillArray(){
        for (int i = 0; i < arr.length; i++) arr[i] = (float) 1;
    }

    public void changeArray1(){
        createFillArray();
        long a = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++)
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        System.out.println("Метод 1, время только на цикл расчета: " + (System.currentTimeMillis() - a) + "\n");
    }

    public void changeArray2(){
        createFillArray();
        float[] a1 = new float[h];
        float[] a2 = new float[h];

        long a = System.currentTimeMillis();
        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);
        System.out.println("Метод 2, время разбивки массива на два: " + (System.currentTimeMillis() - a));

        Thread1 t1 = new Thread1(a1);
        Thread2 t2 = new Thread2(a2);
        long time = System.currentTimeMillis();
        t1.start();
        t2.start();
        try {
            t1.join();
            System.out.println("Метод 2, время просчета массива 1: " + (System.currentTimeMillis() - time));
            t2.join();
            System.out.println("Метод 2, время просчета массива 2: " + (System.currentTimeMillis() - time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long time2 = System.currentTimeMillis();
        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);
        System.out.println("Метод 2, время склейки: " + (System.currentTimeMillis() - time2));
        System.out.println("Метод 2, время итого: " + (System.currentTimeMillis() - a));
    }
}
