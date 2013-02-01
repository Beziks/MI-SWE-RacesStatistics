package cz.cvut.cokrtvac.swe.races_statistics.view;

import com.vaadin.ui.VerticalLayout;

/**
 * Created by
 * User: Vaclav Cokrt
 * Email: beziks@gmail.com
 * Date: 31.1.13
 * Time: 12:52
 */
public abstract class AbstractTab extends VerticalLayout {
    public AbstractTab(String caption){
        super();
        setCaption(caption);
    }
}
