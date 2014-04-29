package com.jayanslow.projection.texture.editor.controller;

import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.jayanslow.projection.texture.controllers.TextureController;
import com.jayanslow.projection.texture.editor.views.BufferedImageTextureFrame;
import com.jayanslow.projection.texture.editor.views.ColorImageTextureFrame;
import com.jayanslow.projection.texture.editor.views.FaceDialog;
import com.jayanslow.projection.texture.editor.views.FileImageTextureFrame;
import com.jayanslow.projection.texture.editor.views.TextureMappingsFrame;
import com.jayanslow.projection.texture.editor.views.TextureTypeDialog;
import com.jayanslow.projection.texture.listeners.TextureListener;
import com.jayanslow.projection.texture.models.BufferedImageTexture;
import com.jayanslow.projection.texture.models.ColorImageTexture;
import com.jayanslow.projection.texture.models.FileImageTexture;
import com.jayanslow.projection.texture.models.PreviewTexture;
import com.jayanslow.projection.texture.models.Texture;
import com.jayanslow.projection.texture.models.TextureMapping;
import com.jayanslow.projection.texture.models.TextureType;
import com.jayanslow.projection.world.controllers.WorldController;
import com.jayanslow.projection.world.models.Face;
import com.jayanslow.utils.swing.JFileChooserHelpers;

public class StandardTextureEditorController implements TextureEditorController {
	private final List<TextureListener>	listeners	= new LinkedList<>();
	private final TextureController		textures;
	private final WorldController		world;

	public StandardTextureEditorController(WorldController world, TextureController textures) {
		super();
		this.world = world;
		this.textures = textures;
	}

	@Override
	public void addTextureListener(TextureListener l) {
		listeners.add(l);
	}

	@Override
	public <T extends Texture> void create(Face face, TextureType type) {
		Texture texture;
		Frame frame;
		switch (type) {
		case COLOR:
			texture = new ColorImageTexture(Color.BLACK);
			frame = new ColorImageTextureFrame(this, (ColorImageTexture) texture);
			break;
		case FILE:
			File file = JFileChooserHelpers.chooseImageFile(null, null);
			if (file == null)
				return;
			texture = new FileImageTexture(file);
			frame = new FileImageTextureFrame(this, (FileImageTexture) texture);
			break;
		case DIRECTORY:
		case LIST:
			throw new UnsupportedOperationException();
		case PREVIEW:
		case BUFFERED:
		default:
			throw new UnsupportedOperationException();
		}
		textures.putTexture(face, texture);
		showFrame(frame);
	}

	@Override
	public void create(Frame parent) {
		TextureType type = selectTextureType(parent);
		Face face = selectFace(parent);
		create(face, type);
	}

	@Override
	public void edit(Texture t) {
		switch (t.getTextureType()) {
		case COLOR:
			showFrame(new ColorImageTextureFrame(this, (ColorImageTexture) t));
			break;
		case FILE:
			showFrame(new FileImageTextureFrame(this, (FileImageTexture) t));
			break;
		case BUFFERED:
			showFrame(new BufferedImageTextureFrame(this, (BufferedImageTexture) t));
			break;
		case DIRECTORY:
			break;
		case LIST:
			break;
		case PREVIEW:
			((PreviewTexture) t).restore();
			break;
		default:
			throw new RuntimeException("Unknown TextureType in StandardTextureEditorController");
		}

	}

	@Override
	public void editMappings() {
		TextureMappingsFrame frame = new TextureMappingsFrame(this, textures);
		showFrame(frame);
		addTextureListener(frame);
	}

	@Override
	public TextureController getTextureController() {
		return textures;
	}

	@Override
	public WorldController getWorldController() {
		return world;
	}

	@Override
	public void move(Frame parent, TextureMapping mapping) {
		Face face = selectFace(parent);
		if (face == null)
			return;
		textures.remove(mapping.getFace());
		textures.putTexture(face, mapping.getTexture());
	}

	@Override
	public void remove(Face f) {
		textures.remove(f);
	}

	private Face selectFace(Frame parent) {
		return FaceDialog.selectFace(parent, world, textures);
	}

	private TextureType selectTextureType(Frame parent) {
		return TextureTypeDialog.selectTextureType(parent);
	}

	private void showFrame(Frame frame) {
		frame.setVisible(true);
	}

	@Override
	public void textureChange(Texture texture) {
		for (TextureListener l : listeners)
			if (l != null)
				l.textureChange(texture);
	}

	@Override
	public void textureFrameChange(int current, int old) {
		for (TextureListener l : listeners)
			if (l != null)
				l.textureFrameChange(current, old);
	}

}
