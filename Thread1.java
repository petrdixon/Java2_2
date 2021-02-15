package Lesson5;

public class Thread1 extends Thread{
    private float[] a1;

    public Thread1(float[] a1){
        this.a1 = a1;
    }
        @Override
        public void run(){
            for (int i = 0; i < a1.length; i++)
                a1[i] = (float)(a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
}
