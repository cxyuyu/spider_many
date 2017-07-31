
package Get_douban;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-7-21.
 */

//每次先想好框架
//输入参数搜索，然后
public class Get_page implements Runnable {

    public static void get_page() {
        while (true) {
            try {
                String word = Words.remove(0);
                List<String> urls = new ArrayList<String>();
                Document document_zan = null;
                for (int i = 0; i < 134; i++) {
                    String ip_port = "";
                    try {
                        while (true) {
                            ip_port = Cheak_ip.get_cheakip();
                            if (ip_port == null){
                                Thread.sleep(10000);
                                continue;}
                            else if(!Cheak_ip.cheak_ip(ip_port))
                                continue;
                            else
                                break;
                        }
                        System.out.println("use ip"+ip_port);
                        String ip = "";
                        String port = "";
                        ip = ip_port.split(":")[0];
                        port = ip_port.split(":")[1];
                        System.getProperties().setProperty("http.proxyHost", ip);
                        System.getProperties().setProperty("http.proxyPort", port);
                        try {
                            word = URLEncoder.encode(word, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        Document document = Jsoup
                                .connect("https://movie.douban.com/subject_search?start=" + i * 15 + "&search_text=" + word + "&cat=1002")
                                .userAgent(rmb.get_useragent())
                                .timeout(5000)
                                .get();
                        Elements divs = document.select("#content > div > div.article > div:nth-child(2) > table");
                        for (Element div : divs) {
                            //#content > div > div.article > div:nth-child(2) > table:nth-child(2) > tbody > tr > td:nth-child(2) > div > a
                            String url = div.select("tr > td:nth-child(2) > div > a").attr("href");
                            String Urls[] = url.split("/");
                            String id = Urls[Urls.length - 1];
                            System.out.println(id);
                            if (isNumeric(id))
                                if (!id_have.contains(id)) {
                                    id_have.add(id);
                                    urls.add(id);
                                }
                        }
                        if (divs.size() == 0)
                            break;
                        Cheak_ip.ip_cheak.add(ip_port);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Words.add(word);
                    }
                }
                for (String id : urls)
                    Get_word.get_word(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //检查是否只含有数字
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void start_config() {
        //useragent换成电脑的
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
        for (int i = 0; i < 5; i++) {
            Thread one = new Thread(cheak_ip);
            one.start();
        }

    }


    public void run() {
        get_page();
    }

    static List<String> Words = Collections.synchronizedList(new ArrayList<String>());
    static List<String> id_have = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String args[]) {
        start_config();
        Words = Get_db.Try_baidu.read("/home/cxyu/Downloads/word");
        Get_page get_page = new Get_page();
        for (int i = 0; i < 100; i++) {
            Thread one = new Thread(get_page);
            one.start();
        }
    }

}
