package Get_douban;

import Get_db.Get_word_one;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cxyu on 17-7-21.
 */
public class Get_word {


    //访问五次豆瓣首页,只要一次就表示可以使用
    //true表示不可以用了
    //false表示可以使用
    public static Boolean IP_cheak(String ip_port) {
        for (int i = 0; i < 5; i++)
            try {
                Document document = Jsoup.connect("https://movie.douban.com/")
                        //.userAgent(rmb.get_useragent())
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .get();


            return true;
            } catch (Exception e) {

            }
        return false;
    }

    //通过id获取页面。
    public static void get_word(String id) {
        List<String> one = new ArrayList<String>();
        for (int i = 0;i<11 ; i++) {
            String ip_port="";
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
                String ip = "";
                String port = "";
                ip = ip_port.split(":")[0];
                port = ip_port.split(":")[1];
                System.getProperties().setProperty("http.proxyHost", ip);
                System.getProperties().setProperty("http.proxyPort", port);
                Document document = Jsoup.connect("https://movie.douban.com/subject/" + id + "/comments?start=" + i * 20 + "&limit=20&sort=new_score&status=P")
                        .userAgent(rmb.get_useragent())
                        .get();
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
                    System.out.println(content + "\t||\t" + how);
                    one.add(content + "\t||\t" + how);}
                }
                Cheak_ip.ip_cheak.add(ip_port);
                if (divs.size() == 0)
                    break;
            } catch (Exception e) {
                //403,
                if(IP_cheak(ip_port))
                    break;
                else{
                    i--;
                    Cheak_ip.ip_cheak.add(ip_port);
                }
                e.printStackTrace();
            }
        }

        //保存
        String content="";
        for(int i=0;i<one.size();i++){
            content=content+"\n"+one.get(i);
        }
        content=content.substring(1,content.length());
        Get_word_one.appendMethodB("/home/cxyu/tmp/db/"+id,content);

    }

    public static void main(String args[]) {
        get_word("4876722");
    }
}
