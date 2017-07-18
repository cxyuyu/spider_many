/**
 * Created by cxyu on 17-6-26.
 */


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
public class get_ty implements Runnable {

    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    public static String getflie(String fileName) {
        String word = "";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                line++;
                word += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return word;
    }

    public static List<String> read() {
        List<String> words = new ArrayList<String>();
        File file = new File("word");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                tempString=tempString.split("\t")[1];
                words.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
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

    public static void get() {
        for (; ; )
            if (id < all.size())
                try {
                    String content = all.get(id++);
                    Document document = Jsoup.connect("http://baike.baidu.com/item/" + content)
                            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                    "like Gecko) Chrome/58.0.3029.110 Safari/537.36").get();
                   // System.out.println("http://baike.baidu.com/item/" + content+"\n\n");
                    System.out.println(document.toString().length());

                    //保存数据,
                    //appendMethodB("/home/cxyu/tmp/bdbk/" + content, document.toString());
                    //提取数据进行保存shi
                    String get_content = get_ci(content, document);
                    if (get_content.length() > 0)
                        appendMethodB("/home/cxyu/tmp/bdbk-ci/" + content, get_content);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            else
                break;

    }


    //通过判断有没有a属性，来获取
    //这里走不下去，就走到多义，多义走不下去，就放弃
    public static String get_ci(String word, Document document) {

        String content = "";
        //名称
        try {
            List<String> dt = new ArrayList<String>();
            //修饰词
            List<String> dd = new ArrayList<String>();
            Elements div = document.select("body > div.body-wrapper " +
                    "> div.content-wrapper > div > div.main-content > div.basic-info.cmn-clearfix > dl");
            System.out.println(document.title());
            for (int i = 0; i < 2; i++) {
                //左右有两个
                //
                Set<Integer> dd_dt = new HashSet<Integer>();
                for (int s = 0; s < div.get(i).select("dd").size(); s++) {
                    Element dd_e = div.get(i).select("dd").get(s);
                    if (dd_e.select("a").size() > 0 && !dd_e.select("a").text().equals(" ")) {
                        //选出有a的，即实体，与去除带着左上标的。
                        dd.add(dd_e.select("a").text());
                        dd_dt.add(s);
                    }
                }

                //进行添加dt

                for (int q : dd_dt) {
                    Element dt_e = div.get(i).select("dt").get(q);
                    dt.add(remove(dt_e.text()));
                }

            }
            if (dt.size() == dd.size()) {
                for (int i = 0; i < dd.size(); i++)
                    content += word + "\t" + dt.get(i) + "\t" + dd.get(i) + "\n";
            } else {
                System.out.println(dt.size() + " " + dd.size());
            }
            //System.out.println(content);
        } catch (Exception e) {
        //放置多义
        Get_2.get_dy(word);
        }
        return content;
    }

    //不进行2次替换
    public static String get_ci_2(String word, Document document) {

        String content = "";
        //名称
            List<String> dt = new ArrayList<String>();
            //修饰词
            List<String> dd = new ArrayList<String>();
            Elements div = document.select("body > div.body-wrapper " +
                    "> div.content-wrapper > div > div.main-content > div.basic-info.cmn-clearfix > dl");
            System.out.println(document.title());
            for (int i = 0; i < 2; i++) {
                //左右有两个
                //
                Set<Integer> dd_dt = new HashSet<Integer>();
                for (int s = 0; s < div.get(i).select("dd").size(); s++) {
                    Element dd_e = div.get(i).select("dd").get(s);
                    if (dd_e.select("a").size() > 0 && !dd_e.select("a").text().equals(" ")) {
                        //选出有a的，即实体，与去除带着左上标的。
                        dd.add(dd_e.select("a").text());
                        dd_dt.add(s);
                    }
                }

                //进行添加dt

                for (int q : dd_dt) {
                    Element dt_e = div.get(i).select("dt").get(q);
                    dt.add(remove(dt_e.text()));
                }

            }
            if (dt.size() == dd.size()) {
                for (int i = 0; i < dd.size(); i++)
                    content += word + "\t" + dt.get(i) + "\t" + dd.get(i) + "\n";
            } else {
                System.out.println(dt.size() + " " + dd.size());
            }
            //System.out.println(content);

        return content;
    }

    //去掉空格
    public static String remove(String word) {
        String[] s = word.split(" ");
        String sd = "";
        for (String a : s) {
            sd += a;
        }
        return sd;
    }

    static List<String> all = Collections.synchronizedList(new ArrayList<String>());
    static int id = 0;

    public static void main(String args[]) {
//        String word=getflie("/home/cxyu/tmp/bdbk/100");
//        Document document=Jsoup.parse(word);
//        System.out.println(document.title());

        List<String> words = read();

        for (String word : words)
            all.add(word);

        //List<String> words = new ArrayList<String>();
        //words.add("北京");
        for (int i = 0; i < 200; i++) {
            get_ty one = new get_ty();
            Thread one_t = new Thread(one);
            one_t.start();
        }

    }


    public void run() {
        get();
    }
}
