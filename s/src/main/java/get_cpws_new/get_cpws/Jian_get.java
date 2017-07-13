package get_cpws_new.get_cpws;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-7-12.
 */
public class Jian_get {
    public static void main(String args[]) {
        try {


            Document document = Jsoup
                    .connect("http://panjueshu.com/map11854.html")
                    .userAgent("Mozilla/5.0 (Linux; U; Android 3.0.1; ja-jp; MZ604 Build/H.6.2-20) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13")
                    .timeout(50000)
                    .get();
            Elements lis=document.select("#content > ul > li");
            List<String> list=new ArrayList<String>();
            for(Element li:lis){
                System.out.println(li.text());
                if(li.text().contains("民事")&&!list.contains(li.text()))
                    list.add(li.text());


            }
            System.out.println(lis.size());
            System.out.println(list.size());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
