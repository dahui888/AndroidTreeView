package com.unnamed.b.atv.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.unnamed.b.atv.R;
import com.unnamed.b.atv.view.TreeNodeWrapperView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bogdan Melnychuk on 2/10/15.
 */
public class TreeNode {
    public static final String NODES_ID_SEPARATOR = ":";

    private int mId;
    private TreeNode mParent;
    private final List<TreeNode> children;
    private BaseNodeViewHolder mViewHolder;
    private TreeNodeClickListener mListener;
    private Object mValue;
    private boolean mExpanded;

    public static TreeNode root() {
        return new TreeNode(null);
    }

    public TreeNode(Object value) {
        children = new ArrayList<TreeNode>();
        mValue = value;
    }

    public TreeNode addChild(TreeNode childNode) {
        childNode.mParent = this;
        childNode.mId = size();
        children.add(childNode);
        return this;
    }

    public TreeNode addChildren(TreeNode... nodes) {
        for (TreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    public TreeNode addChildren(Collection<TreeNode> nodes) {
        for (TreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public int size() {
        return children.size();
    }

    public TreeNode getParent() {
        return mParent;
    }

    public int getId() {
        return mId;
    }

    public boolean isLeaf() {
        return size() == 0;
    }

    public Object getValue() {
        return mValue;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public TreeNode setExpanded(boolean expanded) {
        mExpanded = expanded;
        return this;
    }

    public String getPath() {
        final StringBuilder path = new StringBuilder();
        TreeNode node = this;
        while (node.mParent != null) {
            path.append(mId);
            node = node.mParent;
            if (node.mParent != null) {
                path.append(NODES_ID_SEPARATOR);
            }
        }
        return path.toString();
    }


    public int getLevel() {
        int level = 0;
        TreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
            level++;
        }
        return level;
    }

    public boolean isLastChild() {
        if (!isRoot()) {
            final List<TreeNode> parentChildren = mParent.children;
            return parentChildren.get(size() - 1).mId == mId;
        }
        return false;
    }

    public TreeNode setViewHolder(BaseNodeViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (viewHolder != null) {
            viewHolder.mNode = this;
        }
        return this;
    }

    public TreeNode setClickListener(TreeNodeClickListener listener) {
        mListener = listener;
        return this;
    }


    public TreeNodeClickListener getClickListener() {
        return this.mListener;
    }

    public BaseNodeViewHolder getViewHolder() {
        return mViewHolder;
    }

    public boolean isFirstChild() {
        if (!isRoot()) {
            List<TreeNode> parentChildren = mParent.children;
            return parentChildren.get(0).mId == mId;
        }
        return false;
    }

    public boolean isRoot() {
        return mParent == null;
    }

    public TreeNode getRoot() {
        TreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
        }
        return root;
    }

    public interface TreeNodeClickListener {
        void onClick(TreeNode node, Object value);
    }

    public static abstract class BaseNodeViewHolder {
        private TreeNode mNode;
        private View mView;
        protected int containerStyle;
        protected Context context;

        public BaseNodeViewHolder(Context context) {
            this.context = context;
        }

        public View getView() {
            if (mView != null) {
                return mView;
            }
            final View nodeView = getNodeView();
            final TreeNodeWrapperView nodeWrapperView = new TreeNodeWrapperView(nodeView.getContext(), getContainerStyle());
            nodeWrapperView.insertNodeView(nodeView);
            mView = nodeWrapperView;

            return mView;
        }

        public void setContainerStyle(int style) {
            containerStyle = style;
        }

        public View getNodeView() {
            return createNodeView(mNode);
        }

        public ViewGroup getNodeItemsView() {
            return (ViewGroup) getView().findViewById(R.id.node_items);
        }

        public int getContainerStyle() {
            return containerStyle;
        }


        public abstract View createNodeView(TreeNode node);

        public abstract void toggle(boolean active);
    }
}