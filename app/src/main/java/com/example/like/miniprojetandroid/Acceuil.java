package com.example.like.miniprojetandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.like.miniprojetandroid.Place;
import com.example.like.miniprojetandroid.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import java.nio.channels.Channel;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Acceuil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mMap;
    Button button1;
    EditText editText;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    Marker marker;
    RelativeLayout ajouter,recherche;
    Button btnsave,cancel;
    BreakIterator datetime;
    private StorageReference mStorageRef;
    private EditText editTextName,description,editTextDate;
    private TextView register_txt;
    private RatingBar rating;
    UserInformation userInformation;
    public static final String FB_STORAGE_PATH = "image/";
    private static final int CAMERA_REQUEST_CODE=1234;
    private Uri imgUri;
    public String tikimage ;
    Button  btncapture;
    public ProgressDialog dialog ;
    //TextView locationTv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ImageView Image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        createNotificationChannel();

        button1=(Button)findViewById(R.id.button1);
        editText=(EditText) findViewById(R.id.searchView1);
        //locationTv=(TextView) findViewById(R.id.lat);
        ajouter=(RelativeLayout) findViewById(R.id.ajouter);
        recherche=(RelativeLayout) findViewById(R.id.recherche);
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
       recherche.setVisibility(View.VISIBLE);

        cancel=(Button)findViewById(R.id.cancel);
        mUsers= FirebaseDatabase.getInstance().getReference("Lieu");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUsers.push().setValue(marker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//creer
        //ecoles*/
    /*  DatabaseReference myRefecole = database.getReference("ecoles");

        Place res = new Place(1, 32.328726, -9.267711, "Faculté Polydisciplinaire de Safi");
        String id =  myRefecole.push().getKey();
        myRefecole.child(id).setValue(res);

        Place res2 = new Place(1, 32.326726, -9.263575, "ENSA de Safi");
        String id2 =  myRefecole.push().getKey();
        myRefecole.child(id2).setValue(res2);

        Place res3 = new Place(1, 32.307747, -9.216549, "EST de Safi");
        String id3 =  myRefecole.push().getKey();
        myRefecole.child(id3).setValue(res3);

        /**bancque*/
     /*  DatabaseReference myRefbanques  = database.getReference("banques");

        Place res4 = new Place(1, 32.289504, -9.241605, "BARID AL-MAGHRIB");
        String id4 =  myRefbanques.push().getKey();
        myRefbanques.child(id4).setValue(res4);

        Place res5 = new Place(1, 32.283567, -9.225553, "CIH Bank");
        String id5 =  myRefbanques.push().getKey();
        myRefbanques.child(id5).setValue(res5);

        /**sbitar*/


    /*  DatabaseReference myRefsbitar  = database.getReference("pharmacies");

        Place res6 = new Place(1, 32.288366, -9.236580, "Hôpital Mohamed V");
        String id6 =  myRefsbitar.push().getKey();
        myRefsbitar.child(id6).setValue(res6);

        Place res7 = new Place(1, 32.311941, -9.232105, "???????? ?????? hôpital Qatarian");
        String id7 =  myRefsbitar.push().getKey();
        myRefsbitar.child(id7).setValue(res7);


        /**shopping*/

      /*  DatabaseReference myRefshopping  = database.getReference("shopping");

        Place res8 = new Place(1, 32.288919, -9.226742, "Acima");
        String id8 =  myRefshopping.push().getKey();
        myRefshopping.child(id8).setValue(res8);

        Place res9 = new Place(1,32.298184, -9.215572, "Marjane");
        String id9 =  myRefshopping.push().getKey();
        myRefshopping.child(id9).setValue(res9);

/**Entrerise*/
    /*   DatabaseReference myRefentre  = database.getReference("entreprises");

        Place res10 = new Place(1, 32.288366, -9.236580, "Centre de Compétences Industrielles OCP SAFI ???? ???????? ");
        String id10 =  myRefentre.push().getKey();
        myRefentre.child(id10).setValue(res10);

        Place res11 = new Place(1, 32.293794, -9.229863, "Service médical OCP");
        String id11 =  myRefentre.push().getKey();
        myRefentre.child(id11).setValue(res11);*/




    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String g = editText.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    // Getting a maximum of 3 Address that matches the input
                    // text
                    addresses = geocoder.getFromLocationName(g, 3);
                    if (addresses != null && !addresses.equals(""))
                        search(addresses);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Lieu Introuvable!", Toast.LENGTH_LONG).show();

                }

            }
        });
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

                    Toast.makeText(Acceuil.this, "Image uploaded", Toast.LENGTH_SHORT).show();
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







    protected void search(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        double home_long = address.getLongitude();
        double home_lat = address.getLatitude();
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(editText.getText().toString());

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));



        // get your custom_toast.xml ayout
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a dummy image
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.mipmap.ic_launcher);

        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Latitude:" + address.getLatitude() + ", Longitude:"
                + address.getLongitude()+ ", Pays:"+ address.getCountryName()+ ", Place:"+ address.getLocality());

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acceuil, menu);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            mMap.clear();
            DatabaseReference myRef = database.getReference("restaurants");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot s : dataSnapshot.getChildren()) {

                        Place res = s.getValue(Place.class);


                        LatLng location=new LatLng(res.getLat(),res.getLng());
                        mMap.addMarker(new MarkerOptions().position(location).title(res.getNom()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }
        else if (id == R.id.nav_gallery) {
            mMap.clear();
            DatabaseReference myRef = database.getReference("ecoles");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot s : dataSnapshot.getChildren()) {

                        Place res = s.getValue(Place.class);


                        LatLng location=new LatLng(res.getLat(),res.getLng());
                        mMap.addMarker(new MarkerOptions().position(location).title(res.getNom()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        } else if (id == R.id.nav_slideshow) {
            DatabaseReference myRef = database.getReference("banques");
            mMap.clear();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot s : dataSnapshot.getChildren()) {

                        Place res = s.getValue(Place.class);


                        LatLng location=new LatLng(res.getLat(),res.getLng());

                        mMap.addMarker(new MarkerOptions().position(location).title(res.getNom()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        } else if (id == R.id.nav_hopitaux) {
            mMap.clear();
            DatabaseReference myRef = database.getReference("pharmacies");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot s : dataSnapshot.getChildren()) {

                        Place res = s.getValue(Place.class);
                        LatLng location=new LatLng(res.getLat(),res.getLng());
                        mMap.addMarker(new MarkerOptions().position(location).title(res.getNom()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        } else if (id == R.id.nav_manage) {
            mMap.clear();
            DatabaseReference myRef = database.getReference("entreprises");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot s : dataSnapshot.getChildren()) {

                        Place res = s.getValue(Place.class);


                        LatLng location=new LatLng(res.getLat(),res.getLng());
                        mMap.addMarker(new MarkerOptions().position(location).title(res.getNom()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        } else if (id == R.id.nav_share) {
            mMap.clear();
            DatabaseReference myRef = database.getReference("shopping");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot s : dataSnapshot.getChildren()) {

                        Place res = s.getValue(Place.class);


                        LatLng location=new LatLng(res.getLat(),res.getLng());
                        mMap.addMarker(new MarkerOptions().position(location).title(res.getNom()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static final String CHANNEL_ID = "CHANNEL_ID";

    private void createNotificationChannel() {
        // Créer le NotificationChannel, seulement pour API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("Notification channel description");
            // Enregister le canal sur le système : attention de ne plus rien modifier après
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }

    /*******************************************************/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
      /* LatLng sydney = new LatLng(32.302635, -9.228113);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10F));
*/
      recherche.setVisibility(View.VISIBLE);
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
               recherche.setVisibility(View.INVISIBLE);
                ajouter.setVisibility(View.VISIBLE);
                String  date = DateFormat.getDateTimeInstance().format(new Date());
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
        // Add a marker in Sydney and move the camera
        if ( ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED) {



            LatLng safi = new LatLng(32.302635,-9.228113);
            mMap.addMarker(new MarkerOptions().position(safi).title("Safi,Morroco"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(safi, 12));


            LatLng cafe3 = new LatLng(32.294021, -9.224834);
            mMap.addMarker(new MarkerOptions().position(cafe3).title("Centre régional des métiers d’éducation et de formation Annexe Safi").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cafe3, 12));
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);


        }
    }





















    /*********************Partie 2 ***************************/





    /*********************************PARTIE2*************************/



    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code=99;





    protected  synchronized  void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();
    }



    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);

            }
            return false;
        }
        else{
            return true;
        }
    }


    public void OnRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        //super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "Permissions Refusées", Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }
    @Override
    public void onLocationChanged(Location location) {
        lastLocation=location;
        if(currentUserLocationMarker!=null){
            currentUserLocationMarker.remove();
        }

        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Votre Position Actuelle");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentUserLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED )
        { LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);}
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

















}
