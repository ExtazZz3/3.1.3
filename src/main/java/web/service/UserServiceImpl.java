package web.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao dao;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao dao, BCryptPasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.saveUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        return dao.getUserById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByUsername(String username) {
        return dao.getUserByEmail(username);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByEmail(String email) { return dao.getUserByEmail(email); }

    @Transactional
    @Override
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.updateUser(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        dao.deleteUserById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dao.getUserByEmail(username);
        if(user.getUsername() == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true, getGrantedAuthorities(user));
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(Role role : user.getRoles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
        }
        return authorities;
    }
}