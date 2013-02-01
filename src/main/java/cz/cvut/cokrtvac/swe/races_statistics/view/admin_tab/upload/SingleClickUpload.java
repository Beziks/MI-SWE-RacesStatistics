package cz.cvut.cokrtvac.swe.races_statistics.view.admin_tab.upload;

import com.vaadin.ui.*;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 31.1.13
 * Time: 17:42
 */
public abstract class SingleClickUpload extends VerticalLayout {
    private Label status = new Label(messageIntro());
    private ProgressIndicator pi = new ProgressIndicator();
    private UploadReceiver receiver = new UploadReceiver();
    private HorizontalLayout progressLayout = new HorizontalLayout();
    private Upload upload = new Upload(null, receiver);

    public SingleClickUpload() {
        setSpacing(true);
        setMargin(true);

        status.setSizeUndefined();

        addComponent(status);
        addComponent(upload);
        addComponent(progressLayout);

        setWidth(100, Unit.PERCENTAGE);

        setComponentAlignment(status, Alignment.MIDDLE_CENTER);
        setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
        setComponentAlignment(progressLayout, Alignment.MIDDLE_CENTER);

        // Make uploading start immediately when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption(messageButton());

        progressLayout.setSpacing(true);
        progressLayout.setVisible(false);
        progressLayout.addComponent(pi);
        progressLayout.setComponentAlignment(pi, Alignment.MIDDLE_CENTER);

        final Button cancelProcessing = new Button("Cancel");
        cancelProcessing.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                upload.interruptUpload();
            }
        });
        cancelProcessing.setStyleName("small");
        progressLayout.addComponent(cancelProcessing);

        /**
         * =========== Add needed listener for the upload component: start,
         * progress, finish, success, fail ===========
         */

        upload.addStartedListener(new Upload.StartedListener() {
            public void uploadStarted(Upload.StartedEvent event) {
                // This method gets called immediatedly after upload is started
                upload.setVisible(false);
                progressLayout.setVisible(true);
                pi.setValue(0f);
                pi.setPollingInterval(500);
                status.setValue(messageUploading());
            }
        });

        upload.addProgressListener(new Upload.ProgressListener() {
            public void updateProgress(long readBytes, long contentLength) {
                pi.setValue(new Float(readBytes / (float) contentLength));
            }

        });

        upload.addSucceededListener(new Upload.SucceededListener() {
            public void uploadSucceeded(Upload.SucceededEvent event) {
                setMessage(messageSuccess());
                onUpload(receiver.getContent(), receiver.getFileName(), receiver.getMimeType());
            }
        });

        upload.addFailedListener(new Upload.FailedListener() {
            public void uploadFailed(Upload.FailedEvent event) {
                setMessage(messageInterruption(), Notification.Type.WARNING_MESSAGE);
            }
        });

        upload.addFinishedListener(new Upload.FinishedListener() {
            public void uploadFinished(Upload.FinishedEvent event) {
                progressLayout.setVisible(false);
                upload.setVisible(true);
                status.setValue(messageNextIntro());
                receiver.reset();
            }
        });
    }

    public abstract void onUpload(byte[] content, String filename, String mineType);

    protected void setMessage(String message) {
       setMessage(message, Notification.Type.HUMANIZED_MESSAGE);
    }

    protected void setMessage(String message, Notification.Type type) {
        status.setValue(message);
        Notification.show(message, type);
    }

    protected String messageIntro(){
        return "Please select a file to upload";
    }

    protected String messageNextIntro(){
        return "Select another file";
    }

    protected String messageButton(){
        return "Select file";
    }

    protected String messageInterruption(){
        return "Uploading interrupted";
    }

    protected String messageSuccess(){
        return "Uploading file succeeded";
    }

    protected String messageUploading(){
        return "Uploading message...";
    }






}
