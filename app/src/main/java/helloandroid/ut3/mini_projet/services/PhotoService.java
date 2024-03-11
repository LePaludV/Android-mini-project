package helloandroid.ut3.mini_projet.services;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import helloandroid.ut3.mini_projet.models.Review;

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

    public void setPhotoFromStorage(String id, ImageView image){
        File imagesDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "InsTable");
        File imageFile = new File(imagesDirectory, id);
        if (imageFile.exists()) {
            Uri imageUri = Uri.fromFile(imageFile);
            Glide.with(this.ctx)
                    .load(imageUri)
                    .into(image);
        }
    }
    public void uploadPhoto(Uri imageUri, String imageName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference();
        StorageReference imageRef = imagesRef.child(imageName);
        imageRef.putFile(imageUri);

        }

}
