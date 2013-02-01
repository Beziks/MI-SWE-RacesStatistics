package cz.cvut.cokrtvac.swe.races_statistics.view.admin_tab.upload;

import com.vaadin.ui.Upload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 31.1.13
 * Time: 17:37
 */
public class UploadReceiver implements Upload.Receiver {
    private String fileName;
    private String mtype;

    private ByteArrayOutputStream os = new ByteArrayOutputStream();

    public OutputStream receiveUpload(String filename, String mimetype) {
        fileName = filename;
        mtype = mimetype;
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                os.write(b);
            }
        };
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mtype;
    }

    public byte[] getContent(){
        return os.toByteArray();
    }

    public void reset(){
        os = new ByteArrayOutputStream();
    }


}
