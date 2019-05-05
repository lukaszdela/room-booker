package eu.lukks.repository;

import javax.persistence.criteria.From;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import eu.lukks.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("select e From User e where e.username=:username")
	public User getUserByUsername(String username);
	
}
