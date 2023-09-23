package uz.cluster.services.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.cluster.entity.auth.User;
import uz.cluster.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthUserService implements AbstractService, UserDetailsService {

    private final UserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return authUserRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    public User getByUserIdComponent(int id) {
        return authUserRepository.findById(id).orElse(new User());
    }
}
