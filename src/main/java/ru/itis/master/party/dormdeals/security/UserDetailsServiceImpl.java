package ru.itis.master.party.dormdeals.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new UserDetailsImpl(user);
    }
}
