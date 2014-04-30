package com.jayanslow.projection.texture.editor.controller;

import java.awt.Frame;

import com.jayanslow.projection.texture.controllers.TextureController;
import com.jayanslow.projection.texture.listeners.TextureListener;
import com.jayanslow.projection.texture.models.Texture;
import com.jayanslow.projection.texture.models.TextureMapping;
import com.jayanslow.projection.texture.models.TextureType;
import com.jayanslow.projection.world.controllers.WorldController;
import com.jayanslow.projection.world.models.Face;

public interface TextureEditorController {
	public void addTextureListener(TextureListener l) throws NullPointerException;

	/**
	 * Creates a new TextureMapping
	 * 
	 * @param face
	 *            Face to map new texture to
	 * @param type
	 *            Type of texture to create
	 */
	public <T extends Texture> void create(Face face, TextureType type);

	/**
	 * Creates a new TextureMapping
	 * 
	 * @param parent
	 *            Parent frame to open dialogs under(can be null)
	 */
	public void create(Frame parent);

	/**
	 * Creates a new Texture
	 * 
	 * @param type
	 *            TextureType to create
	 */
	public Texture create(TextureType type);

	/**
	 * Opens a frame to edit a specified texture
	 * 
	 * @param texture
	 *            Texture to edit
	 */
	public void edit(Texture texture);

	/**
	 * Opens a frame to edit mappings between faces and textures
	 */
	public void editMappings();

	/**
	 * Gets the texture controller being edited
	 * 
	 * @return
	 */
	public TextureController getTextureController();

	/**
	 * Gets the world controller of the texture controller being edited
	 * 
	 * @return
	 */
	public WorldController getWorldController();

	/**
	 * Moves a texture from one face to another
	 * 
	 * @param parent
	 *            Parent frame to open dialogs under (can be null)
	 * @param mapping
	 *            Mapping to update
	 */
	public void move(Frame parent, TextureMapping mapping);

	/**
	 * Removes a texture mapping for a specified face
	 * 
	 * @param face
	 *            Face to unmap
	 */
	public void remove(Face face);
}
