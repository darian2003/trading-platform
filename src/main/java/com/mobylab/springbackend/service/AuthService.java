package com.mobylab.springbackend.service;

import com.mobylab.springbackend.config.security.JwtGenerator;
import com.mobylab.springbackend.entity.Asset;
import com.mobylab.springbackend.entity.Portfolio;
import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.PortfolioRepository;
import com.mobylab.springbackend.repository.RoleRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.AssetDto;
import com.mobylab.springbackend.service.dto.LoginDto;
import com.mobylab.springbackend.service.dto.RegisterDto;
import com.mobylab.springbackend.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;


    public void register(RegisterDto registerDto) {

        if (userRepository.existsUserByEmail(registerDto.getEmail())) {
            throw new BadRequestException("Email is already used");
        }

        List<Role> roleList = new ArrayList<>();
        roleList.add(roleRepository.findRoleByName("USER").get());
        User user = new User()
                .setEmail(registerDto.getEmail())
                .setPassword(passwordEncoder.encode(registerDto.getPassword()))
                .setUsername(registerDto.getUsername())
                .setRoles(roleList);

        userRepository.save(user);

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);
    }

    public String login(LoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findUserByEmail(loginDto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new BadRequestException("Wrong credentials");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtGenerator.generateToken(authentication);

    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto toDto(User user) {
        return new UserDto()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setRoles(user.getRoles());
    }
}
