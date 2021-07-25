package com.example.parkinson.features.medic_case;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parkinson.R;

/**
 * A simple class for a bottom sheet dialog. {@link BottomSheetDialogFragment#getTheme()} is overridden
 * and returns the {@link R.layout#bottom_sheet} theme.
 */
final public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    interface BottomSheetInterfaceListener {
        void OnSelectPic();

        void OnChoosePic();
    }

    private BottomSheetInterfaceListener listener;

    @Override
    public int getTheme() {

        return R.style.BottomSheetDialogTheme;  // change color of the navigation bar in the xml file
    }

    public static BottomSheetDialogFragment newInstance() {

        return new BottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        view.findViewById(R.id.select_take_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.OnSelectPic();
                }
            }
        });

        view.findViewById(R.id.select_choose_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.OnChoosePic();
                }
            }
        });

        return view;
    }

    public void setListener(BottomSheetInterfaceListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        listener = null;
    }
}
