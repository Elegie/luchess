package io.elegie.luchess.web.client.models;

/**
 * A DTO for the
 * {@link io.elegie.luchess.web.client.controllers.BuildController}.
 */
@SuppressWarnings("javadoc")
public class BuildInfo {

    private String indexingPassword;

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {indexingPassword: %s}";
        return String.format(value, className, indexingPassword);
    }

    public String getIndexingPassword() {
        return indexingPassword;
    }

    public void setIndexingPassword(String indexingPassword) {
        this.indexingPassword = indexingPassword;
    }

}
