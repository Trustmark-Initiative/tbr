package tm.binding.registry

import java.time.Instant

class Provider {

    int          id
    String       name
    String       entityId
    String       signingCertificate
    String       encryptionCertificate
    String       systemCertificate
    String       systemCertificateFilename
    String       systemCertificateUrl
    String       saml2MetadataXml
    String       saml2MetadataUrl
    Instant      validUntilDate
    ProviderType providerType
    Date         lastTimeSAMLMetadataGeneratedDate

    static belongsTo = [
        organization: Organization
    ]

    static constraints = {
        name nullable: false
        providerType nullable: false
        entityId nullable: false
        endpoints nullable: true
        signingCertificate nullable: true
        encryptionCertificate nullable: true
        systemCertificate nullable: true
        systemCertificateFilename nullable: true
        systemCertificateUrl nullable: true
        saml2MetadataXml nullable: true
        saml2MetadataUrl nullable: true
        validUntilDate nullable: true
        attributes nullable: true
        idpAttributes nullable: true
        protocols nullable: true
        contacts nullable: true
        nameFormats nullable: true
        trustmarks nullable: true
        tags nullable: true
        conformanceTargetTips nullable: true
        lastTimeSAMLMetadataGeneratedDate nullable: true
    }

    static hasMany = [
        endpoints: Endpoint
        , attributes: Attribute
        , idpAttributes: String
        , protocols: String
        , contacts: Contact
        , nameFormats: String
        , trustmarks: Trustmark
        , tags: String
        , conformanceTargetTips: ConformanceTargetTip
        , partnerSystemsTips: PartnerSystemsTip
        , trustmarkRecipientIdentifiers: TrustmarkRecipientIdentifier
    ]

    static mapping = {
        table name: 'provider'
        entityId column: 'entity_id'
        providerType column: 'provider_type'
        signingCertificate column: 'sign_cert', type: 'text'
        encryptionCertificate column: 'encrypt_cert', type: 'text'
        systemCertificate column: 'system_cert', type: 'text'
        systemCertificateFilename column: 'system_cert_filename', type: 'text'
        systemCertificateUrl column: 'system_cert_url', type: 'text'
        saml2MetadataXml column: 'saml2_metadata_xml', type: 'text'
        saml2MetadataUrl column: 'saml2_metadata_url', type: 'text'
        endpoints cascade: "all-delete-orphan"
        attributes cascade: "all-delete-orphan"
        idpAttributes cascade: "all-delete-orphan"
        trustmarks cascade: "all-delete-orphan"
        conformanceTargetTips cascade: "all-delete-orphan"
        partnerSystemsTips cascade: "all-delete-orphan"
        trustmarkRecipientIdentifiers cascade: "all-delete-orphan"
    }

    def findAttribute(String name)  {
        String value = null
        attributes.forEach({ a ->
            if(a.name == name)
                value = a.value
        })
        return value
    }

    Map toJsonMap(boolean shallow = true) {
        def json = [
                id : this.id,
                name : this.name,
                organization: this.organization.toJsonMap(),
                providerType : this.providerType.toString(),
                trustmarks : this.trustmarks,
                conformanceTargetTips : this.conformanceTargetTips
        ]

        if (this.providerType == ProviderType.SAML_IDP || this.providerType == ProviderType.SAML_SP) {
            json.put("entityId", this.entityId)
            json.put("signingCertificate", this.signingCertificate)
            json.put("encryptionCertificate", this.encryptionCertificate)
            json.put("endpoints", this.endpoints)
            json.put("protocols", this.protocols)
            json.put("nameFormats", this.nameFormats)
            json.put("attributes", this.attributes)
            json.put("lastTimeSAMLMetadataGeneratedDate", this.lastTimeSAMLMetadataGeneratedDate)
            json.put("saml2MetadataUrl", this.saml2MetadataUrl)
        } else if (this.providerType == ProviderType.CERTIFICATE) {
            json.put("systemCertificate", this.systemCertificate)
            json.put("systemCertificateUrl", this.systemCertificateUrl)
        }

        if (this.trustmarkRecipientIdentifiers && this.trustmarkRecipientIdentifiers.size() > 0) {
            def jsonTrustmarkRecipientIdentifiers = []
            this.trustmarkRecipientIdentifiers.each { trustmarkRecipientIdentifier ->
                jsonTrustmarkRecipientIdentifiers.add(trustmarkRecipientIdentifier.trustmarkRecipientIdentifierUrl)
            }
            json.put("trustmarkRecipientIdentifiers", jsonTrustmarkRecipientIdentifiers)
        }

        if (this.partnerSystemsTips && this.partnerSystemsTips.size() > 0) {
            def jsonPartnerSystemsTips = []
            this.partnerSystemsTips.each { partnerSystemsTip ->
                jsonPartnerSystemsTips.add(partnerSystemsTip.partnerSystemsTipIdentifier)
            }
            json.put("partnerSystemTips", jsonPartnerSystemsTips)
        }

        return json
    }
}
