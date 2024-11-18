package com.kupstudio.bbarge.service.user;

import com.kupstudio.bbarge.constant.user.UserConstant;
import com.kupstudio.bbarge.dao.dbUser.user.UserDao;
import com.kupstudio.bbarge.dto.user.UserInfoDto;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public UserInfoDto getUserInfo(int userNo) {
        UserInfoDto userInfoDto = userDao.getUserInfo(userNo);

        if (ObjectUtils.isEmpty(userInfoDto)) {
            throw new PostNotExistException(UserConstant.USER_IS_NOT_EXIST);
        }

        return userInfoDto;
    }
}
