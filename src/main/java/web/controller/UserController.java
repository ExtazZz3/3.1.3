package web.controller;

import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {

	private final UserService userService;
	private final RoleService roleService;

	public UserController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping("/admin")
	public String getAllUsers(@AuthenticationPrincipal UserDetails user, Model model) {
		model.addAttribute("user", userService.getUserByUsername(user.getUsername()));
		model.addAttribute("users", userService.getAllUsers());
		return "admin";
	}

	@GetMapping("/user")
	public String getUserById(@AuthenticationPrincipal UserDetails user, Model model) {
		model.addAttribute("user", userService.getUserByUsername(user.getUsername()));
		return "user";
	}

	@ModelAttribute("roleList")
	public List<Role> initializeRoles() {
		return roleService.getAllRoles();
	}

	@PostMapping("/admin/new")
	public String addUser(@ModelAttribute("user") User user,
						  @RequestParam(value = "roles") Set<Role> roles) {
		setIdToRoles(roles, user);
		userService.addUser(user);
		return "redirect:/admin";
	}

	@PutMapping("/admin/edit/{id}")
	public String updateUser(@ModelAttribute("user") User user,
							 @RequestParam(value = "roles") Set<Role> roles) {
		setIdToRoles(roles, user);
		userService.updateUser(user);
		return "redirect:/admin";
	}
	@DeleteMapping("/admin/delete/{id}")
	public String deleteUserById(@PathVariable("id") Long id) {
		userService.deleteUserById(id);
		return "redirect:/admin";
	}

	private void setIdToRoles(Set<Role> roles, User user) {
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
	}
}