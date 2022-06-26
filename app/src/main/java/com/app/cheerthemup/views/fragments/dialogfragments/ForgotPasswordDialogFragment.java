package com.app.cheerthemup.views.fragments.dialogfragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.cheerthemup.R;
import com.app.cheerthemup.interfaces.ForgotPasswordFragmentClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordDialogFragment extends BottomSheetDialogFragment {

    TextInputEditText edtEmail;
    MaterialButton btnReset;


    ForgotPasswordFragmentClickListener forgotPasswordFragmentClickListener;

    public static ForgotPasswordDialogFragment newInstance() {

//        Bundle args = new Bundle();

        ForgotPasswordDialogFragment fragment = new ForgotPasswordDialogFragment();
//        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        forgotPasswordFragmentClickListener = (ForgotPasswordFragmentClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);


        edtEmail = view.findViewById(R.id.edit_email);
btnReset = view.findViewById(R.id.btn_reset);

btnReset.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String email = edtEmail.getText().toString();



        forgotPasswordFragmentClickListener.onClick(email);
    }
});


        return view;
    }
}
