package thang.com.wref.Main.More;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import thang.com.wref.R;

public class InforAgriActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_agri);
        toolbar = (Toolbar) findViewById(R.id.toolbarInfor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mappingView();
        eventOnClick();
    }
    private void mappingView() {
    }
    private void eventOnClick() {
    }

    @Override
    public void onClick(View v) {

    }
}