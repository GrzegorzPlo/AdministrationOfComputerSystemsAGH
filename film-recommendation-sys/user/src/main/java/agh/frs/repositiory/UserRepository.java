package agh.frs.repositiory;

import agh.frs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Transactional
    @Modifying
    @Query("update User u set u.movieId = ?1 where u.name = ?2")
    int setMovieIdById(int movieId, String name);

    @Query("select u from User u where u.name = ?1")
    Optional<User> findByName(String name);

    Boolean existsByName(String name);
}
