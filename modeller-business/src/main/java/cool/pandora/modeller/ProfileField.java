/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * ProfileField
 *
 * @author gov.loc
 */
public class ProfileField implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fieldName = "";
    private String fieldValue = "";
    private String fieldType = "";
    private boolean isReadOnly = false;
    private List<String> elements = new ArrayList<>();
    private boolean isRequired;
    private boolean isValueRequired;

    private final static String FIELD_REQUIRED_VALUE = "requiredValue";
    private final static String FIELD_REQUIRED = "fieldRequired";
    private final static String FIELD_TYPE = "fieldType";
    private final static String FIELD_READ_ONLY = "isReadOnly";
    private final static String FIELD_DEFAULT_VALUE = "defaultValue";
    private final static String FIELD_VALUE_LIST = "valueList";

    void setFieldName(final String s) {
        this.fieldName = s;
    }

    /**
     * @return fieldName
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * @param b boolean
     */
    private void setIsRequired(final boolean b) {
        this.isRequired = b;
    }

    /**
     * @return isRequired
     */
    public boolean getIsRequired() {
        return this.isRequired;
    }

    /**
     * @param s String
     */
    public void setFieldValue(final String s) {
        this.fieldValue = s;
    }

    /**
     * @return fieldValue
     */
    public String getFieldValue() {
        return this.fieldValue;
    }

    /**
     * @param s List
     */
    private void setElements(final List<String> s) {
        this.elements = s;
    }

    /**
     * @return elements
     */
    public List<String> getElements() {
        return this.elements;
    }

    /**
     * @param s String
     */
    private void setFieldType(final String s) {
        this.fieldType = s;
    }

    /**
     * @return fieldType
     */
    public String getFieldType() {
        return this.fieldType;
    }

    /**
     * @param b boolean
     */
    public void setIsValueRequired(final boolean b) {
        this.isValueRequired = b;
    }

    /**
     * @return isValueRequired
     */
    public boolean getIsValueRequired() {
        return this.isValueRequired;
    }

    @Override
    public String toString() {
        return (this.fieldName + '=' + this.fieldValue) + '\n';
    }

    /**
     * @param isReadOnly boolean
     */
    private void setReadOnly(final boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * @return isReadOnly
     */
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * @param profileFieldJson JSONObject
     * @param name String
     * @return profileField
     * @throws JSONException exception
     */
    static ProfileField createProfileField(final JSONObject profileFieldJson, final String name)
            throws JSONException {
        final ProfileField profileField = new ProfileField();
        profileField.setFieldName(name);
        if (profileFieldJson != null) {
            if (profileFieldJson.has(FIELD_TYPE)) {
                final String fieldType = (String) profileFieldJson.get(FIELD_TYPE);
                profileField.setFieldType(fieldType);
            }

            if (profileFieldJson.has(FIELD_REQUIRED_VALUE)) {
                final String fieldValue = (String) profileFieldJson.get(FIELD_REQUIRED_VALUE);
                profileField.setFieldValue(fieldValue);
            }

            if (profileFieldJson.has(FIELD_READ_ONLY)) {
                final boolean isreadOnly = (Boolean) profileFieldJson.get(FIELD_READ_ONLY);
                profileField.setReadOnly(isreadOnly);
            }

            if (profileFieldJson.has(FIELD_VALUE_LIST)) {
                final JSONArray jsonArray = (JSONArray) profileFieldJson.get(FIELD_VALUE_LIST);
                final List<String> valueList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    final String value = (String) jsonArray.get(i);
                    valueList.add(value);
                }
                profileField.setElements(valueList);
                profileField.setFieldValue(valueList.get(0));
            }
            // Default value selected from value list
            if (profileFieldJson.has(FIELD_DEFAULT_VALUE)) {
                final String defaultValue = (String) profileFieldJson.get(FIELD_DEFAULT_VALUE);
                profileField.setFieldValue(defaultValue);
            }

            if (profileFieldJson.has(FIELD_REQUIRED)) {
                final boolean isRequired = (Boolean) profileFieldJson.get(FIELD_REQUIRED);
                profileField.setIsRequired(isRequired);
            }
        }
        return profileField;
    }

    /**
     * @param name String
     * @param value String
     * @return profileField
     */
    public static ProfileField createProfileField(final String name, final String value) {
        final ProfileField profileField = new ProfileField();
        profileField.setFieldName(name);
        profileField.setFieldValue(value);
        return profileField;
    }

    /**
     * @return String
     * @throws JSONException RuntimeException
     */
    String serialize() throws JSONException {
        final StringWriter writer = new StringWriter();
        final JSONWriter profileWriter = new JSONWriter(writer);
        profileWriter.object().key(FIELD_REQUIRED_VALUE).value(this.getFieldValue());
        profileWriter.key(FIELD_REQUIRED).value(getIsRequired());
        profileWriter.key(FIELD_TYPE).value(getFieldType());
        profileWriter.key(FIELD_READ_ONLY).value(isReadOnly());
        if (this.getElements().size() > 0) {
            profileWriter.key(FIELD_VALUE_LIST).value(this.getElements());
        }
        profileWriter.endObject();
        return writer.toString();
    }
}
