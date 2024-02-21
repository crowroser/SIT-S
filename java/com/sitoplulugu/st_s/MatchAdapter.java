package com.sitoplulugu.st_s;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;



public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private Context context;
    private List<Match> matchList;

    public MatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_item, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.txtEvent.setText(match.getEvent());
        holder.txtStatus.setText(match.getStatus());
        holder.txtTournament.setText(match.getTournament());
        holder.txtIn.setText(match.getIn());


        List<Team> teams = match.getTeams();
        if (teams != null && teams.size() == 2) {
            holder.txtTeam1.setText(teams.get(0).getName());
            holder.txtTeam2.setText(teams.get(1).getName());
        }


        }


    @Override
    public int getItemCount() {
        return matchList.size();
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
