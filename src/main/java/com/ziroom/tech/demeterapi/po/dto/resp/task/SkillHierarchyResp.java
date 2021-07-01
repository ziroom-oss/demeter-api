package com.ziroom.tech.demeterapi.po.dto.resp.task;

/**
 * @author lipp3
 * @date 2021/7/1 10:46
 * @Description
 */
public class SkillHierarchyResp {

    private Long parentId;

    private Long id;

    private String name;

    private SkillHierarchyResp child;

    private SkillHierarchyResp parent;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SkillHierarchyResp getChild() {
        return child;
    }

    public void setChild(SkillHierarchyResp child) {
        this.child = child;
    }

    public SkillHierarchyResp getParent() {
        return parent;
    }

    public void setParent(SkillHierarchyResp parent) {
        this.parent = parent;
    }
}
