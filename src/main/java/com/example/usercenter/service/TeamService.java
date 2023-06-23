package com.example.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.usercenter.model.entity.Team;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.model.request.TeamJoinRequest;
import com.example.usercenter.model.request.TeamQuitRequest;

/**
* @author _LJC
* @description 针对表【team(队伍表)】的数据库操作Service
* @createDate 2023-06-19 16:25:32
*/
public interface TeamService extends IService<Team> {

    /**
     * 添加队伍
     * @param team
     * @param currentUser
     * @return
     */
    long addTeam(Team team, User currentUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param currentUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User currentUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param currentUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User currentUser);

    /**
     * 用户是否在队伍中
     * @param teamId
     * @param userId
     * @return
     */
    boolean isUserInTeam(Long teamId, Long userId);
}
