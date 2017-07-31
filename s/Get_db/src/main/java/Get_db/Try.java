package Get_db;

/**
 * Created by cxyu on 17-7-31.
 */
public class Try {
    public static void main(String args[]){
        try {
            Get_id.get_ids("http://asdf/sa/123/asdf");
            int a=12/0;
        }
        catch (Exception e){

            for(StackTraceElement a:e.getStackTrace())
            System.out.println(a);
            System.out.println(e.fillInStackTrace());
            //e.printStackTrace();

        }
    }
}
