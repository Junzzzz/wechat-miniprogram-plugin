package com.zxy.ijplugin.wechat_miniprogram.lang.scrpits;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * FileExtractUtils
 */
public class FileExtractUtils {
    /**
     * The constant BUFFER_SIZE.
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Extract zip.
     *
     * @param zipfile
     *            the zipfile
     * @param outdir
     *            the outdir
     */
    public static void extractZip(File zipfile, File outdir) {
        try {
            ZipInputStream is = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipfile)));
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                String name = entry.getName();
                if (entry.isDirectory()) {
                    mkDirs(outdir, name);
                } else {
                    String dir = directoryPart(name);
                    if (dir != null) {
                        mkDirs(outdir, dir);
                    }
                    extractFile(is, outdir, name);
                }
            }
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extract g zip.
     *
     * @param tgzFile
     *            the tgz file
     * @param outDir
     *            the out dir
     */
    public static void extractGZip(File tgzFile, File outDir) {
        try {
            TarArchiveInputStream tarIs = new TarArchiveInputStream(new GzipCompressorInputStream(
                    new BufferedInputStream(new FileInputStream(tgzFile))));
            TarArchiveEntry entry;
            while ((entry = (TarArchiveEntry) tarIs.getNextEntry()) != null) {
                String name = entry.getName();
                if (entry.isDirectory()) {
                    mkDirs(outDir, name);
                } else {
                    String dir = directoryPart(name);
                    if (dir != null) {
                        mkDirs(outDir, dir);
                    }
                    extractFile(tarIs, outDir, name);
                }
            }
            tarIs.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extract file.
     *
     * @param inputStream
     *            the input stream
     * @param outDir
     *            the out dir
     * @param name
     *            the name
     * @throws IOException
     *             the io exception
     */
    private static void extractFile(InputStream inputStream, File outDir,
                                    String name) throws IOException {
        int count = -1;
        byte buffer[] = new byte[BUFFER_SIZE];
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(new File(outDir, name)), BUFFER_SIZE);
        while ((count = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
            out.write(buffer, 0, count);
        }
        out.close();
    }

    /**
     * Mk dirs.
     *
     * @param outdir
     *            the outdir
     * @param path
     *            the path
     */
    private static void mkDirs(File outdir, String path) {
        File d = new File(outdir, path);
        if (!d.exists()) {
            d.mkdirs();
        }
    }

    /**
     * Directory part string.
     *
     * @param name
     *            the name
     * @return the string
     */
    private static String directoryPart(String name) {
        int s = name.lastIndexOf(File.separatorChar);
        return s == -1 ? null : name.substring(0, s);
    }

}