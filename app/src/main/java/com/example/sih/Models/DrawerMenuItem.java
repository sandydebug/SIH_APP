package com.example.sih.Models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.sih.CropPost;
import com.example.sih.Loggedin;
import com.example.sih.Login;
import com.example.sih.MainActivity;
import com.example.sih.R;
import com.google.firebase.auth.FirebaseAuth;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;


@Layout(R.layout.drawer_item)
public class DrawerMenuItem {

    public static final int DRAWER_MENU_ITEM_PROFILE = 1;
    public static final int DRAWER_MENU_ITEM_POST = 2;
    public static final int DRAWER_MENU_ITEM_CONTACT = 3;
    public static final int DRAWER_MENU_ITEM_SETTINGS = 4;
    public static final int DRAWER_MENU_ITEM_ABOUT = 5;
    public static final int DRAWER_MENU_ITEM_LOGOUT = 6;

    private int mMenuPosition;
    private Context mContext;
    private DrawerCallBack mCallBack;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

//    @View(R.id.itemIcon)
//    private ImageView itemIcon;

    public DrawerMenuItem(Context context, int menuPosition) {
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_PROFILE:
                itemNameTxt.setText("Profile");
//                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_account_circle_black_18dp));
                break;
            case DRAWER_MENU_ITEM_CONTACT:
                itemNameTxt.setText("Contact Us");
                break;
            case DRAWER_MENU_ITEM_POST:
                itemNameTxt.setText("Post");
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                itemNameTxt.setText("Settings");
                break;
            case DRAWER_MENU_ITEM_ABOUT:
                itemNameTxt.setText("About Us");
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                itemNameTxt.setText("Logout");
                break;
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick(){
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_PROFILE:
                Toast.makeText(mContext, "Profile", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onProfileMenuSelected();
                break;
            case DRAWER_MENU_ITEM_POST:
                Toast.makeText(mContext, "Post", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(context, CropPost.class));
                if(mCallBack != null)mCallBack.onPostMenuSelected();
                break;
            case DRAWER_MENU_ITEM_CONTACT:
                Toast.makeText(mContext, "Contact Us", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onContactMenuSelected();
            case DRAWER_MENU_ITEM_SETTINGS:
                Toast.makeText(mContext, "Settings", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onSettingsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_ABOUT:
                Toast.makeText(mContext, "About Us", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onAboutMenuSelected();
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                Toast.makeText(mContext, "Logout", Toast.LENGTH_SHORT).show();
                if(firebaseAuth.getCurrentUser()!= null){
                    new AlertDialog.Builder(mContext)
                            .setTitle("LOGOUT")
                            .setMessage("Are you sure you want to logout ?  I suggest spend some more time :) ")

                            .setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseAuth.signOut();
                                    //mGoogleSignInClient.signOut();
                                    //finish();
                                    //startActivity(new Intent(Loggedin.this, Login.class));
                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), null)
                            .setIcon(android.R.drawable.ic_lock_power_off)
                            .show();}
                if(mCallBack != null)mCallBack.onLogoutMenuSelected();
                break;
        }
    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack{
        void onProfileMenuSelected();
        void onPostMenuSelected();
        void onContactMenuSelected();
        void onSettingsMenuSelected();
        void onAboutMenuSelected();
        void onLogoutMenuSelected();
    }
}