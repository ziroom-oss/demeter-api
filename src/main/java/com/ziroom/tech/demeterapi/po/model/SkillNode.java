package com.ziroom.tech.demeterapi.po.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lipp3
 * @date 2021/7/1 15:26
 * @Description
 */
@Data
public class SkillNode {

    private Long parentId;

    private Long id;

    private String name;

    private List<SkillNode> children = new ArrayList<>();

    public SkillNode(){}

    public SkillNode(Long parentId, Long id, String name){
        this.parentId = parentId;
        this.id = id;
        this.name = name;
    }

    public SkillNode obtainFirstChild(){
        if (children.size() > 0){
            return children.get(0);
        }else{
            return null;
        }
    }

    public SkillNode getChild(Long id){
        for (SkillNode node : children){
            if (node.id == id){
                return node;
            }
        }
        return null;
    }

    public SkillNode getOrAddChild(SkillNode node){
        SkillNode child = getChild(node.getId());
        if (Objects.isNull(child)){
            children.add(node);
            return node;
        }else{
            return child;
        }
    }

    public void addChild(SkillNode node){
        if (Objects.nonNull(node)){
            children.add(node);
        }
    }
}
