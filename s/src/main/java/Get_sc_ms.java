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
public class Get_sc_ms implements Runnable {


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
        for (int i = 0; i < 99; i++)

            try {
                int save_not = 0;
                Document document = Jsoup.connect("http://www.msfy.gov.cn/Article.asp?SmallClassID=114&page=" + i )
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                "like Gecko) Chrome/58.0.3029.110 Safari/537.36").get();
                Elements lis = document.select("body > div.main > div > div.left > div > div.infotxt > div > ul > li");
                for (int s = 0; s < lis.size(); s++) {
                    Element li = lis.get(s);
                    try {

                            Document document_line = Jsoup.connect("http://www.msfy.gov.cn/"+li.select("a").attr("href"))
                                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                            "like Gecko) Chrome/58.0.3029.110 Safari/537.36").get();
                            String url = "http://www.msfy.gov.cn/"+li.select("a").attr("href");

                            String content ="";
                            Elements Ps=document_line.select("#fontZoom > tbody > tr > td > p");
                            for(Element p:Ps)
                                content=content+p.text();

                            String content2="";
                            String[] contents=content.split(" ");
                            for(int f=0;f<contents.length;f++) {
                                String line=contents[f].replace(" ","");
                                if (line.length() < 2)
                                    continue;
                                content2=content2+"\n"+contents[f];
                            }
                            content2=content2.substring(1,content2.length());
                            //System.out.println(content2);


                            String title = document_line.select("head > title").text();

                            System.out.println("http://www.msfy.gov.cn/"+li.select("a").attr("href"));
                            System.out.println(content2);
                            content=Content_handle.handle(content2);
                            if(content==null)
                                continue;
                            Elements ps = document_line.select("article > p");



                            //案件标签
                            String id =  title;

                            System.out.println("url  " + url);
                            System.out.println("title" + title);


                            content = url + "\n" + "" + "\n" + id +"\n"+content ;


                            content=content.replace("\n","\t");
                            //System.out.println("content" + content);
                            appendMethodB("/home/cxyu/tmp/sc-3/" + i + "-" + s, content);

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
