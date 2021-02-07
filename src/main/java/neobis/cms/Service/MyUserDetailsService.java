package neobis.cms.Service;

import neobis.cms.Entity.Role;
import neobis.cms.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService  implements UserDetailsService {
    @Autowired
    private UserRepo userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        neobis.cms.Entity.User userAccount = userAccountRepository.findByEmailAndIsActive(email, true);
        List<Role> roles = new ArrayList<>();
        roles.add(userAccount.getRole());
        return new User(userAccount.getEmail(), userAccount.getPassword(), roles);
    }
}