package Get_db;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-7-31.
 */
public class Get_one implements Runnable {
    public static void get_content(String url,List<String> one){
        try {
            String ip_port = "";
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
                    .connect(url)
                    .userAgent(rmb.get_useragent())
                    .timeout(5000)
                    .get();
            Cheak_ip.ip_cheak.add(ip_port);
            Elements divs = document.select("#comments > div");
            for (Element div : divs) {
                //#comments > div:nth-child(1) > div.comment > p
                String how = "";
                String content = div.select("div.comment > p").text();
                Elements spans = div.select("div.comment > h3 > span.comment-info > span");
                if (spans.size() == 3) {
                    //需要符合一些字符，力荐，推荐，还行，较差，
                    how = spans.get(1).attr("title");
                }
                if(content.length()>1){
                    if(!one.contains(content + "\t||\t" + how))
                    one.add(content + "\t||\t" + how);}
            }
        }
        catch (Exception e){

        }
    }


    public static void save_contents(List<String> one,String id){

        //保存
        String content="";
        for(int i=0;i<one.size();i++){
            content=content+"\n"+one.get(i);
        }
        content=content.substring(1,content.length());
        appendMethodB("/home/cxyu/tmp/db/"+id,content);
    }

    public static void get_one() {
        while (true) {
            try {
                if(Try_baidu.Ids.size()==0)
                    Thread.sleep(10000);
                String id = Try_baidu.Ids.remove(0);
                String ip_port = "";
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
                        .connect("https://www.baidu.com/s?q1=&q2=comments++subject+" + id + "&q3=&q4=&gpc=stf&ft=&q5=2&q6=movie.douban.com&tn=baiduadv")
                        .userAgent(rmb.get_useragent())
                        .timeout(5000)
                        .get();
                Cheak_ip.ip_cheak.add(ip_port);
                Elements divs = document.select("#content_left > div");
                List<String> one=new ArrayList<String>();
                for (int i=0;i<divs.size();i++) {
                    Element div=divs.get(i);
                    String url = div.select("div > div.f13 > a.m").attr("href");
                    get_content(url,one);
                }
                save_contents(one,id);
            } catch (Exception e) {

            }
        }
    }

    public static void appendMethodB(String fileName, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        get_one();
    }
}
