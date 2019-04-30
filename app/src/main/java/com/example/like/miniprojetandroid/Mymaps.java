package com.example.like.miniprojetandroid;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import  android.support.design.widget.FloatingActionButton;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.util.Date;

public class Mymaps extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener{
    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    Marker marker;
    RelativeLayout ajouter;
    Button btnsave,cancel;
    BreakIterator datetime;
    private StorageReference mStorageRef;
    private EditText editTextName,description,editTextDate;
    private TextView register_txt;
    private  RatingBar rating;
    UserInformation userInformation;
    public static final String FB_STORAGE_PATH = "image/";
    private static final int CAMERA_REQUEST_CODE=1234;
    private Uri imgUri;
   public String tikimage ;
    Button  btncapture;
   public    ProgressDialog dialog ;

    ImageView Image1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymaps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ajouter=(RelativeLayout) findViewById(R.id.ajouter);
        btnsave=(Button)findViewById(R.id.btnsave);
        editTextName=(EditText)findViewById(R.id.editTextName);
        btncapture=(Button) findViewById(R.id.btncapture);
        Image1=(ImageView) findViewById(R.id.image);
        editTextDate=(EditText) findViewById(R.id.editTextDate);
        description=(EditText) findViewById(R.id.description);
        rating=(RatingBar) findViewById(R.id.rating);

        dialog=new ProgressDialog(this);

        btncapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });


        cancel=(Button)findViewById(R.id.cancel);
        mapFragment.getMapAsync(this);
        ChildEventListener mChildEventListener;
        mUsers= FirebaseDatabase.getInstance().getReference("Lieu");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUsers.push().setValue(marker);


   /*     FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child("Lieu")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                        for (DataSnapshot child: children)
                        {
                            UserInformation userInformation = child.getValue(UserInformation.class);
                            Toast.makeText(Mymaps.this, "Data: " + userInformation.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            dialog.setMessage("Uploading image");
            dialog.show();
            imgUri = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Image1.setImageBitmap(bitmap);




            StorageReference ref = mStorageRef.child("photo").child(imgUri.getLastPathSegment());
            //Add file to reference

            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    //Dimiss dialog when success
                    dialog.dismiss();

                    Toast.makeText(Mymaps.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                     tikimage = taskSnapshot.getDownloadUrl().toString();




                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Dimiss dialog when error
                            dialog.dismiss();
                            //Display err toast msg
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            //Show upload progress

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });

        }

    }

    //extraire une image
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;
        LatLng safi = new LatLng(32.302635,-9.228113);
        mMap.addMarker(new MarkerOptions().position(safi).title("Safi,Morroco"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(safi, 3F));
         mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener ()
         {
             public void onMapClick(final LatLng latLng)
             {
             //  View activity_main3.setVisibility(View.VISIBLE);

                 mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         for (DataSnapshot s : dataSnapshot.getChildren()){
                             UserInformation user = s.getValue(UserInformation.class);
                             LatLng location=new LatLng(user.latitude,user.longitude);
                             mMap.addMarker(new MarkerOptions().position(location).title(user.name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });

                 ajouter.setVisibility(View.VISIBLE);
                String  date =DateFormat.getDateTimeInstance().format(new Date());
                StringBuilder ab =new StringBuilder(date);
                ab.deleteCharAt(7);
                 editTextDate.setText(ab);
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                                    String name = editTextName.getText().toString().trim();
                                    String desriptionn = description.getText().toString().trim();
                                    float Rating = rating.getRating();

                        double latitude = latLng.latitude;
                                    double longitude = latLng.longitude;

                                    userInformation = new UserInformation();
                                    userInformation.setName(name);
                                    userInformation.setLatitude(latitude);
                                    userInformation.setLongitude(longitude);
                                    userInformation.setUrl(tikimage);
                                 //   userInformation.setDate(date);
                                    userInformation.setDesc(desriptionn);
                                    userInformation.setRating(Rating);
                                    String uploadId = mUsers.push().getKey();

                                    mUsers.child(uploadId).setValue(userInformation);




                        }







                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ajouter.setVisibility(View.INVISIBLE);

                        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot s : dataSnapshot.getChildren()){
                                    UserInformation user = s.getValue(UserInformation.class);
                                    LatLng location=new LatLng(user.latitude,user.longitude);
                                    mMap.addMarker(new MarkerOptions().position(location).title(user.name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
             }

         });
         googleMap.setOnMarkerClickListener(this);


    }

    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}