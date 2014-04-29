package com.jayanslow.projection.texture.editor.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.models.FileImageTexture;
import com.jayanslow.utils.swing.JButtonHelpers;
import com.jayanslow.utils.swing.JFileChooserHelpers;
import com.jayanslow.utils.swing.JLabelHelpers;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class FileImageTextureFrame extends AbstractImageTextureFrame<FileImageTexture> implements ActionListener {

	private static final long	serialVersionUID	= 5257879231919174180L;

	private static final String	COMMAND_LOAD		= "load";
	private static final String	COMMAND_RELOAD		= "reload";

	private static final String	NULL_PATH			= "";

	private final JLabel		lblPath;

	public FileImageTextureFrame(TextureEditorController controller, FileImageTexture texture) {
		super(controller, texture);

		JPanel panelProperties = new JPanel();
		contentPane.add(panelProperties, BorderLayout.NORTH);
		panelProperties.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC }));

		lblPath = JLabelHelpers.createLabel(NULL_PATH, panelProperties, "2, 2");

		JPanel panelControls = new JPanel();
		JButtonHelpers.createButton("Load", panelControls, null, this, COMMAND_LOAD);
		JButtonHelpers.createButton("Reload", panelControls, null, this, COMMAND_RELOAD);
		panelProperties.add(panelControls, "2, 4");

		bind();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(COMMAND_LOAD)) {
			File file = JFileChooserHelpers.chooseImageFile(this, texture.getFile());
			if (file != null)
				texture.setFile(file);
		} else if (command.equals(COMMAND_RELOAD))
			texture.reload();
	}

	@Override
	protected void bindTexture(FileImageTexture texture) {
		File f = texture.getFile();
		if (f == null)
			lblPath.setText(NULL_PATH);
		else
			lblPath.setText(f.getPath());
	}
}
