package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.dao.entity.SkillTree;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Tree.SkillTreeCreateReq;
import com.ziroom.tech.demeterapi.po.dto.req.Tree.SkillTreeModReq;
import com.ziroom.tech.demeterapi.po.dto.resp.Tree.SkillTreeResp;
import com.ziroom.tech.demeterapi.service.TreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Api("技能树")
@Slf4j
@RequestMapping("/api/tree")
public class TreeController {
    @Resource
    private TreeService treeService;

    @ApiOperation("创建技能树节点")
    @PostMapping("/")
    public Resp<Integer> createTree(@RequestBody SkillTreeCreateReq skillTreeCreateReq) {
        return Resp.success(treeService.insertSelective(skillTreeCreateReq.getEntity(skillTreeCreateReq)));
    }

    @ApiOperation("按 id 查询技能树节点")
    @GetMapping("/{id}")
    public Resp<SkillTreeResp> getTree(@PathVariable Integer id) {
        SkillTreeResp skillTreeResp = new SkillTreeResp();
        SkillTree skillTree = treeService.selectByPrimaryKey(id);
        if (Objects.nonNull(skillTree)) {
            BeanUtils.copyProperties(skillTree, skillTreeResp);
            return Resp.success(skillTreeResp);
        }
        return Resp.success();
    }

    @ApiOperation("按 id 更新技能树节点")
    @PatchMapping("/{id}")
    public Resp<Integer> updateTree(@PathVariable Integer id, @RequestBody SkillTreeModReq skillTreeModReq) {
        skillTreeModReq.setId(id);
        return Resp.success(treeService.updateByPrimaryKeySelective(skillTreeModReq.getEntity(skillTreeModReq)));
    }

    @ApiModelProperty("按父节点 id 查询技能树节点列表")
    @GetMapping("/parentId/{id}")
    public Resp<List<SkillTreeResp>> getTreesByParentId(@PathVariable Integer id) {
        List<SkillTreeResp> skillTreeResps = new ArrayList<>();
        Optional.ofNullable(treeService.selectByParentId(id)).ifPresent(trees -> {
            trees.forEach(tree -> {
                SkillTreeResp skillTreeResp = new SkillTreeResp();
                BeanUtils.copyProperties(tree, skillTreeResp);
                skillTreeResps.add(skillTreeResp);
            });
        });
        return Resp.success(skillTreeResps);
    }

    @ApiOperation("移除指定 id 的 tree")
    @DeleteMapping("/{id}")
    public Resp<Integer> deleteTree(@PathVariable Integer id) {
        return Resp.success(treeService.deleteByPrimaryKey(id));
    }
}
