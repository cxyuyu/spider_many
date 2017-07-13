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
public class Get_bdzd_2 implements Runnable {

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
        File file = new File("meta.tsv");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
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
                    get_list(content);
                    //保存数据,
//                    for(int i=0;i<documents.size();i++) {
//                       // Document document=documents.get(i);
//                        //appendMethodB("/home/cxyu/tmp/zd_documents/" + content+"-"+i, document.toString());
//                    }
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
        //System.out.println(document.title());
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

    public static void get_list(String content) {

        List<Document> documents = new ArrayList<Document>();
        String old = "";
        for (int i = 0; ; i++) {
            try {
                Document document = Jsoup.connect("https://zhidao.baidu.com/search?word=" + content + "&pn=" + i * 10)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, " +
                                "like Gecko) Chrome/58.0.3029.110 Safari/537.36").get();
                Elements dls = document.select("#wgt-list > dl");
                if (dls.first().select("dt > a").text().equals(old) || i > 100)
                    break;
                //相似就退出
                for (int s = 0; s < dls.size(); s++) {
                    Element dl = dls.get(s);

                    //从链接获取数据
                    if (dl.select("dt > a").attr("href").contains("zhidao")) {
                        try {
                            Document document_dl = Jsoup.connect(dl.select("dt > a").attr("href"))
                                    .userAgent(get_useragent()).get();
                            //documents.add(document_dl);
                            //System.out.println(dl.select("dt > a").attr("href"));
                            String contents=get_content(document_dl);
                            if(contents!=null)
                            appendMethodB("/home/cxyu/tmp/zd_documents/" + content + "-" + (i * 10 + s), contents);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                old = dls.first().select("dt > a").text();
                System.out.println(document.toString().length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //return  documents;
    }

    public static String get_content(Document document) {
        String content = "";
        //先得到标题与内容
        //再得到每个回答，即wgt-answers  bd answer  最佳是wgt-best
        String describe="";
        String title="";
        String answer_best="";
        List<String> Answers=new ArrayList<String>();

        title = document.select("#main-content > div.question-container > div.w-question-box > div.title > h2").text();
        title="标题"+"\t"+title;
        //System.out.println(title);

        if(document.select("#question-content").size()>0) {
            describe = document.select("#question-content").first().select("div.cont").text();
            describe ="描述"+"\t" + describe;
            //System.out.println(describe);
        }

        if(document.select("div.w-special-answer").size()>0) {
            Element div_best = document.select("div.w-special-answer").first();
            answer_best = div_best.select("div.full-content").text();
            if (answer_best.length()<26) {
                answer_best="最佳答案" + "\t" + answer_best;
                //System.out.println(answer_best);
            }
            else
                answer_best="";
        }



        if(document.select("div.w-replies").size()>0) {
            Elements div_answer = document.select("div.w-replies").first().select("div.reply-item");
            int s = 0;
            for (Element div_1 : div_answer) {
                String answer = div_1.select("div.full-content").text();
                if(answer.length()<26){
                answer="答案" + s + "\t" + answer;
                //System.out.println(answer);
                Answers.add(answer);
                s++;}
            }
        }
        if(Answers.size()==0&&answer_best.length()<4)
            return  null;
        content=title+"\n"+describe+"\n"+answer_best+"\n";
        for(String d:Answers)
            content=content+d+"\n";
        //System.out.println(content);
        return content;
    }

    public static String get_useragent() {
        String user_agent = "";
        int max = 3;
        int min = 0;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        user_agent=useragent.get(s);
        //System.out.println(user_agent);
        return user_agent;
    }


    static List<String> all = Collections.synchronizedList(new ArrayList<String>());
    static int id = 0;
    static List<String> useragent = new ArrayList<String>();

    public static void main(String args[]) {
//        String word=getflie("/home/cxyu/tmp/bdbk/100");
//        Document document=Jsoup.parse(word);
//        System.out.println(document.title());

        useragent.add("Mozilla/5.0 (Linux; U; Android 3.0.1; ja-jp; MZ604 Build/H.6.2-20) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13");
        useragent.add("Mozilla/5.0 (Linux; U; Android 3.1; en-us; K1 Build/HMJ37) AppleWebKit/534.13(KHTML, like Gecko) Version/4.0 Safari/534.13");
        useragent.add("Mozilla/5.0 (Linux; U; Android 3.1; ja-jp; Sony Tablet S Build/THMAS10000) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13");
        useragent.add("Mozilla/5.0 (Android; Linux armv7l; rv:9.0) Gecko/20111216 Firefox/9.0 Fennec/9.0");
        useragent.add("Mozilla/5.0 (Linux; U; Android 2.3.4; ja-jp; SonyEricssonIS11S Build/4.0.1.B.0.112) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        useragent.add("Mozilla/5.0 (Linux; U; Android 1.5; ja-jp; GDDJ-09 Build/CDB56) AppleWebKit/528.5+ (KHTML, like Gecko) Version/3.1.2 Mobile Safari/525.20.1");
        useragent.add("Mozilla/5.0 (iPhone; U; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9A334 Safari/7534.48.3");
        useragent.add("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_4 like Mac OS X; ja-jp) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8K2 Safari/6533.18.5");

        List<String> words = read();
        all.add("李叔人");
        all.add("张低的");
        for (String word : words)
            all.add(word);

        //List<String> words = new ArrayList<String>();
        //words.add("北京");
        for (int i = 0; i < 100; i++) {
            Get_bdzd_2 one = new Get_bdzd_2();
            Thread one_t = new Thread(one);
            one_t.start();
        }

    }


    public void run() {
        get();
    }
}
