package cz.cvut.cokrtvac.swe.races_statistics.view.admin_tab;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import cz.cvut.cokrtvac.swe.races_statistics.model.entity.RaceVolumeEntity;
import cz.cvut.cokrtvac.swe.races_statistics.model.entity.ScoreSheetEntity;
import cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb.GenericDAO;
import cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb.ScoreSheetParser;
import cz.cvut.cokrtvac.swe.races_statistics.view.CustomWindow;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 31.1.13
 * Time: 19:22
 */
public class ScoreSheetsTable extends VerticalLayout {
    private Table t;

    @Inject
    private GenericDAO dao;

    @Inject
    private ScoreSheetParser parser;

    public ScoreSheetsTable() {
    }

    @PostConstruct
    public void init() {
        removeAllComponents();

        Table t = new Table("Uploaded score sheets");
        addComponent(t);
        setComponentAlignment(t, Alignment.MIDDLE_CENTER);

        t.addContainerProperty("Name", String.class, null);
        t.addContainerProperty("Preview", String.class, null);
        t.addContainerProperty("Detail", Button.class, null);
        t.addContainerProperty("Process (parse and save to DB)", Button.class, null);
        t.addContainerProperty("Delete file", Button.class, null);

        for (ScoreSheetEntity e : dao.getAllScoreSheets()) {
            final ScoreSheetEntity ee = e;
            String prev = e.getContent();
            if (e.getContent().length() > 100) {
                prev = prev.substring(0, 100) + "...";
            }

            Button show = new Button("Show content");
            show.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    Label label = new Label(ee.getContent());
                    label.setContentMode(ContentMode.PREFORMATTED);
                    VerticalLayout vl = new VerticalLayout(label);
                    vl.setMargin(true);

                    CustomWindow w = new CustomWindow("Content of " + ee.getId(), vl, false, true);
                    w.show();
                }
            });

            Button process = new Button("Process file");
            process.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    try {
                        parser.parseScoreSheet(ee.getId());
                    } catch (Exception e1) {
                        Notification.show("Cannot be parsed", Notification.Type.ERROR_MESSAGE);
                    }
                    Notification.show("Processed successfully");
                    init();
                }
            });

            Button delete = new Button("Delete");
            delete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    try {
                        dao.remove(ee);
                        Notification.show("File was deleted");
                    } catch (Exception e1) {
                        Notification.show("Something is wrong", Notification.Type.ERROR_MESSAGE);
                    }
                    init();
                }
            });

            for (RaceVolumeEntity r : parser.getAll(RaceVolumeEntity.class)) {
                if (ee.getContent().contains("class=\"volume\">" + r.getVolume() + ". ročník") && ee.getContent().contains("<h1>" + r.getRace().getName())) {
                    process.setEnabled(false);
                }
            }

            System.out.println(t.addItem(new Object[]{e.getId(), prev, show, process, delete}, e.getId()));
        }
    }


}
