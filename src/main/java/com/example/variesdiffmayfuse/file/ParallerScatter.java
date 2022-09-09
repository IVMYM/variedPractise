package com.example.variesdiffmayfuse.file;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.input.NullInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * 高效并行处理文件压缩工具类，支持多层文件夹
 * 2G大小多层级目录压缩时间24秒左右
 * @createdTime: 2022/7/15 15:11
 * @description: 1.0    yuanliu	2022/7/15	创建初始版本
 */
public class ParallerScatter {
    class CustomInputStreamSupplier implements InputStreamSupplier {
        private File currentFile;

        public CustomInputStreamSupplier(File currentFile) {
            this.currentFile = currentFile;
        }


        @Override
        public InputStream get() {
            try {
                // InputStreamSupplier api says:
                // 返回值：输入流。永远不能为Null,但可以是一个空的流
                return currentFile.isDirectory() ? new NullInputStream(0) : new FileInputStream(currentFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private void addEntry(String entryName, File currentFile, ScatterSample scatterSample) throws IOException {
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entryName);
        archiveEntry.setMethod(ZipEntry.DEFLATED);
        final InputStreamSupplier supp = new CustomInputStreamSupplier(currentFile);
        scatterSample.addEntry(archiveEntry, supp);
    }


    private void compressCurrentDirectory(File dir, ScatterSample scatterSample) throws IOException {
        if (dir == null) {
            throw new IOException("源路径不能为空！");
        }
        String relativePath = "";
        if (dir.isFile()) {
            relativePath = dir.getName();
            addEntry(relativePath, dir, scatterSample);
            return;
        }


        // 空文件夹
        if (dir.listFiles().length == 0) {
            relativePath = dir.getAbsolutePath().replace(scatterSample.getRootPath(), "");
            addEntry(relativePath + File.separator, dir, scatterSample);
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                compressCurrentDirectory(f, scatterSample);
            } else {
                relativePath = f.getParent().replace(scatterSample.getRootPath(), "");
                addEntry(relativePath + File.separator + f.getName(), f, scatterSample);
            }
        }
    }

    /**
     *压缩文件算法
     * @param rootPath  要压缩的文件路径
     * @param result 最终的压缩文件
     * @throws Exception
     */
    public void createRarFile(final String rootPath, final File result) throws Exception {
        File dstFolder = new File(result.getParent());
        if (!dstFolder.isDirectory()) {
            dstFolder.mkdirs();
        }
        File rootDir = new File(rootPath);
        final ScatterSample scatterSample = new ScatterSample(rootDir.getAbsolutePath());
        compressCurrentDirectory(rootDir, scatterSample);
        final ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(result);
        scatterSample.writeTo(zipArchiveOutputStream);
        zipArchiveOutputStream.close();
    }
}

