package com.example.sendsmses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sendsmses.databinding.ActivityAuthorMagdiBinding;

public class authorMagdi extends AppCompatActivity {
    ActivityAuthorMagdiBinding binding;
    public  final String ClipData_KEY = "ClipData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthorMagdiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //
        binding.mainTvAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyDeveloperContacts();
            }
        });
        //
        binding.authorIvLinkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://" + "www.linkedin.com/in/moustafa-magdi-5742a1a9/"));
                startActivity(intent);

                copyDeveloperContacts();
            }
        });

        binding.authorIvGithup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://" + "www.github.com/MMIsmail89"));
                startActivity(intent);

                copyDeveloperContacts();
            }
        });

        binding.authorIvGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "magdi.office@gmail.com" });
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Developer Request");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body of the email");
                startActivity(Intent.createChooser(emailIntent, "Dear Dr. Magdi,\n"));

                copyDeveloperContacts();
            }
        });

        binding.authorIvLoction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + "كلية الهندسة, جامعة المنيا, الطريق الزراعى, محافظة المنيا, مصر"));
                startActivity(intent);

                copyDeveloperContacts();
            }
        });

        //
    }

    public void copyDeveloperContacts() {
        String developerContacts_txt = getString(R.string.developerContacts);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(ClipData_KEY, developerContacts_txt);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(authorMagdi.this, R.string.developer_contacts_copied, Toast.LENGTH_SHORT).show();
    }
}