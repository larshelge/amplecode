package no.tfs.nf.api;

import java.util.Set;

public interface TreeNode
{
    int getId();

    String getName();

    TreeNode getParent();

    Set<? extends TreeNode> getChildren();

    boolean hasChildren();
    
    boolean isMarker();
}
