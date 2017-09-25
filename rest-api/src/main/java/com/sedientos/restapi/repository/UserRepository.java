package com.sedientos.restapi.repository;

import com.sedientos.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(@Param("email")String email);
    List<User> findByName(@Param("name")String name);

}