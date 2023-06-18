package com.striveonger.study.core.utils;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-17 18:28
 */
public class FileUtil {
    private final Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new CustomException(ResultStatus.ACCIDENT, String.format("Illegal [%s] to URL", file));
        }
    }
}
