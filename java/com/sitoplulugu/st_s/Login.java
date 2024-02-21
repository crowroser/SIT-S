package com.sitoplulugu.st_s;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("E-posta alanı boş olamaz.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Şifre alanı boş olamaz.");
            return;
        }

        // Firebase ile kullanıcı girişi
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Giriş başarılı, kullanıcı bilgilerini güncelle ve ana ekrana git
                            Log.d("LoginActivity", "signInWithEmail:success");
                            Toast.makeText(Login.this, "Giriş başarılı.",
                                    Toast.LENGTH_SHORT).show();
                            // Burada ana ekrana gitmek için bir Intent başlatılabilir
                        } else {
                            // Giriş başarısız ise hata mesajı göster
                            Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Giriş başarısız.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
