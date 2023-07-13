package com.weather.api.weatherStatus.repository;


import com.weather.api.weatherStatus.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<UserRequest,String> {
}
