package get_qyxx;

import get_cpws.read;
import get_cpws_new.get_cpws.Content_handle;
import get_cpws_new.get_cpws.rmb;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-7-6.
 */
public class Get_word implements Runnable {


    //这个不允许代码出错，必须获取
    public static List<String> get_page_page(String url){

        List<String> urls=new ArrayList<String>();
        try{
            String ip_port = rmb.get_ip();
            String ip = "";
            String port = "";
            ip = ip_port.split(":")[0];
            port = ip_port.split(":")[1];
            System.getProperties().setProperty("http.proxyHost", ip);
            System.getProperties().setProperty("http.proxyPort", port);
            System.out.println(url);
            Document document = Jsoup
                    .connect(url)
                    .userAgent(rmb.get_useragent())
                    .timeout(20000)
                    .get();
            Elements as=document.select("body > div.d.cl > div.r > div > a");

            for(Element a:as){
                String URL="http://www.panjueshu.com"+a.attr("href");
                System.out.println("URL  "+URL);
                //到最后为空为止。
                List<String> urls_more=get_page_page(URL);
                urls.addAll(urls_more);
            }
        }
        //出错了，也得继续获取。
        catch (Exception e){
            e.printStackTrace();
            urls=get_page_page(url);
        }
        return urls;
    }


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
                .connect("http://m.panjueshu.com/10/" + id + ".html")
                .userAgent(rmb.get_useragent())
                .timeout(20000)
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
                            .connect("http://m.panjueshu.com/10/" + id + ".html")
                            .userAgent(rmb.get_useragent())
                            .timeout(20000)
                            .get();
                    if(document.text().length()>200)
                        break;
                }
                if(document.text().length()>200)
                    break;
            }


        Elements lis = document.select("body > div.view-list-page > ul > li");
        for (Element li : lis) {
            String url = li.select("a").attr("href");
            ids.add(url);
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
                String where=document.select("body > div.details-box > div.details-container-title.tc").text();
                //body > div.details-box > div.details-container-title.tc
                Elements ps=document.select("body > div.details-box > div.details-container > p");
                //body > div.details-box > div.details-container
                String content="";
                for(Element p:ps){
                    content=content+"\n"+p.text();
                }
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


    //记录获取到哪一页了
    public static int page = 700000;
    //一条线程可以独立运行
    public void run() {
        get_page();
    }

    public static String read(String filename) {
        String words = "";
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                words =words+"\n"+ tempString;
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words.substring(1,words.length());
    }

    public static  void start_config(){
        rmb.useragent.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        rmb.useragent.add("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
        rmb.useragent.add("Mozilla/5.0 (compatible; Yahoo! Slurp China; http://misc.yahoo.com.cn/help.html”)");
        rmb.useragent.add("iaskspider/2.0(+http://iask.com/help/help_index.html”) ");
        rmb.useragent.add("Mozilla/5.0 (compatible; YodaoBot/1.0; http://www.yodao.com/help/webmaster/spider/”; )");
        rmb.useragent.add("msnbot/1.0 (+http://search.msn.com/msnbot.htm”)");
        rmb.useragent.add("Baiduspider+(+http://www.baidu.com/search/spider.htm”)");
        rmb.useragent.add("Googlebot/2.1 (+http://www.google.com/bot.html)");

//        String words= read("src/main/java/get_qyxx/meta.tsv");
//        String[] ci=words.split("\n");
//        for(String a:ci)
//            Get_page.word.add(a);


        rmb rmb=new rmb();
        for(int i=0;i<1;i++){
            Thread one=new Thread(rmb);
            one.start();}


//        Get_word get_word=new Get_word();
//        for(int i=0;i<100;i++){
//            Thread one=new Thread(get_word);
//            one.start();}

    }



    static List<String> URL_all = Collections.synchronizedList(new ArrayList<String>());
    public static void main(String args[]) {
        String[] where={"bj","zj","snx","js","hen","heb","gd","ah","sd"
                ,"ln","hun","hub","hlj","sx","jx","tj","cq","fj","sh","sc"
                ,"gs","qh","nx","jl","gx","gz","han","yn","xz","zj","nmg",};
        //设置地名，更精细地搜索有利于速度的提升。
        start_config();


        List<String> urls=new ArrayList<String>();
        for(String a:where){
            String url="http://"+a+".m.tianyancha.com/search?key="+"漫画";
            //之后这里添加词语
            urls.add(url);
        }
        //这里将3,4分开
        for(int i=0;i<16;i++){
            URL_all.addAll(get_page_page(urls.get(i)));
            System.out.println(URL_all.size());
        }
        for(String url:URL_all)
            System.out.println(url);
    }
}
