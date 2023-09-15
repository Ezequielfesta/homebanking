package com.mindhub.homebanking.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface GeneralService {
    ResponseEntity<Object> checkLoggedIn(Authentication authentication);
}