package com.example.variesdiffmayfuse.file;
import com.example.variesdiffmayfuse.file.pojo.BreakPointFile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


    @Service
    public class FileUploadService {
        private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);

        public void uploadFile(BreakPointFile breakPointFile) {
//            Result result = Result.success(breakPointFile.getName());

            String fileId = breakPointFile.getGuid();
            Integer chunk =
                    breakPointFile.getChunk() == null ? 0 : breakPointFile.getChunk();
            Integer chunks =
                    breakPointFile.getChunks() == null ? 1 : breakPointFile.getChunks();

            File fileDir = null;

            try {

                fileDir = new File("F:/tempFile/" + fileId);
                if (!fileDir.isDirectory()) {
                    fileDir.mkdirs();
                }
                File file = new File(fileDir, chunk + "");
                if (!file.exists()) {
                    file.createNewFile();
                }

                outPutSliceFile(file,breakPointFile);

                LOGGER.info("分片上传({}/{})" ,chunk+1,chunks);
            } catch (Exception e) {
                e.printStackTrace();
//                Result.error(e);
            }

            if (chunks.intValue() == chunk + 1) {
                LOGGER.info("分片完成!");
                LOGGER.info("开始合并...");

                InputStream in = null;
                OutputStream out = null;
                try {
                    String path = fileDir.getAbsolutePath() + "/" + breakPointFile.getName();

                    File[] files = fileDir.listFiles();
                    List<File> fileList = Arrays.asList(files);
                    List<File> list = fileList.stream()
                            .sorted(Comparator.comparing(this::getSortName))
                            .collect(Collectors.toList());

                    out = new FileOutputStream(path);
                    for (File file : list) {
                        in = new FileInputStream(file);
                        int len = 0;
                        byte[] bt = new byte[1024];
                        while (-1 != (len = in.read(bt))) {
                            out.write(bt, 0, len);
                        }

                        in.close();
                    }
                    // 删除上传的分片文件
                    deletTempFIles(fileList);


                } catch (Exception e) {
                    e.printStackTrace();
//                    Result.error(e);
                } finally {
//                    IoUtils.Close(in);
//                    IoUtils.Close(out);
                }
                LOGGER.info("合并完成!");
                LOGGER.info("文件上传完成! fieId = " + fileId);

            }

//            return result;
        }

        private void deletTempFIles(List<File> fileList) {
            Iterator<File> iterator = fileList.iterator();
            iterator.forEachRemaining(file -> {
                file.delete();
            });
        }

        private void outPutSliceFile(File file, BreakPointFile breakPointFile) throws IOException {

            OutputStream outSliceFile = new FileOutputStream(file);

//            byte[] bytes = breakPointFile.getFile().getBytes(); //保存文件
            byte[] bytes = {};
            outSliceFile.write(bytes);

//            IoUtils.Close(outSliceFile);
        }

        private int getSortName(File file) {

            int i = Integer.parseInt(file.getName());
            return i;
        }

        private static Map<String,Integer> cache = new ConcurrentHashMap<>();

        private static synchronized boolean checkChunk (String key,int chunks){
            Integer counter = cache.get(key);
            if (counter == null){
                counter = 0;
            }
            counter++;
            cache.put(key,counter);

            return chunks == counter.intValue();
        }

        public void uploadFileSyn(BreakPointFile breakPointFile) {
//            Result result = Result.error(breakPointFile.getName());

            String fileId = breakPointFile.getGuid();
            Integer chunk =
                    breakPointFile.getChunk() == null ? 0 : breakPointFile.getChunk();
            Integer chunks =
                    breakPointFile.getChunks() == null ? 1 : breakPointFile.getChunks();

            File fileDir = null;

            try{
                fileDir = new File("F:/tempFileSyn/" + fileId);
                if (!fileDir.isDirectory()) {
                    fileDir.mkdirs();
                }
                File file = new File(fileDir, chunk + "");
                if (!file.exists()) {
                    file.createNewFile();
                }

//                InputStream inStream = breakPointFile.getFile().getInputStream();
                InputStream inStream = new FileInputStream(fileDir);//伪代码
                OutputStream outStream = new FileOutputStream(file);
                IOUtils.copy(inStream,outStream);
//                IoUtils.Close(inStream);
//                IoUtils.Close(outStream);
                LOGGER.info("分片上传({}/{})" ,chunk+1,chunks);

                if (checkChunk(fileId,chunks)){
                    LOGGER.info("分片完成!");
                    LOGGER.info("开始合并...");
                    mergeFile(breakPointFile, fileId, fileDir);
//                    result = Result.success(breakPointFile.getGuid());
                }

            }catch (Exception e){
                e.printStackTrace();
            }
//            return result;

        }

        private void mergeFile(BreakPointFile breakPointFile, String fileId, File fileDir) {

            InputStream in = null;
            OutputStream out = null;
            try {
                String path = fileDir.getAbsolutePath() + "/" + breakPointFile.getName();

                File[] files = fileDir.listFiles();
                List<File> fileList = Arrays.asList(files);
                List<File> list = fileList.stream()
                        .sorted(Comparator.comparing(this::getSortName))
                        .collect(Collectors.toList());

                out = new FileOutputStream(path);
                for (File file1 : list) {
                    in = new FileInputStream(file1);
                    IOUtils.copy(in,out);
                    in.close();
                }
                // 删除上传的分片文件
                deletTempFIles(fileList);

            } catch (Exception e) {
                e.printStackTrace();
//                Result.error(e);
            } finally {
//                IoUtils.Close(in);
//                IoUtils.Close(out);
            }
            LOGGER.info("合并完成!");
            LOGGER.info("文件上传完成! fieId = " + fileId);
        }


        public static void main(String[] args) {
            double random = Math.random();
            String substring = String.valueOf(random).substring(2, 8);
            System.out.println(random);
            System.out.println(substring);
        }
    }

