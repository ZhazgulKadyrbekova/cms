package neobis.cms.Service.Bishkek;

import neobis.cms.Entity.Bishkek.Role;
import neobis.cms.Repo.Bishkek.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService  implements UserDetailsService {
    private final UserRepo userAccountRepository;

    public MyUserDetailsService(UserRepo userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        neobis.cms.Entity.Bishkek.User userAccount = userAccountRepository.findByEmailIgnoringCaseAndActive(email, true);
        List<Role> roles = new ArrayList<>();
        roles.add(userAccount.getRole());
        return new User(userAccount.getEmail(), userAccount.getPassword(), roles);
    }
}