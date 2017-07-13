/**
 * Created by cxyu on 17-6-26.
 */


import get_ip.DateFormatTransfer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

/**
 * Created by cxyu on 17-6-20.
 */
//document保存一份，收集的数据保存一份
public class Get_sc_ya implements Runnable {


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
        for (int i = 2; i < 109; i++)

            try {
                int save_not = 0;
                Document document = Jsoup.connect("http://www.yaancourt.gov.cn/html/ya/cpws/" + i + ".html")
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                "like Gecko) Chrome/58.0.3029.110 Safari/537.36").get();
                Elements lis = document.select("#class_newlist1 > ul > li");
                for (int s = 0; s < lis.size(); s++) {
                    Element li = lis.get(s);
                    try {
                        if (li.select("a").text().contains("民事")) {
                            Document document_line = Jsoup.connect(li.select("a").attr("href"))
                                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                            "like Gecko) Chrome/58.0.3029.110 Safari/537.36").get();
                            String url = li.select("a").attr("href");
                            String content = document_line.select("#div_zhengwen").text();
                            String title = document_line.select("#news_content > h1").text();
                            content = content.replace(" 　　", "\n");
                            //System.out.println(content);
                            content=Content_handle.handle(content);
                            if(content==null)
                                continue;
                            Elements ps = document_line.select("article > p");



                            //案件标签
                            String id = ps.get(2).text();

                            System.out.println("url  " + url);
                            System.out.println("title" + title);
                            //System.out.println("content" + content);

                            content = url + "\n" + title + "\n"+content ;

                            String[] contents=content.split("\n");
                            content=content.replace("\n","\t");

                            appendMethodB("/home/cxyu/tmp/sc/" + i + "-" + s, content);
                        }
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


    //加一小段，因为有负责人，法定代表人等
    public static boolean add(Elements ps, int a, int p_id) {
        //ps所有的p值，a之前到哪一段了，p——id块的大小

        Element p_2 = ps.get(a);
        String line_2 = p_2.text();
        if (line_2.substring(2, 4).equals("被上") ||
                line_2.substring(2, 4).equals("上诉") ||
                line_2.substring(2, 4).equals("被申") ||
                line_2.substring(2, 4).equals("再审") ||
                line_2.substring(2, 4).equals("委托"))
            return false;
        else
            return true;

    }


    public static void main(String args[]) {


        get();
    }


    public void run() {
        get();
    }
}
