package first.project.com.firstproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import first.project.com.firstproject.Contact;
import first.project.com.firstproject.R;
import first.project.com.firstproject.listeners.Listener;

/**
 * Created by user on 8/10/16.
 */
public class PhoneContactsAdapter extends RecyclerView.Adapter<PhoneContactsAdapter.ViewHolder> {
    ArrayList<Contact> list;
    Context mContext;
    boolean state = false;
    Listener.OnItemClickListener itemClickListener;
    Listener.OnItemLongClickListener itemLongClickListener;
    //  Provide a suitable constructor (depends on the kind of dataset)
    public PhoneContactsAdapter(Context context, ArrayList<Contact> list) {
        super();
        this.list = list;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
        ViewGroup viewGroup1 = viewGroup;
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contactitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.contact.setText(list.get(position).getName());
        try {
            viewHolder.userpic.setVisibility(View.VISIBLE);
            if(list.get(position).getImageuri() != null){
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(mContext.getContentResolver(),
                                Uri.parse(list.get(position).getImageuri()));
                viewHolder.userpic.setImageBitmap(bitmap);
                viewHolder.defaultimg.setVisibility(View.GONE);
                viewHolder.userpic.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.defaultimg.setText(String.valueOf(list.get(position).getName().charAt(0)).toUpperCase());
                viewHolder.userpic.setVisibility(View.GONE);
                viewHolder.defaultimg.setVisibility(View.VISIBLE);
                // viewHolder.userpic.setImageResource(R.drawable.defalut_contact);
            }
            if(list.get(position).isSelected()) {
                viewHolder.mark.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.mark.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(list.get(position),position,view);
            }
        });
        viewHolder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemLongClickListener.onItemLongClick(list.get(position),position);
                return false;
            }
        });
        animate(viewHolder);
    }




    public   void setItemClickListener(Listener.OnItemClickListener listener){
        this.itemClickListener = listener;
    }
    public void setItemClickLongListener(Listener.OnItemLongClickListener listener){
        this.itemLongClickListener = listener;
    }


    public void deleteItem(Contact contact){

        int position = list.indexOf(contact);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteitems(ArrayList<Integer> item){


        for(int i : item) {
            list.remove(i);
        }
        notifyItemRangeChanged(0,list.size());



    }

    public Contact getItem(int position){

        return  list.get(position);
    }

    public int getPosition(Contact contact){

        return list.indexOf(contact);
    }

    public char  getFirstletter(int position){
        if(Character.isAlphabetic(list.get(position).getName().charAt(0)))
            return Character.toUpperCase(list.get(position).getName().charAt(0));
        else
            return list.get(position).getName().charAt(0);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView contact;
        public TextView defaultimg;
        public ImageView userpic;
        public ImageView mark;
        public ViewHolder(View itemView) {
            super(itemView);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.contactitem);
            contact = (TextView) itemView.findViewById(R.id.contactname);
            userpic = (ImageView) itemView.findViewById(R.id.contactimg);
            defaultimg = (TextView)itemView.findViewById(R.id.defaultpic);
            mark = (ImageView)itemView.findViewById(R.id.mark);
        }
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

}


