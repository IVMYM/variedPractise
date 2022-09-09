package com.example.variesdiffmayfuse.thread;

/**
 * @author liuyuan
 * @createdAt: 2022/9/9 16:33
 * @since: 1.0
 * @describe
 */
public class WaitNotify {
    private int num = 0;

    private synchronized void setNum() {
     try {
      //模拟线程执行时间
      Thread.sleep(100);
     } catch (InterruptedException e) {
      throw new RuntimeException(e);
     }
     num++;
     if (num == 3) {
      notify();
     }
    }

    private synchronized int getNum() {
     if(num<3){
      try {
       wait();
      } catch (InterruptedException e) {
       throw new RuntimeException(e);
      }
     }
        return num;
    }


 public static void main(String[] args) {
  WaitNotify st = new WaitNotify();
  new Thread(()->{
   System.out.println("current thread"+Thread.currentThread().getName());
   st.setNum();
  }).start();
  new Thread(()->{
   System.out.println("current thread"+Thread.currentThread().getName());
   st.setNum();
  }).start();
  new Thread(()->{
   System.out.println("current thread"+Thread.currentThread().getName());
   st.setNum();
  }).start();
  new Thread(() -> {
   st.getNum();
   System.out.println("num=="+st.num);
  }).start();

 }
}
