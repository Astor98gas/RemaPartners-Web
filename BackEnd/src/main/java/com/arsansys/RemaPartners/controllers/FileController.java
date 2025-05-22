package com.arsansys.RemaPartners.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Crear directorio si no existe
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generar nombre único para el archivo
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(uploadDir, fileName);

            // Guardar archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Devolver URL del archivo
            String fileUrl = fileName;
            return ResponseEntity.ok().body(Map.of("url", fileUrl));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Path.of(uploadDir, fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Determinar el tipo de contenido basado en la extensión del archivo
                String contentType = determineContentType(fileName);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/images/lowRes/{fileName}")
    public ResponseEntity<Resource> getLowResImage(@PathVariable String fileName) {
        try {
            Path filePath = Path.of(uploadDir, fileName);

            if (Files.exists(filePath)) {
                // Determinar el tipo de contenido
                String contentType = determineContentType(fileName);

                // Convertir a versión de baja resolución
                byte[] lowResImageData = createLowResVersion(filePath.toFile());

                // Crear recurso a partir de los bytes
                ByteArrayResource resource = new ByteArrayResource(lowResImageData);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crea una versión de baja resolución de la imagen
     * 
     * @param originalImage archivo de imagen original
     * @return array de bytes de la imagen en baja resolución
     */
    private byte[] createLowResVersion(File originalImage) throws IOException {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();

        // Usar Thumbnailator para crear una versión reducida
        // - Escalar a un máximo de 800px de ancho o alto
        // - Comprimir con calidad 0.7 (70%)
        net.coobird.thumbnailator.Thumbnails.of(originalImage)
                .size(800, 800) // Tamaño máximo
                .keepAspectRatio(true) // Mantener proporciones
                .outputQuality(0.7) // Calidad de compresión (0.0-1.0)
                .outputFormat(getImageFormatName(originalImage.getName()))
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    /**
     * Obtiene el formato de la imagen a partir del nombre del archivo
     */
    private String getImageFormatName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        // Para compatibilidad con thumbnailator
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            return "JPEG";
        } else if (extension.equals("png")) {
            return "PNG";
        } else {
            return extension.toUpperCase();
        }
    }

    @GetMapping("/binary-image/{fileName}")
    public ResponseEntity<byte[]> getBinaryImage(@PathVariable String fileName) {
        try {
            Path filePath = Path.of(uploadDir, fileName);

            if (Files.exists(filePath)) {
                byte[] imageData = Files.readAllBytes(filePath);

                // Determinar el tipo de contenido basado en la extensión del archivo
                String contentType = determineContentType(fileName);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(imageData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String determineContentType(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        if (lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerCaseFileName.endsWith(".png")) {
            return "image/png";
        } else if (lowerCaseFileName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerCaseFileName.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }
}
