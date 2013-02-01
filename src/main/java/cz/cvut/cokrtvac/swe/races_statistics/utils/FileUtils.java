package cz.cvut.cokrtvac.swe.races_statistics.utils;

import java.io.*;
import java.nio.charset.Charset;

public class FileUtils {

    public static String readFile(String path) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader br = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), Charset.forName("utf-8"));
            br = new BufferedReader(isr);

            String s = br.readLine();
            while (s != null) {
                out.append(s + "\n");
                s = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Cannot read file " + path);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
        return out.toString();
    }

    public static String readOnClasspath(String path) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader br = null;

        try {
            InputStream is = FileUtils.class.getResourceAsStream(path);
            br = new BufferedReader(new InputStreamReader(is));

            String s = br.readLine();
            while (s != null) {
                out.append(s + "\n");
                s = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Cannot read file " + path);
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return out.toString();

    }

}
