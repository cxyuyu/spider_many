package get_qyxx;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cxyu on 17-7-11.
 */
public class new_try {

    //输入一个词语获取关于这个词语的页面id
    public static void get_id(String word) throws IOException {
        String urlStr = URLEncoder.encode(
                word,"UTF-8");
        System.out.println(urlStr);
        Set<String> ids=new HashSet<String>();
        for(int i=0;i<4;i++){
            //ola是注册资本排序
            while (true){
            Document document= Jsoup.connect("http://bj.m.tianyancha.com/search/ola"+i+"/p2?key="+urlStr)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .timeout(10000)
                    .get();
            System.out.println(document.text().length());}
    }
    }


    //最后是做成id与获取页面分开的一个逻辑
    public static void main(String args[]){
        try {
//            Get_word.start_config();
//            String ip_port = rmb.get_ip();
//            String ip = "";
//            String port = "";
//            ip = ip_port.split(":")[0];
//            port = ip_port.split(":")[1];
//            System.getProperties().setProperty("http.proxyHost", ip);
//            System.getProperties().setProperty("http.proxyPort", port);




            //转换字符，将中文转为英文。
            String urlStr = URLEncoder.encode(
                    "漫画","UTF-8");
            System.out.println(urlStr);
            for(int i=0;i<4;i++){
                //ola是注册资本排序
            Document document= Jsoup.connect("http://bj.m.tianyancha.com/search/ola"+i+"/p2?key=%E6%BC%AB%E7%94%BB")
                    .userAgent("Baiduspider+(+http://www.baidu.com/search/spider.htm”)")
                    .timeout(10000)
                    .get();
            System.out.println(document.text());
            System.out.println(document.text().length());}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
