package com.jayanslow.projection.texture.editor.views;

import com.jayanslow.projection.texture.editor.controller.TextureEditorController;
import com.jayanslow.projection.texture.models.BufferedImageTexture;

public class BufferedImageTextureFrame extends AbstractImageTextureFrame<BufferedImageTexture> {

	private static final long	serialVersionUID	= 5257879231919174180L;

	public BufferedImageTextureFrame(TextureEditorController controller, BufferedImageTexture texture) {
		super(controller, texture);
		bind();
	}

	@Override
	protected void bindTexture(BufferedImageTexture object) {}

}
