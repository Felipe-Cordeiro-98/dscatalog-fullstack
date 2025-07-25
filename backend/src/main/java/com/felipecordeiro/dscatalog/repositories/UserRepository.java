package com.felipecordeiro.dscatalog.repositories;

import com.felipecordeiro.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
