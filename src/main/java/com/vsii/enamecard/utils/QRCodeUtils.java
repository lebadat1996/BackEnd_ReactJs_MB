package com.vsii.enamecard.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.core.io.ByteArrayResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class QRCodeUtils {

    private QRCodeUtils(){

    }

    public static BufferedImage generateEAN13BarcodeImage(String barcodeText,int width,int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix matrix = qrCodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height);

        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    public static ByteArrayResource toByteArrayAutoClosable(BufferedImage image, String type){
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(image, type, out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
