CREATE TABLE equipment_dir (
	id INT NOT NULL Primary Key AUTO_INCREMENT, 
	equipmentName VARCHAR(225), 
	price DOUBLE,
	quantity INT
);

INSERT INTO equipment_dir (equipmentName, price, quantity) VALUES
('water pump', 99.99, 5),
('glass cleaner', 45.89, 9),
('polish', 10.99, 7),
('pumice', 67.99, 8),
('caulk', 45.42, 5),
('vacuum', 87, 4),
('filter', 23.86, 6),
('chemicals', 34, 66),
('floats', 67, 7),
('valves',69, 9.99 ),
('cleaner', 420, 69);