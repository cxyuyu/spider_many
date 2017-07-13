package get_cpws_new.get_cpws;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sang on 7/3/17.
 */
public class DateFormatTransfer {
  public static Map <String, String> num = new HashMap<String,String>();

  public static void main(String[] args) {

    System.out.println(Trans("二〇一六年七月××日"));
    System.out.println(Trans("二9一五年一月二十九日"));
    if(Trans("二9一五年一月二十九日").contains("null"))
      System.out.println("有空");
    //二O一五年十一月十八日
  }

  
  public static String Trans(String chDate){
    num.put("××","??");
    num.put("一","1");
    num.put("二","2");
    num.put("三","3");
    num.put("四","4");
    num.put("五","5");
    num.put("六","6");
    num.put("七","7");
    num.put("八","8");
    num.put("九","9");
    num.put("O","0");
    num.put("○","0");
    num.put("〇","0");
    num.put("0","0");
    num.put("Ο","0");
    num.put("�","0");
    num.put("Ｏ","0");
    num.put("十","10");
    num.put("十一","11");
    num.put("十二","12");
    num.put("十三","13");
    num.put("十四","14");
    num.put("十五","15");
    num.put("十六","16");
    num.put("十七","17");
    num.put("十八","18");
    num.put("十九","19");
    num.put("二十","20");
    num.put("二十一","21");
    num.put("二十二","22");
    num.put("二十三","23");
    num.put("二十四","24");
    num.put("二十五","25");
    num.put("二十六","26");
    num.put("二十七","27");
    num.put("二十八","28");
    num.put("二十九","29");
    num.put("三十","30");
    num.put("三十一","31");



    char[] chYear = chDate.substring(0,chDate.indexOf("年")).toCharArray();
    StringBuffer enYear = new StringBuffer();
    for (int i = 0; i < chYear.length; i++){
      enYear.append(num.get(String.valueOf(chYear[i])));
    }

    String chMonth = chDate.substring(chDate.indexOf("年")+1,chDate.indexOf("月"));
    String enMonth = num.get(chMonth);

    String chDay = chDate.substring(chDate.indexOf("月")+1, chDate.indexOf("日"));
    String enDay = num.get(chDay);

    StringBuffer enDate = enYear.append("-").append(enMonth).append("-").append(enDay);

    return enDate.toString();
  }
}
