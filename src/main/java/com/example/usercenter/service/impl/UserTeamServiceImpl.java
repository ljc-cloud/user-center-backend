package com.example.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.mapper.UserTeamMapper;
import com.example.usercenter.model.entity.UserTeam;
import com.example.usercenter.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author _LJC
* @description 针对表【user_team(用户_队伍表)】的数据库操作Service实现
* @createDate 2023-06-19 16:27:45
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




