package com.cheeus.member.response;

public class SignInResponse {

	private String jwt;
    private String email;

    public SignInResponse(String jwt, String email) {
        this.jwt = jwt;
        this.email = email;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }
    
}
