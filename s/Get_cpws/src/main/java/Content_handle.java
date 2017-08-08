import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxyu on 17-7-3.
 */
public class Content_handle {

    public static String handle(String content) {

        //输入的文本要很有规则，
        String hang[] = content.split("\n");
        List<String> Lines = new ArrayList<String>();
        for (String x : hang) {
            //System.out.println("hang   "+x);
            Lines.add(x);
        }

        int save_not = 0;
        //当save_not为1时就不保存。

        //记录p的位置
        int p_id = 0;



        //上诉人，被上诉人,法定代表，申请人，再申请，委托代理人
        for (int q = 5; q < Lines.size(); q++) {
            String line = Lines.get(q);
            String line_1 = line.substring(2, line.length());
            String line_2 = line.substring(0, 2);
            if (line_2.equals("代理") || line_2.equals("委托"))
                continue;
            if (line_1.contains(line_2)) {
                p_id = q;
                //记录下最后一行的位置
                //System.out.println(Lines.get(q));
                break;
            }
            if(line_2.equals("原告")&&line_1.contains("本院"))
            {
                p_id=q;
                break;
            }
            if(line_2.equals("本院")){
                p_id=q;
                break;
            }
        }
        //切割出上诉人的一大块，以便于分析


        List<String> ask_1 = new ArrayList<String>();
        //上诉人
        List<String> ask_2 = new ArrayList<String>();
        //再审申请
        List<String> ask_3 = new ArrayList<String>();
        //上诉与再审申请的委托人
        List<String> answer_1 = new ArrayList<String>();
        //被上诉人
        List<String> answer_2 = new ArrayList<String>();
        //被再审申请
        List<String> answer_3 = new ArrayList<String>();
        //被的委托人

        int weituoji = 0;
        for (int a = 0; a < p_id; a++) {
            String line = Lines.get(a);
            line = line.replace("：", "");
            if (line.substring(0, 2).equals("上诉") || line.substring(0, 2).equals("原告")) {
                weituoji = 0;
                if (line.substring(0, 2).equals("上诉")) {
                    int ren_num = line.split("人")[0].length();
                    line = line.substring(ren_num + 1, line.length());
                }
                if (line.contains("公司"))
                    line = "公司：" + line;
                //以人来分割


                //加一小段，因为有负责人，法定代表人等
                for (int b = a + 1; b < p_id + 1; b++) {
                    if (add(Lines, b, p_id)) {
                        line = line + Lines.get(b);
                    } else
                        break;
                }
                ask_1.add(line);
                continue;
            }
            if (line.substring(0, 2).equals("再审") || line.substring(0, 2).equals("申请")) {
                weituoji = 0;
                int ren_num = line.split("人")[0].length();
                line = line.substring(ren_num + 1, line.length());
                if (line.contains("公司"))
                    line = "公司：" + line;
                //以人来分割
                ask_2.add(line);
                //加一小段，因为有负责人，法定代表人等
                for (int b = a + 1; b < p_id + 1; b++)
                    if (add(Lines, b, p_id))
                        line = line + Lines.get(b);
                    else
                        break;
                continue;
            }
            if (line.substring(0, 2).equals("委托")) {
                if (weituoji == 0) {
                    int ren_num = line.split("人")[0].length();
                    line = line.substring(ren_num + 1, line.length());
                    if (line.contains("公司"))
                        line = "公司：" + line;
                    //以人来分割
                    ask_3.add(line);
                    //加一小段，因为有负责人，法定代表人等
                    for (int b = a + 1; b < p_id + 1; b++)
                        if (add(Lines, b, p_id))
                            line = line + Lines.get(b);
                        else
                            break;
                    continue;
                }
            }
            if (line.substring(0, 2).equals("被上") || line.substring(0, 2).equals("被告")) {
                weituoji = 1;
                if (line.substring(0, 2).equals("被上")) {
                    int ren_num = line.split("人")[0].length();
                    line = line.substring(ren_num + 1, line.length());
                }
                if (line.contains("公司"))
                    line = "公司：" + line;
                //以人来分割
                answer_1.add(line);
                //加一小段，因为有负责人，法定代表人等
                for (int b = a + 1; b < p_id + 1; b++)
                    if (add(Lines, b, p_id))
                        line = line + Lines.get(b);
                    else
                        break;
                continue;
            }
            if (line.substring(0, 2).equals("被申")) {
                weituoji = 1;
                int ren_num = line.split("人")[0].length();
                line = line.substring(ren_num + 1, line.length());
                if (line.contains("公司"))
                    line = "公司：" + line;
                //以人来分割
                answer_2.add(line);
                //加一小段，因为有负责人，法定代表人等
                for (int b = a + 1; b < p_id + 1; b++)
                    if (add(Lines, b, p_id))
                        line = line + Lines.get(b);
                    else
                        break;
                continue;
            }
            if (line.substring(0, 2).equals("委托")) {
                if (weituoji == 1) {
                    int ren_num = line.split("人")[0].length();
                    line = line.substring(ren_num + 1, line.length());
                    if (line.contains("公司"))
                        line = "公司：" + line;
                    //以人来分割
                    answer_3.add(line);
                    //加一小段，因为有负责人，法定代表人等
                    for (int b = a + 1; b < p_id + 1; b++)
                        if (add(Lines, b, p_id))
                            line = line + Lines.get(b);
                        else
                            break;
                    continue;
                }
            }
        }
        String ask_1_content = "";
        for (String one : ask_1)
            ask_1_content = ask_1_content + "|" + one;
        if (ask_1_content.length() > 0)
            ask_1_content = ask_1_content.substring(1, ask_1_content.length());
        //System.out.println("上诉" + ask_1_content);

        String ask_2_content = "";
        for (String one : ask_2)
            ask_2_content = ask_2_content + "|" + one;
        if (ask_2_content.length() > 0)
            ask_2_content = ask_2_content.substring(1, ask_2_content.length());
        //System.out.println("再审申请" + ask_2_content);

        String ask_3_content = "";
        for (String one : ask_3)
            ask_3_content = ask_3_content + "|" + one;
        if (ask_3_content.length() > 0)
            ask_3_content = ask_3_content.substring(1, ask_3_content.length());
        //System.out.println("委托" + ask_3_content);

        String answer_1_content = "";
        for (String one : answer_1)
            answer_1_content = answer_1_content + "|" + one;
        if (answer_1_content.length() > 0)
            answer_1_content = answer_1_content.substring(1, answer_1_content.length());
        //System.out.println("被上诉" + answer_1_content);


        String answer_2_content = "";
        for (String one : answer_2)
            answer_2_content = answer_2_content + "`" + one;
        if (answer_2_content.length() > 0)
            answer_2_content = answer_2_content.substring(1, answer_2_content.length());
        //System.out.println("被审" + answer_2_content);


        String answer_3_content = "";
        for (String one : answer_3)
            answer_3_content = answer_3_content + "`" + one;
        if (answer_3_content.length() > 0)
            answer_3_content = answer_3_content.substring(1, answer_3_content.length());
        //System.out.println("委托" + answer_3_content);


        //审判原因
        String why = "";
        int why_line_end = 0;
        int why_line_start = 0;
        for (int d = 0; d < Lines.size(); d++) {
            String s = Lines.get(d);
            if (s.substring(0, 2).equals("本院")) {
                why_line_start = d;
                why = s;
                if (s.substring(s.length() - 3, s.length()).equals("如下：")) {
                    why_line_end = d;
                    break;
                }
                for (int w = d + 1; w < Lines.size(); w++) {
                    s = Lines.get(w);
                    why = why + s;
                    if (s.substring(s.length() - 3, s.length()).equals("如下：")) {
                        why_line_end = w;
                        break;
                    }
                }
                break;
            }
        }


        //时间处理
        String time="";
        for(int i=1;i<5;i++)
        {
            String line=Lines.get(Lines.size()-i);
            if (line.contains("年") && line.contains("月") && line.contains("二"))
            {
                System.out.println(line);
                save_not = Lines.size()-i;
                time=line;
                break;
            }
        }
        System.out.println("time" + time);
        time = DateFormatTransfer.Trans(time).toString();

        System.out.println("time-c" + time);

        String shuji = "";
        List<String> shujis=new ArrayList<String>();
        String daili = "";
        List<String> dailis=new ArrayList<String>();
        String shenpan = "";
        List<String> shenpans=new ArrayList<String>();
        String shenpan_boss = "";
        int name_size = 0;
        for(int i=1;i<8;i++) {
            String line = Lines.get(Lines.size() - i);
            if (line.length() < 10) {
                if (line.contains("审判员")||line.contains("审判长"))
                    name_size = i;
                if(line.contains("审判员")&&!line.contains("代理"))
                    shenpans.add(line);
                if(line.contains("书记"))
                    shujis.add(line);
                if (line.contains("代理审判"))
                    dailis.add(line);
                if(line.contains("审判长"))
                    shenpan_boss=line;
            }

        }

        //将名字重叠
        for(String a:shenpans){
            shenpan=shenpan+"\t"+a;
        }
        if(shenpan.length()>2)
        shenpan=shenpan.substring(1,shenpan.length());

        for(String b:dailis)
            daili=daili+"\t"+b;
        if(daili.length()>2)
        daili=daili.substring(1,daili.length());

        for (String a: shujis)
            shuji=shuji+"\t"+a;
        if(shuji.length()>2)
        shuji=shuji.substring(1,shuji.length());



        //System.out.println(why);
        //审判结果
        String result = "";
        for (int r = why_line_end + 1; r < Lines.size() - name_size; r++)
            result = result + Lines.get(r);
        //System.out.println("result"+result);
        //事件的过程
        String shijian = "";
        for (int e = p_id; e < why_line_start; e++)
            shijian = shijian + Lines.get(e);
        //System.out.println(shijian);

        String id = Lines.get(2);
        content =  ask_1_content + "\n" + ask_2_content + "\n" + ask_3_content + "\n"
                + answer_1_content + "\n" + answer_2_content + "\n" + answer_3_content + "\n"
                + shijian + "\n" + why + "\n" + result + "\n" + shenpan_boss + "\n" + shenpan + "\n" +
                daili + "\n" + time + "\n" + shuji;
        //System.out.println(content);

        //当上诉或被上诉为空时，
        if (ask_2_content.length() == 0 && ask_1_content.length() == 0) {
            System.out.println("上诉人为空");
            return null;
        }
        if (answer_1_content.length() == 0 && answer_2_content.length() == 0) {
//            for (String s : ask_3)
//                System.out.println(s);
            System.out.println("被上诉人为空");
            return null;
        }
        //当time为null为空时，
        if (time.contains("null"))
            return null;


        return content;
    }

    //加一小段，因为有负责人，法定代表人等
    public static boolean add(List<String> ps, int a, int p_id) {
        //ps所有的p值，a之前到哪一段了，p——id块的大小


        String line_2 = ps.get(a);
        if (line_2.substring(0, 2).equals("被上") ||
                line_2.substring(0, 2).equals("上诉") ||
                line_2.substring(0, 2).equals("被申") ||
                line_2.substring(0, 2).equals("再审") ||
                line_2.substring(0, 2).equals("委托") ||
                line_2.substring(0, 2).equals("原告") ||
                line_2.substring(0, 2).equals("被告") ||
                line_2.substring(0, 2).equals("申请")
                )
            return false;
        else
            return true;

    }

}
