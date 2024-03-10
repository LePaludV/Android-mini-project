package helloandroid.ut3.mini_projet.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import helloandroid.ut3.mini_projet.R;
import helloandroid.ut3.mini_projet.Shaker;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Bitmap> droppedBitmaps = new ArrayList<>();
    private List<float[]> coordinatesDroppedBitmaps = new ArrayList<>();

    private static float[][]FILTERS ={
            {1,1,1},
            {1,0.5f,1},
            {1,1,0.5f,},
            {0.5f,1,1},
            {0.67f, 0.28f, 0.94f},
            {0.55f, 0.71f, 0.42f},
            {0.09f, 0.55f, 0.77f},
            {0.32f, 0.65f, 0.16f},
            {0.84f, 0.55f, 0.19f},
            {0.55f, 0.01f, 0.76f},
            {0.68f, 0.87f, 0.36f},
            {0.52f, 0.49f, 0.99f},
            {0.08f, 0.67f, 0.47f},
            {0.55f, 0.34f, 0.21f},
            {0.74f, 0.51f, 0.39f},
            {0.21f, 0.21f, 0.93f},
            {0.68f, 0.65f, 0.26f},
            {0.74f, 0.21f, 0.76f},
            {0.55f, 0.65f, 0.57f},
            {0.23f, 0.87f, 0.62f},
            {0.36f, 0.07f, 0.15f},
            {0.97f, 0.54f, 0.79f},
            {0.21f, 0.46f, 0.88f},
            {0.03f, 0.64f, 0.11f},
            {0.32f, 0.26f, 0.05f},
            {0.58f, 0.67f, 0.61f},
            {0.71f, 0.77f, 0.91f},
            {0.42f, 0.55f, 0.82f},
            {0.61f, 0.93f, 0.45f},
            {0.34f, 0.13f, 0.22f},
            {0.56f, 0.21f, 0.55f},
            {0.18f, 0.24f, 0.39f},
            {0.86f, 0.61f, 0.35f},
            {0.77f, 0.02f, 0.53f}};

    private int indice = 0;

    TextView filterInfo;

    Shaker s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.imageView);
        filterInfo = findViewById(R.id.filterInfo);
        s = new Shaker(this);
        s.start();
        Uri imageUri = getIntent().getParcelableExtra("imageUri");
        Glide.with(this)
                .load(imageUri)
                .into(imageView);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(this, getBitmaps());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DROP:
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        ImageView droppedImage = (ImageView) view;
                        Bitmap bitmap = ((BitmapDrawable) droppedImage.getDrawable()).getBitmap();
                        if (bitmap != null) {
                            Bitmap mutableBitmap = Bitmap.createBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap().getWidth(), ((BitmapDrawable) imageView.getDrawable()).getBitmap().getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(mutableBitmap);
                            canvas.drawBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap(), 0, 0, null);
                            float left = event.getX() - (bitmap.getWidth() / 2f);
                            float top = event.getY() / 2f;
                            canvas.drawBitmap(bitmap, left, top, null);
                            imageView.setImageBitmap(mutableBitmap);
                            droppedBitmaps.add(bitmap);
                            coordinatesDroppedBitmaps.add(new float[]{left, top});
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // do nothing
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.stop();
                finish();
            }
        });
        findViewById(R.id.buttonValidate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToDevice();
            }
        });
        findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indice=(indice+1)%FILTERS.length;
                changeFilter();
            }

        });
        findViewById(R.id.buttonPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indice=(indice-1+FILTERS.length)%FILTERS.length;
                changeFilter();
            }

        });
    }

    private void saveImageToDevice() {
        Bitmap originalBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Bitmap mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(applyFilter());
        canvas.drawBitmap(originalBitmap, 0, 0, paint);
        for( int i = 0; i<droppedBitmaps.size();i++){
            canvas.drawBitmap(droppedBitmaps.get(i), coordinatesDroppedBitmaps.get(i)[0],coordinatesDroppedBitmaps.get(i)[1], null);
        }
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/InsTable");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(this, "Image enregistrée avec succès", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de l'enregistrement de l'image", e);
            Toast.makeText(this, "Erreur lors de l'enregistrement de l'image", Toast.LENGTH_SHORT).show();
        }
    }
    public ColorFilter applyFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        float red = FILTERS[indice][0];
        float green =  FILTERS[indice][1];
        float blue = FILTERS[indice][2];
        matrix.set(new float[] {
                red, 0, 0, 0, 0, // red
                0, green, 0, 0, 0, // green
                0, 0, blue, 0, 0, // blue
                0, 0, 0, 1, 0 // alpha
        });

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        return filter;
    }

    public void onShakeDetected() {
        indice = getRandomNumber(0,FILTERS.length);
        changeFilter();
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void changeFilter(){
        Paint paint = new Paint();
        paint.setColorFilter(applyFilter());
        imageView.setColorFilter(paint.getColorFilter());
        filterInfo.setText(indice==0 ? "No filter selected" : "Filter "+indice+"/"+FILTERS.length);
    }

    private Bitmap[] stickers = {
            BitmapFactory.decodeResource(getResources(), R.drawable.lips),
            BitmapFactory.decodeResource(getResources(), R.drawable.deer),
            BitmapFactory.decodeResource(getResources(), R.drawable.pouce),
            BitmapFactory.decodeResource(getResources(), R.drawable.a),
            BitmapFactory.decodeResource(getResources(), R.drawable.ae),
            BitmapFactory.decodeResource(getResources(), R.drawable.ag),
            BitmapFactory.decodeResource(getResources(), R.drawable.ar),
            BitmapFactory.decodeResource(getResources(), R.drawable.as),
            BitmapFactory.decodeResource(getResources(), R.drawable.at),
            BitmapFactory.decodeResource(getResources(), R.drawable.at),
            BitmapFactory.decodeResource(getResources(), R.drawable.az),
            BitmapFactory.decodeResource(getResources(), R.drawable.ds),
            BitmapFactory.decodeResource(getResources(), R.drawable.ea),
            BitmapFactory.decodeResource(getResources(), R.drawable.sd),
            BitmapFactory.decodeResource(getResources(), R.drawable.za),
    };
    private List<Bitmap> getBitmaps() {
        List<Bitmap> bitmaps = new ArrayList<>();
        for(Bitmap a : stickers){
            bitmaps.add(a);
        }
        return bitmaps;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<Bitmap> mData;
        private Context mContext;

        public MyAdapter(Context context, List<Bitmap> data) {
            mData = data;
            mContext = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(null, shadowBuilder, v, 0);
                    return false;
                }
            });
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Bitmap bitmap = mData.get(position);
            holder.imageView.setImageBitmap(bitmap);
            holder.imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(data, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}
