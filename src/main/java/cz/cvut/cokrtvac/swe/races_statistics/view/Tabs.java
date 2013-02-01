package cz.cvut.cokrtvac.swe.races_statistics.view;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import cz.cvut.cokrtvac.swe.races_statistics.view.admin_tab.AdminTab;
import cz.cvut.cokrtvac.swe.races_statistics.view.query_tab.QueryTab;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 31.1.13
 * Time: 12:47
 */
public class Tabs extends HorizontalLayout {
    private TabSheet tabSheet;

    @Inject
    private AdminTab adminTab;

    @Inject
    private QueryTab queryTab;

    @PostConstruct
    public void init(){
        setSizeFull();
        setMargin(true);

        tabSheet = new TabSheet(adminTab, queryTab);
        tabSheet.setSizeFull();
        addComponent(tabSheet);

    }
}
