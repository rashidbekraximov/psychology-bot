package uz.cluster.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.auth.User;

import javax.validation.constraints.Email;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from users t where t.login=:login", nativeQuery = true)
    Optional<User> getUserByLogin(@Param("login") String login);

    Optional<User> findByLogin(String login);

    Optional<User> findByPassword(String password);

    boolean existsByLoginAndIdNot(String login, int id);

    boolean existsByEmailAndIdNot(@Email String email, int id);
}
