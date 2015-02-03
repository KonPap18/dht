package File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class FileOperations {

	// private static String FILE_NAME = "TextFile.txt";
	// private static final int GROUP_BYTES=1024;
	// private static byte PART_SIZE = 5;
	public static HashMap<String, File> split(File f, int filePartSize) {
		HashMap<String, File> fileParts = new HashMap<String, File>();
		File inputFile = f;
		FileInputStream inputStream;
		String newFileName;
		FileOutputStream filePart;
		File outputfile;
		int fileSize = (int) inputFile.length();
		int nChunks = 0, read = 0, readLength = filePartSize;
		byte[] byteChunkPart;
		try {
			inputStream = new FileInputStream(inputFile);
			while (fileSize > 0) {
				if (fileSize <= 5) {
					readLength = fileSize;
				}
				byteChunkPart = new byte[readLength];
				read = inputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;
				assert (read == byteChunkPart.length);
				nChunks++;
				newFileName = f.getName() + ".part"
						+ Integer.toString(nChunks - 1);
				outputfile = new File(newFileName);

				// filePart = new FileOutputStream(new File(newFileName));
				filePart = new FileOutputStream(outputfile);
				filePart.write(byteChunkPart);
				filePart.flush();
				filePart.close();
				fileParts.put(newFileName, outputfile);
				byteChunkPart = null;
				filePart = null;

			}
			inputStream.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return fileParts;
	}

	public static void merge(String filename, List<File> files) {
		File ofile = new File(filename);
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		List<File> list = files;
		try {
			fos = new FileOutputStream(ofile, true);
			for (File file : list) {
				fis = new FileInputStream(file);
				fileBytes = new byte[(int) file.length()];
				bytesRead = fis.read(fileBytes, 0, (int) file.length());
				assert (bytesRead == fileBytes.length);
				assert (bytesRead == (int) file.length());
				fos.write(fileBytes);
				fos.flush();
				fileBytes = null;
				fis.close();
				fis = null;
			}
			fos.close();
			fos = null;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static byte[] getBytes(File f) {
		byte[] data = null;
		Path path = Paths.get(f.getPath());
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	public static void main(String[] args) {
		File f = new File("TextFile.txt");
		HashMap fileparts = FileOperations.split(f, FilePart.FILE_PART_SIZE);
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOperations
				.merge("TextFile2.txt", new ArrayList(fileparts.values()));
	}
}
