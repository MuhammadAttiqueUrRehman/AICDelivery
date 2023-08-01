package layout;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aic.aicdelivery.HMAppVariables;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.providerInfo;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_providerregister3 extends Fragment {
    private static final int REQUEST_PERMISSION_CAMERA1 = 1;
    private static final int REQUEST_PERMISSION_CAMERA2 = 2;
    private static final int REQUEST_PERMISSION_CAMERA3 = 3;
    private static final int REQUEST_PERMISSION_CAMERA4 = 4;
    private static final int CAMERA_PIC_REQUEST1 = 1;
    private static final int CAMERA_PIC_REQUEST2 = 2;
    private static final int CAMERA_PIC_REQUEST3 = 3;
    private static final int CAMERA_PIC_REQUEST4 = 4;
    private ImageButton camera1, camera2, camera3, camera4;
    Bitmap bmap;
    private EditText txtaadhar, txttrade, txtdl;
    private View v;
    Button previous, next;
    private IntroManager intromanager;
    private providerInfo providerinfo;
    private Toolbar mtoolbar;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    byte[] decodedBytes;
    private Integer mint = 0;

    public fr_providerregister3() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        intromanager = new IntroManager(getActivity());
        providerinfo = new providerInfo(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_providerregister3, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Document Upload");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        next = (Button) v.findViewById(R.id.btnnext);
        previous = (Button) v.findViewById(R.id.btnprev);
        txtaadhar = (EditText) v.findViewById(R.id.txtaadhar);
        txttrade = (EditText) v.findViewById(R.id.txttrade);
        txtdl = (EditText) v.findViewById(R.id.txtdl);

        //retrieve memory
        txtaadhar.setText(providerinfo.getAadhar());
        txttrade.setText(providerinfo.getTrade());
        txtdl.setText(providerinfo.getDL());

        camera1 = (ImageButton) v.findViewById(R.id.imageprofile);
        camera2 = (ImageButton) v.findViewById(R.id.imageaadhar);
        camera3 = (ImageButton) v.findViewById(R.id.imagetrade);
        camera4 = (ImageButton) v.findViewById(R.id.imagedl);


        bmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera_logo);
        if (providerinfo.getProfileImage().length() > 0) {
            decodedBytes = Base64.decode(providerinfo.getProfileImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            camera1.setImageBitmap(decodedByte);
        } else {
            providerinfo.setProfileImage(getEncoded64ImageStringFromBitmap(bmap));
        }


        if (providerinfo.getAadharImage().length() > 0) {
            decodedBytes = Base64.decode(providerinfo.getAadharImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            camera2.setImageBitmap(decodedByte);
        } else {
            providerinfo.setAadharImage(getEncoded64ImageStringFromBitmap(bmap));
        }


        if (providerinfo.getTradeImage().length() > 0) {
            decodedBytes = Base64.decode(providerinfo.getTradeImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            camera3.setImageBitmap(decodedByte);
        } else {
            providerinfo.setTradeImage(getEncoded64ImageStringFromBitmap(bmap));
        }

        if (providerinfo.getDLImage().length() > 0) {
            decodedBytes = Base64.decode(providerinfo.getDLImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            camera4.setImageBitmap(decodedByte);
        } else {
            providerinfo.setTradeImage(getEncoded64ImageStringFromBitmap(bmap));
        }


        camera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mint ++;
                intromanager.setImageCount(intromanager.getImageCount() + 1);
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST1);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA1);
                }
            }
        });


        camera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mint ++;
                intromanager.setImageCount(intromanager.getImageCount() + 1);
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST2);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA2);
                }
            }
        });

        camera3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mint ++;
                intromanager.setImageCount(intromanager.getImageCount() + 1);
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST3);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA3);
                }
            }
        });

        camera4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mint ++;
                intromanager.setImageCount(intromanager.getImageCount() + 1);
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST4);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA4);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                //saveAddInfo();
                if (txtaadhar.getText().length() > 0) {
                    if (txtaadhar.getText().length() != 15) {
                        txtaadhar.setError("The Resident Id number should be 15 digits");
                        return;
                    }
                }

                if (intromanager.getImageCount() < 3) {
                    myDB.showToast(getActivity(), "Atleast one Profile Image and 2 documents should be uploaded");
                    return;
                }

                providerinfo.setAadhar(txtaadhar.getText().toString());
                providerinfo.setTrade(txttrade.getText().toString());
                providerinfo.setDL(txtdl.getText().toString());
                FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment myFragment = new fr_providerregister4();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Fragment myFragment = new fr_providerregister2();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            myDB.showToast(getActivity(), "Camera activity cancelled and closed");
            return;
        }

        if (Integer.toString(resultCode).equals("0")) {
            myDB.showToast(getActivity(), "Camera activity cancelled and closed");
            return;
        }

        if (requestCode == CAMERA_PIC_REQUEST1) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            camera1.setImageBitmap(image);
            camera1.buildDrawingCache();
            bmap = camera1.getDrawingCache();
            providerinfo.setProfileImage(getEncoded64ImageStringFromBitmap(bmap));

        }
        if (requestCode == CAMERA_PIC_REQUEST2) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            camera2.setImageBitmap(image);
            camera2.buildDrawingCache();
            bmap = camera2.getDrawingCache();
            providerinfo.setAadharImage(getEncoded64ImageStringFromBitmap(bmap));
        }
        if (requestCode == CAMERA_PIC_REQUEST3) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            camera3.setImageBitmap(image);
            camera3.buildDrawingCache();
            bmap = camera3.getDrawingCache();
            providerinfo.setTradeImage(getEncoded64ImageStringFromBitmap(bmap));
        }
        if (requestCode == CAMERA_PIC_REQUEST4) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            camera4.setImageBitmap(image);
            camera4.buildDrawingCache();
            bmap = camera4.getDrawingCache();
            providerinfo.setDLImage(getEncoded64ImageStringFromBitmap(bmap));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        Log.i("Debugging", "Option Menu xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx ");
        int id = items.getItemId();
        if (id == android.R.id.home) {
            Fragment myFragment = new fr_providerregister2();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }
}
