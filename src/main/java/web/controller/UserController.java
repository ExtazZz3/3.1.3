package web.controller;

import web.model.Role;
import web.model.User;
import web.service.UserService;
import web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/login")
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
	public String updateUserById(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", userService.getUserById(id));
		return "edit";
	}

	@PutMapping("/admin/edit/{id}")
	public String updateUser(@ModelAttribute("user") User user,
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
		userService.updateUser(user);
		return "redirect:/admin";
	}
	@DeleteMapping("/admin/delete/{id}")
	public String deleteUserById(@PathVariable("id") Long id) {
		userService.deleteUserById(id);
		return "redirect:/admin";
	}
}