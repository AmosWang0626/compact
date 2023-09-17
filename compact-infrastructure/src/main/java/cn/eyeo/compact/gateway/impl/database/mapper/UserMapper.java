package cn.eyeo.compact.gateway.impl.database.mapper;

import cn.eyeo.compact.dto.query.UserListByParamQuery;
import cn.eyeo.compact.gateway.impl.database.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * User Mapper
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/1/8
 */
@Mapper
public interface UserMapper {

    int insert(UserDO userDO);

    int update(UserDO userDO);

    Optional<UserDO> selectById(Long id);

    List<UserDO> selectByParam(UserListByParamQuery query);

    String selectPassword(String username);

    Boolean existByUsername(@Param("userId") Long userId, @Param("username") String username);

}
