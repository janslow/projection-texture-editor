package com.jayanslow.projection.texture.editor.views;

import static com.jayanslow.utils.swing.JButtonHelpers.createButton;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.models.DirectoryVideoTexture;
import com.jayanslow.utils.swing.JFileChooserHelpers;
import com.jayanslow.utils.swing.JLabelHelpers;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class DirectoryVideoTextureFrame extends AbstractVideoTextureFrame<DirectoryVideoTexture> implements
		ActionListener {

	private static final long	serialVersionUID	= 1069519123651868484L;
	private static final String	COMMAND_LOAD		= "load";
	private static final String	COMMAND_RELOAD		= "reload";

	private static final String	NULL_PATH			= "";

	private final JLabel		lblPath;

	public DirectoryVideoTextureFrame(TextureEditorController controller, DirectoryVideoTexture texture) {
		super(controller, texture);

		JPanel panelProperties = new JPanel();
		contentPane.add(panelProperties, BorderLayout.NORTH);
		panelProperties.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC }));

		lblPath = JLabelHelpers.createLabel(NULL_PATH, panelProperties, "2, 2");

		JPanel panelControls = new JPanel();
		createButton("Load", panelControls, null, this, COMMAND_LOAD);
		createButton("Reload", panelControls, null, this, COMMAND_RELOAD);
		panelProperties.add(panelControls, "2, 4");

		bind();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(COMMAND_LOAD)) {
			File directory = JFileChooserHelpers.chooseDirectory(this, texture.getDirectory());
			if (directory != null)
				texture.setDirectory(directory);
		} else if (command.equals(COMMAND_RELOAD))
			texture.reload();
	}

	@Override
	protected void bindTexture(DirectoryVideoTexture texture) {
		File d = texture.getDirectory();
		if (d == null)
			lblPath.setText(NULL_PATH);
		else
			lblPath.setText(d.getPath());
	}

}
