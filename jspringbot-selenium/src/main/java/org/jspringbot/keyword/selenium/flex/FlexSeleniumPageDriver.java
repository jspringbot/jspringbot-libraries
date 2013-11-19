package org.jspringbot.keyword.selenium.flex;

import java.util.Calendar;

public interface FlexSeleniumPageDriver {

    String getForSelenium(final String widgetId, final String propertyType);

    void clickButton(final String widgetId);

    void closeAlert(final String response);

    void ensureAlertVisibility(final boolean visible);

    void ensureCheckBoxState(final String widgetId, final boolean expectedState);

    void ensureComboContainsValue(final String widgetId, final String value);

    void ensureComboNoSelectedItem(final String widgetId);

    void ensureComboSelectedItem(final String widgetId, final String value);

    void ensureComboSelectedItemNotAvailable(final String widgetId, final String value);

    void ensureDataGridPopulated(final String widgetId);

    void ensureDataGridRowCount(final String widgetId, final int expectedRowCount);

    void ensureDataGridRowCountChanged(final String widgetId, final int initialRowCount);

    void ensureDataGridRowLabel(final String widgetId, final int row, final String property, final String expectedValue);

    // replicate
    void ensureDataGridRowValue(final String widgetId, final int row, final int column, final String expectedValue);

    void ensureDataGridRowValue(final String widgetId, final int row, final String property, final String expectedValue);

    void ensureDataGridRowVisible(final String widgetId, final String property, final String value, final boolean visible);

    void ensureDataGridRowWithLabelVisible(final String widgetId, final String property, final String label, final boolean visible);

    void ensureSelectedListItems(final String widgetId, final String[] expectedItems);

    void ensureTabEnabled(final String widgetId, final String tabLabel, final boolean enabled);

    // replicate
    void ensureWidgetEnabled(final String widgetId, final boolean enabledState);

    void ensureWidgetEnabled(final String widgetId, final boolean enabledState, final long timeoutMillis);

    void ensureWidgetErrorString(final String widgetId, final String expectedMessage);

    void ensureWidgetHasDate(final String widgetId, final Calendar date, final String format);

    void ensureWidgetHasMatchingText(final String widgetId, final String regularExpression);

    void ensureWidgetHasText(final String widgetId, final String expectedText);

    void ensureWidgetPropertySet(final String widgetId, final String property);

    void ensureWidgetTextChanged(final String widgetId, final String initialValue);

    void ensureWidgetTextNotBlank(final String widgetId);

    void ensureWidgetVisibility(final String widgetId, final boolean visibility);

    //replicate
    void ensureWidgetVisibility(final String widgetId, final boolean visibility, final long timeoutMillis);

    void enterDate(final String widgetId, final Calendar date, final String format);

    void enterText(final String widgetId, final String text);

    int getDataGridRowCount(final String widgetId);

    int getDataGridRowIndex(final String widgetId, final String field, final String value);

    int getDataGridRowIndexByLabel(final String widgetId, final String field, final String value);

    String getDataGridRowValue(final String widgetId, final String field, final int row);

    String getWidgetText(final String widgetId);

    /**
     * Select an element from a list.
     *
     * @param widgetId the id of the widget from which to select
     * @param field the field (member) of the object to use for selection
     * @param value the data value to look for in the field to find the object to select
     */
    void selectByFieldValue(final String widgetId, final String field, final String value);

    void selectByIndices(final String widgetId, final Integer[] indices);

    void selectComboItem(final String widgetId, final String label);

    void selectMultiChoiceList(final String widgetId, final String[] values);

    void selectMultipleDataGridRows(final String widgetId, final String property, final String[] values);

    void selectTab(final String widgetId, final String tabTitle) ;

    void setCheckBoxState(final String widgetId, final boolean state);

    void setFocus(final String widgetId);

}
