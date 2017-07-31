package Get_db;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-7-21.
 */
public class Try_Baidu implements Runnable {

    public static void get_page(String word) {
        for (int i = 0; ; i++) {
            String ip_port = "";
            try {
                Document document = Jsoup
                        .connect("https://movie.douban.com/subject_search?start="+i*15+"&search_text="+word+"&cat=1002")
                        .userAgent("")
                        .timeout(5000)
                        .get();
                Elements divs = document.select("#content > div > div.article > div:nth-child(2) > table");
                for (Element div : divs) {
                    //#content > div > div.article > div:nth-child(2) > table:nth-child(2) > tbody > tr > td:nth-child(2) > div > a
                    String url = div.select("tr > td:nth-child(2) > div > a").attr("href");
                    String Urls[]=url.split("/");
                    String id=Urls[Urls.length-1];
                    System.out.println(id);
                    if(isNumeric(id))
                        urls.add(id);
                }

                if(divs.size()==0)
                    break;

            } catch (IOException e) {
                e.printStackTrace();
                Cheak_ip.ip_cheak.remove(ip_port);
            }
        }
    }
    //检查是否只含有数字
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static void get(String url){
        try {
            Document document = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .timeout(5000)
                    .get();

            System.out.println(document.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<String> urls= Collections.synchronizedList(new ArrayList<String>());
    public static void main(String args[]) {
        Try_Baidu try_baidu=new Try_Baidu();
        for(int i=0;i<1;i++)
        {
            Thread thread=new Thread(try_baidu);
            thread.start();
        }
        List<String> list=new ArrayList<String>();
        //list = Try_baidu.read("/home/cxyu/Downloads/word");
        list.add("中国");
        for(String word:list)
        get_page(word);

    }

    public void run() {
        while (true) {
            try {
            if(urls.size()==0)
                Thread.sleep(10000);
                get(urls.remove(0));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
