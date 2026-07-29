package com.byteasy.util;

import com.byteasy.dto.TreeNodeDTO;

import java.util.*;

public class TreeUtil {

    public static Set<TreeNodeDTO> getAllLeafNodes(List<TreeNodeDTO> roots) {
        // 保留你原本的基础校验，推荐使用 emptySet() 替代废弃的 EMPTY_SET
        if (roots == null || roots.isEmpty()) {
            return Collections.emptySet();
        }

        Set<TreeNodeDTO> leafNodes = new HashSet<>();
        collectLeafNodes(roots, leafNodes);

        return leafNodes;
    }

    /**
     * 递归收集叶子节点
     *
     * @param nodes     当前层级的节点集合
     * @param leafNodes 用于收集叶子节点的结果集
     */
    private static void collectLeafNodes(Collection<TreeNodeDTO> nodes, Set<TreeNodeDTO> leafNodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }

        for (TreeNodeDTO node : nodes) {
            if (node == null) continue;

            // 判断是否为叶子节点：子节点集合为 null 或 没有任何子元素
            if (node.getChildren() == null || node.getChildren().isEmpty()) {
                leafNodes.add(node);
            } else {
                // 如果有子节点，则继续向下递归遍历
                collectLeafNodes(node.getChildren(), leafNodes);
            }
        }
    }
}
