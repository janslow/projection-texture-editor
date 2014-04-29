package com.jayanslow.projection.texture.editor.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.models.ColorImageTexture;
import com.jayanslow.utils.swing.JButtonHelpers;
import com.jayanslow.utils.swing.JLabelHelpers;
import com.jayanslow.utils.swing.JSliderHelpers;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ColorImageTextureFrame extends AbstractImageTextureFrame<ColorImageTexture> implements ActionListener,
		ChangeListener {

	private static final long	serialVersionUID		= 5257879231919174180L;

	private static final String	COMMAND_COLOR_CHOOSER	= "color_chooser";

	private final JSlider		sliderRed, sliderGreen, sliderBlue;

	public ColorImageTextureFrame(TextureEditorController controller, ColorImageTexture texture) {
		super(controller, texture);

		JPanel panelProperties = new JPanel();
		contentPane.add(panelProperties, BorderLayout.NORTH);
		panelProperties.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC }));

		Color c = texture.getColor();
		JLabelHelpers.createLabel("Red", panelProperties, "2, 2, center, default");
		sliderRed = createColorSlider(panelProperties, c.getRed(), "4, 2");
		JLabelHelpers.createLabel("Green", panelProperties, "2, 4, center, default");
		sliderGreen = createColorSlider(panelProperties, c.getGreen(), "4, 4");
		JLabelHelpers.createLabel("Blue", panelProperties, "2, 6, center, default");
		sliderBlue = createColorSlider(panelProperties, c.getBlue(), "4, 6");

		JButtonHelpers.createButton("Choose Color", panelProperties, "2, 8", this, COMMAND_COLOR_CHOOSER);

		bind();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(COMMAND_COLOR_CHOOSER)) {
			Color c = JColorChooser.showDialog(this, "Choose Texture Color", texture.getColor());
			if (c != null)
				texture.setColor(c);
		}
	}

	@Override
	protected void bindTexture(ColorImageTexture object) {
		Color c = texture.getColor();
		sliderRed.setValue(c.getRed());
		sliderGreen.setValue(c.getGreen());
		sliderBlue.setValue(c.getBlue());
	}

	protected JSlider createColorSlider(JPanel panel, int value, Object constraints) {
		return JSliderHelpers.createSlider(panel, value, 0, 255, constraints, this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (!live)
			return;
		Color c = new Color(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
		texture.setColor(c);
	}
}
