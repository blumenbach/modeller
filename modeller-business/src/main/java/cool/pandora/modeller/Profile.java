package cool.pandora.modeller;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Profile
 *
 * @author gov.loc
 */
public class Profile {
    public static final String NO_PROFILE_NAME = "<no profile>";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_ORGANIZATION = "Organization";
    private static final String FIELD_SENDTO = "Send-To";
    private static final String FIELD_SENDFROM = "Send-From";
    private static final String FIELD_CUSTOM_INFO = "Custom-info";
    private static final String FIELD_STANDARD_INFO = "Standard-info";

    private Contact sendToContact = new Contact(true);
    private Contact sendFromContact = new Contact(false);
    private Organization organization = new Organization();
    private String name;
    private boolean isDefault = false;
    private LinkedHashMap<String, ProfileField> customFields = new LinkedHashMap<>();
    private LinkedHashMap<String, ProfileField> standardFields = new LinkedHashMap<>();

    /**
     * @param sendToContact Contact
     */
    private void setSendToContact(final Contact sendToContact) {
        this.sendToContact = sendToContact;
    }

    /**
     * @return sendToContact
     */
    public Contact getSendToContact() {
        return sendToContact;
    }

    /**
     * @param sendFromContact Contact
     */
    private void setSendFromContact(final Contact sendFromContact) {
        this.sendFromContact = sendFromContact;
    }

    /**
     * @return sendFromContact
     */
    public Contact getSendFromContact() {
        return sendFromContact;
    }

    /**
     * @param organization Organization
     */
    private void setOrganization(final Organization organization) {
        this.organization = organization;
    }

    /**
     * @return organization
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @param profileName String
     */
    public void setName(final String profileName) {
        this.name = profileName;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param fields LinkedHashMap
     */
    private void setCustomFields(final LinkedHashMap<String, ProfileField> fields) {
        this.customFields = fields;
    }

    /**
     * @return customFields
     */
    public LinkedHashMap<String, ProfileField> getCustomFields() {
        return customFields;
    }

    @Override
    public String toString() {
        return "";
    }

    /**
     * @param profileJson JSONObject
     * @param profileName String
     * @return profile
     * @throws JSONException exception
     */
    public static Profile createProfile(final JSONObject profileJson, final String profileName) throws JSONException {
        final Profile profile = new Profile();
        profile.setName(profileName);

        JSONObject organizationJson = null;
        if (profileJson.has(Profile.FIELD_ORGANIZATION)) {
            organizationJson = (JSONObject) profileJson.get(Profile.FIELD_ORGANIZATION);
        }

        final Organization organization = Organization.createOrganization(organizationJson);
        profile.setOrganization(organization);

        JSONObject contactSendToJson = null;
        if (profileJson.has(Profile.FIELD_SENDTO)) {
            contactSendToJson = (JSONObject) profileJson.get(Profile.FIELD_SENDTO);
        }

        final Contact sendToContact = Contact.createContact(contactSendToJson, true);
        profile.setSendToContact(sendToContact);

        JSONObject contactSendFromJson = null;
        if (profileJson.has(Profile.FIELD_SENDFROM)) {
            contactSendFromJson = (JSONObject) profileJson.get(Profile.FIELD_SENDFROM);
        }

        final Contact sendFromContact = Contact.createContact(contactSendFromJson, false);
        profile.setSendFromContact(sendFromContact);

        JSONObject customInfoJson = null;
        if (profileJson.has(Profile.FIELD_CUSTOM_INFO)) {
            customInfoJson = (JSONObject) profileJson.get(Profile.FIELD_CUSTOM_INFO);
        }
        final LinkedHashMap<String, ProfileField> fields = getFields(customInfoJson);
        profile.setCustomFields(fields);

        final LinkedHashMap<String, ProfileField> profileFields = getFields(profileJson);
        profile.setStandardFields(profileFields);

        return profile;
    }

    /**
     * @param fieldsJson JSONObject
     * @return profileFields
     * @throws JSONException exception
     */
    private static LinkedHashMap<String, ProfileField> getFields(final JSONObject fieldsJson) throws JSONException {
        final LinkedHashMap<String, ProfileField> profileFields = new LinkedHashMap<>();
        if (fieldsJson != null) {
            final String[] names = JSONObject.getNames(fieldsJson);
            if (names == null) {
                return profileFields;
            }

            if (fieldsJson.has("ordered")) {
                final JSONArray orderedFields = (JSONArray) fieldsJson.get("ordered");
                for (final Object obj : orderedFields) {
                    final JSONObject field = (JSONObject) obj;
                    profileFields.putAll(getFields(field));
                }
            } else {
                for (final String name : names) {
                    final JSONObject jsonObject = (JSONObject) fieldsJson.get(name);
                    final ProfileField profileField = ProfileField.createProfileField(jsonObject, name);
                    profileFields.put(profileField.getFieldName(), profileField);
                }
            }
        }
        return profileFields;
    }

    /**
     * @param standardFields LinkedHashMap
     */
    private void setStandardFields(final LinkedHashMap<String, ProfileField> standardFields) {
        this.standardFields = standardFields;
    }

    /**
     * @return standardFields
     */
    public LinkedHashMap<String, ProfileField> getStandardFields() {
        return standardFields;
    }

    /**
     *
     */
    public void setIsDefault() {
        this.isDefault = true;
    }

    /**
     * @return isDefault
     */
    public boolean getIsDefault() {
        return isDefault;
    }

    /**
     * @param jsonWriter JSONWriter
     * @throws JSONException exception
     */
    public void serialize(final JSONWriter jsonWriter) throws JSONException {

        final JSONWriter writer = jsonWriter.object().key(Profile.FIELD_NAME).value(getName());
        final String orgStringer = getOrganization().serialize();
        final String fromContact = getSendFromContact().serialize();
        final String toContact = getSendToContact().serialize();
        final String localCustomFields = seralizeFields(this.getCustomFields().values());
        final String localStandardFields = seralizeFields(this.getStandardFields().values());
        writer.key(FIELD_ORGANIZATION).value(new JSONObject(new JSONTokener(orgStringer)));
        writer.key(FIELD_SENDFROM).value(new JSONObject(new JSONTokener(fromContact)));
        writer.key(FIELD_SENDTO).value(new JSONObject(new JSONTokener(toContact)));
        writer.key(FIELD_CUSTOM_INFO).value(new JSONObject(new JSONTokener(localCustomFields)));
        writer.key(FIELD_STANDARD_INFO).value(new JSONObject(new JSONTokener(localStandardFields)));
        writer.endObject();
    }

    /**
     * @param profileFields Collection
     * @return String
     * @throws JSONException exception
     */
    private static String seralizeFields(final Collection<ProfileField> profileFields) throws JSONException {
        final StringWriter writer = new StringWriter();
        final JSONWriter filedWriter = new JSONWriter(writer);
        filedWriter.object();
        for (final ProfileField field : profileFields) {
            final String fieldStringer = field.serialize();
            filedWriter.key(field.getFieldName()).value(new JSONObject(new JSONTokener(fieldStringer)));
        }
        filedWriter.endObject();
        return writer.toString();
    }

    /**
     * @return boolean
     */
    public boolean isNoProfile() {
        return NO_PROFILE_NAME.equals(getName());
    }

    /**
     * @return fields
     */
    public List<ProfileField> getProfileFields() {
        final ArrayList<ProfileField> fields = new ArrayList<>();

        for (final Map.Entry<String, ProfileField> entry : standardFields.entrySet()) {
            fields.add(entry.getValue());
        }

        for (final Map.Entry<String, ProfileField> entry : customFields.entrySet()) {
            fields.add(entry.getValue());
        }

        return fields;
    }
}