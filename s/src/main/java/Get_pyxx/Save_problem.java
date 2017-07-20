package Get_pyxx;


import java.util.*;

/**
 * Created by cxyu on 17-7-19.
 */
public class Save_problem implements Runnable {

    public static void init()  {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                save();
                re_e();
            }
        }, 1000, 60000);//在1秒后执行此任务,每次间隔2秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.

    }

    public static void re_e(){
        String content="";
        List<String> urls=new ArrayList<String>();
        while (true){
            if(Get_word.URL_e.size()==0)
                break;
            urls.add(Get_word.URL_e.remove(0));
        }
        for(String url:urls){
            content=content+"\n"+url;
        }
        //最前端保持一个换行，防止最后粘连在一起
        Get_word.appendMethodB2("wen",content);
    }

    public static void save(){
        String content="";
        List<String> urls=new ArrayList<String>();
        while (true){
            if(Get_word.URL_p.size()==0)
                break;
            urls.add(Get_word.URL_p.remove(0));
        }
        for(String url:urls){
            content=content+"\n"+url;
        }
        //最前端保持一个换行，防止最后粘连在一起
        Get_word.appendMethodB2("za",content);
    }
    public void run() {
        save();
    }

    public static void main(String args[]){
        init();
    }
}
