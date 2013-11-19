package org.jspringbot.keyword.selenium.flex;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class FlexSeleniumHelper implements FlexSelenium {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(FlexSeleniumHelper.class);

    protected String flexAppID;

    protected JavascriptExecutor executor;

    public FlexSeleniumHelper(WebDriver driver, String flexAppID) {
        this.executor = (JavascriptExecutor) driver;

        this.flexAppID = flexAppID;
    }

    @Override
    public String getForSelenium(String widgetId, String propertyType) {
        return callVerifyProperty("getForSelenium", widgetId, propertyType);
    }

    @Override
    public String click(String objectId, String optionalButtonLabel) {
        return call("doFlexClick", objectId, optionalButtonLabel);
    }

    @Override
    public String click(String objectId) {
        return click(objectId, "");
    }

    @Override
    public boolean isVisible(String objectId) {
        final String result = call("getFlexVisible", objectId, "");

        if (!"true".equals(result) && !"false".equals(result)) {
            System.out.println(String.format("Visibility of '%s' returned an unexpected value: %s", objectId, result));
        }

        return Boolean.parseBoolean(result);
    }

    @Override
    public int getNumSelectedItems(String objectId) {
        return Integer.parseInt(call("getFlexNumSelectedItems", objectId, ""));
    }

    @Override
    public String getSelectedItemAtIndex(String objectId, int index) {
        return call("getFlexSelectedItemAtIndex", objectId, Integer.toString(index));
    }

    @Override
    public void enterText(String textField, String text) {
        call("doFlexType", textField, text);
    }

    @Override
    public void enterDate(String dateField, String dateAsText) {
        call("doFlexDate", dateField, dateAsText);
    }

    @Override
    public void getDate(String dateField) {
        call("getFlexDate", dateField, "");
    }

    @Override
    public boolean isEnabled(String objectId) {
        final String state = call("getFlexEnabled", objectId, "");

        if (!"true".equals(state) && !"false".equals(state)) {
            System.out.println(String.format("Enabled state of '%s' returned an unexpected value: %s", objectId, state));
        }
        return Boolean.parseBoolean(state);
    }

    @Override
    public boolean isTabEnabled(String objectId, String label) {
        return Boolean.parseBoolean(call("getFlexEnabled", objectId, label));
    }

    @Override
    public boolean exists(String objectId) {
        return Boolean.parseBoolean(call("getFlexExists", objectId, ""));
    }

    @Override
    public String getErrorString(String textFieldId) {
        return call("getFlexErrorString", textFieldId, "");
    }

    @Override
    public String getText(String textFieldId) {
        return call("getFlexText", textFieldId, "");
    }

    @Override
    public int getSelectionIndex(String selectionFieldId) {
        return Integer.parseInt(call("getFlexSelectionIndex", selectionFieldId, ""));
    }

    @Override
    public String select(String selectionFieldId, String itemToSelect) {
        return call("doFlexSelect", selectionFieldId, itemToSelect);
    }

    @Override
    public String selectComboByLabel(String selectionFieldId, String itemToSelect){
        return call("doFlexSelectComboByLabel", selectionFieldId, itemToSelect);
    }

    @Override
    public String selectMatchingOnField(String selectionFieldId, String underlyingField, String underlyingValue) {
        return call("doFlexSelectMatchingOnField", new String[] {selectionFieldId, underlyingField}, new String[]{underlyingValue});
    }

    @Override
    public void addSelectMatchingOnField(String selectionFieldId, String underlyingField, String underlyingValue) {
        String data = call("doFlexAddSelectMatchingOnField", selectionFieldId, underlyingField, underlyingValue);
        System.out.println("Result: " + data);
    }

    @Override
    public void selectIndex(String selectionFieldId, int index) {
        call("doFlexSelectIndex", selectionFieldId, Integer.toString(index));
    }

    @Override
    public String getProperty(String objectId, String property) {
        return call("getFlexProperty", objectId, property);
    }

    @Override
    public String getDataGridValue(String dataGridId, int row, int col) {
        return call("getFlexDataGridCell", dataGridId, Integer.toString(row), Integer.toString(col));
    }

    @Override
    public int getFlexDataGridRowIndexForFieldValue(String dataGridId, String field, String value) {
        return Integer.parseInt(call("getFlexDataGridRowIndexForFieldValue", dataGridId, field, value));
    }

    @Override
    public int getFlexDataGridRowIndexForFieldLabel(String dataGridId, String field, String label) {
        return Integer.parseInt(call("getFlexDataGridRowIndexForFieldLabel", dataGridId, field, label));
    }

    @Override
    public String getFlexDataFieldValueForGridRow(
            String dataGridId, String field, int row) {
        return callArray("getFlexDataGridFieldValueForGridRow", new String[]{dataGridId, field, Integer.toString(row)});
    }

    @Override
    public String getFlexDataFieldLabelForGridRow(
            String dataGridId, String field, int row) {
        return call("getFlexDataGridFieldLabelForGridRow", dataGridId, field, Integer.toString(row));
    }

    @Override
    public int getFlexDataGridRowCount(String dataGridId) {
        return Integer.parseInt(call("getFlexDataGridRowCount", dataGridId));
    }

    @Override
    public boolean isAlertVisible() {
        return Boolean.parseBoolean(call("getFlexAlertPresent", "", ""));
    }

    @Override
    public String clickAlert(String alertButton) {
        return call("doFlexAlertResponse", alertButton, alertButton);
    }

    @Override
    public String selectCheckbox(String checkBoxId, boolean value) {
        return call("doFlexCheckBox", checkBoxId, Boolean.toString(value));
    }

    @Override
    public boolean isCheckboxChecked(String checkBoxId) {
        return Boolean.parseBoolean(call("getFlexCheckBoxChecked", checkBoxId, ""));
    }

    @Override
    public String getComboBoxSelectedItem(String comboBoxId) {
        return call("getFlexSelection", comboBoxId, "");
    }

    @Override
    public boolean isLabelInComboData(final String comboBoxId, final String expectedLabel) {
        return "true".equals(callArray("getFlexComboContainsLabel", new String[] {comboBoxId, expectedLabel}));
    }

    @Override
    public String setFocus(final String widgetId) {
        return call("doFlexSetFocus", widgetId);
    }

    @Override
    public String doFlexAlertResponse(final String response) {
        return call("doFlexAlertResponse", response, response);
    }

    @Override
    public void waitForPageToLoad(long timeout, long pollTime) {
        final long timeoutPoint = System.currentTimeMillis() + timeout;
        while (timeoutPoint > System.currentTimeMillis()) {
            try {
                if (isVisible(flexAppID)) {
                    return;
                }
                Thread.sleep(pollTime);
            } catch (Exception e) {
                // Ignore this, we are just waiting for the app to load
            }
        }
        Assert.fail("Application did not load");
    }

    private String callVerifyProperty(String functionName, String... args) {
        StringBuilder code = new StringBuilder();
        StringBuilder verifyProperty = new StringBuilder();

        verifyProperty.append("<VerifyProperty value=\"" + args[0] + "\" propertyString=\"" + args[1]  + "\"/>");
        LOG.info("VerifyProperty: " + verifyProperty);
        code.insert(0, String.format("return document['%s'].%s('%s', '%s')", flexAppID, functionName, verifyProperty, ""));
        LOG.info("Call: " + code);

        String result = String.valueOf(executor.executeScript(code.toString()));



        LOG.info("Result: " + result);

        return result;
    }

    private String call(String functionName, String... args){
        StringBuilder code = new StringBuilder();

        for(String arg : args) {
            if(code.length() > 0) {
                code.append(", ");
            }

            code.append("\"");
            code.append(StringEscapeUtils.escapeJavaScript(arg));
            code.append("\"");
        }

        code.insert(0, String.format("return document['%s'].%s(", flexAppID, functionName));
        code.append(");");

        LOG.info("Call: " + code);

        String result = String.valueOf(executor.executeScript(code.toString()));

        LOG.info("Result: " + result);

        return result;
    }

    private String call(String functionName, String[]... args){
        StringBuilder code = new StringBuilder();

        for(String[] arg : args) {
            if(code.length() > 0) {
                code.append(", ");
            }

            try {
                code.append(new JSONArray(arg));
            } catch (JSONException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }

        code.insert(0, String.format("return document['%s'].%s(", flexAppID, functionName));
        code.append(");");

        LOG.info("Call: " + code);

        String result = String.valueOf(executor.executeScript(code.toString()));

        LOG.info("Result: " + result);

        return result;
    }

    private String callArray(String functionName, String[] args) {
        String code = null;
        try {
            code = String.format("return document['%s'].%s(%s);", flexAppID, functionName, new JSONArray(args));
        } catch (JSONException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        LOG.info("Call: " + code);

        String result = String.valueOf(executor.executeScript(code));

        LOG.info("Result: " + result);

        return result;
    }
}
