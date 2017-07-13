package get_cpws_new.get_cpws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-7-12.
 */
public class yanz implements Runnable{
    String Filename="";
    public yanz(String filename){
        Filename=filename;
    }
    //获取第一行的数据
    public static void read(String filename) {
        String words = "";
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                words = tempString;
                break;
                //System.out.println(tempString);
            }
            reader.close();
            //System.out.println(filename);
            if(!urls.contains(words)){
                urls.add(words);
                System.out.println(urls.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<String> urls= Collections.synchronizedList(new ArrayList<String>());
    //有几个文件创建几个线程
    public static void main(String args[]){
        File file = new File("/home/cxyu/Desktop/cpws_last/cpws");
        File[] fileArr = file.listFiles();

        for(int i=0;i<fileArr.length;i++) {
            yanz yanz=new yanz(fileArr[i].getPath());
            Thread one = new Thread(yanz);
            one.start();
            if(i%500==0&&i>1){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    public void run() {
        read(Filename);
    }
}
