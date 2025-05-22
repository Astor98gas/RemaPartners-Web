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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

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

    private byte[] createLowResVersion(File originalImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Leer la imagen original
        BufferedImage original = ImageIO.read(originalImage);

        // Obtener dimensiones originales
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();

        // Calcular nuevas dimensiones manteniendo la proporción
        int targetWidth = 800;
        int targetHeight = 800;

        if (originalWidth > originalHeight) {
            targetHeight = (int) (originalHeight * ((double) targetWidth / originalWidth));
        } else {
            targetWidth = (int) (originalWidth * ((double) targetHeight / originalHeight));
        }

        // Crear versión redimensionada
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g.dispose();

        // Guardar con compresión JPEG
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam params = writer.getDefaultWriteParam();

        // Configurar la compresión (0.0f - 1.0f)
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(0.7f);

        // Escribir la imagen comprimida al stream
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(imageOutputStream);
        writer.write(null, new IIOImage(resizedImage, null, null), params);
        writer.dispose();
        imageOutputStream.close();

        return outputStream.toByteArray();
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
