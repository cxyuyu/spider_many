package webmagic;

import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.DATA_CONVERSION;

import cn.edu.fudan.flow.NamedIdentityRecognizerStart;

public class ner extends Thread {
	   public void run() {
		   while(true)
			   try {
				   
    		if (weixin_new_nerchange.all.size() != 0) {
    			List<String> data_ner = new ArrayList(weixin_new_nerchange.all.remove(0));
    			//因为一个很奇怪的bug，所以加了个1
    			String Ner =weixin_new_nerchange.change(weixin_new_nerchange.Ner_content( "1"+data_ner.get(2)+"1"));
    			data_ner.add(Ner);
    			weixin_new_nerchange.all_ner.add(data_ner);
    			System.out.println("get"+Ner);
    		}
			   } catch (Exception e) {
					// TODO: handle exception
				  
				}
    }
	  
	   
    public static void main(String[] args) {
    	ner n=new ner();
    	List<String> data_ner = new ArrayList();
    	for(int i=0;i<6;i++)
    	data_ner.add("　　4月25日晚8时20分许，一辆中巴车悄然停在成都宽窄巷的巷口。李克强及随行10多人走下车来。像一位游客一样，他一连走访了几家小店铺，并买了几样别具特色的旅游纪念品和书籍。在一间啤酒屋前，他还向一位外国游客推荐起了中国的“青岛啤酒”。1");
    	weixin_new_nerchange.all.add(data_ner);
    	System.out.println(weixin_new_nerchange.all.size());
    	n.run();
    	
    	
    	System.out.println("data:"+
    			weixin_new_nerchange.change(weixin_new_nerchange.Ner_content("1.情人节的我们可以组团把电影院座位隔开。　　2.一群人碰到一对情侣就唱“分手快乐”。　　")));
    	if(weixin_new_nerchange.change(weixin_new_nerchange.Ner_content(""))=="")
    			System.out.println("null");
            }
}
