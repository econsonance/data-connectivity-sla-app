package org.trustrelay.data.connectivity.sla.app.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.trustrelay.data.connectivity.sla.app.enums.OutputType;

@Getter
@Setter
@RequiredArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "driver", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Postgres.class, name = "postgres"),
        @JsonSubTypes.Type(value = Mysql.class, name = "mysql"),
        @JsonSubTypes.Type(value = SqlServer.class, name = "sqlserver"),
})
public abstract class Driver {
    protected String driver;
    protected String conn;
    protected String url;
    protected String type;

    public abstract boolean testConnection();

    public abstract void extractCredentials();

    public void setDefault() {
        if (this.type == null) {
            this.type = OutputType.STDOUT.getValue();
        }
    }
}
