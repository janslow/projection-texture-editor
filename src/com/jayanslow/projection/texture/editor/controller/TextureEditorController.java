package com.jayanslow.projection.texture.editor.controller;

import com.jayanslow.projection.texture.controllers.TextureController;
import com.jayanslow.projection.texture.listeners.TextureListener;
import com.jayanslow.projection.texture.models.Texture;
import com.jayanslow.projection.texture.models.TextureMapping;
import com.jayanslow.projection.world.models.Face;

public interface TextureEditorController {
	public void addTextureListener(TextureListener l) throws NullPointerException;

	public <T extends Texture> void create(Class<T> type);

	public void edit(Texture m);

	public void editMappings();

	public TextureController getTextureController();

	public void markChanged(Texture texture);

	public void move(TextureMapping m);

	public void remove(Face face);

	public void save(Texture texture);
}
