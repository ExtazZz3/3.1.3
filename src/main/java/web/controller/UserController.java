package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/admin")
	public String getAllUsers(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return "index";
	}

	@GetMapping("user/{id}")
	public String getUserById(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", userService.getUserById(id));
		return "user";
	}

	@ModelAttribute("roleList")
	public List<Role> initializeRoles() {
		return roleService.getAllRoles();
	}

	@GetMapping("/admin/new")
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		return "new";
	}

	@PostMapping("/admin/new")
	public String addUser(@ModelAttribute("user") User user,
						  @RequestParam(value = "roles") Set<Role> roles) {

		if (roles != null) {
			for (Role role : roles) {
				if ("ADMIN".equals(role.getRole())) {
					role.setId(1L);
				}
				else if ("USER".equals(role.getRole())) {
					role.setId(2L);
				}
				user.addRole(role);
			}
		}
		userService.addUser(user);
		return "redirect:/admin";
	}

	@GetMapping("/admin/edit/{id}")
	public String updateUserById(@PathVariable("id") Long id,  Model model) {
		model.addAttribute("user", userService.getUserById(id));
		return "edit";
	}
	@DeleteMapping("/admin/delete/{id}")
	public String deleteUserById(@PathVariable("id") Long id) {
		userService.deleteUserById(id);
		return "redirect:/admin";
	}
}