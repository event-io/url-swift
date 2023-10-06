package io.events.models;

//TODO: change to PASSWORD after fix Jackson serialization
public enum SupabaseGrantType {
    password("password");

    private final String grantType;

    SupabaseGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getGrantType() {
        return grantType;
    }
}
