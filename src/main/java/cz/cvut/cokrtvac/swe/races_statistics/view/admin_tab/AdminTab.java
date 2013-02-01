package cz.cvut.cokrtvac.swe.races_statistics.view.admin_tab;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb.GenericDAO;
import cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb.OntologyCreator;
import cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb.ScoreSheetParser;
import cz.cvut.cokrtvac.swe.races_statistics.view.AbstractTab;
import cz.cvut.cokrtvac.swe.races_statistics.view.CustomWindow;
import cz.cvut.cokrtvac.swe.races_statistics.view.admin_tab.upload.SingleClickUpload;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 31.1.13
 * Time: 12:49
 */
public class AdminTab extends AbstractTab {
    @Inject
    private ScoreSheetParser parser;

    @Inject
    private GenericDAO dao;

    @Inject
    private OntologyCreator ontologyCreator;

    @Inject
    private ScoreSheetsTable scoreSheetsTable;

    private Panel uploadPanel = new Panel("Upload score sheets");
    private Panel processScoreSheets = new Panel("Process score sheets");
    private Panel generateRDF = new Panel("Generate RDF file");

    public AdminTab() {
        super("Score sheets administration");
        setSpacing(true);
    }

    @PostConstruct
    public void init() {
        addComponent(uploadPanel);
        uploadPanel.setStyleName(Runo.PANEL_LIGHT);
        addComponent(processScoreSheets);
        processScoreSheets.setStyleName(Runo.PANEL_LIGHT);
        addComponent(generateRDF);
        generateRDF.setStyleName(Runo.PANEL_LIGHT);

        initUpload();
        initTable();
        initOnto();
    }

    private void initTable() {
        VerticalLayout vl = new VerticalLayout(scoreSheetsTable);
        vl.setComponentAlignment(vl.getComponent(0), Alignment.MIDDLE_CENTER);
        vl.setMargin(true);
        vl.addComponent(new Button("Clear database data", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                dao.deleteAll();
                scoreSheetsTable.init();
                Notification.show("All parsed data deleted from DB");
            }
        }));


        processScoreSheets.setContent(vl);
    }

    private void initUpload() {
        SingleClickUpload u = new SingleClickUpload() {
            @Override
            public void onUpload(byte[] content, String filename, String mineType) {
                System.out.println("==================================================");
                System.out.println(filename);
                System.out.println(mineType);

                if (dao.addScoreSheet(filename, new String(content, Charset.forName("UTF-8")))) {
                    scoreSheetsTable.init();
                } else {
                    Notification.show("This file already exists", Notification.Type.ERROR_MESSAGE);
                }

                System.out.println("==================================================");
            }
        };
        uploadPanel.setContent(u);
    }

    private void initOnto() {
        Button gen = new Button("Generate RDF ontology", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    ontologyCreator.createOntology();
                } catch (IOException e) {
                    Notification.show("Error happened during ontology creation", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                Notification.show("Ontology created");
            }
        });

        Button showTemplate = new Button("Show RDF ontology template", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    CustomWindow w = new CustomWindow("RDF ontology template", dao.getOntologyTemplate(), true, false, true);
                    w.show();
                } catch (IOException e) {
                    Notification.show("Error happened", Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });

        Button showFilled = new Button("Show filled (generated) RDF ontology", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                    CustomWindow w = new CustomWindow("RDF ontology template", dao.getFullOntology(), true, false, true);
                    w.show();
            }
        });

        HorizontalLayout hl = new HorizontalLayout();
        hl.setMargin(true);
        hl.setSpacing(true);
        hl.addComponent(gen);

        hl.addComponent(showTemplate);
        hl.addComponent(gen);
        hl.addComponent(showFilled);

        generateRDF.setContent(hl);
    }
}
