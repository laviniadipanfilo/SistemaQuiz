package it.sistemaquiz.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.sistemaquiz.model.LoginUtente;
import it.sistemaquiz.model.RegisterUtente;
import it.sistemaquiz.model.Utente;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Utente> register(@RequestBody RegisterUtente registerUserDto) {
        Utente registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUtente loginUserDto) {
    	Utente authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
    
    public class LoginResponse {
    	
        private String token;
        private long expiresIn;

        public String getToken() {
            return token;
        }

		public LoginResponse setToken(String jwtToken) {
			this.token = jwtToken;
			return this;
		}

		public long getExpiresIn() {
			return expiresIn;
		}

		public LoginResponse setExpiresIn(long expiresIn) {
			this.expiresIn = expiresIn;
			return this;
		}

    }
}
