package com.jayanslow.projection.texture.editor.views;

import static com.jayanslow.utils.swing.JButtonHelpers.createButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.jayanslow.projection.texture.controllers.TextureController;
import com.jayanslow.projection.texture.editor.models.FaceTreeRootNode;
import com.jayanslow.projection.texture.models.ColorImageTexture;
import com.jayanslow.projection.texture.models.PreviewTexture;
import com.jayanslow.projection.world.controllers.WorldController;
import com.jayanslow.projection.world.listeners.WorldListener;
import com.jayanslow.projection.world.models.Face;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class FaceDialog extends JDialog implements ActionListener, WorldListener, TreeSelectionListener {

	private static final long	serialVersionUID	= -8218269193528430119L;

	private static final String	COMMAND_OK			= "ok";

	private static final String	COMMAND_CANCEL		= "cancel";
	private static final String	COMMAND_PREVIEW		= "preview";

	public static Face selectFace(Frame owner, WorldController world, TextureController textures) {
		FaceDialog dialog = new FaceDialog(owner, world, textures);
		world.addWorldListener(dialog);
		dialog.setVisible(true);
		world.removeWorldListener(dialog);
		if (dialog.isCancelled())
			return null;
		else
			return dialog.getSelectedFace();
	}

	private boolean						isCancelled;

	private final JTree					tree;

	private final WorldController		world;

	private final TextureController		textures;

	private final List<PreviewTexture>	previewTextures	= new LinkedList<PreviewTexture>();

	private boolean						preview;

	private final JToggleButton			tglbtnPreview;

	public FaceDialog(Frame owner, WorldController world, TextureController textures) {
		super(owner, true);

		this.world = world;
		this.textures = textures;

		preview = true;

		setBounds(100, 100, 450, 300);

		getContentPane().setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC }));

		tree = new JTree();
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(this);
		panel.add(new JScrollPane(tree), "2, 2, fill, fill");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		tglbtnPreview = new JToggleButton("Preview");
		tglbtnPreview.setSelected(true);
		tglbtnPreview.setActionCommand(COMMAND_PREVIEW);
		tglbtnPreview.addActionListener(this);
		buttonPane.add(tglbtnPreview);

		JButton btnOk = createButton("OK", buttonPane, null, this, COMMAND_OK);
		getRootPane().setDefaultButton(btnOk);
		createButton("Cancel", buttonPane, null, this, COMMAND_CANCEL);

		bind();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(COMMAND_OK)) {
			if (tree.getSelectionCount() > 0) {
				isCancelled = false;
				setVisible(false);
			}
		} else if (command.equals(COMMAND_CANCEL))
			setVisible(false);
		else if (command.equals(COMMAND_PREVIEW)) {
			preview = tglbtnPreview.isSelected();
			preview(preview);
		}
	}

	private void bind() {
		TreeModel model = new DefaultTreeModel(new FaceTreeRootNode(world.getScreens()));
		tree.setModel(model);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	public Face getSelectedFace() {
		TreeNode node = getSelectedNode();
		if (node.isLeaf())
			return ((FaceTreeRootNode.FaceNode) node).getFace();
		else
			return null;
	}

	private TreeNode getSelectedNode() {
		if (tree.getSelectionCount() != 1)
			return null;
		return (TreeNode) tree.getSelectionPath().getLastPathComponent();
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	private void preview(boolean preview) {
		for (PreviewTexture pt : previewTextures)
			pt.restore();
		previewTextures.clear();

		if (!preview)
			return;

		TreeNode node = getSelectedNode();
		if (node == null)
			return;
		else if (node.isLeaf()) {
			Face f = ((FaceTreeRootNode.FaceNode) node).getFace();
			previewTextures.add(PreviewTexture.preview(textures, f, new ColorImageTexture(Color.RED)));
		} else if (node instanceof FaceTreeRootNode.ScreenNode) {

		}
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		preview(false);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		preview(preview);
	}

	@Override
	public void worldChanged() {
		bind();
	}
}
