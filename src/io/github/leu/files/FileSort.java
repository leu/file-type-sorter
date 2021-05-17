package io.github.leu.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileSort {
  FileSystem fileSystem = FileSystems.getDefault();
  File rootFileDir;
  HashMap<String, int[]> fileTypeFolderSize = new HashMap<>();
  public static Set<String> UNWANTED_FILETYPES = Set.of("7z", "ai", "asp", "bat", "c", "csv", "db", "doc",
          "f", "go", "h", "ini", "jar", "java", "json", "jsp", "odg", "php", "pl",
          "ppt", "sxw", "tex", "vsd", "xls", "zip", "txt", "gif", "py", "xml", "m4p");
  public static int MAX_FOLDER_SIZE = 200;

  public FileSort(String rootDir) {
    rootFileDir = fileSystem.getPath(rootDir).toFile();
  }

  public void sortRootDirectory() {
    recursiveSort(rootFileDir, Objects.requireNonNull(rootFileDir.listFiles()));
  }

  private void recursiveSort(File root, File[] files) {
    for (File subFile : Objects.requireNonNull(files)) {
      if (subFile.isDirectory()) {
        recursiveSort(root, subFile.listFiles());
        if (Objects.requireNonNull(subFile.listFiles()).length == 0) {
          try {
            Files.delete(subFile.toPath());
          } catch (IOException e) {
            System.out.println("Could not delete folder!");
          }
        }
      } else if (subFile.isFile()) {
        sortFile(root, subFile);
      }
    }
  }

  private void sortFile(File root, File file) { //PRE: file has format name.(extension)
    String fileType = file.getName().split("\\.")[1];
    if (!fileTypeFolderSize.containsKey(fileType)) {
      int[] initialFolderSize = {0, 0}; //{folderNum, numOfFilesInFolder}
      fileTypeFolderSize.put(fileType, initialFolderSize);
    }
    int folderNum = fileTypeFolderSize.get(fileType)[0];
    int folderSize = fileTypeFolderSize.get(fileType)[1];

    String folderPath = root.getPath() + "/" + fileType + "#" + folderNum;
    Path destinationFolder = fileSystem.getPath(folderPath);
    Path destinationFile = fileSystem.getPath(folderPath + "/" + file.getName());

    try {
      if (UNWANTED_FILETYPES.contains(fileType)
              || fileType.equals("html") && !HtmlSort.isDiscordChatFile(file)
              || fileType.equals("png") && Files.size(file.toPath()) < 100000
              || fileType.equals("jpg") && Files.size(file.toPath()) < 50000) {
        Files.delete(file.toPath());
      } else {
        if (!Files.exists(destinationFolder)) {
          Files.createDirectory(destinationFolder);
        }
        Files.move(file.toPath(), destinationFile);

        if (folderSize == MAX_FOLDER_SIZE-1) {
          folderNum++;
          folderSize = 0;
        } else {
          folderSize++;
        }
        int[] nextFile = {folderNum, folderSize};
        fileTypeFolderSize.replace(fileType, nextFile);
      }
    } catch (IOException e) {
      System.out.println("Problem moving file!");
    }
  }
}
