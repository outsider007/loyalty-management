package ru.kuznetsov.loyaltymanagement.web.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kuznetsov.loyaltymanagement.rest.utils.OperationType;
import ru.kuznetsov.loyaltymanagement.web.domain.BalanceChange;
import ru.kuznetsov.loyaltymanagement.web.repositories.BalanceChangeRepository;
import ru.kuznetsov.loyaltymanagement.web.repositories.BalanceRepository;


import java.util.List;

@SpringComponent
@UIScope
public class BalanceChangeView extends Dialog {

    private BalanceRepository balanceRepository;
    private BalanceChangeRepository balanceChangeRepository;
    private Grid<BalanceChange> balanceChangeGrid;
    private Button close;
    private HorizontalLayout actions;
    private VerticalLayout mainLayout;
    private H4 header;

    @Getter
    @Setter
    private Integer balanceId;

    @Autowired
    public BalanceChangeView(BalanceRepository balanceRepository, BalanceChangeRepository balanceChangeRepository) {
        this.balanceRepository = balanceRepository;
        this.balanceChangeRepository = balanceChangeRepository;
        this.balanceChangeGrid = new Grid<>();
        this.close = new Button("Закрыть", VaadinIcon.CLOSE.create());
        this.actions = new HorizontalLayout(close);
        this.header = new H4("История изменения баланса");
        this.mainLayout = new VerticalLayout(actions, header, balanceChangeGrid);

        add(mainLayout);
        setVisible(false);
        initElements();
    }

    public void setBalanceId(Integer balanceId) {
        List<BalanceChange> balanceChangeList = balanceChangeRepository.findByBalanceId(balanceId);
        balanceChangeGrid.setItems(balanceChangeList);
    }

    private void initElements() {
        configureElements();
        initListeners();
    }

    private void configureElements() {
        setWidth("1200px");
        setHeight("900px");
        balanceChangeGrid.setHeight("730px");
        mainLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, actions);
        mainLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, header);
        balanceChangeGrid.addColumn(BalanceChange::getBalanceId).setHeader("id").setId("id");
        balanceChangeGrid.addColumn(e -> OperationType.OPERATION_TYPES.get(e.getOperationTypeId())).setHeader("operation")
                .setId("operationType");
        balanceChangeGrid.addColumn(BalanceChange::getSumChange).setHeader("sum change").setId("sumChange");
        balanceChangeGrid.addColumn(BalanceChange::getTotalSum).setHeader("sum total").setId("sumTotal");
        balanceChangeGrid.addColumn(BalanceChange::getDateChange).setHeader("date change").setId("dateChange");
    }

    private void initListeners() {
        actions.addClickListener(e -> close());
    }
}
