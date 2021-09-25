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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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

    //progress bar
    ProgressBar progressBar;

    //camera and gallery
    private File mPhotoFile;
    private Uri fileUri;
    private Bitmap bitmap1, bitmap2;


    //Dialogs
    private AlertDialog alertDialog;

    private MedicFile medicFile = new MedicFile();
    private MedicFile medicFile1 = new MedicFile();

    private List<MedicFile> filesList;

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

//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        //Collections.reverseOrder();
//        adapter = new MyMedicCaseMainAdapter(files);
//        recyclerView.setAdapter(adapter);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        //Collections.reverseOrder();
//        adapter = new MyMedicCaseMainAdapter(files);
//        recyclerView.setAdapter(adapter);

        if(adapter !=null) {
            adapter.setListener(new MyMedicCaseMainAdapter.MyMedicCaseMainAdapterListener() {
                @Override
                public void onFileClicked(int position, View view) {

                    loadImageDialog(files.get(position).getFilePath(), files.get(position).getTitle(), files.get(position).getNotes());
                }
            });
        }

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
        progressBar = view.findViewById(R.id.app_bar_progress_bar);

    }

    private void initObservers() {
        medicCaseViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateLoadingScreen(isLoading);
        });

        medicCaseViewModel.myMedicationData.observe(getViewLifecycleOwner(), files2 -> {
            //adapter.updateMedicineList(medicationCategories);
            files = files2;
           // adapter.updateList(files);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            //Collections.reverseOrder();
            adapter = new MyMedicCaseMainAdapter(files);
            recyclerView.setAdapter(adapter);
            //adapter.updateList(files2);
            //adapter = new MyMedicCaseMainAdapter(files);
            //recyclerView.setAdapter(adapter);



            adapter.setListener(new MyMedicCaseMainAdapter.MyMedicCaseMainAdapterListener() {
                @Override
                public void onFileClicked(int position, View view) {

                    loadImageDialog(files.get(position).getFilePath(),files.get(position).getTitle(),files.get(position).getNotes());
                }
            });

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
                            handleUpload(bitmap1);
//                            Calendar calendar = Calendar.getInstance(Locale.getDefault());
//                            String chatTime = DateFormat.format("dd/MM/yyyy HH:mm", calendar).toString();
//                            medicFile1 = new MedicFile();
//                            medicFile1.setFilePath(fileUri.toString());
//                            medicFile1.setTimeStamp(chatTime);
                            fileUri = null;
                            //createCustomDialog(medicFile1);
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
                                handleUpload(bitmap2);
                                //String chatTime = DateFormat.format("dd/MM/yyyy HH:mm", calendar).toString();
                                //medicFile = new MedicFile();
                                //medicFile.setFilePath(fileUri.toString());
                                //medicFile.setTimeStamp(chatTime);
                                fileUri = null;
                               // createCustomDialog(medicFile);
                            }

                        }
                    }
                });
    }

    private void handleUpload(Bitmap bitmap) {

            //profileProgressBar.setVisibility(View.VISIBLE);
            //profileIv.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            final StorageReference storage = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getEmail()).child(System.currentTimeMillis() + ".jpeg");

            storage.putBytes(baos.toByteArray())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownloadUrl(storage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

    }

    private void getDownloadUrl(StorageReference storage) {
        storage.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        medicFile = new MedicFile();
                        createCustomDialog(medicFile);
                            //setUserProfileUrl(uri);
                        Calendar calendar = Calendar.getInstance(Locale.getDefault());
//                        medicFile.setFilePath(uri.toString());
                        String chatTime = DateFormat.format("dd/MM/yyyy HH:mm", calendar).toString();
                        medicFile.setTimeStamp(chatTime);

                        medicFile.setFilePath(uri.toString());
                        progressBar.setVisibility(View.INVISIBLE);
                        //System.out.println(uri.toString());


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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

    ImageView imageView;
    ImageButton backBtn;

    private void loadImageDialog(String url,String name, String notes) {

        View dialogView = getLayoutInflater().inflate(R.layout.image_display_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        alertDialog = builder.setView(dialogView).show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //progressBar = dialogView.findViewById(R.id.img_loader_bar);
        ImageView imageView = dialogView.findViewById(R.id.img_display);
        ImageButton backBtn = dialogView.findViewById(R.id.img_dialog_back_btn);
        TextView nameTv = dialogView.findViewById(R.id.title_name);
        TextView noteTv = dialogView.findViewById(R.id.notes_name);
        TextView noteTitleTv = dialogView.findViewById(R.id.notes_title);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.GONE);
                alertDialog.dismiss(); }
        });

        //progressBar.setVisibility(View.VISIBLE);

        try {
            Glide.with(dialogView).load(url).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    //Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), R.string.failed_upload_image, Snackbar.LENGTH_SHORT).show();
                    //progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    //progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    nameTv.setText(name);
                    if(!notes.isEmpty())
                    {
                        noteTitleTv.setVisibility(View.VISIBLE);
                        noteTv.setVisibility(View.VISIBLE);
                        noteTv.setText(notes);
                    }

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
    public void onStop() {
        super.onStop();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients").child(FirebaseAuth.getInstance().getUid()).child("files");
        databaseReference.setValue(files);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Collections.reverse(files);
        //adapter.notifyDataSetChanged();
    }

    private void createCustomDialog(MedicFile medicFiler) {

        final AlertDialog alertDialog;
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_text_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        alertDialog = builder.setView(dialogView).show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        //TextView titleTv = dialogView.findViewById(R.id.profile_dialog_title);
        EditText bodyTv = dialogView.findViewById(R.id.dialog_body);
        EditText notesTv = dialogView.findViewById(R.id.notes_body);
        //ImageView iconIv = dialogView.findViewById(R.id.profile_dialog_icon);
        final Button okBtn = dialogView.findViewById(R.id.profile_dialog_btn);



       // titleTv.setText(title);
        //bodyTv.setText(body);
//        iconIv.setImageResource(icon);
//        iconIv.animate().scaleX(1f).scaleY(1f).setDuration(250).withEndAction(new Runnable() {
//            @Override
//            public void run() {
//                okBtn.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
//            }
//        }).start();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                medicFiler.setTitle(bodyTv.getText().toString());
                medicFiler.setNotes(notesTv.getText().toString());
                if(!medicFiler.getTitle().isEmpty()) {
                    files.add(medicFiler);
                    medicCaseViewModel.myMedicationData.postValue(files);
                    alertDialog.dismiss();
                }


            }
        });
    }



}
