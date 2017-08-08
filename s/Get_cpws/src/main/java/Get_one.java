import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-8-2.
 */
public class Get_one {
    public static void get_page(List<String> urls) {
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
                String content = "";
                Elements divs = document.select("#entry-cont > div");
                //#entry-cont
                int f_ji=0;
                for (Element div : divs){
                for( Element p: div.select("p"))
                {
                    if (p.text().substring(0, 1).equals("附")){
                        f_ji=1;
                        break;
                    }
                    content = content + "\n" + p.text();
                    content = content.replace("　", "");
                    content = content.replace(" ", "");
                }
                if(f_ji==1)
                    break;
                }




                String content_url=document.select("#bd_snap_note > a").attr("href");
                String content_title=document.select("#ht-kb > article > header > h2").text();
                String content_id=document.select("#ht-kb > article > header > ul > li.ht-kb-em-category").text();
                if(!content_title.contains("民事"))
                    continue;
                content = content.substring(1, content.length());
                //System.out.println(content);
                content = Content_handle.handle(content);
                content=content_id+"\n"+content;
                //System.out.println(content_title);


                appendMethodB("/home/cxyu/tmp/cpws/"+content_title,content_url+"\n"+ content_title+  "\n" + content);
                if(Cheak_ip.cheak_ip(ip_port))
                Cheak_ip.ip_cheak.add(ip_port);
            } catch (IOException e) {
                e.printStackTrace();
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


    public static void main(String args[]) {

        List<String> urls = new ArrayList<String>();
        urls.add("http://cache.baiducontent.com/c?m=9f65cb4a8c8507ed4fece763104b9531450ad3262bd7a74423c9cb0fc5310c1e103aa5a62675475084957e6500aa1e0beca43d23675021b3c8988d4189efc2282ad827626459db0144dc0edebb5154c737e05cfedc13f0ba8125e2afc5a7dc4322b944747a97f0fa4d701fdd1987&p=882a9545d4b108bc01be9b7c454b95&newp=9366860f81934eaf58efcb2d0214cd231610db2151d4d21f6b82c825d7331b001c3bbfb42324160fd5c27f6506aa435ee9f23278300527a3dda5c91d9fb4c57479d7&user=baidu&fm=sc&query=site%3A%28openlaw%2Ecn/%29+%CB%C4%B4%A8+%C3%F1%CA%C2+%B9%DE&qid=c3492afb00044870&p1=11");
        get_page(urls);
    }
}

