package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class MemberController {
	
	@Autowired
	private MemberRepository memberRepo;
	
	
	@GetMapping("/members")
	public String viewMember(Model model) {
		
		List<Member> memberList=memberRepo.findAll();
		model.addAttribute("memberList",memberList);
		return "ViewMember";
	}
	
	@GetMapping("/member/add")
	public String addMember(Model model) {
		model.addAttribute("member",new Member());
		return "add_member";
	}
	
	@PostMapping("/member/save")
	public String saveMember(@Valid Member member,BindingResult bindingResult,RedirectAttributes redirectAttribute) {
		if(bindingResult.hasErrors()) {
			return "add_member";
		}
		BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		String encodePassword=passwordEncoder.encode(member.getPassword());
		member.setPassword(encodePassword);
		memberRepo.save(member);
		
		redirectAttribute.addFlashAttribute("success","Member Registered!");	
		return "redirect:/members";
	}
	
	@GetMapping("/member/edit/{id}")
	public String editMember(@PathVariable("id") Integer id,Model model) {
		Member member=memberRepo.getReferenceById(id);
		model.addAttribute("member",member);
		return "edit_member";
	}
	@PostMapping("/member/edit/{id}")
	public String saveMember(@PathVariable("id")Integer id, Member member) {
		memberRepo.save(member);
		return "redirect:/members";
	}
	
	@GetMapping("/member/delete/{id}")
	public String deleteMember(@PathVariable("id")Integer id,Member member) {
		memberRepo.delete(member);
		return "redirect:/members";
	}
}
