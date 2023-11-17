package nz.ac.canterbury.seng302.tab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.TokenDump;
import nz.ac.canterbury.seng302.tab.repository.TokenDumpRepository;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for Token Dump, defined by the @link{Service} annotation.
 * This class links automatically with @link{TokenDumpRepository},
 * see the @link{Autowired} annotation below
 */
@Service
public class TokenDumpService {
    /**
     * To use the TokenDump functionalities with the database
     */
    @Autowired
    private TokenDumpRepository tokenDumpRepository;

    /**
     * Adds Token to Dump
     *
     * @param token String representation of Token
     * @return TokenEntity once saved
     */
    public TokenDump addToken(String token) {
        TokenDump tokenDump = new TokenDump(token);
        return tokenDumpRepository.save(tokenDump);
    }

    /**
     * Returns all tokens
     *
     * @return List of all tokens
     */
    public List<TokenDump> findAll() {
        return tokenDumpRepository.findAll();
    }

    /**
     * Finds token by id
     *
     * @param id long representing tokens id
     * @return TokenDump Entity if Found
     */
    public Optional<TokenDump> findById(long id) {
        return tokenDumpRepository.findById(id);
    }

    /**
     * Gets all tokens in dump
     *
     * @return List of all tokens in dump
     */
    public List<String> getAllTokens() {
        List<String> allTokens = new ArrayList<>();
        List<TokenDump> tokens = findAll();
        for (TokenDump token : tokens) {
            allTokens.add(token.getToken());
        }
        return allTokens;
    }

    /**
     * Checks to see if token is in dump
     *
     * @param token String representation of token
     * @return True if token is in dump else false
     */
    public boolean checkIfTokenExists(String token) {
        return tokenDumpRepository.findByToken(token).isPresent();
    }

    /**
     * Continuously creates a token if token is found in dump
     *
     * @return String representation of token
     */
    public String createToken() {
        String token = TokenGenerator.genAlphaToken();
        while (checkIfTokenExists(token)) {
            token = TokenGenerator.genAlphaToken();
        }
        return token;
    }

    /**
     * Checks if there is only one instance of the token within the token_dump table
     *
     * @param token String token
     * @return boolean true if 1, else false
     */
    public boolean checkOnlyOneInstance(String token) {
        return tokenDumpRepository.findInstanceOfToken(token) == 1;
    }
}
