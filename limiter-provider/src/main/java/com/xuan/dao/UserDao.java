package com.xuan.dao;

import com.xuan.pojo.UserVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Leo xuan
 * @date 2018/9/4
 */
@Repository
public interface UserDao {

	@Select("select * from user")
	List<UserVO> selectAll();
}
