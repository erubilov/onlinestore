package jrcom.ru.actionbarcontect.api;

/**
 * Created by eugeniy.rubilov on 14.03.2015.
 */
public class ApiManager {

    private static final String FILE_URL = "https://dl.dropboxusercontent.com/u/26914405/goods_list.txt";


    public static void downloadFile(ResponseCallback callback){
        RequestHelper.loadFile(FILE_URL, callback);
    }

    public interface ResponseCallback{
        public void onSuccess(Object response);
        public void onFailure(int code, String reason);

    }
}
