package com.example.demo;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class MemberController {
    
    @Autowired
    private MemberRepository memberRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    
    
    @GetMapping("/members")
    public String viewMember(Model model) {
        List<Member> memberList = memberRepo.findAll();
        model.addAttribute("memberList", memberList);
        return "ViewMember";
    }
    
    @GetMapping("/member/add")
    public String addMember(Model model) {
        model.addAttribute("member", new Member());
        return "add_member";
    }
    
    @PostMapping({"/member/save", "/register"})
    public String saveMember(@Valid Member member, BindingResult bindingResult,
                             @RequestParam(name = "memberImage", required = false) MultipartFile imgFile,
                             RedirectAttributes redirectAttribute, Model model) {
        if (bindingResult.hasErrors()) {
            return "add_member"; // Return to the form with errors
        }

        // Check if the username or email already exists
        if (memberRepo.existsByUsername(member.getUsername())) {
            bindingResult.rejectValue("username", "error.member", "Username already exists.");
            return "add_member";
        }

        if (memberRepo.existsByEmail(member.getEmail())) {
            bindingResult.rejectValue("email", "error.member", "Email already exists.");
            return "add_member";
        }

        // Set the role to "USER" if not already set
        if (member.getRole() == null || member.getRole().isEmpty()) {
            member.setRole("ROLE_USER");
        }

        // Encode the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        try {
            // Save the member entity first to generate the ID
            Member savedMember = memberRepo.save(member);

            // Handle image upload
            if (imgFile != null && !imgFile.isEmpty()) {
                String imageName = StringUtils.cleanPath(imgFile.getOriginalFilename());
                String uploadDir = "uploads/members/" + savedMember.getId();
                Path uploadPath = Paths.get(uploadDir);

                // Create directories if they don't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Destination path for uploaded image
                Path filePath = uploadPath.resolve(imageName);
                Files.copy(imgFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Set uploaded image URL in member entity
                savedMember.setImageUrl(imageName);

                // Update member entity with image URL
                memberRepo.save(savedMember);
            } else {
                // Handle case where no image is provided, if necessary
            }

            // Redirect with success message
            redirectAttribute.addFlashAttribute("success", "Member Registered!");
            return "redirect:/login"; // Redirect to login page after successful registration

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttribute.addFlashAttribute("error", "Failed to upload image.");
            return "redirect:/register"; // Redirect to registration form with error message
        }
    }


    @GetMapping("/member/edit/{id}")
    public String editMember(@PathVariable("id") Integer id, Model model) {
        Member member = memberRepo.getReferenceById(id);
        model.addAttribute("member", member);
        return "edit_member";
    }

    @PostMapping("/member/edit/{id}")
    public String saveEditedMember(@PathVariable("id") Integer id, @Valid Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit_member"; // Return to the form with errors
        }

        Member existingMember = memberRepo.getReferenceById(id);

        // Only update the password if a new one is provided
        if (member.getPassword() != null && !member.getPassword().isEmpty()) {
            existingMember.setPassword(passwordEncoder.encode(member.getPassword()));
        }

        existingMember.setName(member.getName());
        existingMember.setUsername(member.getUsername());
        existingMember.setEmail(member.getEmail());
        existingMember.setRole(member.getRole());
        existingMember.setPhonenum(member.getPhonenum());

        // Log the member details being saved
        logger.info("Saving member: {}", existingMember);

        memberRepo.save(existingMember);
        return "redirect:/members";
    }
    @GetMapping("/member/delete/{id}")
    public String deleteMember(@PathVariable("id") Integer id) {
        memberRepo.deleteById(id);
        return "redirect:/members";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "Register";
    }
    
    
    
    



 

}
