package vn.hust.api.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hust.api.service.DriveService;
import vn.hust.common.Common;
import vn.hust.common.exception.BadRequestException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class DriveServiceImpl implements DriveService {

    @Data
    public static class Res {
        private int status;
        private String message;
        private String url;
    }

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoogleCredentials();
    private static final String SUPER_PARENT_FOLDER = "1Ee0uBdPeSvDuuwyOXdIsq78vY-llhyBm";

    private static String getPathToGoogleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "google-cred.json");
        return filePath.toString();
    }

    @Override
    public String createFolder(String folderName) {
        try {
            Drive drive = createDriveService();

            // If folder doesn't exist, create it
            File fileMetaData = new File();
            fileMetaData.setParents(Collections.singletonList(SUPER_PARENT_FOLDER));
            fileMetaData.setName(folderName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            File folder = drive.files().create(fileMetaData)
                    .setFields("id")
                    .execute();
            return folder.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Res uploadImageToDrive(MultipartFile file, String parentFolder) {
        Res res = new Res();

        try {
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty!");
            }

            java.io.File tempFile = java.io.File.createTempFile("temp", null);
            file.transferTo(tempFile);

            Drive drive = createDriveService();

            File fileMetaData = new File();
            fileMetaData.setName(tempFile.getName());
            fileMetaData.setParents(Collections.singletonList(Common.defaultIfNullOrEmpty(parentFolder, SUPER_PARENT_FOLDER)));

            FileContent mediaContent = switch (Objects.requireNonNull(file.getContentType())) {
                case "image/png" -> new FileContent("image/png", tempFile);
                case "image/jpeg" -> new FileContent("image/jpeg", tempFile);
                default ->
                        throw new IllegalStateException("Unexpected value: " + Objects.requireNonNull(file.getContentType()));
            };
            File uploadedFile = drive.files().create(fileMetaData, mediaContent)
                    .setFields("id").execute();
            String imageUrl = "https://drive.google.com/thumbnail?id=" + uploadedFile.getId();
            System.out.println("IMAGE URL: " + imageUrl);
            if (tempFile.delete()) {
                res.setStatus(200);
                res.setMessage("Image Successfully Uploaded To Drive");
                res.setUrl(imageUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(500);
            res.setMessage(e.getMessage());
        }
        return res;

    }

    private Drive createDriveService() throws IOException {

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
//        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
//                .createScoped(List.of(DriveScopes.DRIVE_FILE));
//        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
//                credentials);

        return new Drive.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
                .setApplicationName("vitech")
                .build();
    }

    @Override
    public String checkFolderExists(String folderName) {
        try {
            Drive drive = createDriveService();
            String query = "mimeType='application/vnd.google-apps.folder' and name='" + folderName + "'";
            FileList result = drive.files().list()
                    .setQ(query)
                    .setSpaces("drive")
                    .setFields("files(id)")
                    .execute();
            List<File> files = result.getFiles();
            if (files != null && !files.isEmpty()) {
                return files.get(0).getId();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteFolder(String folderId) {
        try {
            Drive drive = createDriveService();

            drive.files().delete(folderId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteImage(String fileId) {
        try {
            Drive drive = createDriveService();

            drive.files().delete(fileId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
