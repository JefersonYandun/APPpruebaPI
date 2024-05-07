package com.jsyp.pruebapi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PiService {
    @GET("/leibniz")
    Call<PiResponse> getApproximation(@Query("terms") int terms);
}


public class PiResponse {
    public int terms;
    public double approximation;
    public String series; // para la cadena de la serie
}

public class MainActivity extends AppCompatActivity {
    private EditText editTextTerms;
    private TextView textViewResult;
    private Retrofit retrofit;
    private PiService piService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTerms = findViewById(R.id.txtNum1);
        textViewResult = findViewById(R.id.txtResul);
        Button buttonResults = findViewById(R.id.BtnResultados);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://tu-servidor:puerto/") // Ajusta con tu servidor
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        piService = retrofit.create(PiService.class);

        buttonResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int terms = Integer.parseInt(editTextTerms.getText().toString());
                getPiApproximation(terms);
            }
        });
    }

    private void getPiApproximation(int terms) {
        Call<PiResponse> call = piService.getApproximation(terms);
        call.enqueue(new Callback<PiResponse>() {
            @Override
            public void onResponse(Call<PiResponse> call, Response<PiResponse> response) {
                if (response.isSuccessful()) {
                    PiResponse piResponse = response.body();
                    textViewResult.setText(
                            "Aproximación de π: " + piResponse.approximation + "\nSerie: " + piResponse.series
                    );
                } else {
                    textViewResult.setText("Error en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<PiResponse> call, Throwable t) {
                textViewResult.setText("Error en la solicitud");
            }
        });
    }
}
