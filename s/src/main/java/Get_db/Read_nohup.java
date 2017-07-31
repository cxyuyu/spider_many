package Get_db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by cxyu on 17-7-19.
 */
public class Read_nohup {
    public static List<String> read(String filename) {
        List<String> words = new ArrayList<String>();
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {

                if(tempString.contains("title")){
                    System.out.println(tempString);
                     words.add(tempString);

                }
                //System.out.println(tempString);
            }
            System.out.println(words.size());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
    public static void main(String args[]){
        Set<String> one = new HashSet<String>();
        List<String >a=read("/home/cxyu/Downloads/nohup_2.out");
        for(String d:a){
            one.add(d);
        }
        System.out.println(one.size());

        Iterator i = one.iterator();//先迭代出来

        while(i.hasNext()){//遍历
            int g=0;
            String next=i.next().toString();
            for(String s:a){
                if(s.equals(next))
                    g++;
            }
            System.out.println("重复："+g);
        }


    }
}
