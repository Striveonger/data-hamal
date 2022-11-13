package com.striveonger.study.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-08 21:32
 */
public enum FileHash {

    MD5("MD5"), SHA1("SHA1"), SHA256("SHA-256"), SHA512("SHA-512");

    private final Logger log = LoggerFactory.getLogger(SpringContextHolder.class);
    private final String name;

    FileHash(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public String code(File file) {
        try {
            return code(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("读取文件失败", e);
        }
        return null;
    }

    public String code(InputStream input) {
        try (input) {
            MessageDigest digest = MessageDigest.getInstance(getName());
            byte[] bytes = new byte[4096];
            int count;
            while ((count = input.read(bytes)) > 0) {
                digest.update(bytes, 0, count);
            }
            bytes = digest.digest();
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                code.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return code.toString();
        } catch (FileNotFoundException e) {
            log.error("读取文件失败", e);
        } catch (IOException e) {
            log.error("文件流异常", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("未生成文件摘要", e);
        }
        return null;
    }

}