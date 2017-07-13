package get_cpws_new.get_cpws;

/**
 * Created by cxyu on 17-7-10.
 */
public class alone_product {
    //一般的文件是怎么处理的
    public static void main(String args[]){
        String word=change_page.read("/home/cxyu/Desktop/cpws_wei_cp/215387-5");
        word=change_page.change_word(word);
        System.out.println(word);
    }
}
