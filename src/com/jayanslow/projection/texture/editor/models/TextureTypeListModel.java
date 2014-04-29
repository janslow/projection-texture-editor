package com.jayanslow.projection.texture.editor.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.jayanslow.projection.texture.models.TextureType;

public class TextureTypeListModel extends AbstractListModel<String> {

	private static final long			serialVersionUID	= -2449400729014327407L;

	private static List<TextureType>	allTypes			= createTypesList();

	private static List<TextureType> createTypesList() {
		TextureType[] array = TextureType.values();

		List<TextureType> list = new ArrayList<>(array.length);
		for (TextureType t : array)
			list.add(t);

		list.remove(TextureType.PREVIEW);
		list.remove(TextureType.BUFFERED);
		return list;
	}

	private List<TextureType>	types;

	public TextureTypeListModel() {
		clearFilter();
	}

	public void clearFilter() {
		types = allTypes;
		fireIntervalAdded(this, 0, getSize() - 1);
	}

	public void filter(boolean imagesOnly) {
		types = new ArrayList<>();
		for (TextureType t : allTypes)
			if ((imagesOnly && t.extendsImageTexture()) || (!imagesOnly && t.extendsVideoTexture()))
				types.add(t);
		fireIntervalAdded(this, 0, getSize() - 1);
	}

	@Override
	public String getElementAt(int index) {
		return types.get(index).name();
	}

	@Override
	public int getSize() {
		return types.size();
	}

	public TextureType getTypeAt(int index) {
		return types.get(index);
	}

}
