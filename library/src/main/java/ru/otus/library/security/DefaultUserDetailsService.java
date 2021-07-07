package ru.otus.library.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.library.model.LibraryUser;
import ru.otus.library.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return repository.findByName(userName)
                .map(this::getUser)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
    }

    private User getUser(LibraryUser libraryUser) {
        List<SimpleGrantedAuthority> authorities = libraryUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+ role.name()))
                .collect(Collectors.toList());
        return new User(libraryUser.getName(), libraryUser.getPassword(), authorities);
    }
}
