package com.jayanslow.projection.texture.editor.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.jayanslow.projection.world.models.Face;
import com.jayanslow.projection.world.models.Screen;

public class FaceTreeRootNode implements TreeNode {
	public static class FaceNode implements TreeNode {

		private final ScreenNode	parent;
		private final Face			face;

		public FaceNode(ScreenNode parent, Face face) {
			this.face = face;
			this.parent = parent;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Enumeration children() {
			return Collections.emptyEnumeration();
		}

		@Override
		public boolean getAllowsChildren() {
			return false;
		}

		@Override
		public TreeNode getChildAt(int childIndex) {
			return null;
		}

		@Override
		public int getChildCount() {
			return 0;
		}

		public Face getFace() {
			return face;
		}

		@Override
		public int getIndex(TreeNode node) {
			return -1;
		}

		@Override
		public TreeNode getParent() {
			return parent;
		}

		@Override
		public boolean isLeaf() {
			return true;
		}

		@Override
		public String toString() {
			if (face.getName() == null)
				return String.format("#%d", face.getFaceId());
			else
				return String.format("#%d - %s", face.getFaceId(), face.getName());
		}
	}

	public static class ScreenNode implements TreeNode {

		private final Screen				screen;
		private final ArrayList<FaceNode>	faceNodes;
		private final FaceTreeRootNode		parent;

		public ScreenNode(FaceTreeRootNode parent, Screen screen) {
			this.screen = screen;
			this.parent = parent;

			faceNodes = new ArrayList<FaceNode>(screen.getFaces().size());
			for (Face f : screen.getFaces())
				faceNodes.add(new FaceNode(this, f));
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Enumeration children() {
			return Collections.enumeration(faceNodes);
		}

		@Override
		public boolean getAllowsChildren() {
			return true;
		}

		@Override
		public TreeNode getChildAt(int childIndex) {
			return faceNodes.get(childIndex);
		}

		@Override
		public int getChildCount() {
			return faceNodes.size();
		}

		@Override
		public int getIndex(TreeNode node) {
			return faceNodes.indexOf(node);
		}

		@Override
		public TreeNode getParent() {
			return parent;
		}

		public Screen getScreen() {
			return screen;
		}

		@Override
		public boolean isLeaf() {
			return false;
		}

		@Override
		public String toString() {
			return String.format("#%d - %s (%d)", screen.getScreenId(), screen.getScreenType(), screen.getFaces()
					.size());
		}
	}

	private final ArrayList<ScreenNode>	screenNodes;

	public FaceTreeRootNode(Collection<Screen> screens) {
		screenNodes = new ArrayList<ScreenNode>(screens.size());
		for (Screen s : screens)
			screenNodes.add(new ScreenNode(this, s));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration children() {
		return Collections.enumeration(screenNodes);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return screenNodes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return screenNodes.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return screenNodes.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
}
