package get_cpws_new.get_cpws;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-7-12.
 */
public class error_read implements Runnable {
    //读取文件
    public static List<String> read(String filename) {
        String words = "";
        File file = new File(filename);
        BufferedReader reader = null;
        List<String> lines = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                lines.add(tempString);
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    //放入文件名就开始干活。
    public static void save(String filename){
        List<String> words=read(filename);
        for(String s:words){
            String all_word[]=s.split("\t");
            if(!urls.contains(all_word[1])){
                urls.add(all_word[1]);
                System.out.println(all_word[1]);
                String content="";
                for(int i=1;i<all_word.length;i++)
                    content=content+"\t"+all_word[i];
                content=content.substring(1,content.length());
                all.add(content);
                System.out.println(all.size());
            }
        }
        System.out.println("page   "+files_path.get(page-1));
        page--;
    }

    public static void method2(String fileName, String content) {
        System.out.println(fileName);
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    static List<String> urls=Collections.synchronizedList(new ArrayList<String>());
    //所有的数据存入这里面，当没有时
    static List<String> all=Collections.synchronizedList(new ArrayList<String>());
    static List<String> files_path = Collections.synchronizedList(new ArrayList<String>());
    static Integer page = 0;

    //一个文件一个线程，先读完再处理,休息几秒
    //输入是文件
    //输出也是500个文件
    //延迟4秒
    //当读完，没有重复之后，进行数据保存。
    //url来进行区分。
    public static void main(String args[]) {
        File[] files = new File("/home/cxyu/tmp/cpws_new").listFiles();
        for (int i = 0; i < files.length; i++)
            files_path.add(files[i].getPath());

        page = files_path.size();
        //输出文件夹
        error_read error_read=new error_read();
        Thread one=new Thread(error_read);
        for(String filename:files_path){
            save(filename);
        }


        //最后的检查
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (page == 0) {
                //执行写入函数
                String content="";
                for(int i=0;i<(all.size()/500-1);i++)
                {
                    for(int s=0;s<501;s++){
                    content=content+"\n"+i*500+s+"\t"+all.get(i*500+s);
                    }
                    content=content.substring(1,content.length());
                    method2("/home/cxyu/Desktop/cpws_out/out/"+i,content);
                    content="";
                }
                for(int s=(all.size()/500-1)*500;s<all.size();s++){
                    content=content+"\n"+s+"\t"+all.get(s);
                }
                content=content.substring(1,content.length());
                method2("/home/cxyu/Desktop/cpws_out/out/"+all.size()/500,content);

                break;
            }
        }


    }

    public void run() {

    }
}
