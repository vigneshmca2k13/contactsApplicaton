package first.project.com.firstproject.listeners;

import android.view.View;

import first.project.com.firstproject.Contact;

/**
 * Created by user on 8/10/16.
 */
public class Listener {


    public interface OnItemClickListener {
        void onItemClick(Contact item, int position, View view);
    }

    public  interface  OnItemLongClickListener{
        void onItemLongClick(Contact item,int position);

    }
}
