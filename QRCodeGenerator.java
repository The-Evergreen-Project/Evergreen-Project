import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    public static void main(String[] args) {
        // Replace with your actual Google Drive PDF link (make sure it's shareable!)
        String fileId = "YOUR_FILE_ID_HERE"; 
        String pdfUrl = "" + fileId;

        int width = 300;
        int height = 300;
        String filePath = "pdf_qrcode.png";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(pdfUrl, BarcodeFormat.QR_CODE, width, height);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            System.out.println("QR Code saved to: " + filePath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}
