package org.hyperion.rs2.cache;

public final class FilesContainer extends Container {
	
	private int[] filesIndexes;
	private Container[] files;

	public void setFiles(Container[] containers) {
		this.files = containers;
	}

	public Container[] getFiles() {
		return files;
	}

	public void setFilesIndexes(int[] containersIndexes) {
		this.filesIndexes = containersIndexes;
	}

	public int[] getFilesIndexes() {
		return filesIndexes;
	}
	
}
