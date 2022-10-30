package ca.sheridancollege.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.beans.Equipment;
import ca.sheridancollege.database.DatabaseAccess;

@Controller
public class HomeController {
	
	@Autowired
	@Lazy
	private DatabaseAccess da;
	
	@GetMapping("/")
    public String root() {
        return "index.html";
    }

	@GetMapping("/boss/addEquipment") 
	public String addEquipment(Model model) {
		model.addAttribute("equipment", new Equipment());
		return "/boss/addEquipment.html";
	}
	
	@GetMapping("/saveEquipment")
	public String save(Model model, @ModelAttribute Equipment equipment) {
		da.addEquipment(equipment);
		
		return "redirect:/addEquipment";
	}
	
	@GetMapping("/viewEquipment")
	public String viewEquipmentsAll(Model model) {
		model.addAttribute("equipments", da.getEquipments());
		return "/viewEquipments.html";
	}
	
	@GetMapping("/boss/viewEquipment")
	public String viewEquipments(Model model) {
		model.addAttribute("equipments", da.getEquipments());
		return "/boss/viewEquipments.html";
	}
	
	@GetMapping("/boss/edit/{id}")
	public String edit(Model model, @PathVariable int id){
		Equipment e = da.getEquipmentById(id);
		model.addAttribute("equipment", e);
		return "/boss/modify.html";
	}

	@GetMapping("/boss/modify")
	public String modify(Model model, @ModelAttribute Equipment equipment) {
		da.editEquipment(equipment);
		return "redirect:/boss/viewEquipment";
	}

	@GetMapping("/boss/delete/{id}")
	public String delete(Model model, @PathVariable int id){
		da.deleteEquipment(id);
		return "redirect:/boss/viewEquipment";
	}
	
	@GetMapping("/login")
    public String login() {
        return "login.html";
    }
	
	@GetMapping("/user/search")
	public String goToSearch(Model model) {
		model.addAttribute("equipment", new Equipment());
		return "/user/search.html";
	}
	
	@GetMapping("/user/searchByName")
	public String serachByName(Model model, @ModelAttribute Equipment equipment) {
		model.addAttribute("equipmentsByName", da.searchEquipmentsByName(equipment.getEquipmentName()));
		return "/user/searchByName.html";
	}
	
	@GetMapping("/user/searchByPrice")
	public String serachByPriceRange(@RequestParam double min, @RequestParam double max, Model model) {
		model.addAttribute("equipmentsByPrice", da.searchEquipmentsByRange(min, max, "price"));
		return "/user/searchByPrice.html";
	}
	
	@GetMapping("/user/searchByQuantity")
	public String serachByQuantityRange(@RequestParam double min, @RequestParam double max, Model model) {
		model.addAttribute("equipmentsByQuantity", da.searchEquipmentsByRange(min, max, "quantity"));
		return "/user/searchByQuantity.html";
	}
	
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied.html";
    }

    @GetMapping("/register")
	public String goRegistration () {
		return "register.html";
	}
	
	@PostMapping("/register")
	public String doRegistration(@RequestParam String name, @RequestParam String password,
			@RequestParam(defaultValue = "false") boolean boss, @RequestParam(defaultValue = "false") boolean worker) {

		da.addUser(name, password);
		long userId = da.findUserAccount(name).getUserId();
		if(boss) {
			da.addRole(userId, 1);
		}
		if(worker) {
			da.addRole(userId, 2);
		}
		return "redirect:/";

	}

}
