package Get_db;

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
 * Created by cxyu on 17-7-19.
 */
public class Try_baidu implements Runnable {


    public static List<String> read(String filename) {
        List<String> words = new ArrayList<String>();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                tempString=tempString.split("\t")[1];
                words.add(tempString);
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static void get_page() {
        while (true) {
            try {

                String word = Words.remove(0);
                List<String> urls = new ArrayList<String>();
                Document document_zan=null;
                int ji=0;
                String ji_1="";
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
                                .connect("https://www.baidu.com/s?wd=site%3A(movie.douban.com)%20" + word + "&pn=" + i * 10 + "&oq=site%3A(movie.douban.com)%20" + word + "&ie=utf-8")
                                .userAgent(rmb.get_useragent())
                                .timeout(5000)
                                .get();
                        Cheak_ip.ip_cheak.add(ip_port);
                        Elements divs = document.select("#content_left > div");
                        for (Element div : divs) {
                            String url = div.select("div > div.f13 > a.m").attr("href");
                            urls.add(url);
                        }
                        if(ji==divs.size()||ji_1.equals(divs.get(0).text()))
                            break;
                        ji=divs.size();
                        ji_1=divs.get(0).text();
                        System.out.println(i);
                        Get_id.get_id(urls);
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



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

        Get_one get_one= new Get_one();
        for(int i=0;i<80;i++){
            Thread one = new Thread(get_one);
            one.start();
        }

    }

    static List<String> Ids = Collections.synchronizedList(new ArrayList<String>());
    static List<String> Ids_have = Collections.synchronizedList(new ArrayList<String>());
    static List<String> Words = Collections.synchronizedList(new ArrayList<String>());

    //cheak_ip没有进行修改
    //按斜杠切割，然后ip
    //修改到没有read time out的情况
    public static void main(String args[]) {
        Words = read("word");
        start_config();
        Try_baidu try_baidu=new Try_baidu();
        for(int i=0;i<20;i++) {
            Thread one = new Thread(try_baidu);
            one.start();
        }
    }

    public void run() {
        get_page();
    }
}