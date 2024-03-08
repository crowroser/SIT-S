package com.sitoplulugu.st_s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private EditText usernameEditText;
    private CheckBox checkBoxValorant, checkBoxLoL, checkBoxCS2;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Geri tuşuna basıldığında önceki aktiviteye dön
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        usernameEditText = findViewById(R.id.usernameEditText);
        checkBoxValorant = findViewById(R.id.checkBoxValorant);
        checkBoxLoL = findViewById(R.id.checkBoxLoL);
        checkBoxCS2 = findViewById(R.id.checkBoxCS2);

        // Profil bilgilerini güncelleme butonu
        Button updateProfileButton = findViewById(R.id.updateProfileButton);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        // Firestore'dan mevcut kullanıcı bilgilerini al
        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");
                                usernameEditText.setText(username);
                                String gamesString = document.getString("games");
                                if (gamesString != null && !gamesString.isEmpty()) {
                                    List<String> games = Arrays.asList(gamesString.split(", "));
                                    for (String game : games) {
                                        switch (game) {
                                            case "Valorant":
                                                checkBoxValorant.setChecked(true);
                                                break;
                                            case "League of Legends (LoL)":
                                                checkBoxLoL.setChecked(true);
                                                break;
                                            case "Counter-Strike 2":
                                                checkBoxCS2.setChecked(true);
                                                break;
                                        }
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(Profile.this, "Veritabanına erişim sağlanamadı: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Profil bilgilerini güncelleme metodu
    private void updateProfile() {
        String username = usernameEditText.getText().toString().trim();
        StringBuilder selectedGames = new StringBuilder();

        if (checkBoxValorant.isChecked()) {
            selectedGames.append("Valorant, ");
        }
        if (checkBoxLoL.isChecked()) {
            selectedGames.append("League of Legends (LoL), ");
        }
        if (checkBoxCS2.isChecked()) {
            selectedGames.append("Counter-Strike 2, ");
        }

        // Son virgülü kaldır
        if (selectedGames.length() > 0) {
            selectedGames.setLength(selectedGames.length() - 2);
        }

        if (!username.isEmpty()) {
            // Kullanıcı verilerini Firestore'a güncelle
            Map<String, Object> user = new HashMap<>();
            user.put("username", username);
            user.put("games", selectedGames.toString());

            db.collection("users").document(currentUser.getUid())
                    .set(user, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Toast.makeText(Profile.this, "Profil bilgileri güncellendi", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(Profile.this, "Veritabanına erişim sağlanamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Lütfen kullanıcı adını girin", Toast.LENGTH_SHORT).show();
        }
    }
}
