package io.github.adigunhammedolalekan.authkitsample.api;

import io.github.adigunhammedolalekan.authkit.types.Token;
import io.github.adigunhammedolalekan.authkit.types.User;
import io.github.adigunhammedolalekan.authkitsample.service.AuthService;
import io.github.adigunhammedolalekan.authkitsample.types.AuthenticateAccountRequest;
import io.github.adigunhammedolalekan.authkitsample.types.CreateAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    public ResponseEntity<User> signup(@RequestBody() CreateAccountRequest request) {
        return ResponseEntity.ofNullable(authService.createAccount(request));
    }

    @PostMapping("login")
    public ResponseEntity<Token> login(@RequestBody() AuthenticateAccountRequest request) {
        return ResponseEntity.ofNullable(authService.login(request));
    }

    @GetMapping("me")
    public ResponseEntity<User> me(
            @RequestHeader() String authorization) {
        var token = authorization.split(" ")[1];
        return ResponseEntity.ok(authService.getUser(token));
    }

    @GetMapping("thirdparty/{provider}/login")
    public ResponseEntity<String> thirdPartyLogin(
            @PathVariable() String provider) {
        return ResponseEntity.ok(authService.thirdPartyLogin(provider));
    }

    @GetMapping("thirdparty/{provider}/complete")
    public ResponseEntity<Token> thirdPartyLoginComplete(
            @PathVariable() String provider,
            @RequestParam() String code) {
        return ResponseEntity.ok(authService.completeThirdPartyLogin(provider, code));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> delete(@RequestHeader() String authorization) {
        var token = authorization.split(" ")[1];
        authService.deleteAccount(token);
        return ResponseEntity.noContent().build();
    }
}
