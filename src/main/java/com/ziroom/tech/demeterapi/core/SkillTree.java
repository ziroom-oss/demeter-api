package com.ziroom.tech.demeterapi.core;

import lombok.Data;

import java.util.Objects;

/**
 * @author lipp3
 * @date 2021/7/1 15:25
 * @Description
 */
@Data
public class SkillTree {

    private SkillNode root;

    public void addBranch(SkillNode branch){
        if (Objects.isNull(branch)){
            return;
        }
        if (Objects.isNull(root)){
            root = branch;
            return;
        }
        SkillNode currentNode = root;
        SkillNode childNode = branch.obtainFirstChild();
        while (Objects.nonNull(childNode)){
            currentNode = currentNode.getOrAddChild(childNode);
            childNode = childNode.obtainFirstChild();
        }
    }
}
