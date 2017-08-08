import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-8-3.
 */
public class Get_ci {
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
    public static void main(String args[]){
        List<String> content=read("/home/cxyu/za/ci");
        String contents="";
        for(String b:content)
            contents=contents+b;
        contents=contents.replace(" ","");
        contents=contents.replace("\n","");
        System.out.println(contents);
        //分割中文字符
        String ci_all[]=contents.split("");

        List<String> cis=new ArrayList<String>();
        for(String ci:ci_all)
            if(!cis.contains(ci))
                cis.add(ci);

        //合并词
        String save="";
        for(String ci:cis)
            save=save+"\n"+ci;
        save=save.substring(1,save.length());

        appendMethodB("/home/cxyu/za/ci2",save);
    }
}
