package hackathon.digitalocean.youdecide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import developer.shivam.perfecto.OnNetworkRequest;
import developer.shivam.perfecto.Perfecto;
import hackathon.digitalocean.youdecide.R;
import hackathon.digitalocean.youdecide.StaticData;
import hackathon.digitalocean.youdecide.fragment.ForgetPasswordFragment;
import hackathon.digitalocean.youdecide.fragment.SignInFragment;
import hackathon.digitalocean.youdecide.fragment.SignUpFragment;

public class LoginActivity extends AppCompatActivity implements
        SignInFragment.SignInListener,
        SignUpFragment.SignUpListener,
        ForgetPasswordFragment.ResetPasswordListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 2001;
    SignInFragment signInFragment;
    SignUpFragment signUpFragment;
    ForgetPasswordFragment forgetPasswordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInFragment = new SignInFragment();
        signUpFragment = new SignUpFragment();
        forgetPasswordFragment = new ForgetPasswordFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout, signInFragment, "Sign In Fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResetPassword(String email) {

    }

    @Override
    public void onNormalSignIn(String email, String password) {
        try {
            JSONObject object = new JSONObject();
            object.put("email", email);
            object.put("password", password);
            Perfecto.with(this).fromUrl(StaticData.NORMAL_LOGIN).ofTypePost(object).connect(new OnNetworkRequest() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(LoginActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                            getSharedPreferences(StaticData.USER_INFO, MODE_PRIVATE).edit().putString("fullName", object.getString("fullName"))
                                    .putString("userName", object.getString("userName")).apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, String s, String s1) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onSignUpClick() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout, signUpFragment, "Sign Up Fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onForgetPasswordClick() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout, forgetPasswordFragment, "Forget Password Fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSignUp(String fullname, String email, String password) {
        try {
            JSONObject object = new JSONObject();
            object.put("fullName", fullname);
            object.put("password", password);
            object.put("email", email);
            Perfecto.with(this).fromUrl(StaticData.SIGN_UP).ofTypePost(object).connect(new OnNetworkRequest() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(LoginActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            getSharedPreferences(StaticData.USER_INFO, MODE_PRIVATE).edit().putString("fullName", object.getString("fullName"))
                                    .putString("userName", object.getString("userName")).apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, String s, String s1) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("GOOGLE SIGN", "true " + result.getStatus().getStatusCode());
            if (result.isSuccess()) {
                Log.d("GOOGLE SIGN", "false");
                GoogleSignInAccount signInAccount = result.getSignInAccount();
                Toast.makeText(this, "" + signInAccount.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
