package Get_db;

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

    public static List<String> URL_p = Collections.synchronizedList(new ArrayList<String>());
    public static List<String> URL_e = Collections.synchronizedList(new ArrayList<String>());
    public static void get_word(List<String> urls) {
        for (int i = 0; i < urls.size(); i++)
            try {
                //get_word是最近一个月的一种，get_word2是最近一个月的另一种较少
                //get_word3数量最多
                String content = null;
                switch (1) {
                    case 1:
                        content = get_word_3(urls.get(i));
                        System.out.println();
                        if (content != null)
                            break;
                    case 2:
                        content = get_word_1(urls.get(i));
                        if (content != null)
                            break;
                    case 3:
                        content = get_word_2(urls.get(i));
                        if (content != null)
                            break;
                    default:
                        content = get_word_4(urls.get(i));
                        if (content != null)
                            break;
                }

                if(content==null)
                    Get_word_one.appendMethodB2("za",urls.get(i)+"\n");
                if(content.equals("false"))
                    Get_word_one.appendMethodB2("wen",urls.get(i)+"\n");
                if(!(content==null)&&!content.equals("false")){
                    Get_word_one.appendMethodB("/home/cxyu/tmp/qyxx/"+content.split(":")[0],content.split(":")[1]);}
            } catch (Exception e) {
                e.printStackTrace();

            }
    }


    //下面某一参数数量为0时，则退出，并记录这个url，以便后续研究

    //电话，邮箱参数网址，
    // 地址参数，一些公开参数，
    // 法人参数
    //此三参数为空则返回空
    //还有就是title的长度小于1时返回null

    //考虑到使用ip代理影响效率，所以不使用ip了
    //考虑到百度，应该对useragent不加限制
    //一些参数为空则停止抓取
    public static String get_word_4(String url) {
        String ip_port="";
        String content = "";
        try {

            while (true) {
                ip_port = Cheak_ip.get_cheakip();
                if (ip_port==null)
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
            Document document = Jsoup.connect(url)
                    .timeout(20000)
                    .userAgent(rmb.get_useragent())
                    .get();
            List<String> data = new ArrayList<String>();
            //body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.show_in_pc > div.col-xs-6.col-sm-8.col-md-9.company_name_box > div > div
            //body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.show_in_pc > div.col-xs-6.col-sm-8.col-md-9.company_name_box > div > div
            Elements spans = document.select("body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.show_in_pc > div.col-xs-6.col-sm-8.col-md-9.company_name_box > div > div > span");
            if (spans.size() == 0)
                return null;
            for (Element span : spans) {
                if (!span.text().contains(":"))
                    continue;
                String one = span.text().replace(" ", "");
                one = one.replace(":", " ");
                data.add(one);
            }


            Elements trs = document.select("body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div:nth-child(4) > div > p");

            //去除掉多余的图片
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                for (Element td : tds) {
                    if (td.text().contains("：")) {
                        String one = td.text().replace("：", " ");
                        data.add(one);
                    }
                }
            }


            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.baseInfo_model2017 > table > tbody > tr > td:nth-child(1) > div > div.human-top > div.in-block.vertical-top.pl15 > div > a
            String fr = document.select("body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div:nth-child(4) > table > tbody > tr:nth-child(2) > td.td-legalPersonName-value.c9").text();
            if (fr.length() > 1)
                data.add("法人 " + fr.split(" ")[0]);

            if (document.text().contains("高管信息") || document.text().contains("主要任职人员]")) {
                //body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div:nth-child(6) > div
                Elements div4s = document.select("body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div:nth-child(6) > div > table");

                if (div4s.size() > 1)
                    for (int a = 0; a < div4s.size(); a++) {
                        Elements div3s = div4s.get(a).select("tbody > tr");
                        if (div3s.size() > 1) {
                            String s[] = div3s.get(0).text().split(" ");
                            String s2[] = div3s.get(1).text().split(" ");
                            for (int i = 0; i < s.length; i++) {
                                String one = "高管:" + s2[i] + " " + s[i];
                                data.add(one);
                            }
                        }
                    }
            }
            if (document.text().contains("对外投资")) {
                //body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div:nth-child(6) > div
                Elements trs2 = document.select("body > div:nth-child(4) > div > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div:nth-child(8) > div > div");
                if (trs2.size() == 0)
                    trs2 = document.select("body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div:nth-child(6) > div > div");
                for (Element tr : trs2) {
                    String s[] = tr.text().split(" ");
                    String one = "";
                    for (String d : s) {
                        if (d.contains("家公司") || d.contains(">"))
                            continue;
                        one = one + " " + d;
                    }
                    one = "对外投资 " + one.substring(1, one.length());
                    data.add(one);
                }
            }
            for (String s : data)
                System.out.println(s);
            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > span
            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > span
            String title = document.select("body > div:nth-child(4) > div > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.show_in_pc > div.col-xs-6.col-sm-8.col-md-9.company_name_box > div > div > p").text();
            System.out.println("title   " + title);
            if (title.length() < 1)
                return null;

            //好像没有获取到的会直接忽略掉
            for (String s : data) {
                content = content + "\n" + s;
            }
            content = content.substring(1, content.length());
            content=title+":"+content;
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(url);
            Cheak_ip.ip_cheak.remove(ip_port);
            return "false";}
    }

    public static String get_word_3(String url) {
        String ip_port="";
        String content = "";
        try {

            while (true) {
                ip_port = Cheak_ip.get_cheakip();
                if (ip_port==null)
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
            Document document = Jsoup.connect(url)
                    .timeout(20000)
                    .userAgent(rmb.get_useragent())
                    .get();
            List<String> data = new ArrayList<String>();
            //body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.show_in_pc > div.col-xs-6.col-sm-8.col-md-9.company_name_box > div > div
            Elements spans = document.select("body > div:nth-child(4) > div.company-page.ng-scope > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.show_in_pc > div.col-xs-6.col-sm-8.col-md-9.company_name_box > div > div > span");
            if (spans.size() == 0)
                return null;
            for (Element span : spans) {
                if (!span.text().contains(":"))
                    continue;
                String one = span.text().replace(" ", "");
                one = one.replace(":", " ");
                data.add(one);
            }


            Elements trs = document.select("body > div:nth-child(4) > div.company-page.ng-scope > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.company-content > table:nth-child(2) > tbody > tr");
            if (trs.size() == 0)
                return null;
            //去除掉多余的图片
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                for (Element td : tds) {
                    if (td.text().contains("：")) {
                        String one = td.text().replace("：", " ");
                        data.add(one);
                    }
                }
            }

            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.baseInfo_model2017 > table > tbody > tr > td:nth-child(1) > div > div.human-top > div.in-block.vertical-top.pl15 > div > a
            String fr = document.select("body > div:nth-child(4) > div.company-page.ng-scope > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.company-content > table:nth-child(1) > tbody > tr:nth-child(2) > td.td-legalPersonName-value.c9 > p > a").text();
            if (fr.length() > 1)
                data.add("法人 " + fr);

            if (document.text().contains("高管信息") || document.text().contains("主要任职人员")) {
                //body > div:nth-child(4) > div.company-page.ng-scope > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.ng-scope > div > table
                Elements div3s = document.select("body > div:nth-child(4) > div.company-page.ng-scope > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.ng-scope > div > table > tbody >tr");
                if (div3s.size() > 1) {
                    String s[] = div3s.get(0).text().split(" ");
                    String s2[] = div3s.get(1).text().split(" ");
                    for (int i = 0; i < s.length; i++) {
                        String one = "高管:" + s2[i] + " " + s[i];
                        data.add(one);
                    }
                }
            }

            if (document.text().contains("对外投资")) {
                Elements trs2 = document.select("body > div:nth-child(4) > div:nth-child(17) > div > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div:nth-child(6) > div > div");
                for (Element tr : trs2) {
                    String s[] = tr.text().split(" ");
                    String one = "";
                    for (String d : s) {
                        if (d.contains("家公司") || d.contains(">"))
                            continue;
                        one = one + " " + d;
                    }
                    one = "对外投资 " + one.substring(1, one.length());
                    data.add(one);
                }
            }
            for (String s : data)
                System.out.println(s);
            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > span
            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > span
            String title = document.select("body > div:nth-child(4) > div.company-page.ng-scope > div.ng-scope > div > div > div > div.col-lg-9.col-md-9.col-sm-9.col-xs-12 > div > div.row.b-c-white.show_in_pc > div.col-xs-6.col-sm-8.col-md-9.company_name_box > div > div > p").text();
            System.out.println("title   " + title);
            if (title.length() < 1)
                return null;


            //好像没有获取到的会直接忽略掉
            for (String s : data) {
                content = content + "\n" + s;
            }
            content = content.substring(1, content.length());
            content=title+":"+content;
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(url);
            Cheak_ip.ip_cheak.remove(ip_port);
            return "false";}
    }

    public static String get_word_2(String url) {
        String content = "";
        String ip_port="";
        try {

            while (true) {
                ip_port = Cheak_ip.get_cheakip();
                if (ip_port==null)
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
            Document document = Jsoup.connect(url)
                    .timeout(20000)
                    .userAgent(rmb.get_useragent())
                    .get();
            List<String> data = new ArrayList<String>();


            Elements divs = document.select("body > div:nth-child(4) > div:nth-child(15) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white.pt20.pl30.pr30.pb13 > div.companyTitleBox55 > div.company_header_width.ie9Style > div > div.f14.new-c3.mt10 > div");
            if (divs.size() == 0)
                return null;
            for (Element div : divs) {
                String one = div.text().replace(" ", "");
                one = one.replace("：", " ");
                data.add(one);
            }


            Elements divs2 = document.select("body > div:nth-child(4) > div:nth-child(15) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white.pt20.pl30.pr30.pb13 > div.companyTitleBox55 > div.company_header_width.ie9Style > div > div:nth-child(4) > div");
            if (divs2.size() == 0)
                return null;
            for (Element div : divs2) {
                String one = div.text().replace(" ", "");
                one = div.text().replace("：", " ");
                data.add(one);
            }


            Elements trs = document.select("body > div:nth-child(4) > div:nth-child(15) > div:nth-child(1) > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.row.b-c-white.company-content.base2017 > table > tbody > tr");
            if (trs.size() == 0)
                return null;
            //去除掉多余的图片
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                for (Element td : tds) {
                    if (td.text().contains("：")) {
                        String one = td.text().replace("：", " ");
                        data.add(one);
                    }
                }
            }

            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.baseInfo_model2017 > table > tbody > tr > td:nth-child(1) > div > div.human-top > div.in-block.vertical-top.pl15 > div > a
            String fr = document.select("body > div:nth-child(4) > div:nth-child(15) > div:nth-child(1) > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.baseInfo_model2017 > table > tbody > tr > td:nth-child(1) > a").text();
            if (fr.length() > 1)
                data.add("法人 " + fr);
            if (document.text().contains("高管信息") || document.text().contains("主要任职人员")) {
                //#_container_staff
                Elements div3s = document.select("#_container_staff > div > div > div");
                for (Element div : div3s) {
                    String s[] = div.text().split(" ");
                    String one = "";
                    for (String d : s) {
                        if (d.contains("家公司") || d.contains(">"))
                            continue;
                        one = one + " " + d;
                    }
                    one = "高管:" + one.substring(1, one.length());
                    data.add(one);
                }
            }
            if (document.text().contains("对外投资")) {
                //#_container_invest > div > div.out-investment-container > table > tbody
                Elements trs2 = document.select("#_container_invest > div > div > table > tbody > tr");
                for (Element tr : trs2) {
                    String s[] = tr.text().split(" ");
                    String one = "";
                    for (String d : s) {
                        if (d.contains("家公司") || d.contains(">"))
                            continue;
                        one = one + " " + d;
                    }
                    one = "对外投资 " + one.substring(1, one.length());
                    data.add(one);
                }
            }
            for (String s : data)
                System.out.println(s);
            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > span
            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > span
            String title = document.select("body > div:nth-child(4) > div:nth-child(15) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white.pt20.pl30.pr30.pb13 > div.companyTitleBox55 > div.company_header_width.ie9Style > div > span").text();
            System.out.println("title   " + title);
            if (title.length() < 1)
                return null;


            //好像没有获取到的会直接忽略掉
            for (String s : data) {
                content = content + "\n" + s;
            }
            content = content.substring(1, content.length());
            content=title+":"+content;
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(url);
            Cheak_ip.ip_cheak.remove(ip_port);
            return "false";}
    }

    public static String get_word_1(String url) {
        String content = "";
        String ip_port="";
        try {
            while (true) {
                ip_port = Cheak_ip.get_cheakip();
                if (ip_port==null)
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

            Document document = Jsoup.connect(url)
                    .timeout(20000)
                    .userAgent(rmb.get_useragent())
                    .get();
            List<String> data = new ArrayList<String>();


            Elements divs = document.select("body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > div.f14.new-c3.mt10 > div");
            if (divs.size() == 0)
                return null;
            for (Element div : divs) {
                String one = div.text().replace(" ", "");
                one = one.replace("：", " ");
                data.add(one);
            }


            Elements divs2 = document.select("body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > div:nth-child(4) > div");
            if (divs2.size() == 0)
                return null;
            for (Element div : divs2) {
                String one = div.text().replace(" ", "");
                one = div.text().replace("：", " ");
                data.add(one);
            }


            Elements trs = document.select("body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.row.b-c-white.base2017 > table > tbody > tr");
            if (trs.size() == 0)
                return null;
            //去除掉多余的图片
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                for (Element td : tds) {
                    if (td.text().contains("：")) {
                        String one = td.text().replace("：", " ");
                        data.add(one);
                    }
                }
            }

            //body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.baseInfo_model2017 > table > tbody > tr > td:nth-child(1) > div > div.human-top > div.in-block.vertical-top.pl15 > div > a
            String fr = document.select("body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.baseInfo_model2017 > table > tbody > tr > td:nth-child(1) > div > div.human-top > div.in-block.vertical-top.pl15 > div").text();
            if (fr.length() > 1)
                data.add("法人 " + fr);
            if (document.text().contains("高管信息") || document.text().contains("主要任职人员")) {
                Elements div3s = document.select("#_container_staff > div > div > div");
                for (Element div : div3s) {
                    String s[] = div.text().split(" ");
                    String one = "";
                    for (String d : s) {
                        if (d.contains("家公司") || d.contains(">"))
                            continue;
                        one = one + " " + d;
                    }
                    one = "高管:" + one.substring(1, one.length());
                    data.add(one);
                }
            }
            if (document.text().contains("对外投资")) {
                //#_container_invest > div > div.out-investment-container > table > tbody
                Elements trs2 = document.select("#_container_invest > div > div > table > tbody > tr");
                for (Element tr : trs2) {
                    String s[] = tr.text().split(" ");
                    String one = "";
                    for (String d : s) {
                        if (d.contains("家公司") || d.contains(">"))
                            continue;
                        one = one + " " + d;
                    }
                    one = "对外投资 " + one.substring(1, one.length());
                    data.add(one);
                }
            }
            for (String s : data)
                System.out.println(s);

            String title = document.select("body > div:nth-child(4) > div:nth-child(19) > div:nth-child(1) > div > div > div > div.position-rel.mt10.mb10.new-border.b-c-white > div.companyTitleBox55.pt20.pl30.pr30 > div.company_header_width.ie9Style > div > span").text();
            System.out.println("title   " + title);
            if (title.length() < 1)
                return null;


            //好像没有获取到的会直接忽略掉
            for (String s : data) {
                content = content + "\n" + s;
            }
            content = content.substring(1, content.length());
            content=title+":"+content;
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(url);
            Cheak_ip.ip_cheak.remove(ip_port);
            return "false";
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
    public static void appendMethodB2(String fileName, String content) {
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
        //get_page();
    }


    public static List<String> read(String filename) {
        List<String> words = new ArrayList<String>();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                words.add(tempString);
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }




    public static void start_config() {
        rmb.useragent.add("Mozilla/5.0 (compatible; MSIE 10.0; Macintosh; Intel Mac OS X 10_7_3; Trident/6.0)'");
        rmb.useragent.add("Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; GTB7.4; InfoPath.2; SV1; .NET CLR 3.3.69573; WOW64; en-US)");
        rmb.useragent.add("Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)");
        rmb.useragent.add("Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25");
        rmb.useragent.add("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:15.0) Gecko/20100101 Firefox/15.0.1");
        rmb.useragent.add("Mozilla/5.0 (Windows NT 6.2; Win64; x64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1");
        rmb.useragent.add("Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11");
        rmb.useragent.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/537.13 (KHTML, like Gecko) Chrome/24.0.1290.1 Safari/537.13");


        rmb rmb = new rmb();
        for (int i = 0; i < 1; i++) {
            Thread one = new Thread(rmb);
            one.start();
        }

        Cheak_ip cheak_ip=new Cheak_ip();
        for (int i = 0; i < 20; i++) {
            Thread one = new Thread(cheak_ip);
            one.start();
        }

//        Save_problem save_problem=new Save_problem();
//        Thread one = new Thread(save_problem);
//        one.start();
//
//
//        Retry retry = new Retry();
//        Thread one = new Thread(retry);
//        one.start();


    }


    static List<String> URL_all = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String args[]) {
        start_config();
        String url = "http://cache.baiducontent.com/c?m=9f65cb4a8c8507ed4fece763105392230e54f72567848c5e2c88c212c0735b36163bbca6767f4f468099377a01a44d5ae0f03c77310634f2c688de45cacb943f5e8f3035000bf64005a46d&p=9179840cc5904ead09bd9b7d0d148b&newp=92759a46d6c11bfc57ef856845598b231610db2151d4d7176b82c825d7331b001c3bbfb423241607d7c67f6d0aab4859eff13571370221a3dda5c91d9fb4c57479d2&user=baidu&fm=sc&query=site%3A%28tianyancha%2Ecom%29+%B4%C5%B3%EF&qid=e002844f00037ded&p1=1";
        List<String> urls = new ArrayList<String>();
        urls.add(url);
        Get_word.get_word(urls);
    }
}
