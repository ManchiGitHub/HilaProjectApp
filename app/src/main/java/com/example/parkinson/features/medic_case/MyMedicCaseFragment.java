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
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.MODE_PRIVATE;
import static com.example.parkinson.features.main.MainActivity.files;

@AndroidEntryPoint
public class MyMedicCaseFragment extends Fragment {

    private ActivityResultLauncher<String> mRequestPermissionLauncher;
    private ActivityResultLauncher<Uri> mTakePictureLauncher;
    private ActivityResultLauncher<String> mChoosePictureLauncher;

    private MedicCaseViewModel medicCaseViewModel;

    private FloatingActionButton fabBtn;
    private RecyclerView recyclerView;
    private MyMedicCaseMainAdapter adapter;

    //camera and gallery
    private File mPhotoFile;
    private Uri fileUri;
    private Bitmap bitmap1, bitmap2;

    //Dialogs
    private AlertDialog alertDialog;

    public MyMedicCaseFragment() {
        super(R.layout.fragment_medic_case);
    }

    @Override
    public void onAttach(@NonNull Context context) {
//        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medicCaseViewModel = new ViewModelProvider(this).get(MedicCaseViewModel.class);

        medicCaseViewModel.initMedicineData();
        initViews(view);
        //initUi(view);
        initObservers();
        initLaunchers();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //Collections.reverseOrder();
        adapter = new MyMedicCaseMainAdapter(files);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new MyMedicCaseMainAdapter.MyMedicCaseMainAdapterListener() {
            @Override
            public void onFileClicked(int position, View view) {

                loadImageDialog(files.get(position).getFilePath());
            }
        });

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildSheetDialog();
            }
        });

        getView().findViewById(R.id.medicCaseFragExitBtn).setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.medicCaseFragRecycler);
//        addBtn = view.findViewById(R.id.medicCaseFragAddBtn);
        fabBtn = view.findViewById(R.id.medic_case_fab_btn);

    }

    private void initObservers() {
        medicCaseViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateLoadingScreen(isLoading);
        });
        medicCaseViewModel.myMedicationData.observe(getViewLifecycleOwner(), medicationCategories -> {
            //adapter.updateMedicineList(medicationCategories);
        });
    }

    private void buildSheetDialog() {
        final BottomSheetDialogFragment dialog = BottomSheetDialogFragment.newInstance();

        dialog.setListener(new BottomSheetDialogFragment.BottomSheetInterfaceListener() {
            @Override
            public void OnSelectPic() {
                takePicture();
                dialog.dismiss();
            }

            @Override
            public void OnChoosePic() {
                pickImageFromGalleryConfirmPermission();
                dialog.dismiss();
            }
        });

        dialog.show(requireActivity().getSupportFragmentManager(), "add_file_dialog_fragment");
    }

    private void takePicture() {
        try {
            mPhotoFile = File.createTempFile(
                    "IMG_",
                    ".jpg",
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileUri = FileProvider.getUriForFile(requireActivity(), "com.example.parkinson.provider", mPhotoFile);

        mTakePictureLauncher.launch(fileUri);
    }

    private void initLaunchers() {

        mRequestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean isGranted) {
                        if (isGranted) {
                            pickImageFromGalleryConfirmPermission();
                        }
                        else {
                            Toast.makeText(getContext(), "Need permission to add image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        mTakePictureLauncher =
                registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {

                        bitmap2 = null;
                        try {

                            bitmap1 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), fileUri));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (bitmap1 != null) {
                            Calendar calendar = Calendar.getInstance(Locale.getDefault());
                            String chatTime = DateFormat.format("dd/MM/yyyy HH:mm", calendar).toString();
                            MedicFile medicFile = new MedicFile();
                            medicFile.setFilePath(fileUri.toString());
                            medicFile.setTimeStamp(chatTime);
                            files.add(medicFile);
                        }
                    }
                });

        mChoosePictureLauncher =
                registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {

                        if (uri != null) { // in case of user didnt choose a picture and returned to the app

                            fileUri = uri;

                            bitmap1 = null;

                            try {
                                bitmap2 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), fileUri));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (bitmap2 != null) {
                                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                                String chatTime = DateFormat.format("dd/MM/yyyy HH:mm", calendar).toString();
                                MedicFile medicFile = new MedicFile();
                                medicFile.setFilePath(fileUri.toString());
                                medicFile.setTimeStamp(chatTime);
                                files.add(medicFile);
                            }

                        }
                    }
                });
    }

    private void pickImageFromGalleryConfirmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWritePermission = requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasWritePermission == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            }
            else {
                mRequestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        else {
            pickImageFromGallery();
        }
    }


    private void pickImageFromGallery() {
        mChoosePictureLauncher.launch("image/*");
    }

    public void saveFiles() {
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

    public void readFiles() {
        try {
            FileInputStream fis = getActivity().openFileInput("files");
            ObjectInputStream ois = new ObjectInputStream(fis);
            files = (List<MedicFile>) ois.readObject();
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

    ProgressBar progressBar;
    ImageView imageView;
    ImageButton backBtn;

    private void loadImageDialog(String url) {

        View dialogView = getLayoutInflater().inflate(R.layout.image_display_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        alertDialog = builder.setView(dialogView).show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressBar = dialogView.findViewById(R.id.img_loader_bar);
        ImageView imageView = dialogView.findViewById(R.id.img_display);
        ImageButton backBtn = dialogView.findViewById(R.id.img_dialog_back_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.GONE);
                alertDialog.dismiss(); }
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
        //Collections.reverse(files);
        adapter.notifyDataSetChanged();
    }

}
