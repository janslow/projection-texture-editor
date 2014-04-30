package com.jayanslow.projection.texture.editor.views;

import static com.jayanslow.utils.swing.JButtonHelpers.createButton;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.models.ImageTexture;
import com.jayanslow.projection.texture.models.ListVideoTexture;
import com.jayanslow.projection.texture.models.TextureType;

public class ListVideoTextureFrame extends AbstractVideoTextureFrame<ListVideoTexture> implements ActionListener {

	private static final long	serialVersionUID	= 5119018733483355424L;

	private static final String	COMMAND_ADD			= "add";
	private static final String	COMMAND_EDIT		= "edit";
	private static final String	COMMAND_DELETE		= "delete";

	public ListVideoTextureFrame(TextureEditorController controller, ListVideoTexture texture) {
		super(controller, texture);

		JPanel panelControls = new JPanel();
		contentPane.add(panelControls, BorderLayout.SOUTH);

		createButton("Add", panelControls, null, this, COMMAND_ADD);
		createButton("Edit", panelControls, null, this, COMMAND_EDIT);
		createButton("Delete", panelControls, null, this, COMMAND_DELETE);

		bind();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(COMMAND_ADD)) {
			TextureType type = TextureTypeDialog.selectTextureType(this, true);
			if (type == null)
				return;
			ImageTexture t = (ImageTexture) controller.create(type);
			if (texture == null)
				return;
			texture.add(t, getSelectedIndex());
		} else if (command.equals(COMMAND_EDIT))
			controller.edit(texture.getImageTexture(getSelectedIndex()));
		else if (command.equals(COMMAND_DELETE))
			texture.remove(getSelectedIndex());

	}

	@Override
	protected void bindTexture(ListVideoTexture texture) {}

}
