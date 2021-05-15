package io.github.leu;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class FileSort {
  FileSystem fileSystem = FileSystems.getDefault();
  File rootFileDir;

  FileSort(String rootDir) {
    rootFileDir = fileSystem.getPath(rootDir).toFile();
  }

  public void run() {
    recursiveRun(rootFileDir, Objects.requireNonNull(rootFileDir.listFiles()));
  }

  private void recursiveRun(File root, File[] files) {
    for (File subFile : Objects.requireNonNull(files)) {
      if (subFile.isDirectory()) {
        recursiveRun(root, subFile.listFiles());
        if (Objects.requireNonNull(subFile.listFiles()).length == 0) {
          try {
            Files.delete(subFile.toPath());
          } catch (IOException e) {
            System.out.println("Could not delete folder!");
          }
        }
      } else if (subFile.isFile()) {
        fileSort(root, subFile);
      }
    }
  }

  private void fileSort(File root, File file) { //PRE: file has format name.(extension)
    String fileType = file.getName().split("\\.")[1];
    Path destinationFolder = fileSystem.getPath(root.getPath() + "/" + fileType);
    Path destinationFile = fileSystem.getPath(root.getPath() + "/" + fileType + "/" + file.getName());

    try {
      if (!Files.exists(destinationFolder)) {
        Files.createDirectory(destinationFolder);
      }
      Files.move(file.toPath(), destinationFile);
    } catch (IOException e) {
      System.out.println("Problem moving file!");
    }
  }
}
