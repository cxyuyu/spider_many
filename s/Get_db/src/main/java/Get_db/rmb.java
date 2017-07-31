package Get_db;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by cxyu on 17-7-6.
 */
public class rmb implements Runnable {


    //多设置几个useragetn，然后max还没有设置
    public static String get_useragent() {
        String user_agent = "";
        int max = 7;
        int min = 0;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        user_agent = useragent.get(s);
        //System.out.println(user_agent);
        return user_agent;
    }


    //考虑爬失败,失败之后
    //少于120条就停止

    public static List<String> ip = Collections.synchronizedList(new ArrayList<String>());

    public static void ip_get() {
        while (true)
            try {
                if(ip.size()>400){
                    Thread.sleep(20000);
                    continue;
                }
                Document document = Jsoup
                        .connect("http://dec.ip3366.net/api/?key=20170706151152111&getnum=90&anonymoustype=3&area=1&proxytype=01")
                       // .connect("http://dec.ip3366.net/api/?key=20170706151152111&getnum=100&anonymoustype=4&proxytype=01")
                        .timeout(10000)
                        .get();
                System.out.println(document.body().text().length());
                String all_ip = document.select("body").text();

                if (all_ip.length() > 150) {
                    String[] all_ips = all_ip.split(" ");
                    //使用的时候再分割
                    for (String s : all_ips) {
                        ip.add(s);
                    }
                    change=0;
                }
                else{
                        try {
                            Thread.sleep(7000 * (++change));
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    System.out.println(ip.size());

                Thread.sleep(7000);
            } catch (Exception e) {

                try {
                    Thread.sleep(7000 * (++change));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }


    }

    public static void try_alldc(){
        while (true){
            try {
                Document document=Jsoup.connect("http://dec.ip3366.net/api/?key=20170706151152111&getnum=90&anonymoustype=3&area=1&proxytype=0").get();
                //System.out.println(document.text());
                System.out.println("cahngdu"+document.text().length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取ip，使ip能够实时更新,这样就不用创建另外一条线程了
    public static String get_ip() {
        String IP = "";
        if (ip.size() == 0) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        IP = ip.remove(0);
        System.out.println(IP);
        return IP;
    }


    public static int change = 0;
    //多线程
    //一秒钟获取一次
    //现在受限就卡在这里
    public static List<String> useragent = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String args[]) {

        rmb rmb = new rmb();
        for (int i = 0; i < 1; i++) {
            Thread one = new Thread(rmb);
            one.start();
        }
        //try_alldc();
        rmb.ip.remove(0);
    }

    public void run() {
        ip_get();
    }
}
