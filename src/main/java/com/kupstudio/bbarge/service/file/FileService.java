package com.kupstudio.bbarge.service.file;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.kupstudio.bbarge.constant.file.FileConstant;
import com.kupstudio.bbarge.exception.file.FileNotExistException;
import com.kupstudio.bbarge.exception.file.FileUrlException;
import lombok.Cleanup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FileService {

    private final static String STORAGE_PATH = "storage/";


    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final String FILE_URL_EXCEPTION = "FileUrlException";

    private AmazonS3 s3Client;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.endPoint}")
    private String endPoint;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3Url}")
    private String s3Url;


    @PostConstruct
    public void setS3Client() {
        s3Client = AmazonS3ClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region)).withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))).build();
    }

    public String uploadProductImg(MultipartFile file, int storeNo, String directoryPath) {

        if (!file.isEmpty()) {

            String fileOriginalFilename = file.getOriginalFilename();

            String ext = fileOriginalFilename.substring(fileOriginalFilename.lastIndexOf(".") + 1).toLowerCase();

            if (!IMAGE_EXTENSIONS.contains(ext)) {
                throw new FileUrlException("img only");
            }

            Date date = new Date();

            long time = date.getTime();

            String fileNameFormat = STORAGE_PATH + directoryPath + "/" + +storeNo + "_" + time + "." + ext;

            try {
                ObjectMetadata objMeta = new ObjectMetadata();
                objMeta.setContentType(Mimetypes.getInstance().getMimetype(fileNameFormat));

                @Cleanup
                InputStream inputStream = file.getInputStream();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                objMeta.setContentLength(bytes.length);

                ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

                PutObjectRequest putObjReq = new PutObjectRequest(bucket, fileNameFormat, byteArrayIs, objMeta);
                s3Client.putObject(putObjReq);

                AccessControlList accessControlList = s3Client.getObjectAcl(bucket, fileNameFormat);
                accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
                s3Client.setObjectAcl(bucket, fileNameFormat, accessControlList);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return s3Client.getUrl(bucket, fileNameFormat).toString();

        } else {

            throw new FileNotExistException("File is not Exist");

        }

    }

    public String deleteAfterUploadModifyFile(String file, String existFile, MultipartFile modifyFile, int storeNo, String directoryPath) {

        if (StringUtils.isBlank(existFile)) {
            if (modifyFile.isEmpty()) {
                return null;
            } else {
                return uploadProductImg(modifyFile, storeNo, directoryPath);
            }
        } else {
            if (modifyFile.isEmpty()) {
                if (StringUtils.isNotBlank(file) && file.equals(existFile)) {
                    return existFile;
                } else {
                    deleteFile(existFile);
                    return null;
                }
            } else {
                deleteFile(existFile);
                return uploadProductImg(modifyFile, storeNo, directoryPath);
            }
        }
    }

    public void deleteFile(String fileUrl) {

        if (!fileUrl.contains(s3Url)) throw new FileUrlException(FileConstant.FILE_URL_VALID);

        fileUrl = fileUrl.replace(s3Url, "");

        s3Client.deleteObject(bucket, fileUrl);
    }

    public void deleteFileList(List<String> fileUrlList) {
        String exceptionFlag = null;
        List<String> deleteImgUrlList = new ArrayList<>();

        for (String fileUrl : fileUrlList) {

            if (!fileUrl.contains(s3Url)) exceptionFlag = FILE_URL_EXCEPTION;

            if (!StringUtils.isEmpty(exceptionFlag)) {
                if (!fileUrl.contains(s3Url)) throw new FileUrlException(FileConstant.FILE_URL_VALID);
            }

            String deleteFileUrl = fileUrl.replace(s3Url, "");
            deleteImgUrlList.add(deleteFileUrl);
        }

        for (String fileUrl : deleteImgUrlList) {
            s3Client.deleteObject(bucket, fileUrl);
        }
    }

}
