package com.jayanslow.projection.texture.editor.models;

import java.awt.Color;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.jayanslow.projection.texture.models.ColorImageTexture;
import com.jayanslow.projection.texture.models.DirectoryVideoTexture;
import com.jayanslow.projection.texture.models.FileImageTexture;
import com.jayanslow.projection.texture.models.Texture;
import com.jayanslow.projection.texture.models.TextureMapping;
import com.jayanslow.projection.texture.models.TextureType;

public class MappingsTableModel extends AbstractTableModel {

	private static final long	serialVersionUID	= 2674047028387318232L;

	private static final int	COLUMN_SCREEN_ID	= 0;
	private static final int	COLUMN_FACE_ID		= 1;
	private static final int	COLUMN_FACE_NAME	= 2;
	private static final int	COLUMN_TEXTURE_TYPE	= 3;
	private static final int	COLUMN_TEXTURE_INFO	= 4;

	public static void useModel(JTable table, List<TextureMapping> mappings) {
		MappingsTableModel model = new MappingsTableModel(mappings);
		table.setModel(model);

		for (int i = 0; i < model.getColumnCount(); i++) {
			TableColumn c = table.getColumnModel().getColumn(i);
			switch (i) {
			case COLUMN_SCREEN_ID:
			case COLUMN_FACE_ID:
				c.setPreferredWidth(60);
				break;
			}
		}

		ListSelectionModel selection = table.getSelectionModel();
		selection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (model.getRowCount() > 0)
			selection.setSelectionInterval(0, 0);
	}

	private final List<TextureMapping>	mappings;

	/**
	 * @param universeFrame
	 */
	MappingsTableModel(List<TextureMapping> mappings) {
		this.mappings = mappings;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case COLUMN_SCREEN_ID:
			return Integer.class;
		case COLUMN_FACE_ID:
			return Integer.class;
		case COLUMN_FACE_NAME:
			return String.class;
		case COLUMN_TEXTURE_TYPE:
			return TextureType.class;
		case COLUMN_TEXTURE_INFO:
			return Object.class;
		default:
			throw new IllegalArgumentException("Column out of range");
		}
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case COLUMN_SCREEN_ID:
			return "Screen ID";
		case COLUMN_FACE_ID:
			return "Face ID";
		case COLUMN_FACE_NAME:
			return "Face Name";
		case COLUMN_TEXTURE_TYPE:
			return "Texture Type";
		case COLUMN_TEXTURE_INFO:
			return "";
		default:
			throw new IllegalArgumentException("Column out of range");
		}
	}

	public TextureMapping getMapping(int rowIndex) {
		return mappings.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return mappings.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TextureMapping m = getMapping(rowIndex);
		Texture t = m.getTexture();
		switch (columnIndex) {
		case COLUMN_SCREEN_ID:
			return m.getScreenId();
		case COLUMN_FACE_ID:
			return m.getFaceId();
		case COLUMN_FACE_NAME:
			String name = m.getFace().getName();
			return name == null ? "" : name;
		case COLUMN_TEXTURE_TYPE:
			return t.getTextureType();
		case COLUMN_TEXTURE_INFO:
			switch (t.getTextureType()) {
			case FILE:
				return ((FileImageTexture) t).getFile().getPath();
			case BUFFERED:
				return null;
			case COLOR:
				Color c = ((ColorImageTexture) t).getColor();
				return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()).toUpperCase();
			case DIRECTORY:
				return ((DirectoryVideoTexture) t).getDirectory().getPath();
			case LIST:
				return String.format("%d images", t.getNumberOfFrames());
			case PREVIEW:
				return null;
			default:
				throw new RuntimeException("Unknown TextureType in MappingsTableModel");
			}
		default:
			throw new IllegalArgumentException("Column out of range");
		}
	}
}
