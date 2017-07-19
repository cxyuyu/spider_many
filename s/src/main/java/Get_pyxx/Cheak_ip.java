package Get_pyxx;

import get_cpws_new.get_cpws.rmb;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by cxyu on 17-7-19.
 */
public class Cheak_ip implements  Runnable{
    public static List<String> ip_cheak = Collections.synchronizedList(new ArrayList<String>());
    //ip不是使用一次就丢弃，而是保存，直到这个ip获取数据出错误为止

    public static Integer random_get(int max) {
        String user_agent = "";
        int min = 0;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;

        return s;
    }


    public static String get_cheakip(){
        if(ip_cheak.size()==0){
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {

            }
        }
        else{
            System.out.println("ip_cheak  "+ip_cheak.size());
            return ip_cheak.get(random_get(ip_cheak.size()));
        }
        return null;
    }

    //有的ip不能使用
    public static void  cheak_ip(){
        while (true){
            try {
            if(ip_cheak.size()>100)
                Thread.sleep(2000);
        String ip_port= rmb.get_ip();
        while (true){
            String ip = "";
            String port = "";
            ip = ip_port.split(":")[0];
            port = ip_port.split(":")[1];
            System.getProperties().setProperty("http.proxyHost", ip);
            System.getProperties().setProperty("http.proxyPort", port);

            //因为ip能使用一段时间，可以使用这个，来避免。
            Document document2= null;
            try {
                document2 = Jsoup
                        .connect("http://ip.chinaz.com/getip.aspx")
                        .userAgent(rmb.get_useragent())
                        .timeout(8000)
                        .get();
                System.out.println(document2.text());
                //本机则不使用此ip
                if(document2.text().contains("27.148.153")){
                    ip_port=rmb.get_ip();
                    continue;
                }
            } catch (IOException e) {

                ip_port=rmb.get_ip();
                continue;
            }
            break;
        }
        ip_cheak.add(ip_port);}
        catch (Exception e){
                e.printStackTrace();
        }
        }
    }
    public void run() {
        cheak_ip();
    }
}
