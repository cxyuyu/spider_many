package get_cpws_new.get_cpws;

import get_cpws.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-7-10.
 */
public class change_page implements Runnable {
    public static String read(String filename) {
        String words = "";
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                words = words + "\n" + tempString;
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words.substring(1, words.length());
    }

    public static String change_word(String word) {
        String[] lines = word.split("\n");
        String url = lines[0];
        String title = lines[1];
        String content = "";
        for (int i = 2; i < lines.length; i++){

            if(lines[i].length()<2)
                continue;
            if(lines[i].substring(0,1).equals("附"))
                break;
            if(lines[i].length()>8){
                if(lines[i].substring(0,9).equals("本案引用的法律条文"))
                    break;
                if(lines[i].substring(0,5).equals("校对责任人"))
                    break;}
            content = content + "\n" + lines[i];
            if(lines[i].length()>2)
            if(lines[i].substring(0,3).equals("书记员"))
                break;
            //书　记　员
            if(lines[i].length()>5)
            if(lines[i].substring(0,5).equals("书　记　员"))
                break;
        }
        content = content.substring(1, content.length());
        content = Content_handle.handle(content);
        if(content==null)
            return null;
        content = url + "\n" + title + "\n" + content;
        content = content.replace("\n", "\t");
        //System.out.println(content);
        return content;
    }

    public void chuli() {
        for (String path : file_path) {
            File file = new File(path);
            String file_name=file.getName();
            System.out.println(path);
            String word = read(path);
            try {
                String content = change_word(word);
                if(content==null)
                    Get_word.appendMethodB("/home/cxyu/tmp/cpws_source/error_7/"+file_name,word);
                else
                Get_word.appendMethodB("/home/cxyu/tmp/cpws_source/right/"+file_name,content);
            }
            catch (Exception e){
                e.printStackTrace();
                Get_word.appendMethodB("/home/cxyu/tmp/cpws_source/error_7/"+file_name,word);
            }
        }
    }

    public change_page(List<String> paths) {
        file_path = paths;
    }

    public List<String> file_path = new ArrayList<String>();


    //打算一个一个问题解决，然后把error的数据不断分开error——2，error-3，对的数据还是放在一个文件夹里面。
    public static void main(String[] args) {
        File file = new File("/home/cxyu/tmp/cpws_source/error_6/");
        File[] fileArr = file.listFiles();
        List<String> filepath = new ArrayList<String>();
        for (File f : fileArr)
            filepath.add(f.getPath());
        //get所有的路径

        int s = 0;
        //最后一个不放入
        for (int i = 0; i < (filepath.size() / 500 + 1); i++) {
            List<String> paths = filepath.subList(i * 500, (i + 1) * 500);
            change_page change_page = new change_page(paths);
            Thread one = new Thread(change_page);
            one.start();
        }
        //最后一个的处理
        List<String> paths = filepath.subList((filepath.size() / 500 + 1) * 500, filepath.size());
        change_page change_page = new change_page(paths);
        Thread one = new Thread(change_page);
        one.start();


    }

    public void run() {
        chuli();
    }
}
