package get_cpws_new.get_cpws;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-7-6.
 */
public class Get_word_2 implements Runnable {

    public static int get_page(String ip_port) throws Exception {
        //传给下面也是，
        //要有标记
        List<String> ids = new ArrayList<String>();
        int id = page++;
        //设置ip，还没有设置
        String ip = "";
        String port = "";
        ip = ip_port.split(":")[0];
        port = ip_port.split(":")[1];
        System.getProperties().setProperty("http.proxyHost", ip);
        System.getProperties().setProperty("http.proxyPort", port);

        Document document = Jsoup
                .connect("http://panjueshu.com/map" + id + ".html")
                .userAgent(rmb.get_useragent())
                .timeout(50000)
                .get();
        //Thread.sleep(5000);
        //http://www.panjueshu.com/wenshu/9e913e7dff81750b.html
        //http://m.panjueshu.com//wenshu/73120a546a182b61.html
        //http://www.panjueshu.com/wenshu/73120a546a182b61.html
        //如果获取失败则重新获取四次
        if(document.title().equals("请输入验证码-判决书")||document.text().length()<200)
            //每个ip用三遍
            for(int d=0;d<3;d++){
                if(d==1)
                {
                    System.out.println(document.title());
                    ip_port = rmb.get_ip();
                    ip = ip_port.split(":")[0];
                    port = ip_port.split(":")[1];
                }
                for(int ji=0;ji<4;ji++){
                    //一旦失败就重新设置ip
                    System.getProperties().setProperty("http.proxyHost", ip);
                    System.getProperties().setProperty("http.proxyPort", port);
                    document = Jsoup
                            //.connect("http://openlaw.cn/search/judgement/type?causeId=270cfcd1df47453d9ff4b8d40901a587&selected=true&page="+id+".html")
                            .connect("http://panjueshu.com/map" + id + ".html")
                            .userAgent(rmb.get_useragent())
                            .timeout(50000)
                            .get();
                    if(document.text().length()>200)
                        break;
                }
                if(document.text().length()>200)
                    break;
            }


        Elements lis = document.select("#content > ul > li");
        for (Element li : lis) {
            String url = li.select("a").attr("href");
            if(li.text().contains("民事")&&!all_url.contains(url)){
            all_url.add(url);
            ids.add("http://m.panjueshu.com"+url);}
        }
        get_word(ids,ip,port,""+id);
        return document.text().length();
    }

    public static void get_page() {

        while (true)
            try {
                String ip_port = rmb.get_ip();
                //让同一个ip循环三次以上，防止他的tcp
                for(int i=0;i<4;i++)
                try{
                int length=get_page(ip_port);
                if(length>200)
                    break;
                }
                catch (Exception e){
                    e.printStackTrace();
                    page--;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //页面肯定可以获取
            }
    }

    //保存两份，一份是documt一份是提取的文档。
    //不能保存两份，因为两份会导致硬盘一下就满了
    //id是页数
    public static void get_word(List<String> urls, String ip, String port,String id) {
        for (int i=0;i<urls.size();i++)
            try {
                String url=urls.get(i);
                url=url.replace("www","m");
                System.getProperties().setProperty("http.proxyHost", ip);
                System.getProperties().setProperty("http.proxyPort", port);

                Document document = Jsoup.connect(url)
                        .userAgent(rmb.get_useragent())
                        .timeout(20000)
                        .get();
                //如果获取失败则重新获取四次
                if(document.title().equals("请输入验证码-判决书")||document.text().length()<200)
                    for(int d=0;d<3;d++) {
                        if(d==1)
                        {
                            System.out.println(document.title());
                            String ip_port = rmb.get_ip();
                            ip = ip_port.split(":")[0];
                            port = ip_port.split(":")[1];
                        }
                        for (int ji = 0; ji < 4; ji++) {
                            //一旦失败就重新设置ip
                            System.getProperties().setProperty("http.proxyHost", ip);
                            System.getProperties().setProperty("http.proxyPort", port);
                            document = Jsoup.connect(url)
                                    .userAgent(rmb.get_useragent())
                                    .timeout(20000)
                                    .get();
                            if (document.text().length() > 200)
                                break;
                        }
                        if(document.text().length()>200)
                            break;


                }
                if(document.text().length()<200)
                    continue;


                String title=document.select("body > div.details-box > div.details-title.tc").text();
                //body > div.details-box > div.details-title.tc
                //body > div.d.cl > div.l > div.icon-con > div.mgt
                String where=document.select("body > div.details-box > div.details-container-title.tc").text();
                //body > div.details-box > div.details-container-title.tc
                //
                Elements ps=document.select("body > div.details-box > div.details-container > p");
                //body > div.details-box > div.details-container
                String content="";
                for(Element p:ps){
                    content=content+"\n"+p.text();

                }
//                System.out.println(url);
//                System.out.println(content);
                content=content.substring(1,content.length());
                content=where+"\n"+content;
                //System.out.println(content);

                appendMethodB("/home/cxyu/tmp/cpws_wei/"+id+"-"+i,url+"\n"+ title+  "\n" + content);

                content= Content_handle.handle(content);




                if(content==null)
                    continue;
                content =url+"\n"+ title+  "\n" + content ;
                content=content.replace("\n","\t");
                appendMethodB("/home/cxyu/tmp/cpws/"+id+"-"+i,content);
            }
            catch (Exception e){
            e.printStackTrace();
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

    //3获取到了211000
    //4重新开始，因为3没有拿到普通数据
    //一天加一晚上可以21万
    //4已经到41万，7.9晚上
    //一天可以40万
    //总共100万
    //还有60万，
    //4也就70万开始
    public static int page = 260;
    static List<String> all_url= Collections.synchronizedList(new ArrayList<String>());
    //一条线程可以独立运行
    public void run() {
        get_page();
    }



    //国外的能用吗
    //不匿名的能用吗
    //ip数量是不是有限的
    public static void main(String args[]) {

        rmb.useragent.add("Mozilla/5.0 (Linux; U; Android 3.0.1; ja-jp; MZ604 Build/H.6.2-20) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13");
        rmb.useragent.add("Mozilla/5.0 (Linux; U; Android 3.1; en-us; K1 Build/HMJ37) AppleWebKit/534.13(KHTML, like Gecko) Version/4.0 Safari/534.13");
        rmb.useragent.add("Mozilla/5.0 (Linux; U; Android 3.1; ja-jp; Sony Tablet S Build/THMAS10000) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13");
        rmb.useragent.add("Mozilla/5.0 (Android; Linux armv7l; rv:9.0) Gecko/20111216 Firefox/9.0 Fennec/9.0");
        rmb.useragent.add("Mozilla/5.0 (Linux; U; Android 2.3.4; ja-jp; SonyEricssonIS11S Build/4.0.1.B.0.112) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        rmb.useragent.add("Mozilla/5.0 (Linux; U; Android 1.5; ja-jp; GDDJ-09 Build/CDB56) AppleWebKit/528.5+ (KHTML, like Gecko) Version/3.1.2 Mobile Safari/525.20.1");
        rmb.useragent.add("Mozilla/5.0 (iPhone; U; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9A334 Safari/7534.48.3");
        rmb.useragent.add("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_4 like Mac OS X; ja-jp) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8K2 Safari/6533.18.5");



        rmb rmb=new rmb();
        for(int i=0;i<1;i++){
            Thread one=new Thread(rmb);
            one.start();}

//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Get_word_2 get_word=new Get_word_2();
        for(int i=0;i<100;i++){
            Thread one=new Thread(get_word);
            one.start();}



        //http://www.panjueshu.com/wenshu/73120a546a182b61.html
        //http://m.panjueshu.com//wenshu/73120a546a182b61.html
//        String sd="http://www.panjueshu.com//wenshu/73120a546a182b61.html";
//        sd=sd.replace("www","m");
//        System.out.println(sd);
//
//        try {
//
//            String ip_port = rmb.get_ip();
//            System.out.println(ip_port);
//            String ip = ip_port.split(":")[0];
//            String port = ip_port.split(":")[1];
//            System.getProperties().setProperty("http.proxyHost", ip);
//            System.getProperties().setProperty("http.proxyPort", port);
//            Document document=Jsoup.connect("http://ip.chinaz.com/getip.aspx")
//                    .userAgent(rmb.get_useragent())
//                    .get();
//            String title=document.title();
//            System.out.println(title);
//            System.out.println(document.body().text());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

    }
}
