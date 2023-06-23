package com.example.usercenter.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ResultCode;
import com.example.usercenter.common.ResultUtils;
import com.example.usercenter.dto.TeamQuery;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.entity.Team;
import com.example.usercenter.model.entity.User;
import com.example.usercenter.model.request.TeamAddRequest;
import com.example.usercenter.model.request.TeamJoinRequest;
import com.example.usercenter.model.request.TeamQuitRequest;
import com.example.usercenter.service.TeamService;
import com.example.usercenter.service.UserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author _LJC
 */
@RestController
@RequestMapping("/team")
@CrossOrigin("http://localhost:5173")
public class TeamController {

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtil.copyProperties(teamAddRequest, team);
        User currentUser = userService.getCurrentUser(request);
        long teamId = teamService.addTeam(team, currentUser);
        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(long id) {
//        if (team == null) {
//            throw new BusinessException(ResultCode.PARAMS_ERROR);
//        }
        boolean delete = teamService.removeById(id);
        if (!delete) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    @PutMapping("/update")
    public BaseResponse<Boolean> updateTeam(Team team) {
        if (team == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        boolean update = teamService.updateById(team);
        if (!update) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "不存在该队伍");
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/get/list")
    public BaseResponse<List<Team>> getTeamList(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtil.copyProperties(teamQuery, team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> teamList = teamService.list(queryWrapper);
        return ResultUtils.success(teamList);
    }


    @GetMapping("/get/page")
    public BaseResponse<Page<Team>> getTeamListByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtil.copyProperties(teamQuery, team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> page = teamService.page(new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize()), queryWrapper);
        return ResultUtils.success(page);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        User currentUser = userService.getCurrentUser(request);
        boolean res = teamService.joinTeam(teamJoinRequest, currentUser);
        if (!res) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "加入队伍失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        User currentUser = userService.getCurrentUser(request);
        boolean res = teamService.quitTeam(teamQuitRequest, currentUser);
        return ResultUtils.success(true);
    }

}
