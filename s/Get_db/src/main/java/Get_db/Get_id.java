package Get_db;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by cxyu on 17-7-31.
 */
public class Get_id {
    public static boolean get_id_2(String url){
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
            Elements links = document.select("a[href]");
            for (Element link : links)
                if (link.text().contains("subject")) {
                    String Url = link.text();
                    String ID = get_ids(Url);
                    if (ID != null) {
                        System.out.println("get id"+ID);
                        //添加id
                        if(!Try_baidu.Ids_have.contains(ID)){
                            Try_baidu.Ids.add(ID);
                            Try_baidu.Ids_have.add(ID);
                        }
                    }
                }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }


    public static void get_id(List<String> urls) {
        for (String url : urls) {
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
                        .connect(url)
                        .userAgent(rmb.get_useragent())
                        .timeout(5000)
                        .get();
                Cheak_ip.ip_cheak.add(ip_port);

                Elements links = document.select("a[href]");
                for (Element link : links)
                    if (link.text().contains("subject")) {
                        String Url = link.text();
                        String ID = get_ids(Url);
                        if (ID != null) {
                            System.out.println("get id"+ID);
                            //添加id
                            if(!Try_baidu.Ids_have.contains(ID)){
                                Try_baidu.Ids.add(ID);
                                Try_baidu.Ids_have.add(ID);
                            }
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
                if (e.fillInStackTrace().toString().contains("HTTP error fetching URL") ||
                        e.fillInStackTrace().toString().contains("Connection reset")
                        || e.fillInStackTrace().toString().contains("Read timed out"))
                    for (int i = 0; i < 3; i++) {
                        if(get_id_2(url))
                            break;
                    }
            }
        }
    }



    public static String get_ids(String url){
        String urls[]=url.split("\\/");
        for(String a:urls)
        {
            if(isNumeric(a))
                return a;
        }

        return null;
    }
    //检查是否为数字
    public static boolean isNumeric(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        if(str.length()<1)
            return false;
        return true;
    }
}
