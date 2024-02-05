package com.example.mediumcode23.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ReadExcelCacheAsyncService {
    public void readXls(String filePath, String filename) throws IOException;
}
