package com.IBatisConfiguration.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import com.IBatisConfiguration.Entity.Product;
import com.IBatisConfiguration.Entity.UserDetails;

@Mapper
public interface ProductMapper {
	@Select("SELECT * FROM product")
	List<Product> findAll();

	@Select("SELECT * FROM product WHERE id = #{id}")
	Product findById(Long id);

	@Insert("INSERT INTO product (name, description, price) VALUES (#{name}, #{description}, #{price})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(Product product);

	@Update("UPDATE product SET name = #{name}, description = #{description}, price = #{price} WHERE id = #{id}")
	int update(Product product);

	@Delete("DELETE FROM product WHERE id = #{id}")
	int delete(Long id);

	@Select("exec GetUserWithRoleById @id=#{id}")
	@Results({ @Result(property = "id", column = "id"), @Result(property = "username", column = "username"),
			@Result(property = "role.id", column = "role_id"), @Result(property = "role.name", column = "role_name") })
	List<UserDetails> getUsersFromStoredProcedure(Long id);

	@Select("exec multiResultSetStored @id=#{id}")
    @Options(statementType = StatementType.CALLABLE)
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "role.id", column = "role_id"),
            @Result(property = "role.name", column = "role_name")
    })
    List<List<Map<String, Object>>> getMultipleResultSets(Map<String,Object> id);
}
