package ru.mirea.laptevavs.button_clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

        private TextView textViewStudent;
        private Button btnWhoAmI;
        private Button btnItIsNotMe ;

        private CheckBox checkBox;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textViewStudent = findViewById(R.id.tvOut);
            btnWhoAmI = findViewById(R.id.btnWhoAmI);
            btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
            checkBox = findViewById(R.id.checkBox);
            View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
                public void onClick(View v) {
                    textViewStudent.setText("Мой номер по списку № 15");
                    checkBox.setChecked(true);
                }
            };
            btnWhoAmI.setOnClickListener(oclBtnWhoAmI);
        }
        public void onMyButtonClick(View view)
        {
            textViewStudent.setText("Ок");
            checkBox.setChecked(false);
        }




}