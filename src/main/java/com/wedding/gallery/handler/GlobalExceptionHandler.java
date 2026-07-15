package com.wedding.gallery.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("⚠️ Validasi gagal: {}", errors);

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("Validasi data gagal, mohon periksa kembali inputan Anda.")
                .data(errors) // Detail field yang error dikirim ke ReactJS
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 2. MENANGKAP ATURAN BISNIS APLIKASI (Custom BusinessException)
     * Ini yang menangkap kalau kuota upload foto tamu sudah mencapai maksimal 10.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        log.warn("⚠️ Pelanggaran aturan aplikasi: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error(ex.getMessage())
        );
    }

    /**
     * 3. BUMPER UTAMA: MENANGKAP SEMUA ERROR SERVER (500 Internal Server Error)
     * Dipakai jika database mati, network VPS bermasalah, atau token Cloudflare R2 bermasalah.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllUncaughtExceptions(Exception ex) {
        // Detail stack trace error lengkap tetap aman dicatat oleh ExceptionAuditAspect kamu di file log VPS
        log.error("💥 Server Error terdeteksi: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Terjadi kesalahan pada server. Mohon coba beberapa saat lagi.")
        );
    }
}