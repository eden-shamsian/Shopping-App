package com.example.secondandroidhita.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.secondandroidhita.R;
import com.example.secondandroidhita.activitys.models.Client;
import com.example.secondandroidhita.activitys.models.Product;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {

        String email = ((EditText) findViewById(R.id.emailText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(view).navigate(R.id.action_fragmentOne_to_fragmentThree);
                                } else {
                                    // Sign in failed, handle specific exceptions
                                    if (task.getException() != null) {
                                        throw task.getException(); // Rethrow to catch specific errors
                                    }
                                    Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(MainActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                                Log.e("FirebaseAuth", "Invalid credentials", e);
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(MainActivity.this, "No account found with this email!", Toast.LENGTH_SHORT).show();
                                Log.e("FirebaseAuth", "User not found", e);
                            } catch (FirebaseNetworkException e) {
                                Toast.makeText(MainActivity.this, "Network error! Check your connection.", Toast.LENGTH_SHORT).show();
                                Log.e("FirebaseAuth", "Network error", e);
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Authentication error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e("FirebaseAuth", "Unknown login error", e);
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Authentication process failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("FirebaseAuth", "Sign-in process error", e);
        }

    }

    public void register() {

        String email = ((EditText) findViewById(R.id.emailTextReg)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordTextReg)).getText().toString();
        // בדיקת שדות ריקים
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        // בדיקת אורך סיסמה (Firebase דורש מינימום 6 תווים לסיסמה)
        if (password.length() < 6) {
            Toast.makeText(MainActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "register succeed!", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign   in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "register fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // Write a message to the database
    public void addDATA() {
        String phone =((EditText)findViewById((R.id.phoneTextReg))).getText().toString();//הוצאת נתונים מהלייאוט
        String email=((EditText)findViewById((R.id.emailTextReg))).getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(phone);
        Client c= new Client(phone,email);
        myRef.setValue(c);

    }
    public void getStudent(String phone)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(phone);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client value = snapshot.getValue(Client.class);
                Toast.makeText(MainActivity.this, value.getEmail().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}










