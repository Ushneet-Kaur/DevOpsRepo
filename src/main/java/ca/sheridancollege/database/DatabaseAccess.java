package ca.sheridancollege.database;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.Equipment;
import ca.sheridancollege.beans.User;



@Repository
public class DatabaseAccess {

	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	public void addEquipment(Equipment equipment) {
		String query = "INSERT INTO equipment_dir (equipmentName, price, quantity) "
				+ "VALUES  (:equipmentName, :equipmentName, :quantity)";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("equipmentName", equipment.getEquipmentName());
		parameters.addValue("price", equipment.getPrice());
		parameters.addValue("quantity", equipment.getQuantity());
		jdbc.update(query, parameters);
	}
	
	
	public ArrayList<Equipment> getEquipments() {
		ArrayList<Equipment> equipments = new ArrayList<Equipment>();
		
		String query = "SELECT * FROM equipment_dir";
		
		List<Map<String, Object>> rows = jdbc.queryForList(query, new HashMap<String, Object>());
		for (Map<String, Object> row : rows) {
			Equipment e = new Equipment();
			e.setId((Integer)row.get("id"));
			e.setEquipmentName( (String)row.get("equipmentName") );
			e.setPrice((Double)row.get("price"));
			e.setQuantity((Integer)row.get("quantity"));
			equipments.add(e);
		}
		return equipments;
	}
	
	public Equipment getEquipmentById(int id) {
		ArrayList<Equipment> equipments = new ArrayList<Equipment>();
		
		String query = "SELECT * FROM equipment_dir WHERE id=:id";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id", id);
		
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			Equipment e = new Equipment();
			e.setId((Integer)row.get("id"));
			e.setEquipmentName( (String)row.get("equipmentName") );
			e.setPrice((Double)row.get("price"));
			e.setQuantity((Integer)row.get("quantity"));
			equipments.add(e);
		}
		if (equipments.size()>0) {
			return equipments.get(0);
		}else {
			return null;
		}
	}

	public void editEquipment(Equipment equipment) {
		String query = "UPDATE equipment_dir "
				+ "SET equipmentName=:equipmentName, price=:price, "
				+ "quantity=:quantity WHERE id=:id";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id", equipment.getId());
		parameters.addValue("equipmentName", equipment.getEquipmentName());
		parameters.addValue("price", equipment.getPrice());
		parameters.addValue("quantity", equipment.getQuantity());
		jdbc.update(query, parameters);
	}
	
	public void deleteEquipment(int id) {
		String query = "DELETE equipment_dir WHERE id=:id";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id", id);
		
		jdbc.update(query, parameters);
	}
	
	public ArrayList<Equipment> searchEquipmentsByName (String name) {
		ArrayList<Equipment> equipments = new ArrayList<Equipment>();
 
		String query = "SELECT * FROM equipment_dir WHERE equipmentName=:equipmentName";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("equipmentName", name);
		
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		for(Map<String, Object> row: rows) {
			Equipment e = new Equipment();
			e.setId((Integer)row.get("id"));
			e.setEquipmentName( (String)row.get("equipmentName") );
			e.setPrice((Double)row.get("price"));
			e.setQuantity((Integer)row.get("quantity"));
			equipments.add(e);
		}
		return equipments;
	}
	
	public ArrayList<Equipment> searchEquipmentsByRange(double min, double max, String attribute){
		ArrayList<Equipment> equipments = new ArrayList<Equipment>();

		String query = "SELECT * FROM equipment_dir WHERE "+attribute+" BETWEEN "+min +" AND "+max;
		
		List<Map<String, Object>> rows = jdbc.queryForList(query, new HashMap<String, Object>());
		for(Map<String, Object> row: rows) {
			Equipment e = new Equipment();
			e.setId((Integer)row.get("id"));
			e.setEquipmentName( (String)row.get("equipmentName") );
			e.setPrice((Double)row.get("price"));
			e.setQuantity((Integer)row.get("quantity"));
			equipments.add(e);
		}
		return equipments;
	}
	
	public User findUserAccount(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where userName=:userName";
		parameters.addValue("userName", userName);
		ArrayList<User> users = (ArrayList<User>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<User>(User.class));
		if (users.size()>0)
			return users.get(0);
		else
			return null;
	}
	
	public List<String> getRolesById(long userId) {
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "select user_role.userId, sec_role.roleName "
				+ "FROM user_role, sec_role "
				+ "WHERE user_role.roleId=sec_role.roleId "
				+ "and userId=:userId";
		parameters.addValue("userId", userId);
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		for (Map<String, Object> row : rows) {
			roles.add((String)row.get("roleName"));
		}
		return roles;
	}

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public void addUser(String userName, String password) {
		String query = "insert into SEC_User " 
				+ "(userName, encryptedPassword, ENABLED)" 
				+ " values (:userName, :encryptedPassword, 1)";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("userName", userName);	
		parameters.addValue("encryptedPassword", 
				passwordEncoder.encode(password));
		jdbc.update(query, parameters);
	}

	public void addRole(long userId, long roleId) {
		String query = "insert into user_role (userId, roleId)" 
				+ "values (:userId, :roleId);";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("userId", userId);
		parameters.addValue("roleId", roleId);
		jdbc.update(query, parameters);	
	}

	public String editEqipmentRestful(Equipment e) {
		String query = "UPDATE equipment_dir "
				+ "SET quantity=:quantity WHERE id=:id";
		String re ="";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if(e.getQuantity() > 0) {
			parameters.addValue("quantity", (e.getQuantity()-1) );
			re="SOLD";
		}
		if(e.getQuantity()== 0) {
			parameters.addValue("quantity", 0 );
			re="SOLD OUT";
		}
		jdbc.update(query, parameters);
		
		return re;
	}

}
