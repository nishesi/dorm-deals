package ru.itis.master.party.dormdeals.security.details;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itis.master.party.dormdeals.models.User;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    @Getter
    private final User user;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(User user) {
        this.user = user;
        grantedAuthorities = user.getAuthorities().stream()
                .map(Enum::toString)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getHashPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getState() == User.State.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAccountNonLocked();
    }
}
