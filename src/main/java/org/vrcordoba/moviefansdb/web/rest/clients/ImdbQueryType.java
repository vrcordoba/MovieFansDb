package org.vrcordoba.moviefansdb.web.rest.clients;

public enum ImdbQueryType {

    ACTOR_DIRECTOR("nm"),
    MOVIE("tt");

    private String shortType;
    private String longType;

    ImdbQueryType(final String type) {
        this.shortType = type;
        if (type.equals("nm")) {
            this.longType = "name";
        } else {
            this.longType = "title";
        }
    }

    String getShortType() {
        return shortType;
    }

    String getLongType() {
        return longType;
    }
}
