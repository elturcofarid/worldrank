package com.worldrank.app.publicacion.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class ImageMetadataService {

    public double[] extractGpsCoordinates(byte[] imageBytes) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageBytes));
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (gpsDirectory != null) {
            Double latitude = gpsDirectory.getGeoLocation().getLatitude();
            Double longitude = gpsDirectory.getGeoLocation().getLongitude();
            if (latitude != null && longitude != null) {
                return new double[]{longitude, latitude}; // Note: longitude first for consistency with request
            }
        }
        return null;
    }
}