package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public MyUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm user trong Database của bạn
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy user: " + username);
        }

        // 2. Chuyển đổi User của bạn thành User của Spring Security
        // Lưu ý: user.getPassword() trong DB phải có dạng "{noop}123456"
        // (đã xử lý ở bước Đăng ký trước đó rồi)
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}