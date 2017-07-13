package get_cpws_new.get_cpws;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-7-4.
 */
public class read implements Runnable {
    public List<String> file_path=new ArrayList<String>();
    public String name="";
    public String input_path="";
    public String output_path="";


    public read(List<String> d,String name,String in,String out){
        file_path=d;
        this.name=name;
        input_path=in;
        output_path=out;
    }

    public  void write(){
        String one="";
        for(int s=0;s<file_path.size();s++)
            one = one + "\n" + s + "\t" + read(file_path.get(s));
        one = one.substring(1, one.length());
        method2(output_path+name, one);
    }


    public static String read(String filename) {
        String words = "";
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                words = tempString;
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
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





    //读取所有子文件
    public static void main(String[] args) throws Exception {

        String config=read_config();
        System.out.println(config);

        String input_path=config.split("\n")[0];
        //要输入文件所在的文件夹
        String output_path=config.split("\n")[1];
        //要输出文件所在的文件夹



        File file = new File(input_path);
        File[] fileArr = file.listFiles();

        //500个一分配，然后由线程自己去运行
        //最后一个
        List<String> filepath = new ArrayList<String>();
        for (File f : fileArr)
            filepath.add(f.getPath());
        //get所有的路径

        for (int i = 0; i < (filepath.size() / 500 - 1); i++) {
            List<String> filep = new ArrayList<String>();
            filep = filepath.subList(0 + 500 * i, 500 + 500 * i);//不包括500
            //传入异步函数中
            read read=new read(filep,"cpws-"+i,input_path,output_path);
            Thread a=new Thread(read);
            a.start();
        }
        List<String> filep = new ArrayList<String>();
        filep = filepath.subList(0 + 500 * (filepath.size() / 500 - 1), filepath.size());//不包括500
        read read=new read(filep,"cpws-"+(filepath.size() / 500 - 1),input_path,output_path);
        //文件夹名
        Thread a=new Thread(read);
        a.start();

        System.out.println("jieshu");


    }

    public static String read_config(){
        String words = "";
        File file = new File("src/main/java/get_cpws_new/get_cpws/config");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                words =words+"\n"+ tempString;
                //System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        words=words.substring(1,words.length());
        return words;
    }


    public void run() {
        write();
    }
}
