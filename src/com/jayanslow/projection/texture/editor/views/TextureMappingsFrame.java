package com.jayanslow.projection.texture.editor.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import com.jayanslow.projection.texture.controllers.TextureController;
import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.editor.models.MappingsTableModel;
import com.jayanslow.projection.texture.listeners.TextureListener;
import com.jayanslow.projection.texture.models.Texture;
import com.jayanslow.projection.texture.models.TextureMapping;

public class TextureMappingsFrame extends JFrame implements ActionListener, TextureListener {

	private static final long				serialVersionUID	= -4597857118664782627L;

	private static final String				COMMAND_ADD			= "add";
	private static final String				COMMAND_EDIT		= "edit";
	private static final String				COMMAND_REMOVE		= "remove";
	private static final String				COMMAND_REFRESH		= "refresh";
	private static final String				COMMAND_MOVE		= "move";

	private final JPanel					contentPane;

	private final JButton					btnAdd, btnEdit, btnMove, btnRefresh, btnRemove;

	private final TextureController			textures;
	private final TextureEditorController	controller;

	private final JTable					tableMappings;

	/**
	 * Create the frame.
	 */
	public TextureMappingsFrame(TextureEditorController controller, TextureController textures) {
		this.controller = controller;
		this.textures = textures;

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelControls = new JPanel();
		contentPane.add(panelControls, BorderLayout.SOUTH);

		// Control buttons
		btnAdd = createButton("Add", COMMAND_ADD);
		panelControls.add(btnAdd);

		btnEdit = createButton("Edit", COMMAND_EDIT);
		panelControls.add(btnEdit);

		btnMove = createButton("Move", COMMAND_MOVE);
		panelControls.add(btnMove);

		btnRemove = createButton("Remove", COMMAND_REMOVE);
		panelControls.add(btnRemove);

		btnRefresh = createButton("Refresh", COMMAND_REFRESH);
		panelControls.add(btnRefresh);

		tableMappings = new JTable();
		tableMappings.addMouseListener(new MouseAdapter() {
			private static final long	DOUBLE_CLICK_TIME	= 500;

			private long				lastTime;
			private int					lastRow				= -1;

			@Override
			public void mouseClicked(MouseEvent e) {
				int currentRow = tableMappings.rowAtPoint(e.getPoint());
				if (currentRow < 0)
					return;

				long currentTime = System.currentTimeMillis();

				if (lastRow == currentRow && (currentTime - lastTime) < DOUBLE_CLICK_TIME) {
					TextureMapping m = ((MappingsTableModel) tableMappings.getModel()).getMapping(currentRow);
					TextureMappingsFrame.this.controller.edit(m.getTexture());
				}
				lastRow = currentRow;
				lastTime = currentTime;
			}
		});

		contentPane.add(new JScrollPane(tableMappings), BorderLayout.CENTER);

		bind();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == COMMAND_ADD) {
			// Object[] options = { "Standard Projector", "Flat Screen", "Cuboid Screen" };
			// int result = JOptionPane.showOptionDialog(UniverseFrame.this, "What type of object should be added?",
			// "Add Object", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
			// "Standard Projector");
			// switch (result) {
			// case 0:
			// controller.create(StandardProjector.class);
			// break;
			// case 1:
			// controller.create(FlatScreen.class);
			// break;
			// case 2:
			// controller.create(CuboidScreen.class);
			// break;
			// default:
			// return;
			// }
		} else if (command.equals(COMMAND_EDIT) || command.equals(COMMAND_MOVE) || command.equals(COMMAND_REMOVE)) {
			int index = tableMappings.getSelectedRow();
			TextureMapping m = ((MappingsTableModel) tableMappings.getModel()).getMapping(index);
			if (command.equals(COMMAND_EDIT))
				controller.edit(m.getTexture());
			else if (command.equals(COMMAND_MOVE))
				controller.move(m);
			else {
				boolean confirmed = JOptionPane.showConfirmDialog(
						this,
						String.format("Do you want to remove mapping for Screen %d, Face %d?", m.getScreenId(),
								m.getFaceId()), "Confirm delete", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
				if (confirmed)
					controller.remove(m.getFace());
			}

		} else if (command.equals(COMMAND_REFRESH))
			bind();
	}

	private void bind() {
		MappingsTableModel.useModel(tableMappings, new ArrayList<>(textures.getTextureMappings()));
	}

	private JButton createButton(String text, String command) {
		JButton button = new JButton(text);
		button.setActionCommand(command);
		button.addActionListener(this);
		return button;
	}

	@Override
	public void textureChange(Texture texture) {
		bind();
	}

	@Override
	public void textureFrameChange(int current, int old) {
		bind();
	}
}
