import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cxyu on 17-8-6.
 */
public class Get_all implements Runnable{
    static List<String> all = Collections.synchronizedList(new ArrayList<String>());

    public static void get_ids()  {
        //130不知道0的概念是什么
        //706从701到715试试，不知道0代表什么，先设定7为学院
        //没有711，就跟没有130，后面的13
        //理工的120702233-赵阳
        //136，其中1为班级，36为学号，班级顶多只有5个，学号顶多到50
        String id = "1";
        for(int w=3;w<7;w++)
        for (int s = 0; s < 2; s++)//0
            for (int i = 1; i < 10; i++)//7
                for (int d = 0; d < 2; d++)//0
                    for (int g = 1; g < 10; g++)//6
                        for (int a = 1; a < 6; a++)//1
                        {
                            id = id+w + s + i + d + g + a;
                            System.out.println(id);
                            all.add(id);
                            id = "1";
                        }
    }

    public static void start(){
        while (true) {
            try {
                if (all.size() == 0)
                    break;
                String id = all.remove(0);
                Try_get.get_class(id);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]){
        Try_get.start_config();
        get_ids();
        Get_all get_all=new Get_all();
        for(int i=0;i<100;i++){
        Thread one=new Thread(get_all);
        one.start();}
    }

    public void run() {
        start();
    }
}
