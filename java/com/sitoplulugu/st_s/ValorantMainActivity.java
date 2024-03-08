package com.sitoplulugu.st_s;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ValorantMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ValorantMatchAdapter adapter;
    private List<ValorantMatch> valorantMatchList;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        valorantMatchList = new ArrayList<>();
        adapter = new ValorantMatchAdapter(this, valorantMatchList);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://valorant-esports1.p.rapidapi.com/v1/matches")
                .get()
                .addHeader("X-RapidAPI-Key", "16272a6441msh45a023db2e24a21p127c90jsn39c98531fce5")
                .addHeader("X-RapidAPI-Host", "valorant-esports1.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject matchObject = jsonArray.getJSONObject(i);
                            String id = matchObject.getString("id");
                            String status = matchObject.getString("status");
                            String event = matchObject.getString("event");
                            String tournament = matchObject.getString("tournament");
                            String img = matchObject.getString("img");
                            String in = matchObject.getString("in");

                            JSONArray teamsArray = matchObject.getJSONArray("teams");
                            List<ValorantTeam> teams = new ArrayList<>();
                            for (int j = 0; j < teamsArray.length(); j++) {
                                JSONObject teamObject = teamsArray.getJSONObject(j);
                                String teamName = teamObject.getString("name");
                                String country = teamObject.getString("country");
                                ValorantTeam team = new ValorantTeam(teamName, country);
                                teams.add(team);
                            }

                            ValorantMatch valorantMatch = new ValorantMatch(id, teams, status, event, tournament, img, in);
                            valorantMatchList.add(valorantMatch);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Ana sayfaya yönlendiren bir intent oluştur
        super.onBackPressed();
        Intent intent = new Intent(this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Yığını temizle
        startActivity(intent);
    }

}

class ValorantMatchAdapter extends RecyclerView.Adapter<ValorantMatchAdapter.MatchViewHolder> {

    private final Context context;
    private final List<ValorantMatch> valorantMatchList;

    public ValorantMatchAdapter(Context context, List<ValorantMatch> valorantMatchList) {
        this.context = context;
        this.valorantMatchList = valorantMatchList;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_item, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        ValorantMatch valorantMatch = valorantMatchList.get(position);
        holder.txtEvent.setText(valorantMatch.getEvent());
        holder.txtStatus.setText(valorantMatch.getStatus());
        holder.txtTournament.setText(valorantMatch.getTournament());
        holder.txtIn.setText(valorantMatch.getIn());

        List<ValorantTeam> valorantTeams = valorantMatch.getTeams();
        if (valorantTeams != null && valorantTeams.size() == 2) {
            holder.txtTeam1.setText(valorantTeams.get(0).getName());
            holder.txtTeam2.setText(valorantTeams.get(1).getName());
        }
    }

    @Override
    public int getItemCount() {
        return valorantMatchList.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView txtEvent, txtStatus, txtTournament, txtIn, txtTeam1, txtTeam2;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);

            txtEvent = itemView.findViewById(R.id.txtEvent);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtTournament = itemView.findViewById(R.id.txtTournament);
            txtIn = itemView.findViewById(R.id.txtIn);
            txtTeam1 = itemView.findViewById(R.id.txtTeam1);
            txtTeam2 = itemView.findViewById(R.id.txtTeam2);
        }
    }
}

class ValorantTeam {
    private String name;
    private String country;

    public ValorantTeam(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

class ValorantMatch {
    private String id;
    private List<ValorantTeam> teams;
    private String status;
    private String event;
    private String tournament;
    private String img;
    private String in;

    public ValorantMatch(String id, List<ValorantTeam> teams, String status, String event, String tournament, String img, String in) {
        this.id = id;
        this.teams = teams;
        this.status = status;
        this.event = event;
        this.tournament = tournament;
        this.img = img;
        this.in = in;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ValorantTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<ValorantTeam> teams) {
        this.teams = teams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTournament() {
        return tournament;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }
}
