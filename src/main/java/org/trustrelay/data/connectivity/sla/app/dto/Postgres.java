package org.trustrelay.data.connectivity.sla.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trustrelay.data.connectivity.sla.app.service.TrustRelayService;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Postgres extends Driver {

    private String username;
    private String password;

    @Override
    public boolean testConnection() {
        return TrustRelayService.testConnection(this.conn, this.username, this.password);
    }

    @Override
    public void extractCredentials() {
        this.username = TrustRelayService.parseUrl(getConn(), "(?<=user=)(.*)(?=&)");
        this.password = TrustRelayService.parseUrl(getConn(), "(?<=password=)(.*)");
        this.conn = TrustRelayService.parseUrl(getConn(), "(.*)(?=\\?user=)");
    }

}
