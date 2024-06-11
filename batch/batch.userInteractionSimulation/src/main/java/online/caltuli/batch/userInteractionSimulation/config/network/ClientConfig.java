package online.caltuli.batch.userInteractionSimulation.config.network;

public enum ClientConfig {
    TRUST(true),
    CHECK(false);

    private boolean trustAllCertificate;

    ClientConfig(boolean trustAllCertificate) {
        this.trustAllCertificate = trustAllCertificate;
    }

    public boolean isTrustAllCertificate() {
        return trustAllCertificate;
    }
}

