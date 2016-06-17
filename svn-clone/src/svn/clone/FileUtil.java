package svn.clone;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

public class FileUtil {

    /**
     * 获取文件实际占用的空间
     * @param file
     * @return
     */
    public static long getFileSpace(File file) {
        long size = 0;
        if (file == null || !file.exists()) {
            return size;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                size += getFileSpace(listFile);
            }
        } else {
            size += file.length();
        }
        return size;
    }

    /**
     * 猎取取目录文件个数
     * @param f
     * @return
     */
    public long getSubFileSize(File f) {
        long size = 0;
        if (f == null || !f.isDirectory()) {
            return size;
        }
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getSubFileSize(flist[i]);
                size--;
            }
        }
        return size;
    }

    /** 
     *  根据路径删除指定的目录或文件，无论存在与否 
     *@param sPath  要删除的目录或文件 
     *@return 删除成功返回 true，否则返回 false。 
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在  
        if (!file.exists()) { // 不存在返回 false  
            return flag;
        } else {
            // 判断是否为文件  
            if (file.isFile()) { // 为文件时调用删除文件方法  
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法  
                return deleteDirectory(sPath);
            }
        }
    }

    /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件  
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } //删除子目录  
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        //删除当前目录  
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**  
     * 根据文件的扩展名来获取对应的“输出流的HTTP MIME“类型
     *
     * @param filename
     * @return
     */
    public static String getMimeType(String filename) {
        String ContentType = "text/plain";
        if (filename == null || "".equals(filename) || filename.indexOf(".") < 0) {
            return ContentType;
        }
        switch (filename.substring(filename.lastIndexOf(".")).trim().toLowerCase()) {
            case ".css":
                break;
            case ".js":
                break;
            case ".vm":
                break;
            case ".java":
                break;
            case ".xml":
                break;
            case ".properties":
                break;
            case ".html":
                ContentType = "text/html";
                break;
            case ".htm":
                ContentType = "text/html";
                break;
            case ".gif":
                ContentType = "image/gif";
                break;
            case ".jpg":
                ContentType = "image/jpeg";
                break;
            case ".png":
                ContentType = "image/png";
                break;
            case "jpeg":
                ContentType = "image/jpeg";
                break;
            case ".xls":
                ContentType = "application/vnd.ms-excel";
                break;
            case ".wav":
                ContentType = "audio/wav";
                break;
            case ".mp3":
                ContentType = "audio/mpeg3";
                break;
            case ".mpg":
                ContentType = "video/mpeg";
                break;
            case ".mepg":
                ContentType = "video/mpeg";
                break;
            case ".rtf":
                ContentType = "application/rtf";
                break;
            case ".txt":
                break;
            case ".asf":
                ContentType = "video/x-ms-asf";
                break;
            case ".avi":
                ContentType = "video/avi";
                break;
            case ".doc":
                ContentType = "application/msword";
                break;
            case ".zip":
                ContentType = "application/zip";
                break;
            default:
                ContentType = "application/octet-stream";
                break;
        }
        return ContentType;
    }

    /**
     * 把文件写入到输出流中
     * @param file
     * @param outputStream
     */
    public static void write(File file, OutputStream outputStream) {
        if (file == null || outputStream == null) {
            return;
        }
        if (file.exists() && file.isFile()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                byte[] b = new byte[1024];
                while (fileInputStream.read(b) > -1) {
                    outputStream.write(b);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("E:\\My Photos");
        file.isDirectory();
        System.out.println(file.length());
        System.out.println(getFileSpace(file));
        File[] list = file.listFiles();
        for (File dir : list) {
            System.out.println(dir.length());
            System.out.println(dir.getName());
        }
        System.out.println(file.getParent());
        try {
            byte[] bytes = "苛2 ".getBytes("UTF-8");
            String x = new String(bytes, "ISO-8859-1");
            System.out.println(URLDecoder
                .decode("http://localhost:9097/project/Yunjee-Asserts/ja%20rib%E5%90%8C", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String REGEX = "([0-9].)*";
        System.out.println(Pattern.matches("970", REGEX));
        System.out.println(Pattern.matches("9.1.2.", REGEX));
    }
}
