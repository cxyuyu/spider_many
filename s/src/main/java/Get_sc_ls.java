/**
 * Created by cxyu on 17-6-26.
 */


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by cxyu on 17-6-20.
 */
//document保存一份，收集的数据保存一份
public class Get_sc_ls implements Runnable {


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

    public static void get() {
        for (int i = 1; i < 19; i++)

            try {
                Document document = Jsoup.connect("http://www.lsfy.gov.cn/ws/list-335-" + i + ".html")
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                "like Gecko) Chrome/58.0.3029.110 Safari/537.36").get();
                Elements lis = document.select("#List > ul > li");
                for (int s = 0; s < lis.size(); s++) {
                    Element li = lis.get(s);
                    try {
                        String url = li.select("p > a").attr("href");
                        String URL="http://www.lsfy.gov.cn/ws" + url.substring(1, url.length());
                        //System.out.println(url);
                        Document document_line = Jsoup.connect("http://www.lsfy.gov.cn/ws"
                                + url.substring(1, url.length()))
                                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                        "like Gecko) Chrome/58.0.3029.110 Safari/537.36").get();


                        String content = "http://www.lsfy.gov.cn/ws"+ url.substring(1, url.length());
                        //content设置
                        Elements ps = document_line.select("#Text > p");
                        for (Element p : ps)
                            content=content+"\n"+p.text();

                        String time = document_line.select("#qt").text();
                        String title = document_line.select("#Info > h1").text();

                        time=time.split("    ")[2];


                        //提纯文本
                        String[] contents=content.split("\n");
                        String content2="";
                        for(String w:contents)
                            if(w.length()>1)
                            content2=content2+"\n"+w;

                        //System.out.println(content2);

                        //去掉附
                        String[] content2s=content2.split("\n");
                        String content3="";

                        for(int a=0 ;a<content2s.length;a++){
                            String A=content2s[a];
                            if(A.length()<3)
                                continue;
                            if(A.substring(0,1).equals("附"))
                            {
                                break;
                            }
                            content3=content3+"\n"+A;
                        }
                        content3=content3.substring(1,content3.length());
                       // System.out.println(content3);
                        content=Content_handle.handle(content3);

                        //System.out.println(content);
                        String id=document_line.select("#Text > p:nth-child(4) > span").text();


                        if(content==null)
                            continue;
                        content =URL+"\n"+ title+  "\n" + content ;

                        content=content.replace("\n","\t");

                        System.out.println(title);
//                        System.out.println(url);
//                        System.out.println(title);
//                        System.out.println(time);







                        appendMethodB("/home/cxyu/tmp/sc-2/"+i+"-"+s,content);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //保存数据,
                //  appendMethodB("/home/cxyu/tmp/bdbk/" + content, document.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    public static void main(String args[]) {


        get();
    }


    public void run() {
        get();
    }
}
