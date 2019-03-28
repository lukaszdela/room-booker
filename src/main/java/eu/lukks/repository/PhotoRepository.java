package eu.lukks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.lukks.domain.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>{

}
