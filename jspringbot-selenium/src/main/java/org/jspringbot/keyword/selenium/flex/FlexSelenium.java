package org.jspringbot.keyword.selenium.flex;

public interface FlexSelenium {

    String getForSelenium(String widgetId, String propertyType);

    String click(String objectId, String optionalButtonLabel);

    String click(String objectId);

    boolean isVisible(String objectId);

    int getNumSelectedItems(String objectId);

    String getSelectedItemAtIndex(String objectId, int index);

    void enterText(String textField, String text);

    void enterDate(String dateField, String dateAsText);

    void getDate(String dateField);

    boolean isEnabled(String objectId);

    boolean isTabEnabled(String objectId, String label);

    boolean exists(String objectId);

    String getErrorString(String textFieldId);

    String getText(String textFieldId);

    int getSelectionIndex(String selectionFieldId);

    String select(String selectionFieldId, String itemToSelect);

    String selectComboByLabel(String selectionFieldId, String itemToSelect) ;

    String selectMatchingOnField(String selectionFieldId, String underlyingField, String underlyingValue);

    void addSelectMatchingOnField(String selectionFieldId, String underlyingField, String underlyingValue);

    void selectIndex(String selectionFieldId, int index);

    String getProperty(String objectId, String property);

    String getDataGridValue(String dataGridId, int row, int col);

    int getFlexDataGridRowIndexForFieldValue(String dataGridId, String field, String value);

    int getFlexDataGridRowIndexForFieldLabel(String dataGridId, String field, String label);

    String getFlexDataFieldValueForGridRow(String dataGridId, String field, int row);

    String getFlexDataFieldLabelForGridRow(String dataGridId, String field, int row);

    int getFlexDataGridRowCount(String dataGridId);

    boolean isAlertVisible();

    String clickAlert(String alertButton);

    String selectCheckbox(String checkBoxId, boolean value);

    boolean isCheckboxChecked(String checkBoxId);

    String getComboBoxSelectedItem(String comboBoxId);

    boolean isLabelInComboData(final String comboBoxId, final String expectedLabel);

    String setFocus(final String widgetId);

    String doFlexAlertResponse(final String response);

    void waitForPageToLoad(long timeout, long pollTime) ;
}
