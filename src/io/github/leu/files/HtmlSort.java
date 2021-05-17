package io.github.leu.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlSort {
  static FileSystem fileSystem = FileSystems.getDefault();

  public static void main(String[] args) {
    System.out.println(isDiscordChatFile(fileSystem.getPath(
            "/media/leu/574b57ee-84cb-46a3-baa1-b585b2ac3247/windows-home/html#29/f54800320_StencilOp.html"
    ).toFile()));
  }

  static boolean isDiscordChatFile(File file) {
    try {
      String fileContents = Files.readString(file.toPath());
      Pattern pattern = Pattern.compile("\r\n\r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n\r\n<head>\r\n +<title>.*</title>");
      Matcher matcher = pattern.matcher(fileContents);
      return matcher.find();
    } catch (IOException e) {
      System.out.println("Couldn't read file!");
      return false;
    }
  }
}
