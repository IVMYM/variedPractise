package com.example.variesdiffmayfuse.file.pojo;

import com.example.variesdiffmayfuse.file.FileController;

import java.io.File;

/**
 * @createdTime: 2022/8/29 10:07
 * @description: 1.0    yuanliu	2022/8/29	创建初始版本
 */
public class BreakPointFile {
    private File file;
    private String name;
    private Integer chunks;//总片数
    private Integer chunk;//当前第几片
    private String guid;//fileId

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public Integer getChunk() {
        return chunk;
    }

    public void setChunk(Integer chunk) {
        this.chunk = chunk;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
