package com.jsyp.pruebapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private EditText editTextTerms;
    private TextView textViewResult;
    private Button buttonResults;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTerms = findViewById(R.id.txtNum1);
        textViewResult = findViewById(R.id.txtResul);
        buttonResults = findViewById(R.id.BtnResultados);

        client = new OkHttpClient();

        buttonResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int terms = Integer.parseInt(editTextTerms.getText().toString());
                    if (terms < 1) {
                        throw new NumberFormatException("El número de términos debe ser mayor que cero");
                    }
                    fetchPiApproximation(terms);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Ingrese un número válido de términos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchPiApproximation(int terms) {
        String url = "http://10.10.28.39:3000/leibniz?terms=" + terms; // Cambia a tu servidor y endpoint correctos

        Request get = new Request.Builder()
                .url(url)
                .build();

        client.newCall(get).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Error en la solicitud: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                ResponseBody responseBody = response.body();
                String result = responseBody.string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewResult.setText(result);
                        Toast.makeText(MainActivity.this, "Resultado: " + result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
