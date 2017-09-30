package pl.piterpti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.*;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find User by login
	 * @param login user login
	 * @return found user
	 */
	public User findByLogin(String login);
	
}
