package com.example.testmanagment.helper;

import com.example.testmanagment.model.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class GenericServiceHelper {
    public static <T> UserResponse saveEntity(T entity, JpaRepository<T, Long> repository, String successMessage, List<UserResponse.UserDetail> userDetails) {
        try {
            repository.save(entity);
            userDetails.add(new UserResponse.UserDetail(0, true, successMessage));
        } catch (Exception e) {
            userDetails.add(new UserResponse.UserDetail(0, false, "SERVICE_RESPONSE_FAILURE: " + e.getMessage()));
        }
        return new UserResponse(userDetails);
    }
}
