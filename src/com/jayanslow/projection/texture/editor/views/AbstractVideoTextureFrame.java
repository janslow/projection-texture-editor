package com.jayanslow.projection.texture.editor.views;

import static com.jayanslow.utils.swing.JSliderHelpers.createSlider;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.listeners.TextureListener;
import com.jayanslow.projection.texture.models.Texture;
import com.jayanslow.projection.texture.models.VideoTexture;

public abstract class AbstractVideoTextureFrame<T extends VideoTexture> extends JFrame implements TextureListener,
		ChangeListener {

	private static final long				serialVersionUID	= 7681579868364029166L;

	protected final JPanel					contentPane;

	protected final T						texture;
	protected final TextureEditorController	controller;

	private final ImageIcon					imgPreview;

	private final JLabel					lblDimensions;

	protected boolean						live;
	private final JSlider					sliderFrame;

	/**
	 * Create the frame.
	 */
	public AbstractVideoTextureFrame(TextureEditorController controller, T texture) {
		this.controller = controller;
		this.texture = texture;

		texture.addTextureListener(this);

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelPreview = new JPanel();
		contentPane.add(panelPreview, BorderLayout.CENTER);
		panelPreview.setLayout(new BorderLayout(0, 0));

		imgPreview = new ImageIcon();
		lblDimensions = new JLabel("No Image", imgPreview, JLabel.CENTER);
		panelPreview.add(lblDimensions);

		sliderFrame = createSlider(panelPreview, 0, 0, texture.getNumberOfFrames() - 1, BorderLayout.SOUTH, this);
		sliderFrame.setPaintTicks(true);
		sliderFrame.setMinorTickSpacing(1);
	}

	public void bind() {
		live = false;

		sliderFrame.setMaximum(texture.getNumberOfFrames() - 1);
		int current = sliderFrame.getValue();

		BufferedImage image = texture.getImageTexture(current).getBufferedImage();
		imgPreview.setImage(image.getScaledInstance(100, 100, BufferedImage.SCALE_FAST));

		lblDimensions.setText(String.format("Frame %d of %d - %d x %d", current + 1, texture.getNumberOfFrames(),
				image.getWidth(), image.getHeight()));
		lblDimensions.repaint();

		bindTexture(texture);
		live = true;
	}

	protected abstract void bindTexture(T texture);

	protected int getSelectedIndex() {
		return sliderFrame.getValue();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		live = false;

		if (e.getSource() == sliderFrame)
			bind();

		live = true;
	}

	@Override
	public void textureChange(Texture texture) {
		bind();
	}

	@Override
	public void textureFrameChange(int current, int old) {}
}
