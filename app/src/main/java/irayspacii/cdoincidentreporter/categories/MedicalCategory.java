package irayspacii.cdoincidentreporter.categories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import irayspacii.cdoincidentreporter.MainActivity;
import irayspacii.cdoincidentreporter.R;

public class MedicalCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_category);
    }

    public void onClick(View v) {

        Button btn = (Button) findViewById(v.getId());

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("mainCategory", "Medical");
        i.putExtra("subCategory", btn.getText());
        startActivity(i);

    }

}
