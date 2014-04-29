package com.jayanslow.projection.texture.editor.views;

import static com.jayanslow.utils.swing.JButtonHelpers.createButton;
import static com.jayanslow.utils.swing.JLabelHelpers.createLabel;
import static com.jayanslow.utils.swing.JSpinnerHelpers.createSpinner;
import static com.jayanslow.utils.swing.JSpinnerHelpers.getSpinnerValue;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jayanslow.projection.texture.controllers.TextureController;
import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.editor.models.MappingsTableModel;
import com.jayanslow.projection.texture.listeners.TextureListener;
import com.jayanslow.projection.texture.models.Texture;
import com.jayanslow.projection.texture.models.TextureMapping;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TextureMappingsFrame extends JFrame implements ActionListener, TextureListener, ChangeListener {

	private static final long				serialVersionUID	= -4597857118664782627L;

	private static final String				COMMAND_ADD			= "add";
	private static final String				COMMAND_EDIT		= "edit";
	private static final String				COMMAND_REMOVE		= "remove";
	private static final String				COMMAND_REFRESH		= "refresh";
	private static final String				COMMAND_MOVE		= "move";
	private static final String				COMMAND_START		= "start";
	private static final String				COMMAND_END			= "end";

	private final JPanel					contentPane;

	private final TextureController			textures;
	private final TextureEditorController	controller;

	private final JTable					tableMappings;

	private JSpinner						spinnerFrame;

	private JPanel							panelProperties;
	private JTextField						textMaximum;

	private boolean							live				= true;
	private JPanel							panelJump;

	/**
	 * Create the frame.
	 */
	public TextureMappingsFrame(TextureEditorController controller, TextureController textures) {
		this.controller = controller;
		this.textures = textures;

		textures.addTextureListener(this);

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Panels
		JPanel panelControls = new JPanel();
		createButton("Add", panelControls, null, this, COMMAND_ADD);
		createButton("Edit", panelControls, null, this, COMMAND_EDIT);
		createButton("Move", panelControls, null, this, COMMAND_MOVE);
		createButton("Remove", panelControls, null, this, COMMAND_REMOVE);
		createButton("Refresh", panelControls, null, this, COMMAND_REFRESH);
		contentPane.add(panelControls, BorderLayout.SOUTH);

		panelProperties = new JPanel();
		contentPane.add(panelProperties, BorderLayout.NORTH);
		panelProperties.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, }));

		// Properties
		int maximumFrame = textures.getMaximumFrame();

		createLabel("Current Frame", panelProperties, "2, 2, center, default");
		spinnerFrame = createSpinner(new SpinnerNumberModel(textures.getCurrentFrame(), 0, null, 1), panelProperties,
				"4, 2, fill, default", this);

		createLabel("Maximum Frame", panelProperties, "2, 4, center, default");

		textMaximum = new JTextField();
		textMaximum.setEnabled(false);
		textMaximum.setText(Integer.toString(maximumFrame));
		panelProperties.add(textMaximum, "4, 4, fill, default");
		textMaximum.setColumns(10);

		// Jump Buttons

		panelJump = new JPanel();
		createButton("Start", panelJump, null, this, COMMAND_START);
		createButton("End", panelJump, null, this, COMMAND_END);
		panelProperties.add(panelJump, "6, 2, fill, fill");

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
		if (command == COMMAND_ADD)
			controller.create(this);
		else if (command.equals(COMMAND_EDIT) || command.equals(COMMAND_MOVE) || command.equals(COMMAND_REMOVE)) {
			int index = tableMappings.getSelectedRow();
			TextureMapping m = ((MappingsTableModel) tableMappings.getModel()).getMapping(index);
			if (command.equals(COMMAND_EDIT))
				controller.edit(m.getTexture());
			else if (command.equals(COMMAND_MOVE))
				controller.move(this, m);
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
		else if (command.equals(COMMAND_START))
			textures.setCurrentFrame(0);
		else if (command.equals(COMMAND_END))
			textures.setCurrentFrame(textures.getMaximumFrame() - 1);
	}

	private void bind() {
		live = false;

		int maximumFrame = textures.getMaximumFrame();
		SpinnerNumberModel model = (SpinnerNumberModel) spinnerFrame.getModel();
		model.setValue(textures.getCurrentFrame() % maximumFrame);
		textMaximum.setText(Integer.toString(maximumFrame));

		MappingsTableModel.useModel(tableMappings, new ArrayList<>(textures.getTextureMappings()));

		live = true;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (live)
			textures.setCurrentFrame(getSpinnerValue(spinnerFrame).intValue());
	}

	@Override
	public void textureChange(Texture texture) {
		bind();
	}

	@Override
	public void textureFrameChange(int current, int old) {
		live = false;
		spinnerFrame.setValue(current);
		live = true;
	}
}
