package cn.gogpay.dcb.docp.service.report;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * zip压缩工具
 *
 * @author zhangxiangqi@gogpay.cn
 * @date 2019/12/25 025 9:40
 */
public class ZipCompress {

    /**
     * 文件压缩方法
     *
     * @param zipFileName    Zip文件
     * @param sourceFileName 源文件
     * @throws IOException
     */
    public static void zip(String zipFileName, String sourceFileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            File sourceFile = new File(sourceFileName);
            compress(zos, sourceFile, sourceFile.getName());
        }
    }

    /**
     * 压缩操作
     *
     * @param zos        ZipOutputStream
     * @param sourceFile File
     * @param base       BasePath
     * @throws IOException
     */
    private static void compress(ZipOutputStream zos, File sourceFile, String base) throws IOException {
        if (sourceFile.isDirectory()) {
            //如果路径为文件夹

            //取出文件夹中的文件
            File[] flist = sourceFile.listFiles();

            assert flist != null;
            if (flist.length == 0) {
                //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                ZipEntry entry = new ZipEntry(StringUtils.join(base, File.separator));
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                //如果文件夹不为空，则递归调用compress对文件夹中的每一个文件进行压缩
                for (File file : flist) {
                    compress(zos, file, StringUtils.join(base, File.separator, file.getName()));
                }
            }
        } else {
            ZipEntry entry = new ZipEntry(base);
            zos.putNextEntry(entry);

            try (FileInputStream fis = new FileInputStream(sourceFile);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                int len;
                byte[] buf = new byte[1024 * 4];
                while ((len = bis.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
            }
            zos.closeEntry();
        }
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        zip("d:/log.zip", "d:/log");
        long end = System.currentTimeMillis();
        System.out.println("压缩完成，耗时" + (end - start));
    }
}
