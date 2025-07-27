package downloader;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
    private final WebDriver driver;
    private String downloadDirectory;

    public Downloader(WebDriver driver) {
        this.driver = driver;
    }

    public Downloader setDownloadPath(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
        return this;
    }

    public String startDownload(WebElement link) {
        HttpURLConnection connection = null;  // For resource cleanup
        try {
            String fileURL = link.getAttribute("href");

            // ---- Step 1: Get cookies from Selenium session ----
            Set<Cookie> cookies = driver.manage().getCookies();
            StringBuilder cookieHeader = new StringBuilder();
            for (Cookie cookie : cookies) {
                cookieHeader.append(cookie.getName()).append("=")
                        .append(cookie.getValue()).append("; ");
            }

            // ---- Step 2: Get User-Agent from the browser ----
            String userAgent = (String) ((JavascriptExecutor) driver)
                    .executeScript("return navigator.userAgent;");

            // ---- Step 3: Open HTTP connection ----
            URL url = new URL(fileURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Cookie", cookieHeader.toString());
            connection.setRequestProperty("User-Agent", userAgent);
            connection.connect();

            // ---- Step 4: Determine filename ----
            String fileName = extractFileName(connection, url);

            // ---- Step 5: Handle null downloadDirectory ----
            if (downloadDirectory == null || downloadDirectory.trim().isEmpty()) {
                downloadDirectory = System.getProperty("user.dir") + File.separator + "downloads";
            }

            // Ensure directory exists
            File dir = new File(downloadDirectory);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Could not create directory: " + downloadDirectory);
            }

            // ---- Step 6: Avoid overwriting existing files ----
            File targetFile = getUniqueFileName(new File(dir, fileName));

            // ---- Step 7: Download and save file ----
            try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                 FileOutputStream out = new FileOutputStream(targetFile)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            String downloadedFilePath = targetFile.getAbsolutePath();
            System.out.println("✅ File downloaded to: " + downloadedFilePath);
            return downloadedFilePath;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect(); // ✅ Resource cleanup
            }
        }
        return null;
    }

    private String extractFileName(HttpURLConnection connection, URL url) throws IOException {
        String contentDisposition = connection.getHeaderField("Content-Disposition");
        String fileName = null;

        if (contentDisposition != null && contentDisposition.contains("filename")) {
            Matcher matcher = Pattern.compile("filename\\*=UTF-8''(.+)|filename=\"?([^\";]+)\"?")
                    .matcher(contentDisposition);
            if (matcher.find()) {
                fileName = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            }
        }

        // Fallback: use last URL path segment
        if (fileName == null || fileName.trim().isEmpty()) {
            String path = url.getPath();
            fileName = path.substring(path.lastIndexOf('/') + 1);
            if (fileName.isEmpty()) {
                fileName = "downloaded_file";
            }
        }
        return fileName;
    }

    private File getUniqueFileName(File file) {
        if (!file.exists()) {
            return file;
        }

        String name = file.getName();
        String baseName;
        String extension = "";

        int dotIndex = name.lastIndexOf(".");
        if (dotIndex > 0) {
            baseName = name.substring(0, dotIndex);
            extension = name.substring(dotIndex);
        } else {
            baseName = name;
        }

        int counter = 1;
        File newFile;
        do {
            newFile = new File(file.getParent(), baseName + "(" + counter + ")" + extension);
            counter++;
        } while (newFile.exists());

        return newFile;
    }
}
