package first.project.com.firstproject;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class Globalclass extends Application {
    Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }





    void ShowToast (Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
