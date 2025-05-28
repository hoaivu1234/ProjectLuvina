package com.luvina.la.config.jwt;

import com.luvina.la.common.EmployeeRole;
import com.luvina.la.entity.Employee;
import com.luvina.la.repository.EmployeeRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final EmployeeRepository userRepository;
    UserDetailsServiceImpl(EmployeeRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm kiếm nhân viên trong DB theo username (tức là loginId của nhân viên)
        Optional<Employee> entity = this.userRepository.findByEmployeeLoginId(username);
        Collection<GrantedAuthority> roles;  // Khai báo biến để lưu danh sách quyền (role) của người dùng

        if (entity.isPresent()) { // Nếu tìm thấy nhân viên
            // Nếu role của nhân viên là ADMIN thì gán quyền ROLE_ADMIN
            if (EmployeeRole.ADMIN.equals(entity.get().getEmployeeRole())) {
                roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else { // Ngược lại, gán quyền ROLE_USER cho các role khác
                roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            }
            // Trả về đối tượng AuthUserDetails (custom UserDetails) chứa thông tin nhân viên và danh sách quyền
            return new AuthUserDetails(entity.get(), roles);
        } else {
            // Nếu không tìm thấy nhân viên thì ném ra exception thông báo lỗi
            throw new UsernameNotFoundException("Employee not found with username: " + username);
        }
    }
}
