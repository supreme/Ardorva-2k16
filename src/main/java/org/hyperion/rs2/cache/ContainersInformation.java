package org.hyperion.rs2.cache;

import java.util.zip.CRC32;

import org.hyperion.rs2.cache.stream.InputStream;
import org.hyperion.rs2.cache.util.Utils;

public final class ContainersInformation {

	private Container informationContainer;
	private int protocol;
	private int revision;
	private int[] containersIndexes;
	private FilesContainer[] containers;
	private boolean filesNamed;
	
	public ContainersInformation(byte[] informationContainerPackedData) {
		informationContainer = new Container();
		informationContainer.setVersion((informationContainerPackedData[informationContainerPackedData.length - 2] << 8 & 0xff00) + (informationContainerPackedData[-1 + informationContainerPackedData.length] & 0xff));
		CRC32 crc32 = new CRC32();
		crc32.update(informationContainerPackedData);
		informationContainer.setCrc((int) crc32.getValue());
		decodeContainersInformation(Utils.unpackCacheContainer(informationContainerPackedData));
	}
	
	public int[] getContainersIndexes() {
		return containersIndexes;
	}
	
	public FilesContainer[] getContainers() {
		return containers;
	}
	public Container getInformationContainer() {
		return informationContainer;
	}
	
	public int getRevision() {
		return revision;
	}

	public void decodeContainersInformation(byte[] data) {
		InputStream stream = new InputStream(data);
		protocol = stream.readUnsignedByte();
		if (protocol >= 6)
		    revision = stream.readInt();
		else
		    revision = 0;
		int nameHash = stream.readUnsignedByte();
		filesNamed = (0x1 & nameHash) != 0;
		containersIndexes = new int[protocol >= 7 ? stream.readSmart32() : stream.readUnsignedShort()];
		int lastIndex = -1;
		for (int index = 0; index < containersIndexes.length; index++) {
			containersIndexes[index] = protocol >= 7 ? stream.readSmart32() : stream.readUnsignedShort() + (index == 0 ? 0 : containersIndexes[index-1]);
			if (containersIndexes[index] > lastIndex)
				lastIndex = containersIndexes[index];
		}
		containers = new FilesContainer[lastIndex+1];
		for(int index = 0; index < containersIndexes.length; index++)
			containers[containersIndexes[index]] = new FilesContainer();
		if(filesNamed)
			for(int index = 0; index < containersIndexes.length; index++)
				containers[containersIndexes[index]].setNameHash(stream.readInt());
		for(int index = 0; index < containersIndexes.length; index++)
			containers[containersIndexes[index]].setCrc(stream.readInt());
		for(int index = 0; index < containersIndexes.length; index++)
			containers[containersIndexes[index]].setVersion(stream.readInt());
		for(int index = 0; index < containersIndexes.length; index++)
			containers[containersIndexes[index]].setFilesIndexes(new int[protocol >= 7 ? stream.readSmart32() : stream.readUnsignedShort()]);
		for (int index = 0; index < containersIndexes.length; index++) {
			int lastFileIndex = -1;
			for(int fileIndex = 0; fileIndex < containers[containersIndexes[index]].getFilesIndexes().length; fileIndex++) {
				containers[containersIndexes[index]].getFilesIndexes()[fileIndex] = protocol >= 7 ? stream.readSmart32() : stream.readUnsignedShort() + (fileIndex == 0 ? 0 : containers[containersIndexes[index]].getFilesIndexes()[fileIndex-1]);
				if (containers[containersIndexes[index]].getFilesIndexes()[fileIndex] > lastFileIndex)
					lastFileIndex = containers[containersIndexes[index]].getFilesIndexes()[fileIndex];
			}
			containers[containersIndexes[index]].setFiles(new Container[lastFileIndex+1]);
			for(int fileIndex = 0; fileIndex < containers[containersIndexes[index]].getFilesIndexes().length; fileIndex++)
				containers[containersIndexes[index]].getFiles()[containers[containersIndexes[index]].getFilesIndexes()[fileIndex]] = new Container();
		}
		if(filesNamed)
			for (int index = 0; index < containersIndexes.length; index++)
				for(int fileIndex = 0; fileIndex < containers[containersIndexes[index]].getFilesIndexes().length; fileIndex++)
					containers[containersIndexes[index]].getFiles()[containers[containersIndexes[index]].getFilesIndexes()[fileIndex]].setNameHash(stream.readInt());
	}
	
}
