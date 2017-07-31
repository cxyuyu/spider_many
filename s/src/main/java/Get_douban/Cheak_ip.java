package Get_douban;

import org.jsoup.Connection;
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
public class Cheak_ip implements Runnable {
    public static List<String> ip_cheak = Collections.synchronizedList(new ArrayList<String>());
    //ip不是使用一次就丢弃，而是保存，直到这个ip获取数据出错误为止

    public static Integer random_get(int max) {
        String user_agent = "";
        int min = 0;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;

        return s;
    }


    public static String get_cheakip() {
        try {
            if (ip_cheak.size() == 0) {
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                }
            } else {
                System.out.println("ip_cheak:" + ip_cheak.size());
                int x = random_get(ip_cheak.size());
                String ip = ip_cheak.remove(x);
                Thread.sleep(1000);
                //没次休息一秒以免访问过快，导致这个ip一下就不能使用
                return ip;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    public static boolean cheak_ip(String ip_port) {

        String ip = "";
        String port = "";
        ip = ip_port.split(":")[0];
        port = ip_port.split(":")[1];
        System.getProperties().setProperty("http.proxyHost", ip);
        System.getProperties().setProperty("http.proxyPort", port);

        //因为ip能使用一段时间，可以使用这个，来避免。
        Document document2 = null;
        try {
            document2 = Jsoup
                    .connect("http://ip.chinaz.com/getip.aspx")
                    .userAgent(rmb.get_useragent())
                    .timeout(8000)
                    .get();
            //本机则不使用此ip
            if (document2.text().contains("27.148.153")) {
                return false;
            }
            if (document2.text().contains("219.142")) {
                return false;
            }

        } catch (IOException e) {
            return false;
        }
        System.out.println("可以使用此ip" + ip_port);
        return true;

    }


    //有的ip不能使用
    public static void cheak_ip() {
        while (true) {
            try {
                if (ip_cheak.size() > 100)
                    Thread.sleep(2000);
                String ip_port = rmb.get_ip();
                while (true) {
                    String ip = "";
                    String port = "";
                    ip = ip_port.split(":")[0];
                    port = ip_port.split(":")[1];
                    System.getProperties().setProperty("http.proxyHost", ip);
                    System.getProperties().setProperty("http.proxyPort", port);

                    //因为ip能使用一段时间，可以使用这个，来避免。
                    Document document2 = null;
                    try {
                        document2 = Jsoup
                                .connect("http://ip.chinaz.com/getip.aspx")
                                .userAgent(rmb.get_useragent())
                                .timeout(8000)
                                .get();
                        System.out.println(document2.text());
                        //本机则不使用此ip
                        if (document2.text().contains("27.148.153")) {
                            ip_port = rmb.get_ip();
                            continue;
                        }
                        if (document2.text().contains("219.142")) {
                            ip_port = rmb.get_ip();
                            continue;
                        }
                        //检查二
                        document2 = Jsoup.connect("http://httpbin.org/get?show_env=1")
                                .timeout(8000)
                                .ignoreContentType(true)
                                .get();
                        if (document2.text().contains("219.142.69")) {
                            ip_port = rmb.get_ip();
                            continue;
                        }
                    } catch (IOException e) {
                        ip_port = rmb.get_ip();
                        continue;
                    }
                    break;
                }
                ip_cheak.add(ip_port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        cheak_ip();
    }

    public static void main(String args[]) {
        Get_page.start_config();
        String ip_port = "";
        while (true) {
            while (true) {
                ip_port = Cheak_ip.get_cheakip();
                if (ip_port == null) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else if (!Cheak_ip.cheak_ip(ip_port))
                    continue;
                else
                    break;
            }
            System.out.println("use ip" + ip_port);
            String ip = "";
            String port = "";
            ip = ip_port.split(":")[0];
            port = ip_port.split(":")[1];
            System.getProperties().setProperty("http.proxyHost", ip);
            System.getProperties().setProperty("http.proxyPort", port);
            try {
                //因为ip能使用一段时间，可以使用这个，来避免。
                Document document2 = null;
                document2 = Jsoup.connect("http://httpbin.org/get?show_env=1")
                        .timeout(8000)
                        .ignoreContentType(true)
                        .get();
                System.out.println("use ip" + ip_port + "   " + document2.text());
                if (document2.text().contains("219.142.69"))
                    System.out.println("包含了");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
