package qp.grocery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import qp.grocery.entity.User;
import qp.grocery.entity.UserRole;
import qp.grocery.repository.UserRepository;
import qp.grocery.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByName(username).orElse(null);

        if(u == null) {
            throw new UsernameNotFoundException("No user found for the given username");
        }

        String userRole = userRoleRepository.findByUserId(u.getId()).get().getRole().toString();

        return getUserDetails(u, userRole);
    }

    private UserDetails getUserDetails(User user, String userRole) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
