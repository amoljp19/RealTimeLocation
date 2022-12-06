package com.softaai.realtimelocation.data;

import android.location.Location;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softaai.realtimelocation.data.rxfirestore.RxFirestore;
import com.softaai.realtimelocation.model.SharedLocation;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class DeviceLocationDataStore {

    private static final String TAG = "DeviceLocationDataStore";

    private FirebaseUser mUser;
    private DocumentReference mUserDocRef;
    private DocumentReference mShareLocDocRef;
    private Disposable mShareLocDisposable;

    public DeviceLocationDataStore() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) return;

        mUserDocRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(mUser.getUid());

        SharedLocation sl = new SharedLocation();
        sl.setUserId(mUser.getEmail());
        mUserDocRef
                .set(sl)
                .addOnSuccessListener(aVoid -> Log.v(TAG, "Firestore: User ID Update Success"))
                .addOnFailureListener(e -> Log.v(TAG, "Firestore: User ID Update Failure"));

    }

    public Observable<String> getDeviceUserId() {
        return RxFirestore.getDocument(mUserDocRef)
                .map(documentSnapshot -> documentSnapshot.getString("userId"));
    }

    public Observable<SharedLocation> getSharedLocationUpdate(String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("shared_locations")
                .document(userId);
        return RxFirestore.getDocument(docRef)
                .map(documentSnapshot -> documentSnapshot.toObject(SharedLocation.class));
    }

    public void shareMyLocation(Flowable<Location> locationFlowable) {
        mShareLocDisposable = Flowable
                .combineLatest(locationFlowable, getDeviceUserId().toFlowable(BackpressureStrategy.MISSING), (location, userId) -> {
                    SharedLocation sl = new SharedLocation();
                    sl.setUserId(userId);
                    sl.setLocation(new SharedLocation.LatLong(location.getLatitude(), location.getLongitude()));
                    sl.setName(mUser.getDisplayName());
                    if (mUser.getPhotoUrl() != null)
                        sl.setPhotoUrl(mUser.getPhotoUrl().toString());
                    return sl;
                })
                .subscribe(this::saveToFirebase,
                        throwable -> Log.v(TAG, "shareMyLocation:onError: " + throwable.toString()));
    }

    public Completable stopShareMyLocation() {
        if (mShareLocDisposable != null && !mShareLocDisposable.isDisposed()) {
            mShareLocDisposable.dispose();
            mShareLocDisposable = null;

            if (mShareLocDocRef != null) {
                return Completable.create(emitter -> {
                    mShareLocDocRef.delete()
                            .addOnSuccessListener(aVoid -> emitter.onComplete())
                            .addOnFailureListener(emitter::onError);
                    mShareLocDocRef = null;
                });
            }
        }

        return Completable.complete();
    }

    private void saveToFirebase(SharedLocation sharedLocation) {
        Log.v(TAG, "Update shared location to firebase " + sharedLocation.toString());
        mShareLocDocRef = FirebaseFirestore.getInstance()
                .collection("shared_locations")
                .document(sharedLocation.getUserId());
        mShareLocDocRef
                .set(sharedLocation)
                .addOnSuccessListener(aVoid -> Log.v(TAG, "Firestore: Location Update Success"))
                .addOnFailureListener(e -> Log.v(TAG, "Firestore: Location Update Failure"));
    }

}
