package Get_pyxx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-7-20.
 */
public class duiyin {
    public static void find_bs(){
        File[] g=new File("/home/cxyu/Downloads/qyxx_ss").listFiles();
        List<String> Names=new ArrayList<String>();
        for(File s:g){
            //System.out.println(s.getName());
            Names.add(s.getName());
        }

       List<String> w= Get_one.read("/home/cxyu/Downloads/case_appell.txt");
        List<String> w2=new ArrayList<String>();
        List<String> w3= Get_word_one.read("/home/cxyu/Downloads/case_appell.txt");
            for (String name_a : w) {
                String name=name_a.split("\t")[1];
                int ji = 0;
                String name_zan="";
                for (String name_url : Names) {
                    //name原先的数据
                    //name_url后来获取的数据
                    name_zan=name_url;
                    if (name.contains("（")) {
                        String names[] = name.split("（");
                        for (int s = 0; s < names.length; s++) {
                            if (names[s].contains("）")) {
                                String names_2[] = names[s].split("）");
                                for (String one : names_2)
                                    if (!name_url.contains(one))
                                        break;
                                if (!name_url.contains(names[s]))
                                    break;
                            } else if (name_url.contains(names[s]))
                                break;
                        if(s==names.length-1)
                            ji=1;
                        }
                    }
                    else
                        if(name_url.contains(name))
                            ji=1;
                    if(ji==1)
                        break;
                }
                    if (ji == 1) {
                        w2.add(name_a.split("\t")[0] + "\t" + name_zan);

                        System.out.println(name_a.split("\t")[0] + "\t" + name_zan);

                    } else {

                    }

                }
            System.out.println(w2.size());

            List<String> w4=new ArrayList<String>();
        for(String a:w3)
        for(int i=0;i<w2.size();i++)
        {
            String one=w2.get(i);
            if(one.split("\t")[0].equals(a.split("\t")[0])){
                     w4.add(a+"\t"+one.split("\t")[1]);
                     System.out.println(a+"\t"+one.split("\t")[1]);
                    break;
            }
            if(i==w2.size()-1){
                w4.add(a+"\t"+"");
                System.out.println(a+"\t"+"");
            }
        }
        String content="";
        for(int i=0;i<w4.size();i++) {
            content=content+"\n"+w4.get(i);
            System.out.println(w4.get(i));
        }
        content=content.substring(1,content.length());
        Get_word.appendMethodB("/home/cxyu/Downloads/case_appell2.txt",content);
    }
    public static void main(String args[]){
        find_bs();
    }
}
