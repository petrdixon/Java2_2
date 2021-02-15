package Lesson5;

public class Thread2 extends Thread{
    private float[] a2;

    public Thread2(float[] a2){
        this.a2 = a2;
    }
    @Override
    public void run(){
        for (int i = 0; i < a2.length; i++)
            a2[i] = (float)(a2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
    }
}