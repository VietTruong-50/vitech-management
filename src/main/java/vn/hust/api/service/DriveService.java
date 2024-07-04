package vn.hust.api.service;

import org.springframework.web.multipart.MultipartFile;
import vn.hust.api.service.impl.DriveServiceImpl;

public interface DriveService {
    String createFolder(String folderName);

    DriveServiceImpl.Res uploadImageToDrive(MultipartFile file, String parentFolder) ;

    String checkFolderExists(String folderName) ;

    void deleteFolder(String folderId);

    void deleteImage(String fileId);

    void renameFolder(String name, String folderId);
}
