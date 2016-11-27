package hackathon.digitalocean.youdecide.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import hackathon.digitalocean.youdecide.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    SignInListener signInListener;


    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        final EditText email = (EditText) view.findViewById(R.id.email);
        final EditText password = (EditText) view.findViewById(R.id.password);

        view.findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInListener.onSignUpClick();
            }
        });

        view.findViewById(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInListener.onForgetPasswordClick();
            }
        });

        view.findViewById(R.id.google_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInListener.onGoogleSignIn();
            }
        });

        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || !email.getText().toString().contains("@"))
                    ((TextInputLayout) view.findViewById(R.id.email_layout)).setError("Invalid email address");
                else if (password.getText().toString().length() < 10)
                    ((TextInputLayout) view.findViewById(R.id.password_layout)).setError("Invalid password");
                else
                    signInListener.onNormalSignIn(email.getText().toString(), password.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignInListener) {
            signInListener = (SignInListener) context;
        } else {
            throw new RuntimeException(" must implement " + getClass().toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        signInListener = null;
    }

    public interface SignInListener {
        void onNormalSignIn(String email, String password);

        void onGoogleSignIn();

        void onSignUpClick();

        void onForgetPasswordClick();
    }

}
