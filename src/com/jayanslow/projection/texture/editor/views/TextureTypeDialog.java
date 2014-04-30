package com.jayanslow.projection.texture.editor.views;

import static com.jayanslow.utils.swing.JButtonHelpers.createButton;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import com.jayanslow.projection.texture.editor.models.TextureTypeListModel;
import com.jayanslow.projection.texture.models.TextureType;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TextureTypeDialog extends JDialog implements ActionListener {

	private static final long	serialVersionUID		= 8220532728965972407L;

	private static final String	COMMAND_FILTER_IMAGE	= "filter_image";
	private static final String	COMMAND_FILTER_VIDEO	= "filter_video";
	private static final String	COMMAND_FILTER_BOTH		= "filter_both";
	private static final String	COMMAND_OK				= "ok";
	private static final String	COMMAND_CANCEL			= "cancel";

	public static TextureType selectTextureType(Frame owner, boolean imageOnly) {
		TextureTypeDialog dialog = new TextureTypeDialog(owner, imageOnly);
		dialog.setVisible(true);
		if (dialog.isCancelled())
			return null;
		else
			return dialog.getSelectedTextureType();
	}

	private final JPanel		contentPanel	= new JPanel();
	private final JList<String>	listTypes;

	private boolean				cancelled		= true;

	public TextureTypeDialog(Frame owner, boolean imageOnly) {
		super(owner, true);

		setBounds(100, 100, 450, 300);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow") }));

		JPanel panelTypeFilters = new JPanel();
		contentPanel.add(panelTypeFilters, "2, 2, 3, 1, left, fill");
		ButtonGroup group = new ButtonGroup();

		JRadioButton radioImage = createRadioButton(panelTypeFilters, group, "Image", COMMAND_FILTER_IMAGE);
		JRadioButton radioVideo = createRadioButton(panelTypeFilters, group, "Video", COMMAND_FILTER_VIDEO);
		JRadioButton radioBoth = createRadioButton(panelTypeFilters, group, "Both", COMMAND_FILTER_BOTH);

		listTypes = new JList<>(new TextureTypeListModel());
		listTypes.addMouseListener(new MouseAdapter() {
			private static final long	DOUBLE_CLICK_TIME	= 500;

			private long				lastTime;
			private int					lastRow				= -1;

			@Override
			public void mouseClicked(MouseEvent e) {
				int currentRow = listTypes.getSelectedIndex();
				if (currentRow < 0)
					return;

				long currentTime = System.currentTimeMillis();

				if (lastRow == currentRow && (currentTime - lastTime) < DOUBLE_CLICK_TIME) {
					cancelled = false;
					setVisible(false);
				}
				lastRow = currentRow;
				lastTime = currentTime;
			}
		});
		contentPanel.add(listTypes, "2, 4, 3, 1, fill, fill");

		JPanel panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(panelButton, BorderLayout.SOUTH);

		JButton okButton = createButton("OK", panelButton, null, this, COMMAND_OK);
		getRootPane().setDefaultButton(okButton);
		createButton("Cancel", panelButton, null, this, COMMAND_CANCEL);

		if (imageOnly) {
			radioImage.setSelected(true);
			radioVideo.setEnabled(false);
			radioBoth.setEnabled(false);
			getModel().filter(true);
		} else
			radioBoth.setSelected(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(COMMAND_FILTER_IMAGE))
			getModel().filter(true);
		else if (command.equals(COMMAND_FILTER_VIDEO))
			getModel().filter(false);
		else if (command.equals(COMMAND_FILTER_BOTH))
			getModel().clearFilter();
		else if (command.equals(COMMAND_OK)) {
			if (listTypes.getSelectedIndex() >= 0) {
				cancelled = false;
				setVisible(false);
			}
		} else if (command.equals(COMMAND_CANCEL))
			setVisible(false);
	}

	private JRadioButton createRadioButton(JPanel panel, ButtonGroup group, String label, String command) {
		JRadioButton rdbtn = new JRadioButton(label);
		rdbtn.setActionCommand(command);
		rdbtn.addActionListener(this);
		panel.add(rdbtn);
		group.add(rdbtn);
		return rdbtn;
	}

	private TextureTypeListModel getModel() {
		return (TextureTypeListModel) listTypes.getModel();
	}

	public TextureType getSelectedTextureType() {
		if (cancelled)
			return null;
		TextureTypeListModel model = (TextureTypeListModel) listTypes.getModel();
		return model.getTypeAt(listTypes.getSelectedIndex());
	}

	public boolean isCancelled() {
		return cancelled;
	}
}
