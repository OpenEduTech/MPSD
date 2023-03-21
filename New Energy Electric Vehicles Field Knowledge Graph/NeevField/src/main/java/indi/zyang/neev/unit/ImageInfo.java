package indi.zyang.neev.unit;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ImageInfo {
    private int errno;
    private List<Map<String, String>> data;
}
