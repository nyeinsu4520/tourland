package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/reset-password")
    public String showResetPasswordPage() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("username") String username,
                                      @RequestParam("phonenum") String phonenum,
                                      @RequestParam("email") String email,
                                      Model model) {
        Member member = memberRepository.findByUsername(username);
        if (member != null) {
            if (member.getPhonenum().equals(phonenum) && member.getEmail().equals(email)) {
                String token = memberService.createPasswordResetTokenForMember(member);
                model.addAttribute("token", token); // For demonstration purposes
                model.addAttribute("resetLink", "/change-password?token=" + token);
                // In a real app, send the token via SMS or another secure method
            } else {
                model.addAttribute("error", "Phone number or email does not match");
            }
        } else {
            model.addAttribute("error", "User not found");
        }
        return "reset-password";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(@RequestParam("token") String token, Model model) {
        Optional<Member> memberOptional = memberService.getMemberByPasswordResetToken(token);
        if (memberOptional.isPresent()) {
            model.addAttribute("token", token);
            return "change-password";
        } else {
            model.addAttribute("error", "Invalid token");
            return "reset-password";
        }
    }

    @PostMapping("/change-password")
    public String handleChangePassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "change-password";
        }
        Optional<Member> memberOptional = memberService.getMemberByPasswordResetToken(token);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            memberService.changeMemberPassword(member, password);
            return "password-changed";
        } else {
            model.addAttribute("error", "Invalid token");
            return "reset-password";
        }
    }
}
