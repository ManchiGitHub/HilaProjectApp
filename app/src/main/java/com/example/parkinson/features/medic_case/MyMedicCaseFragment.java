package com.example.parkinson.features.medic_case;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.medicine.MyMedicinesFragmentDirections;
import com.example.parkinson.features.medicine.MyMedicinesMainAdapter;
import com.example.parkinson.features.medicine.MyMedicinesMainAdapter.MyMedicinesMainAdapterListener;
import com.example.parkinson.model.general_models.Medicine;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.MODE_PRIVATE;
import static com.example.parkinson.features.main.MainActivity.files;

@AndroidEntryPoint
public class MyMedicCaseFragment extends Fragment {

    private MedicCaseViewModel medicCaseViewModel;

    RecyclerView recyclerView;
    MyMedicCaseMainAdapter adapter;
    ImageButton addBtn;

    // Activity requests
    final int CAMERA_REQUEST = 1;
    final int SELECT_IMAGE = 2;
    final int WRITE_PERMISSION_REQUEST = 3;
    boolean permission = true;

    //camera and gallery
    Uri fileUri;
    Bitmap bitmap1, bitmap2;

    //Dialogs
    private AlertDialog alertDialog;


    public MyMedicCaseFragment(){
        super(R.layout.fragment_medic_case);
    }

    @Override
    public void onAttach(@NonNull Context context) {
//        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        medicCaseViewModel = new ViewModelProvider(this).get(MedicCaseViewModel.class);

        medicCaseViewModel.initMedicineData();
        initViews(view);
        //initUi(view);
        initObservers();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyMedicCaseMainAdapter(files);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new MyMedicCaseMainAdapter.MyMedicCaseMainAdapterListener() {
            @Override
            public void onFileClicked(int position, View view) {

                loadImageDialog(files.get(position));
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildSheetDialog();
            }
        });

        getView().findViewById(R.id.medicCaseFragExitBtn).setOnClickListener(v->{ getActivity().onBackPressed();
       });

    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.medicCaseFragRecycler);
        addBtn = view.findViewById(R.id.medicCaseFragAddBtn);

    }

//    private void initUi(View view) {
//        adapter = new MyMedicCaseMainAdapter(getMainAdapterListener());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//
//        getView().findViewById(R.id.medicCaseFragExitBtn).setOnClickListener(v->{
//            getActivity().onBackPressed();
//        });
//
////        CardView addMedicine = view.findViewById(R.id.myMedicinesFragAddBtn);
////        addMedicine.setOnClickListener(v -> {
////            NavDirections action = MyMedicinesFragmentDirections.actionMedicineFragmentToMedicineCategoryFragment();
////            Navigation.findNavController(view).navigate(action);
////        });
//    }

    private void initObservers() {
        medicCaseViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading-> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateLoadingScreen(isLoading);
        });
        medicCaseViewModel.myMedicationData.observe(getViewLifecycleOwner(), medicationCategories -> {
            //adapter.updateMedicineList(medicationCategories);
        });
    }

//
//    private MyMedicCaseMainAdapter.MyMedicCaseMainAdapterListener getMainAdapterListener(){
//        return new MyMedicCaseMainAdapter.MyMedicCaseMainAdapterListener() {
//            @Override
//            public void onMedicineClick(Medicine medicine) {
//                NavDirections action = MyMedicinesFragmentDirections.actionMyMedicinesFragmentToSingleMedicineFrag(medicine);
//                Navigation.findNavController(getView()).navigate(action);
//            }
//        };
//    }

    private void buildSheetDialog() {

            final BottomSheetDialog bottomDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet, null);

            LinearLayout takePic, selectPic;
            takePic = bottomSheetView.findViewById(R.id.select_take_pic);
            selectPic = bottomSheetView.findViewById(R.id.select_choose_pic);


            takePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePicture(CAMERA_REQUEST);
                    bottomDialog.dismiss();
                }
            });

            selectPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askStoragePermissions(SELECT_IMAGE);
                    bottomDialog.dismiss();
                }
            });

            bottomDialog.setContentView(bottomSheetView);
            bottomDialog.show();


    }

    private void takePicture(int requestCode) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from");
        fileUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, requestCode);
    }

    private void askStoragePermissions(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWritePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED && hasReadPermission != PackageManager.PERMISSION_GRANTED) { //no permissions
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            } else { //have permissions
                openGallery(requestCode);
            }
        }
    }

    private void openGallery(int requestCode) {
        //TODO ask permissions here!!!
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "אין הרשאה", Toast.LENGTH_SHORT).show();
                permission = false;
            } else {

                Toast.makeText(getActivity(), "ההרשאה ניתנה", Toast.LENGTH_SHORT).show();
                permission = true;

                openGallery(SELECT_IMAGE);
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {

                bitmap2 = null;
                try {
                    bitmap1 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), fileUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap1 != null) {
                    files.add(fileUri.toString());
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                fileUri = null;
            }
        }

        if (requestCode == SELECT_IMAGE) {

            if (resultCode == Activity.RESULT_OK && permission == true) {

                fileUri = data.getData();
                bitmap1 = null;
                try {
                    bitmap2 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), fileUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap2 != null) {
                    files.add(fileUri.toString());
                }
            }
        }


    }

    public void saveFiles()
    {
        try {
            FileOutputStream fos = getActivity().openFileOutput("files", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(files);
            oos.close();
            Toast.makeText(getActivity(), "SAVED", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFiles()
    {
        try {
            FileInputStream fis = getActivity().openFileInput("files");
            ObjectInputStream ois = new ObjectInputStream(fis);
            files = (List<String>) ois.readObject();
            Toast.makeText(getActivity(), "READ", Toast.LENGTH_SHORT).show();

//            String GAG = Integer.toString(songs.size());
//            Toast.makeText(this, GAG, Toast.LENGTH_LONG).show();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadImageDialog(String url) {

        View dialogView = getLayoutInflater().inflate(R.layout.image_display_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        alertDialog = builder.setView(dialogView).show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final ProgressBar progressBar = dialogView.findViewById(R.id.img_loader_bar);
        final ImageView imageView = dialogView.findViewById(R.id.img_display);
        final ImageButton backBtn = dialogView.findViewById(R.id.img_dialog_back_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        try {
            Glide.with(dialogView).load(url).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    //Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), R.string.failed_upload_image, Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(imageView);
        } catch (Exception e) {

        }
    }






    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Collections.reverse(files);
        adapter.notifyDataSetChanged();
    }

}
