package com.IBatisConfiguration.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.IBatisConfiguration.Entity.Product;
import com.IBatisConfiguration.Entity.Role;
import com.IBatisConfiguration.Entity.UserDetails;
import com.IBatisConfiguration.mapper.ProductMapper;

@Service
public class ProductService {
	private final ProductMapper productMapper;

	@Autowired
	public ProductService(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	public List<Product> getAllProducts() {
		return productMapper.findAll();
	}

	public Product getProductById(Long id) {
		return productMapper.findById(id);
	}

	public void addProduct(Product product) {
		productMapper.insert(product);
	}

	public void updateProduct(Product product) {
		productMapper.update(product);
	}

	public void deleteProduct(Long id) {
		productMapper.delete(id);
	}

	public List<UserDetails> getUsersFromStoredProcedure(Long id) {
		return productMapper.getUsersFromStoredProcedure(id);
	}

	public List<UserDetails> getUsersAndRoles(Map<String,Object> id) {
		Map<String,Object> map= new HashMap<>();
		map.put("id", id);
		List<List<Map<String, Object>>> resultSets = productMapper.getMultipleResultSets(id);

		// Process the first result set for users
		List<UserDetails> users = processUserResultSet(resultSets.get(0));

		// Process the second result set for roles
		List<Role> roles = processRoleResultSet(resultSets.get(1));

		// Perform any logic with users and roles if needed

		return users;
	}

	private List<UserDetails> processUserResultSet(List<Map<String, Object>> resultSet) {
		List<UserDetails> users = new ArrayList<>();
		for (Map<String, Object> row : resultSet) {
			UserDetails user = new UserDetails();
			user.setId((Long) row.get("id"));
			user.setUsername((String) row.get("username"));
			users.add(user);
		}
		return users;
	}

	private List<Role> processRoleResultSet(List<Map<String, Object>> resultSet) {
		List<Role> roles = new ArrayList<>();
		for (Map<String, Object> row : resultSet) {
			Role role = new Role();
			role.setId((Long) row.get("role_id"));
			role.setName((String) row.get("role_name"));
			roles.add(role);
		}
		return roles;
	}
}
