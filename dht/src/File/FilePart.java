package File;

import java.io.File;

import Hash.FilePartKey;

public class FilePart {
	private String fileName;
	private String filePartName;
	private long id;
	private FilePartKey filePartKey;
	private boolean isMinusOne;

	public static final int FILE_PART_SIZE = 1024;// in bytes

	/*
	 * public static FilePart minusOneFilepart(File f) {
	 * 
	 * 
	 * return null;
	 * 
	 * }
	 */
	public FilePart(String fileName, String filePartName, long id,
			FilePartKey filePartKey) {
		this.id = id;
		if (this.id == -1) {
			isMinusOne = true;
		}
		this.fileName = fileName;
		this.filePartName = filePartName;

		this.filePartKey = filePartKey;
	}

	public FilePart(String fileName, long id) {
		this.id = id;
		if (this.id == -1) {
			isMinusOne = true;
		}
		this.fileName = fileName;
		this.filePartName = fileName + ".part" + String.valueOf(id);
		this.filePartKey = new FilePartKey(this.filePartName);

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePartName() {
		return filePartName;
	}

	public void setFilePartName(String filePartName) {
		this.filePartName = filePartName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public FilePartKey getFilePartKey() {
		return filePartKey;
	}

	public void setFilePartKey(FilePartKey filePartKey) {
		this.filePartKey = filePartKey;
	}

	public boolean isMinusOne() {
		return isMinusOne;
	}

	public void setMinusOne(boolean isMinusOne) {
		this.isMinusOne = isMinusOne;
	}

}
