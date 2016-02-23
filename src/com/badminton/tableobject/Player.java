package com.badminton.tableobject;

public class Player {
	private String playerName;
	private String date;
	private String tournament;
	private String match;
	private String stadium;
	public Player(String playerName, String date, String tournament, String match, String stadium) {
		this.playerName = playerName;
		this.date = date;
		this.tournament = tournament;
		this.match = match;
		this.stadium = stadium;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTournament() {
		return tournament;
	}
	public void setTournament(String tournament) {
		this.tournament = tournament;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public String getStadium() {
		return stadium;
	}
	public void setStadium(String stadium) {
		this.stadium = stadium;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((match == null) ? 0 : match.hashCode());
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + ((stadium == null) ? 0 : stadium.hashCode());
		result = prime * result + ((tournament == null) ? 0 : tournament.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (match == null) {
			if (other.match != null)
				return false;
		} else if (!match.equals(other.match))
			return false;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		if (stadium == null) {
			if (other.stadium != null)
				return false;
		} else if (!stadium.equals(other.stadium))
			return false;
		if (tournament == null) {
			if (other.tournament != null)
				return false;
		} else if (!tournament.equals(other.tournament))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Player [playerName=" + playerName + ", date=" + date + ", tournament=" + tournament + ", match=" + match
				+ ", stadium=" + stadium + "]";
	}
	
	
}
