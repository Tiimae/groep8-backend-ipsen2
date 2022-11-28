package ipsen2.groep8.werkplekkenreserveringsappbackend.service;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.VerifyTokenRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.VerifyToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VerifyTokenService {
    private final VerifyTokenRepository verifyTokenRepository;

    public void saveVerifyToken(VerifyToken token){
        verifyTokenRepository.save(token);
    }

}
