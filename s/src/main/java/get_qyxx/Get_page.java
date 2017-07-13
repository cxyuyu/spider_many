package get_qyxx;

import get_cpws_new.get_cpws.rmb;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-7-11.
 */
public class Get_page implements Runnable {
    public static void get_page(String word) {
        String[] where = {"bj", "zj", "snx", "js", "hen", "heb", "gd", "ah", "sd"
                , "ln", "hun", "hub", "hlj", "sx", "jx", "tj", "cq", "fj", "sh", "sc"
                , "gs", "qh", "nx", "jl", "gx", "gz", "han", "yn", "xz", "zj", "nmg",};
        //设置地名，更精细地搜索有利于速度的提升。


        List<String> urls = new ArrayList<String>();
        try {
            word = URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (String a : where) {
            for (int i = 0; i < 4; i++)
                try {


                    String url = "http://" + a + ".m.tianyancha.com/search/ola" + i + "/p?key=" + word;
                    //之后的链接到哪一页为止交给下一个产生id的函数。
                    //它的分割用？来分割，已经有p
                    urls.add(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        product_id(urls);


    }


    public static void product_id(List<String> urls) {
        for (String url : urls)
            for (int i = 1; i<6; i++) {
                try {
                    String url1 = url.split("\\?")[0];
                    String url2 = url.split("\\?")[1];
                    String new_url = url1 + i+"?" + url2;

                    String ip_port = rmb.get_ip();
                    //一个ip用4次，防止因为只有一次就对ip设限制
                    int lentgh = 0;
                    for (int s = 0; s < 4; s++) {
                        lentgh = product_id(ip_port, new_url);
                        if (lentgh!=0)
                            break;

                    }
                    System.out.println(lentgh+"     "+new_url);
                    //当小于100时，就停止循环
                    if(lentgh< 100)
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }


    //返回这个页面的大小，错误就返回0,防止一个ip访问次数出现的问题
    public static Integer product_id(String ip_port, String new_url) {
        int length=0;
        try {
            String ip = "";
            String port = "";
            ip = ip_port.split(":")[0];
            port = ip_port.split(":")[1];
            System.getProperties().setProperty("http.proxyHost", ip);
            System.getProperties().setProperty("http.proxyPort", port);

            Document document = Jsoup
                    .connect(new_url)
                    .userAgent(rmb.get_useragent())
                    .timeout(10000)
                    .get();
            length=document.text().length();
            //不进行下面的步骤,当页面的大小小于100时
            if(length<100)
                return length;
            //通过判断这个参数有没有，来
            Elements divs=document
                    .select("#ng-view > div.webPadding.ng-scope > div > div > div > div.b-c-white.search_result_container > div");
            //#ng-view > div.webPadding.ng-scope > div > div > div > div.b-c-white.search_result_container
            for(Element s:divs)
                System.out.println(s.text());
            System.out.println(document.text());
            System.out.println("elements  "+divs.size());

            //提取id
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        return length;
    }


    public void run() {

    }

    //在Get——Word的startconfig中让其获取赋值。
    static List<String> word = Collections.synchronizedList(new ArrayList<String>());
    //以一个词语链为基础，然后附加成链接，循环也是以词语为基础


    public static void main(String args[]) {
        Get_word.start_config();
        get_page("漫画");
    }


}
