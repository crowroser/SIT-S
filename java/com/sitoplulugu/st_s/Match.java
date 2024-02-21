package com.sitoplulugu.st_s;

import java.util.List;

public class Match {
    private String event;
    private String status;
    private String tournament;
    private String in;
    private List<Team> teams;

    public Match(String event, String status, String tournament, String in, List<Team> teams) {
        this.event = event;
        this.status = status;
        this.tournament = tournament;
        this.in = in;
        this.teams = teams;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTournament() {
        return tournament;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
