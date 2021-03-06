package org.moren.spring.service.imp;

import org.moren.spring.model.Role;
import org.moren.spring.model.User;
import org.moren.spring.repository.RoleRepository;
import org.moren.spring.repository.UserRepository;
import org.moren.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean save(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }

        Role role = roleRepository.findByName("ROLE_USER").get();

        user.setRoles(Collections.singleton(role));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void delete(User user) {
        userRepository.findById(user.getId()).ifPresent(user1 -> userRepository.delete(user));
    }

    @Override
    public void update(User user) {
        userRepository.findById(user.getId()).ifPresent(user1 -> userRepository.save(user));
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}
