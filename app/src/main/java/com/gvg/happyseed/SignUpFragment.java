package com.gvg.happyseed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SignUpFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Button button = view.findViewById(R.id.btn_sign_up);
        final TextInputLayout loginNameInputText = view.findViewById(R.id.sign_up_ti_name);
        final TextInputLayout loginLastNameInputText = view.findViewById(R.id.sign_up_ti_last_name);
        final TextInputLayout loginEmailInputText = view.findViewById(R.id.sign_up_ti_email);
        final TextInputLayout loginPasswordInputText = view.findViewById(R.id.sign_up_ti_password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(loginNameInputText.getEditText().getText().toString(),
                        loginLastNameInputText.getEditText().getText().toString(),
                        loginEmailInputText.getEditText().getText().toString(),
                        loginPasswordInputText.getEditText().getText().toString());
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void signUp(final String name, final String lastName, String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            Map<String, Object> user = new HashMap<>();
                            user.put("auth_uid", task.getResult().getUser().getUid());
                            user.put("name", name);
                            user.put("last_name", lastName);

                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            Log.d(TAG, "DocumentSnapshot added with ID: " +
                                                    documentReference.getId());

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });

                        } else {

                        }
                    }
                });

    }

}