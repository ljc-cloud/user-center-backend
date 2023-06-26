package com.example.usercenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.common.ResultCode;
import com.example.usercenter.dto.TeamQuery;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.mapper.TeamMapper;
import com.example.usercenter.model.entity.Team;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.model.entity.UserTeam;
import com.example.usercenter.model.enums.TeamStatus;
import com.example.usercenter.model.request.TeamJoinRequest;
import com.example.usercenter.model.request.TeamQuitRequest;
import com.example.usercenter.model.vo.TeamVO;
import com.example.usercenter.service.TeamService;
import com.example.usercenter.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.usercenter.constant.TeamConstant.TEAM_FULL;

/**
* @author _LJC
* @description 针对表【team(队伍表)】的数据库操作Service实现
* @createDate 2023-06-19 16:25:32
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

    @Resource
    private UserTeamService userTeamService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User currentUser) {
        if (team == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        if (currentUser == null) {
            throw new BusinessException(ResultCode.NOT_LOGIN, "用户未登录");
        }
        final long userId = currentUser.getId();
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 50) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "队伍名称不合法");
        }
        String description = team.getDescription();
        if (StringUtils.isBlank(description) || description.length() > 200) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "队伍描述过长");
        }
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(1);
        if (maxNum > 10) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "队伍最大人数过多");
        }
        Integer status = team.getStatus();
        TeamStatus teamStatus = TeamStatus.getStatusByValue(status);
        if (teamStatus == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        String password = team.getPassword();
        if (TeamStatus.SECRETE.equals(teamStatus) && (StringUtils.isBlank(password) || password.length() > 32)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "队伍密码设置不正确");
        }
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "超时时间 > 当前时间");
        }
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        long count = this.count(queryWrapper);
        if (count > 5) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "当前用户创建的队伍过多");
        }
        // 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "插入队伍表失败");
        }
        // 插入 用户队伍关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setId(null);
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "插入用户队伍关系表失败");
        }
        return teamId;
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User currentUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        Long teamId = teamJoinRequest.getTeamId();
        Long userId = currentUser.getId();
        getTeamById(teamId);
        // 查看该用户是否已经加入该队伍
        boolean userInTeam = isUserInTeam(teamId, userId);
        if (userInTeam) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "该用户已经加入这个队伍了");
        }
        // 查看队伍是否已满
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        long teamCount = userTeamService.count(queryWrapper);
        if (teamCount >= TEAM_FULL) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "队伍已满");
        }
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(teamId);
        userTeam.setUserId(userId);
        userTeam.setJoinTime(new Date());
        boolean saveRes = userTeamService.save(userTeam);
        if (!saveRes) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "加入队伍失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User currentUser) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        Long teamId = teamQuitRequest.getTeamId();
        Team team = getTeamById(teamId);
        Long userId = currentUser.getId();
        boolean userInTeam = isUserInTeam(teamId, userId);
        if (!userInTeam) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "该用户没有加入过这个队伍") ;
        }
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        long userTeamCount = userTeamService.count(queryWrapper);
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId).eq("user_id", userId);
        boolean userTeamRemoveRes = userTeamService.remove(queryWrapper);
        // 队伍只剩1人
        if (userTeamCount == 1) {
//            queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("team_id", teamId).eq("user_id", userId);
//            boolean userTeamRemoveRes = userTeamService.remove(queryWrapper);
            boolean teamRemoveRes = this.removeById(teamId);
            return userTeamRemoveRes && teamRemoveRes;
        } else {
            // 如果是队长退出队伍
            if (Objects.equals(userId, team.getUserId())) {
//                queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("team_id", teamId).eq("user_id", userId);
//                userTeamService.remove(queryWrapper);
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("team_id", teamId).orderByDesc("join_time");
                List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
                UserTeam nextUserTeamLeader = userTeamList.get(0);
                Team teamForUpdate = new Team();
                teamForUpdate.setId(teamId);
                teamForUpdate.setUserId(nextUserTeamLeader.getUserId());
                boolean updateRes = this.updateById(teamForUpdate);
                return userTeamRemoveRes && updateRes;
            }
        }
        return false;
    }

    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "队伍ID错误");
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "不存在该队伍");
        }
        return team;
    }

    public boolean isUserInTeam(Long teamId, Long userId) {
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId).eq("user_id", userId);
        long teamCount = userTeamService.count(queryWrapper);
        return teamCount > 0;
    }

    @Override
    public List<TeamVO> getTeamList(TeamQuery teamQuery) {
        Team team = new Team();
        BeanUtil.copyProperties(teamQuery, team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<TeamVO> teamVOList = this.list(queryWrapper).stream().map(item -> {
            TeamVO teamVo = new TeamVO();
            BeanUtil.copyProperties(item, teamVo);
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper.eq("team_id", item.getId());
            long teamUserCount = userTeamService.count(userTeamQueryWrapper);
            teamVo.setJoinNum((int) teamUserCount);
            return teamVo;
        }).collect(Collectors.toList());
        return teamVOList;
    }
}




