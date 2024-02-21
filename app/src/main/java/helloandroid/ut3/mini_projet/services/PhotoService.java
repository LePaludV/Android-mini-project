package helloandroid.ut3.mini_projet.services;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PhotoService {

    Context ctx;

    public PhotoService(Context ctx) {
        this.ctx = ctx;
    }


    public void setPhoto(String id, ImageView image){
        StorageReference storageRef = (FirebaseStorage.getInstance()).getReference();

        StorageReference pathReference = storageRef.child(id);
        Glide.with(this.ctx)
                .load(pathReference)
                .into(image);
    }
}
