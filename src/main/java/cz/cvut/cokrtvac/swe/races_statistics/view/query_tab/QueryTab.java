package cz.cvut.cokrtvac.swe.races_statistics.view.query_tab;

import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import cz.cvut.cokrtvac.swe.races_statistics.model.jpa.ejb.SPARQLProcesor;
import cz.cvut.cokrtvac.swe.races_statistics.view.AbstractTab;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 1.2.13
 * Time: 0:20
 */
public class QueryTab extends AbstractTab {

    @Inject
    private SPARQLProcesor sparql;

    public QueryTab() {
        super("Querying");
    }

    private VerticalLayout vl = new VerticalLayout();

    private TextArea textArea = new TextArea("Insert SPARQL query");
    private Label result = new Label("Result of query...");
    private Button process = new Button("Process query");
    private ComboBox samples;

    @PostConstruct
    public void init() {
        samples = new ComboBox("Query samples", sparql.getQueriesIds());

        samples.addBlurListener(new FieldEvents.BlurListener() {
            @Override
            public void blur(FieldEvents.BlurEvent blurEvent) {
                String q = sparql.getQueries().get(samples.getValue());
                if (q != null) {
                    textArea.setValue(q);
                }
            }
        });

        vl.setSpacing(true);
        vl.setMargin(true);
        vl.addComponent(samples);
        vl.addComponent(textArea);
        vl.setSizeFull();

        textArea.setWidth("100%");
        textArea.setHeight("400px");

        vl.addComponent(process);
        process.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    String res = sparql.resolve(textArea.getValue().trim());
                    result.setValue(res);
                    Notification.show("Query was processed");
                } catch (Exception e) {
                    Notification.show("Query could not be processed, maybe syntax mistake?", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        result.setContentMode(ContentMode.PREFORMATTED);
        vl.addComponent(result);

        addComponent(vl);
    }
}
