package ca.sheridancollege.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.beans.Equipment;
import ca.sheridancollege.database.DatabaseAccess;

@RestController
public class RestfulController {

	@Autowired
	@Lazy
	private DatabaseAccess da;
	
	//POST COLLECTION
		@PostMapping(value="/equipment", headers={"Content-type=application/json"})
		public String poststudent(@RequestBody Equipment equipment) {
			da.addEquipment(equipment);
			return "equipment was added";
		}
	//GET COLLECTION
	@GetMapping("/equipment")
	public ArrayList<Equipment> getStudents() {
		return da.getEquipments();
	}
	
	//GET ELEMENT
	@GetMapping("/equipment/{id}")
	public Equipment getEquipmentById(@PathVariable int id) {
		return da.getEquipmentById(id);
	}
	
	//PUT ELEMENT
		@PutMapping(value="/equipment/{id}", headers={"Content-type=application/json"})
		public String putEquipment(@RequestBody Equipment equipment, @PathVariable int id) {
			Equipment e = da.getEquipmentById(id);
			String result = da.editEqipmentRestful(e);

			return result;	
		}
}
