package ipsen2.groep8.werkplekkenreserveringsappbackend.service;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.VerifyTokenRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.VerifyToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VerifyTokenService {
    private final VerifyTokenRepository verifyTokenRepository;
    private final UserService userService;

    public void saveVerifyToken(VerifyToken token){
        verifyTokenRepository.save(token);
    }

    public Optional<VerifyToken> getToken(String token) {
        return verifyTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return verifyTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    @Transactional
    public void confirmToken(String token) {
        VerifyToken verifyToken = this.getToken(token).orElseThrow(() -> new IllegalStateException("token not found"));

        if (verifyToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email has already been confirmed");
        }

        LocalDateTime expiresAt = verifyToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("This token has expired");
        }

        this.setConfirmedAt(token);
        userService.verifyUser(verifyToken.getUser().getId());
    }

}
