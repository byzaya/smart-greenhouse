package org.greenhouse.service.user;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.user.User;
import org.greenhouse.dto.auth.ChangePasswordRequest;
import org.greenhouse.repository.user.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // проверяем, что введенный пароль корректный
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password"); // todo сделать свое исключение
        }
        // проверка что оба введенных пароля одинаковые
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same"); // todo сделать свое исключение
        }
        // обновляем пароль
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }
}
