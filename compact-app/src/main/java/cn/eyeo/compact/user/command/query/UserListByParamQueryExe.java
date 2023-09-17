package cn.eyeo.compact.user.command.query;

import cn.eyeo.compact.domain.gateway.UserGateway;
import cn.eyeo.compact.domain.user.UserEntity;
import cn.eyeo.compact.dto.data.UserVO;
import cn.eyeo.compact.dto.query.UserListByParamQuery;
import cn.eyeo.compact.user.assembler.UserAssembler;
import com.alibaba.cola.dto.MultiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserListByNameQueryExe
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/1/10
 */
@Component
public class UserListByParamQueryExe {

    @Autowired
    private UserGateway userGateway;

    public MultiResponse<UserVO> execute(UserListByParamQuery query) {
        List<UserEntity> userEntities = userGateway.findByParam(query);
        List<UserVO> userVOList = userEntities.stream()
                .map(UserAssembler::toValueObject)
                .collect(Collectors.toList());

        return MultiResponse.of(userVOList);
    }

}
