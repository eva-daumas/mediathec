package com.eva_daumas.crud_mongo.model;

public class Stat {
    private Game game;
    private  Member member;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
