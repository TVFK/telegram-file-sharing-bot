package ru.taf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.taf.entity.AppPhoto;

@Repository
public interface AppPhotoRepository extends JpaRepository<AppPhoto, Long> {
}
