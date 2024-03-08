package com.sitoplulugu.st_s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CSMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Etkinlik verilerini getiren AsyncTask'i başlat
        new FetchEventDataTask().execute();
    }

    private class FetchEventDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://esportapi1.p.rapidapi.com/api/esport/category/1572/events/19/12/2024")
                    .get()
                    .addHeader("X-RapidAPI-Key", "16272a6441msh45a023db2e24a21p127c90jsn39c98531fce5")
                    .addHeader("X-RapidAPI-Host", "esportapi1.p.rapidapi.com")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string(); // RapidAPI'den gelen JSON yanıtını döndür
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            if (jsonResponse != null) {
                // JSON yanıtını analiz et ve RecyclerView üzerinde göster
                try {
                    JSONArray eventsArray = new JSONObject(jsonResponse).getJSONArray("events");
                    eventAdapter = new EventAdapter(eventsArray);
                    recyclerView.setAdapter(eventAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Hata durumunda kullanıcıya bir uyarı verebilirsiniz
                Toast.makeText(CSMainActivity.this, "Ağ isteği sırasında bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private JSONArray eventsArray;

    public EventAdapter(JSONArray eventsArray) {
        this.eventsArray = eventsArray;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View eventView = LayoutInflater.from(context).inflate(R.layout.cs_event_item, parent, false);
        return new EventViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        try {
            JSONObject event = eventsArray.getJSONObject(position);
            holder.bind(event);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return eventsArray.length();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView teamsTextView;
        TextView statusTextView;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.event_title);
            teamsTextView = itemView.findViewById(R.id.event_teams);
            statusTextView = itemView.findViewById(R.id.event_status);
        }

        void bind(JSONObject event) {
            try {
                String tournamentName = event.getJSONObject("tournament").getString("name");
                String homeTeamName = event.getJSONObject("homeTeam").getString("name");
                String awayTeamName = event.getJSONObject("awayTeam").getString("name");
                String status = event.getJSONObject("status").getString("description");

                titleTextView.setText(tournamentName);
                teamsTextView.setText(homeTeamName + " vs " + awayTeamName);
                statusTextView.setText("Status: " + status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
