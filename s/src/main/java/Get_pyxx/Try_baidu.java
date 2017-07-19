package Get_pyxx;

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
                                .connect("https://www.baidu.com/s?wd=site%3A(tianyancha.com)%20" + word + "&pn=" + i * 10 + "&oq=site%3A(tianyancha.com)%20" + word + "&ie=utf-8")
                                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                                .timeout(5000)
                                .get();
                        if(document==document_zan)
                            break;
                        Elements divs = document.select("#content_left > div");
                        for (Element div : divs) {
                            String url = div.select("div > div.f13 > a.m").attr("href");
                            System.out.println("https://www.baidu.com/s?wd=site%3A(tianyancha.com)%20" + word + "&pn=" + i * 10 + "&oq=site%3A(tianyancha.com)%20" + word + "&ie=utf-8"+"   url        "+url);
                            urls.add(url);

                        }
                        Get_word.get_word(urls);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Cheak_ip.ip_cheak.remove(ip_port);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    static List<String> Words = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String args[]) {
        Words = read("word");
        Get_word.start_config();
        Try_baidu try_baidu=new Try_baidu();
        for(int i=0;i<100;i++){
            Thread one=new Thread(try_baidu);
            one.start();
        }

    }

    public void run() {
        get_page();
    }
}
