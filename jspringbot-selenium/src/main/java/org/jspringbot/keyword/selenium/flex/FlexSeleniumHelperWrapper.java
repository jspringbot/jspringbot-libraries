package org.jspringbot.keyword.selenium.flex;

import org.apache.commons.lang.Validate;
import org.openqa.selenium.WebDriver;

public class FlexSeleniumHelperWrapper implements  FlexSelenium {

    private WebDriver driver;

    private FlexSeleniumHelper flexHelper;

    public FlexSeleniumHelperWrapper(WebDriver driver) {
        this.driver = driver;
    }

    public void setApp(String id) {
        flexHelper = new FlexSeleniumHelper(driver, id);
    }


    @Override
    public String getForSelenium(String widgetId, String propertyType) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getForSelenium(widgetId, propertyType);
    }

    @Override
    public String click(String objectId, String optionalButtonLabel) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.click(objectId, optionalButtonLabel);
    }

    @Override
    public String click(String objectId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.click(objectId);
    }

    @Override
    public boolean isVisible(String objectId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.isVisible(objectId);
    }

    @Override
    public int getNumSelectedItems(String objectId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getNumSelectedItems(objectId);
    }

    @Override
    public String getSelectedItemAtIndex(String objectId, int index) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getSelectedItemAtIndex(objectId, index);
    }

    @Override
    public void enterText(String textField, String text) {
        Validate.notNull(flexHelper, "No selected flex app");
        flexHelper.enterText(textField, text);
    }

    @Override
    public void enterDate(String dateField, String dateAsText) {
        Validate.notNull(flexHelper, "No selected flex app");
        flexHelper.enterDate(dateField, dateAsText);
    }

    @Override
    public void getDate(String dateField) {
        Validate.notNull(flexHelper, "No selected flex app");
        flexHelper.getDate(dateField);
        // bakit to void?
    }

    @Override
    public boolean isEnabled(String objectId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.isEnabled(objectId);
    }

    @Override
    public boolean isTabEnabled(String objectId, String label) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.isTabEnabled(objectId, label);
    }

    @Override
    public boolean exists(String objectId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.exists(objectId);
    }

    @Override
    public String getErrorString(String textFieldId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getErrorString(textFieldId);
    }

    @Override
    public String getText(String textFieldId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getText(textFieldId);
    }

    @Override
    public int getSelectionIndex(String selectionFieldId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getSelectionIndex(selectionFieldId);
    }

    @Override
    public String select(String selectionFieldId, String itemToSelect) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.select(selectionFieldId, itemToSelect);
    }

    @Override
    public String selectComboByLabel(String selectionFieldId, String itemToSelect) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.selectComboByLabel(selectionFieldId, itemToSelect);
    }

    @Override
    public String selectMatchingOnField(String selectionFieldId, String underlyingField, String underlyingValue) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.selectMatchingOnField(selectionFieldId, underlyingField, underlyingValue);
    }

    @Override
    public void addSelectMatchingOnField(String selectionFieldId, String underlyingField, String underlyingValue) {
        Validate.notNull(flexHelper, "No selected flex app");
        flexHelper.addSelectMatchingOnField(selectionFieldId, underlyingField, underlyingValue);
    }

    @Override
    public void selectIndex(String selectionFieldId, int index) {
        Validate.notNull(flexHelper, "No selected flex app");
        flexHelper.selectIndex(selectionFieldId, index);
    }

    @Override
    public String getProperty(String objectId, String property) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getProperty(objectId, property);
    }

    @Override
    public String getDataGridValue(String dataGridId, int row, int col) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getDataGridValue(dataGridId, row, col);
    }

    @Override
    public int getFlexDataGridRowIndexForFieldValue(String dataGridId, String field, String value) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getFlexDataGridRowIndexForFieldValue(dataGridId, field, value);
    }

    @Override
    public int getFlexDataGridRowIndexForFieldLabel(String dataGridId, String field, String label) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getFlexDataGridRowIndexForFieldLabel(dataGridId, field, label);
    }

    @Override
    public String getFlexDataFieldValueForGridRow(String dataGridId, String field, int row) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getFlexDataFieldValueForGridRow(dataGridId, field, row);
    }

    @Override
    public String getFlexDataFieldLabelForGridRow(String dataGridId, String field, int row) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getFlexDataFieldLabelForGridRow(dataGridId, field, row);
    }

    @Override
    public int getFlexDataGridRowCount(String dataGridId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getFlexDataGridRowCount(dataGridId);
    }

    @Override
    public boolean isAlertVisible() {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.isAlertVisible();
    }

    @Override
    public String clickAlert(String alertButton) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.clickAlert(alertButton);
    }

    @Override
    public String selectCheckbox(String checkBoxId, boolean value) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.selectCheckbox(checkBoxId, value);
    }

    @Override
    public boolean isCheckboxChecked(String checkBoxId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.isCheckboxChecked(checkBoxId);
    }

    @Override
    public String getComboBoxSelectedItem(String comboBoxId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.getComboBoxSelectedItem(comboBoxId);
    }

    @Override
    public boolean isLabelInComboData(String comboBoxId, String expectedLabel) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.isLabelInComboData(comboBoxId, expectedLabel);
    }

    @Override
    public String setFocus(String widgetId) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.setFocus(widgetId);
    }

    @Override
    public String doFlexAlertResponse(String response) {
        Validate.notNull(flexHelper, "No selected flex app");
        return flexHelper.doFlexAlertResponse(response);
    }

    @Override
    public void waitForPageToLoad(long timeout, long pollTime) {
        Validate.notNull(flexHelper, "No selected flex app");
        flexHelper.waitForPageToLoad(timeout, pollTime);
    }
}
