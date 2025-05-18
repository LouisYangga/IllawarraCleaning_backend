package com.example.user_service.service;

import java.util.Base64;
import org.springframework.stereotype.Service;

import com.example.user_service.dto.AdminDTO;
import com.example.user_service.dto.CreateAdminDTO;
import com.example.user_service.entity.Admin;
import com.example.user_service.mapper.AdminMapper;
import com.example.user_service.repository.AdminRepository;
import com.example.user_service.util.PasswordHashUtil;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminService(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
        this.adminRepository = adminRepository;
    }

    public AdminDTO createAdmin(CreateAdminDTO createAdminDTO) {
        // Logic to create an admin
        if(adminRepository.existsByEmail(createAdminDTO.getEmail())) {
            throw new IllegalArgumentException("Admin with this email already exists");
        }
        
        Admin admin = adminMapper.toEntity(createAdminDTO);
        
        // Generate salt and hash password
        byte[] salt = PasswordHashUtil.generateSalt();
        String hashedPassword = PasswordHashUtil.hashPassword(createAdminDTO.getPassword(), salt);
        
        admin.setPassword(hashedPassword);
        admin.setPasswordSalt(Base64.getEncoder().encodeToString(salt));
        
        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDTO(savedAdmin);
    }    
    public boolean adminLogin(String email, String password) {
        // Logic to check admin login
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with this email"));
                
        byte[] salt = Base64.getDecoder().decode(admin.getPasswordSalt());
        if (PasswordHashUtil.verifyPassword(password, admin.getPassword(), salt)) {
            admin.setLastLoginDate(java.time.LocalDate.now());
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    public AdminDTO getAdminByEmail(String email) {
        // Logic to get an admin by email
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with this email"));
        return adminMapper.toDTO(admin);
    }

    public boolean checkAdminExists(String email) {
        // Logic to check if an admin exists
        return adminRepository.existsByEmail(email);
    }
    public String deleteAdminByEmail(String email, CreateAdminDTO authorisedAdminDTO) {
        // Only allow authorized admin to delete
        Admin authAdmin = adminRepository.findByEmail(authorisedAdminDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Authorized admin not found"));
                
        if (!authAdmin.getEmail().equalsIgnoreCase("superadmin@gmail.com")) {
            throw new SecurityException("Only super admin can delete admins");
        }
        
        byte[] salt = Base64.getDecoder().decode(authAdmin.getPasswordSalt());
        if(!PasswordHashUtil.verifyPassword(authorisedAdminDTO.getPassword(), authAdmin.getPassword(), salt)) {
            throw new SecurityException("Invalid password for authorized admin");
        }
        
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with this email"));
        adminRepository.delete(admin);
        return "Admin deleted successfully";
    }
}
