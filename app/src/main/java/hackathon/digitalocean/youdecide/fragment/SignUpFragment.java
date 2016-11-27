package hackathon.digitalocean.youdecide.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import hackathon.digitalocean.youdecide.R;

public class SignUpFragment extends Fragment {

    SignUpListener signUpListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        final EditText fullName = (EditText) view.findViewById(R.id.full_name);
        final EditText password = (EditText) view.findViewById(R.id.password);
        final EditText email = (EditText) view.findViewById(R.id.email);
        view.findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || !email.getText().toString().contains("@"))
                    ((TextInputLayout) view.findViewById(R.id.email_layout)).setError("Invalid email address");
                else if (password.getText().toString().length() < 10)
                    ((TextInputLayout) view.findViewById(R.id.password_layout)).setError("Invalid password");
                else if (fullName.getText().toString().equals(""))
                    ((TextInputLayout) view.findViewById(R.id.full_name_layout)).setError("Invalid name");
                else
                    signUpListener.onSignUp(fullName.getText().toString(), email.getText().toString(), password.getText().toString());

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignUpListener) {
            signUpListener = (SignUpListener) context;
        } else {
            throw new RuntimeException(" must implement " + getClass().toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        signUpListener = null;
    }

    public interface SignUpListener {
        void onSignUp(String fullname, String email, String password);
    }
}
