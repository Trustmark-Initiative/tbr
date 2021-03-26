package tm.binding.registry

import grails.converters.JSON
import org.apache.commons.lang.StringUtils

import javax.servlet.ServletException

class PublicController {

    ProviderService providerService

    def index() { }

    def listByType()  {
        log.info("listByType -> ${params.type}")

        def providers = providerService.listByType(params.type)
        render providers as JSON
    }

    def download() {

        if( StringUtils.isBlank(params.id) ){
            log.warn "Missing required parameter id"
            throw new ServletException("Missing required parameter: 'id")
        }

        X509CertificateService x509CertificateService = new X509CertificateService()

        // the filename is passed in the id parameter
        String filename = params.id
        SigningCertificate certificate = SigningCertificate.findByFilename(filename)

        String organizationName = certificate.organizationName.replaceAll("[^A-Za-z0-9]", "")
        String organizationalUnitName = certificate.organizationalUnitName.replaceAll("[^A-Za-z0-9]", "")

        response.setHeader("Content-length", certificate.x509CertificatePem.length().toString())

        String mimeType = "text/html"

        response.setContentType( "application-xdownload")
        response.setHeader("Content-Disposition", "attachment;filename=${filename}")
        response.getOutputStream() << new ByteArrayInputStream(certificate.x509CertificatePem.getBytes())
    }

}
