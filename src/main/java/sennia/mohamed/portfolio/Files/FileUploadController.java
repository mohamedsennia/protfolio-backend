package sennia.mohamed.portfolio.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "https://senniamohamed.netlify.app/"

        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        })
@RestController
@RequestMapping("/api/upload")

public class FileUploadController {
    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<CostumeFile> uploadFile(@RequestParam("file") MultipartFile file) {


        try {
            String fileName = file.getOriginalFilename();
            String absoluteUploadDir = new File(uploadDir).getAbsolutePath();
            System.out.println( absoluteUploadDir+"   ");
            File directory = new File(absoluteUploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File destFile = new File(absoluteUploadDir + File.separator + fileName);

            file.transferTo(destFile);

            String[] path=uploadDir.split("/");

            CostumeFile costumeFile=new CostumeFile( path[path.length-1]+"/"+fileName);

            return new ResponseEntity<>(costumeFile, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }


}
