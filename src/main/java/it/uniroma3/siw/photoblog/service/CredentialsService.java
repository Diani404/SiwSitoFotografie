package it.uniroma3.siw.photoblog.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.photoblog.exception.DuplicateUsernameException;
import it.uniroma3.siw.photoblog.model.Credentials;
import it.uniroma3.siw.photoblog.model.User;
import it.uniroma3.siw.photoblog.repository.CredentialsRepository;

@Service
public class CredentialsService {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;

    public CredentialsService(PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(String username) {
        return this.credentialsRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public User getUser(String username) {
        Credentials credentials = this.credentialsRepository.findByUsername(username);
        return credentials == null ? null : credentials.getUser();
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) throws DuplicateUsernameException {
        if (this.credentialsRepository.findByUsername(credentials.getUsername()) != null) {
            throw new DuplicateUsernameException(credentials.getUsername());
        }
        credentials.setRole(Credentials.USER_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }
}
