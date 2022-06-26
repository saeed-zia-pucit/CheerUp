package com.app.cheerthemup.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.app.cheerthemup.R;
import com.app.cheerthemup.interfaces.ItemClickListener;
import com.app.cheerthemup.models.User;
import com.app.cheerthemup.utils.Constants;
import com.app.cheerthemup.views.activities.SigninActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersVH> {

    private ArrayList<User> users;


    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public UsersAdapter(ArrayList<User> users) {
        this.users = users;

    }

    @NonNull
    @Override
    public UsersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UsersVH(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersVH holder, int position) {
        User user = users.get(position);

//        if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

//        }else{


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getColor("user@gmail.com")

        String userName = user.getUserName();
        String firstTwoLetters = userName.substring(0, 2);


        TextDrawable textDrawable = TextDrawable.builder()
                .buildRoundRect(userName.substring(0, 1).toUpperCase(), color1, 48);
        holder.imgUserProfile.setImageDrawable(textDrawable);

//        Random random = new Random();
//        int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(firstTwoLetters, Color.RED);

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        boolean userIdentity = sharedPreferences.getBoolean(Constants.USERIDENTITY,false);

        if (user.isPaidUser()) {
            if (user.isIdentityHidden()) {
                holder.txtUserName.setText("Anonymous");
            } else {
                holder.txtUserName.setText(user.getUserName());

            }
        } else {

            holder.txtUserName.setText(user.getUserName());
        }


//        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UsersVH extends RecyclerView.ViewHolder {


        ImageView imgUserProfile;
        TextView txtUserName;

        public UsersVH(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            imgUserProfile = itemView.findViewById(R.id.img_user_profile);
            txtUserName = itemView.findViewById(R.id.txt_user_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemClickListener.onItemClick(getAdapterPosition(), txtUserName.getText().toString());
                }
            });

        }
    }
}
