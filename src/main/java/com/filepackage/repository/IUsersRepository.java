package com.filepackage.repository;

import com.filepackage.entity.Users;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface IUsersRepository extends IBaseRepository<Users>{
    Optional<Users> findByEmail(String email);
//    Optional<Users> findByName(String name);
}
