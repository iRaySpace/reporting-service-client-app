package irayspacii.cdoincidentreporter.categories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import irayspacii.cdoincidentreporter.MainActivity;
import irayspacii.cdoincidentreporter.R;

public class MainCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
    }

    public void onClick(View v) {

        int ButtonId = v.getId();

        if(ButtonId == R.id.TraumaButton) {
            Intent i = new Intent(this, TraumaCategory.class);
            startActivity(i);
        } else if(ButtonId == R.id.MedicalButton) {
            Intent i = new Intent(this, MedicalCategory.class);
            startActivity(i);
        }

    }

}
