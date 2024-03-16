package helloandroid.ut3.mini_projet.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

import helloandroid.ut3.mini_projet.models.Review;

public class ReviewService {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reviewsCollection = db.collection("reviews");

    public void addReview(Review review) {
        reviewsCollection.add(review);
    }
    public void getReview(String reviewId, final OnReviewCallback callback) {
        Task<DocumentSnapshot> task = reviewsCollection.document(reviewId).get();

        task.addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Review review = documentSnapshot.toObject(Review.class);
                callback.onSuccess(review);
            } else {
                callback.onFailure("Review not found");
            }
        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }


    public void getAllReviews(final OnReviewsCallback callback) {
        Task<QuerySnapshot> task = reviewsCollection.get();

        task.addOnSuccessListener(queryDocumentSnapshots -> {
            List<Review> reviews = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Review review = documentSnapshot.toObject(Review.class);
                reviews.add(review);
            }
            callback.onSuccess(reviews);
        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getAllReviewsByRestaurantId(String restaurantId, final OnReviewsCallback callback) {
        Task<QuerySnapshot> task = reviewsCollection.whereEqualTo("restaurant_id", restaurantId).get();

        task.addOnSuccessListener(queryDocumentSnapshots -> {
            List<Review> reviews = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Review review = documentSnapshot.toObject(Review.class);
                reviews.add(review);
            }
            callback.onSuccess(reviews);
        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
    public interface OnReviewsCallback {
        void onSuccess(List<Review> reviews);
        void onFailure(String errorMessage);
    }


    public interface OnReviewCallback {
        void onSuccess(Review review);
        void onFailure(String errorMessage);
    }
}