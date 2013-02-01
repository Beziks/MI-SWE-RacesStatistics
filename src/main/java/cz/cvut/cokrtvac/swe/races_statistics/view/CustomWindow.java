package cz.cvut.cokrtvac.swe.races_statistics.view;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 31.1.13
 * Time: 20:34
 */
public class CustomWindow extends Window {
    public CustomWindow(String caption, Component content, boolean modal, boolean close) {
        init(caption, content, modal, close);
    }

    public CustomWindow(String caption, String content, boolean preformatedText, boolean modal, boolean close) {
        Label label = new Label(content);
        if(preformatedText){
              label.setContentMode(ContentMode.PREFORMATTED);
        }

        VerticalLayout vl = new VerticalLayout(label);
        label.setSizeUndefined();

        vl.setSizeFull();
        vl.setSpacing(true);
        vl.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

        init(caption, vl, modal, close);
    }

    protected void init(String caption, Component content, boolean modal, boolean close){
        setCaption(caption);
        setContent(content);
        setModal(modal);
        final VerticalLayout vl = new VerticalLayout();
        vl.addComponent(content);
        vl.setExpandRatio(content, 1f);

        setClosable(close);
        if (close) {
            vl.addComponent(new Button("Close", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    hide();
                }
            }));
        }

        setContent(vl);

        setHeight("500px");
        setWidth("800px");
        setResizable(true);
    }

    public void show() {
        setVisible(true);
        if (getParent() != null) {
            return;
        }
        MainUI.instance().addWindow(this);
    }

    public void hide() {
        setVisible(false);
        if (getParent() == null) {
            return;
        }
        removeFromParent(this);
    }

    private static CustomWindow progressWindow = null;

    public static void showProgressWindow() {
        if (progressWindow == null) {
            VerticalLayout vl = new VerticalLayout();
            Label l = new Label("I am working, please wait...");
            l.setSizeUndefined();
            ProgressIndicator ind = new ProgressIndicator();

            vl.addComponent(l);
            vl.addComponent(ind);
            vl.setComponentAlignment(l, Alignment.MIDDLE_CENTER);
            vl.setComponentAlignment(ind, Alignment.MIDDLE_CENTER);
            vl.setSizeFull();
            vl.setSpacing(true);

            progressWindow = new CustomWindow("Working...", vl, true, false);
            progressWindow.setWidth("300px");
            progressWindow.setHeight("150px");
        }

        progressWindow.show();
    }

    public static void hideProgressWindow() {
        progressWindow.hide();
    }


}
