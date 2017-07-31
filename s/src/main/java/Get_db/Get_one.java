package Get_db;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by cxyu on 17-7-20.
 */
public class Get_one implements Runnable{


    //一行可以允许多个公司
    //数字    公司
    public static List<String> read(String filename) {
        List<String> words = new ArrayList<String>();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String s[] = tempString.split("\t");
                String content = s[0];
                if (s.length > 2) {
                    String line = s[2].substring(0, s[2].length() - 5);
                    if (line.contains("（反诉）"))
                        line = line.substring(4, line.length());
                    //去掉最后的一个括号

                    if (line.length() > 3) {
                        Character c = line.charAt(line.length() - 1);
                        if (c == '）') {
                            line = line.substring(0, line.length() - 1);
                            line = line.split("（")[0];
                        }
                        content = content + "\t" + line;
                    }
                }
                if (content.split("\t").length > 1) {
                    System.out.println(content);
                    words.add(content);
                }
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }


    public static List<String> read_2(String filename) {
        List<String> words = new ArrayList<String>();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String s[] = tempString.split("\t");
                String content = s[0];
                if (s.length > 2) {
                    String line = s[2].substring(0, s[2].length() - 4);
                    line=line.replaceAll("\\(","（");
                    line=line.replaceAll("\\)","）");
//                    if (line.contains("（反诉）"))
//                        line = line.substring(4, line.length());
//                    //去掉最后的一个括号

                    if (line.length() > 3) {
                        //去掉第一个的值
                        Character c = line.charAt(line.length() - 1);
                        if (c == '）') {
                            line = line.substring(0, line.length() -1);
                            String lines[]=line.split("（");
                            line="";
                            for(int i=0;i<lines.length-1;i++)
                                line = line+lines[i];
                        }
                        //去掉第二个的值
                        Character c2 = line.charAt(0);
                        if (c2 == '（') {
                            line = line.substring(1, line.length());
                            String lines[]=line.split("）");
                            line="";
                            for(int i=1;i<lines.length;i++)
                                line=line+lines[i];
                        }
                        content = content + "\t" + line;
                    }
                }
                if (content.split("\t").length > 1) {
                    System.out.println(content);
                    words.add(content);
                }
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }


    public static List<String> read_3(String filename) {
        List<String> words = new ArrayList<String>();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String one=tempString.split("\\|\\|")[0];
                words.add(one);
                System.out.println(one);
            }
            System.out.println(words.size());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static void get(){
        while (true)
        {
            String word=Words.remove(0);
            String name = word.split("\t")[1];
            get(name);
        }
    }

    //最后的查询，如果有小括号，则把小括号
    //创建文件当一行中有两个，则创建两个小文件
    //上诉的放在qyxx_ss,被上诉的放在qyxx_bss
    public static void get(String name) {
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
                    .connect("https://www.baidu.com/s?wd=site%3A(tianyancha.com)%20" + name + "&pn=0&oq=site%3A(tianyancha.com)%20" + name + "&ie=utf-8")
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .timeout(5000)
                    .get();
            String name_url = document.select("#content_left > div").first().select("a").text();

            System.out.println("原先："+name+"|    现在："+name_url);
            //ji记录是否包含，有一个不包含就跳出
            int ji = 0;
            if (name.contains("（")) {
                String names[] = name.split("（");
                for (int s = 0; s < names.length; s++)
                    if (names[s].contains("）")) {
                        String names_2[] = names[s].split("）");
                        for (String one : names_2)
                            if (!name_url.contains(one)) {
                                ji = 1;
                                break;
                            }
                    } else if (!name_url.contains(names[s])) {
                        ji = 1;
                        break;
                    }
                if (ji == 0) {
                    System.out.println("进入下载");
                    String url = document.select("#content_left > div").first().select("div.f13 > a.m").attr("href");
                    System.out.println(url);
                    List<String> urls = new ArrayList<String>();
                    urls.add(url);
                    String content=Get_word_one.get_word(urls);
                    if(content==null)
                        Get_word_one.appendMethodB2("za",name+"||"+url+"\n");
                    if(content.equals("false"))
                        Get_word_one.appendMethodB2("wen",name+"||"+url+"\n");
                    if(!(content==null)&&!content.equals("false")){
                        Get_word_one.appendMethodB("/home/cxyu/tmp/qyxx_bs/"+name_url,content);}
                }
            } else {
                if (name_url.contains(name)) {
                    System.out.println("进入下载");
                    String url = document.select("#content_left > div").first().select("div.f13 > a.m").attr("href");
                    System.out.println(url);
                    List<String> urls = new ArrayList<String>();
                    urls.add(url);
                    String content=Get_word_one.get_word(urls);
                    if(content==null)
                        Get_word_one.appendMethodB2("za",name+"||"+url+"\n");
                    if(content.equals("false"))
                        Get_word_one.appendMethodB2("wen",name+"||"+url+"\n");
                    if(!(content==null)&&!content.equals("false")){
                        Get_word_one.appendMethodB("/home/cxyu/tmp/qyxx_bs/"+name,content);}
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static List<String> Words = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String args[]) {

//        List<String> s=read("/home/cxyu/Downloads/case_appellee.txt");
//        Set<String> d=new HashSet<String>();
//        for(String g:s)
//            d.add(g);
//        System.out.println(d.size());


//       List<String> s=read_3("wen_bs");
//       List<String> g=new ArrayList<String>();
//        for(String d:s)
//            g.add("1"+"\t"+d);
//        Words=g;


         Get_word_one.start_config();
        Words = read("case_appellee.txt");
        Get_one get_one=new Get_one();
        for(int i=0;i<100;i++){
            Thread one=new Thread(get_one);
            one.start();
        }

    }

    public void run() {
        get();
    }
}
