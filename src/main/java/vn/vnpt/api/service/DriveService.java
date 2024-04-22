package vn.vnpt.api.service;

import org.springframework.web.multipart.MultipartFile;
import vn.vnpt.api.service.impl.DriveServiceImpl;

public interface DriveService {
    String createFolder(String folderName);

    DriveServiceImpl.Res uploadImageToDrive(MultipartFile file, String parentFolder) ;

    String checkFolderExists(String folderName) ;

    void deleteFolder(String folderId);

    void deleteImage(String fileId);
}
