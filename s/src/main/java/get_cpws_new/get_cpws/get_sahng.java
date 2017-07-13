package get_cpws_new.get_cpws;

import get_qyxx.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by cxyu on 17-7-12.
 */
public class get_sahng implements Runnable{


    public static void get_page(){
        //Get_word.start_config();
        while (true)
        try {
//            String ip = "";
//            String port = "";
//            ip = ip_port.split(":")[0];
//            port = ip_port.split(":")[1];
//            System.getProperties().setProperty("http.proxyHost", ip);
//            System.getProperties().setProperty("http.proxyPort", port);

            System.out.println("getin");
            Document document= Jsoup
                    .connect("http://www.wdpai.com/a/msal/list_15_"+page+".html")
                    .timeout(5000)
                    .get();
            Elements as=document
                    .select("#content > div > div.content_main > article > ul > font > span > a");


            for(Element a:as){

                get_word(a.attr("href"));
            }
            if(page==313)
                break;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void get_word(String url){
        try {
            Document document= Jsoup
                    .connect(url)
                    .timeout(5000)
                    .get();
            Element span=document
                    .select("#content > div > div.content_main > article > font > span").first();
            System.out.println("jinru");

            String temp = span.toString().replace("<br>", "\n");
            temp=temp.replace("<a href=\"http://www.wdpai.com/\">上海讨债公司</a>","");
            temp=temp.replace("</span>","");
            System.out.println(temp);
            Thread.sleep(10000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        get_page();
    }

    public static int page=1;
    public static void main(String args[]){
        get_word("http://www.wdpai.com/a/msal/67911.html");


        for(int i=0;i<1;i++) {
            get_sahng get_sahng = new get_sahng();
            Thread one = new Thread(get_sahng);
            one.start();
        }


    }

}
