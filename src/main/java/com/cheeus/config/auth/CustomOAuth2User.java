package com.cheeus.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cheeus.member.domain.Member;

public class CustomOAuth2User implements OAuth2User {

    private final Member member;

    public CustomOAuth2User(Member member) {

        this.member = member;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return member.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return member.getEmail();
    }

    public String getUsername() {

        //return member.getUsername();
    	return null;
    }
}
