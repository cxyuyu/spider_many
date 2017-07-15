package get_cpws_new.get_cpws;

import get_cpws.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-7-15.
 */
public class Get_result {
    //读取一行一行，然后一行一行保存，顺序不要乱了，
    //带原本的id，效果好
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

    //文件一个一个读
    //0 原告胜 1 被告胜 2 撤诉 3 重审
    //文件读取加文件保存
    public static void one_file(File file){
        String id="";
        String result="";
        List<String> one=read(file.getPath());
        List<String> two=new ArrayList<String>();
        String a=one.get(0);
        //检验完毕之后，加个
        for(String d:one){
            String s[]=d.split("\t");
            id=s[0];
            //默认原告胜
            result="0";
            if(s[18].contains("撤回")||s[18].contains("撤诉"))
                result="2";
            if(s[18].contains("驳回"))
                result="1";
            if(s[18].contains("重审"))
                result="3";
            //裁定结果没有数据，并且s13写的是审判长
            if(s[18].length()<2){
                result="未定";
            }
            two.add(id+"\t"+result);
        }
        String last="";
        for(String d:two)
        {
            last=last+"\n"+d;
        }
        last=last.substring(1,last.length());
        Get_word.appendMethodB("/home/cxyu/Desktop/cpws_out2/"+file.getName(),last);

    }

    public static void tj(File file){
        List<String>files=read(file.getPath());
        List<String>files_copy=new ArrayList<String>();
        int ys=0;
        int bs=0;
        int ch=0;
        int cs=0;
        int wei=0;
        for (String a:files){
            if(a.split("\t")[1].equals("0")){
                ys++;
            }
            if(a.split("\t")[1].equals("1")){
                bs++;
            }
            if(a.split("\t")[1].equals("2")){
                ch++;
            }
            if(a.split("\t")[1].equals("3")){
                cs++;
            }
            if(a.split("\t")[1].equals("未定")){
                wei++;
            }
        }
        int all=files.size();
        for(String s:files){
            int ys1=ys/all*wei;
            int bs1=bs/all*wei;
            int ch1=ch/all*wei;
            int cs1=wei-ys1-bs1-ch1;

            if(s.split("\t")[1].equals("未定"))
            {
                if(ys1>0)
                {
                    s=s.split("\t")[0]+"\t"+"0";
                    ys1--;
                    continue;
                }
                if(bs1>0)
                {
                    s=s.split("\t")[0]+"\t"+"1";
                    bs1--;
                    continue;
                }
                if(ch1>0)
                {
                    s=s.split("\t")[0]+"\t"+"2";
                    ch1--;
                    continue;
                }
                if(cs1>0)
                {
                    s=s.split("\t")[0]+"\t"+"3";
                    cs1--;
                    continue;
                }
                s=s.split("\t")[0]+"\t"+"3";
            }
            files_copy.add(s);
        }

        String last="";
        for(String d:files_copy)
        {
            last=last+"\n"+d;
        }
        last=last.substring(1,last.length());
        Get_word.appendMethodB("/home/cxyu/Desktop/cpws_out2/l-"+file.getName(),last);
    }


    public static void main(String args[]){
//        File all=new File("/home/cxyu/Desktop/cpws_out");
//        File[] files=all.listFiles();
//        for(File d:files)
//            one_file(d);

        tj(new File("/home/cxyu/Desktop/cpws_out2/data.txt"));

        //one_file(new File("/home/cxyu/Desktop/data.txt"));
    }
}
