package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.services.GeneralService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class GeneralServiceImplement implements GeneralService {

    @Override
    public ResponseEntity<Object> checkLoggedIn(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("You must be logged in", HttpStatus.FORBIDDEN);
        } else return null;
    }
}
