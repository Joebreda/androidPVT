package edu.washington.cs.ubicomplab.com.pvtapp.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import edu.washington.cs.ubicomplab.com.pvtapp.R;

public class StartActivity extends AppCompatActivity {


    Button startButton;
    EditText codeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        startButton = findViewById(R.id.start_button);
        codeText  = findViewById(R.id.codeText);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(codeText.getText().toString().equals("1234")) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (codeText.getText().toString().equals("")) {
                    openDialog("Enter code!");
                }
                else {
                    openDialog("Wrong code!");
                }
            }
        });
       }

    public void openDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle Ok
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
