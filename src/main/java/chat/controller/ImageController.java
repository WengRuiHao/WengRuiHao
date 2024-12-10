package chat.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import chat.response.ApiResponse;

@RestController
@RequestMapping("/file")
public class ImageController {

	private static final String BASE_DIR = "uploads/";
	
    // 初始化目錄
    static {
        try {
            Files.createDirectories(Paths.get(BASE_DIR));
        } catch (IOException e) {
            throw new RuntimeException("初始化目錄失敗：" + e.getMessage());
        }
    }

	@PutMapping("/ImageUpload")
	public ResponseEntity<ApiResponse<?>> uploadImage(@RequestParam("File") MultipartFile file) {
		String fileName = file.getOriginalFilename();
		
		try {
			Path folderPath = Paths.get(BASE_DIR);
			Path filePath = folderPath.resolve(fileName);
			 Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			 return ResponseEntity.ok(ApiResponse.success("檔案已成功上傳至：" + filePath.toAbsolutePath(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "檔案上傳失敗：" + e.getMessage()));
		}
	}
}
