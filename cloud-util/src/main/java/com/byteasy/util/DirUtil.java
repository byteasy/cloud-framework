package com.byteasy.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Log4j2
public final class DirUtil {

    /**
     * 删除过期文件及目录
     *
     * @param path            目录
     * @param deadTime        截止时间，所有创建时间早于此时间的文件均被删除
     * @param extensions      文件后缀
     * @param includeEmptyDir 是否删除空目录
     */
    public static void deleteExpireFiles(String path, LocalDateTime deadTime, String[] extensions, boolean includeEmptyDir) throws IOException {
        if (!FileUtil.exist(path)) {
            return;
        }

        Iterator<File> iterator = FileUtils.iterateFiles(new File(path), extensions, true);
        while (iterator.hasNext()) {
            File file = iterator.next();
            LocalDateTime createdTime = getCreatedTime(file);
            if (createdTime != null) {
                if (deadTime.isAfter(createdTime)) {
                    log.info("delete file: {}", file.getAbsoluteFile());
                    FileUtils.delete(file);
                }
            }
        }
        if (includeEmptyDir) {
            deleteEmptyFoldersRecursive(path);
        }
    }


    /**
     * 删除指定目录下的所有空文件夹（包括嵌套的空文件夹）
     *
     * @param directoryPath 要清理的目录路径
     * @return 被删除的空文件夹列表
     * @throws IOException 如果目录不存在或无法访问
     */
    public static List<String> deleteEmptyFolders(String directoryPath) throws IOException {
        if (!FileUtil.exist(directoryPath)) {
            return Collections.emptyList();
        }

        List<String> deletedFolders = new ArrayList<>();
        Path startPath = Paths.get(directoryPath);

        // 检查目录是否存在
        if (!Files.exists(startPath)) {
            throw new IOException("目录不存在: " + directoryPath);
        }

        if (!Files.isDirectory(startPath)) {
            throw new IOException("路径不是目录: " + directoryPath);
        }

        // 使用Files.walkFileTree遍历目录树
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                if (exc != null) {
                    throw exc;
                }

                // 检查目录是否为空（不包括目录本身）
                if (isEmptyDirectory(dir) && !dir.equals(startPath)) {
                    try {
                        Files.delete(dir);
                        deletedFolders.add(dir.toString());
                        log.info("已删除空文件夹: {}", dir);
                    } catch (DirectoryNotEmptyException e) {
                        // 可能在删除过程中目录不再为空，跳过
                        log.info("跳过非空文件夹: {}", dir);
                    } catch (IOException e) {
                        log.error("无法删除文件夹: {}", dir);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return deletedFolders;
    }

    /**
     * 检查目录是否为空（不包含任何文件或文件夹）
     */
    private static boolean isEmptyDirectory(Path path) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            return !directoryStream.iterator().hasNext();
        } catch (IOException e) {
            return false; // 如果无法访问，认为非空
        }
    }

    /**
     * 递归方式删除空文件夹（备选方案）
     */
    public static List<String> deleteEmptyFoldersRecursive(String directoryPath) {
        List<String> deletedFolders = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            deleteEmptyFoldersRecursive(directory, deletedFolders);
        }

        return deletedFolders;
    }

    private static void deleteEmptyFoldersRecursive(File directory, List<String> deletedFolders) {
        File[] files = directory.listFiles();

        if (files != null) {
            // 先递归处理子目录
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteEmptyFoldersRecursive(file, deletedFolders);
                }
            }

            // 重新获取文件列表，因为子目录可能已被删除
            files = directory.listFiles();

            // 如果目录为空且不是根目录，则删除
            if (files != null && files.length == 0 && !directory.getAbsolutePath().equals("/")) {
                if (directory.delete()) {
                    deletedFolders.add(directory.getAbsolutePath());
                    log.info("已删除空文件夹: {}", directory.getAbsolutePath());
                } else {
                    log.error("已删除空文件夹: {}", directory.getAbsolutePath());
                }
            }
        }
    }

    public static LocalDateTime getCreatedTime(File file) {
        Path filePath = Paths.get(file.getAbsolutePath());
        try {
            // 获取文件创建时间属性
            BasicFileAttributes attrs = Files.readAttributes(
                    filePath, BasicFileAttributes.class
            );

            FileTime creationTime = attrs.creationTime();
            // 转换为LocalDateTime
            return LocalDateTime.ofInstant(
                    creationTime.toInstant(), ZoneId.systemDefault()
            );
        } catch (IOException e) {
            log.error("无法获取文件属性", e);
        }
        return null;
    }
}
