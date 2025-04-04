package com.arsansys.RemaPartners.models.firebase;

import java.util.Map;
import lombok.Data;

@Data
public class Note {
    private String subject;
    private String content;
    private String token;
    private String imageUrl;
    private Map<String, String> data;
}
