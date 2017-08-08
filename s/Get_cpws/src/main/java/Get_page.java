import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-8-2.
 */
public class Get_page implements Runnable{


    public static void start_config() {
        rmb.useragent.add(" Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        rmb.useragent.add("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        rmb.useragent.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        rmb.useragent.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)");
        rmb.useragent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
        rmb.useragent.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv,2.0.1) Gecko/20100101 Firefox/4.0.1");
        rmb.useragent.add("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11");
        rmb.useragent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)");


        rmb rmb = new rmb();
        for (int i = 0; i < 1; i++) {
            Thread one = new Thread(rmb);
            one.start();
        }

        Cheak_ip cheak_ip = new Cheak_ip();
        for (int i = 0; i < 10; i++) {
            Thread one = new Thread(cheak_ip);
            one.start();
        }
    }

    public static void get_page() {
        while (true) {
            try {

                String word = Words.remove(0);

                List<String> urls = new ArrayList<String>();
                Document document_zan=null;
                String ji="";
                for (int i = 0; i < 76; i++) {
                    String ip_port = "";
                    try {

                        while (true) {
                            ip_port = Cheak_ip.get_cheakip();
                            if (ip_port == null)
                                continue;
                            else
                                break;
                        }
                        String ip = "";
                        String port = "";
                        ip = ip_port.split(":")[0];
                        port = ip_port.split(":")[1];
                        System.getProperties().setProperty("http.proxyHost", ip);
                        System.getProperties().setProperty("http.proxyPort", port);
                        Document document = Jsoup
                                .connect("https://www.baidu.com/s?wd=site%3A(openlaw.cn%2F)%20四川%20民事%20"+word+"&pn="+i*10+"&oq=site%3A(openlaw.cn%2F)%20四川%20民事%20"+word)
                                .userAgent(rmb.get_useragent())
                                .timeout(5000)
                                .get();
                        String first_tag_url=document.select("#content_left > div > h3 > a").text();
                        if(document==document_zan||ji.equals(first_tag_url))

                            break;
                        Elements divs = document.select("#content_left > div");
                        for (Element div : divs) {
                            String url = div.select("div.f13 > a.m").attr("href");
                            // h3 > a
                            //               System.out.println("https://www.baidu.com/s?wd=site%3A(tianyancha.com)%20" + word + "&pn=" + i * 10 + "&oq=site%3A(tianyancha.com)%20" + word + "&ie=utf-8"+"   url        "+url);
                            System.out.println(url);
                            urls.add(url);

                        }
                        document_zan=document;
                        ji=first_tag_url;
                        Get_one.get_page(urls);
                        if(Cheak_ip.cheak_ip(ip_port))
                        Cheak_ip.ip_cheak.add(ip_port);
                        System.out.println(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static List<String> read(String filename) {
        List<String> words = new ArrayList<String>();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                words.add(tempString);
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public void run() {
        get_page();
    }
    static List<String> Words = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String args[]) {

        //baidu页面的遍历
        start_config();
        Words = read("ci");
        for(int i=0;i<100;i++){
            Get_page get_page=new Get_page();
            Thread one=new Thread(get_page);
            one.start();
        }
    }


}
