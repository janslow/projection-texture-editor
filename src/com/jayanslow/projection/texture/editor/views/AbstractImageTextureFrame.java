package com.jayanslow.projection.texture.editor.views;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.listeners.TextureListener;
import com.jayanslow.projection.texture.models.ImageTexture;
import com.jayanslow.projection.texture.models.Texture;

public abstract class AbstractImageTextureFrame<T extends ImageTexture> extends JFrame implements TextureListener {

	private static final long				serialVersionUID	= 7681579868364029166L;

	protected final JPanel					contentPane;

	protected final T						texture;
	protected final TextureEditorController	controller;

	private final ImageIcon					imgPreview;

	private final JLabel					lblDimensions;

	protected boolean						live;

	/**
	 * Create the frame.
	 */
	public AbstractImageTextureFrame(TextureEditorController controller, T texture) {
		this.controller = controller;
		this.texture = texture;

		texture.addTextureListener(this);

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		imgPreview = new ImageIcon();
		lblDimensions = new JLabel("No Image", imgPreview, JLabel.CENTER);
		contentPane.add(lblDimensions, BorderLayout.CENTER);
	}

	public void bind() {
		live = false;
		BufferedImage image = texture.getBufferedImage();
		lblDimensions.setText(String.format("%d x %d", image.getWidth(), image.getHeight()));
		imgPreview.setImage(image.getScaledInstance(100, 100, BufferedImage.SCALE_FAST));
		lblDimensions.repaint();

		bindTexture(texture);
		live = true;
	}

	protected abstract void bindTexture(T texture);

	@Override
	public void textureChange(Texture texture) {
		bind();
	}

	@Override
	public void textureFrameChange(int current, int old) {}

}
