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

public class ForgetPasswordFragment extends Fragment {
    ResetPasswordListener passwordListener;

    public ForgetPasswordFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        final EditText email = (EditText) view.findViewById(R.id.email);
        view.findViewById(R.id.send_reset_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || email.getText().toString().contains("@"))
                    ((TextInputLayout) view.findViewById(R.id.email_layout)).setError("Invalid email address");
                else
                    passwordListener.onResetPassword(email.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResetPasswordListener) {
            passwordListener = (ResetPasswordListener) context;
        } else {
            throw new RuntimeException(" must implement " + getClass().toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        passwordListener = null;
    }

    public interface ResetPasswordListener {
        void onResetPassword(String email);
    }

}