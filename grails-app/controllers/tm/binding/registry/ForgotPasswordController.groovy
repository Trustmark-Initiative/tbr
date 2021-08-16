package tm.binding.registry

import edu.gatech.gtri.trustmark.v1_0.impl.util.TrustmarkMailClientImpl
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import tm.binding.registry.util.TBRProperties
import tm.binding.registry.util.PasswordUtil
import org.apache.commons.lang.StringUtils

@Secured("permitAll")
class ForgotPasswordController {

    def index() {
        log.info("Showing forgot password page [IP: ${request.remoteAddr}]...");
    }

    def resetPassword() {
        log.info("Request to reset password for user[${params.email}] from IP[${request.remoteAddr}]")

        def result = [:]

        if( StringUtils.isEmpty(params.email) )  {
            log.warn("Uh-oh - params.email is empty.")
            result.status = "FAILURE"
            result.message = "Missing required parameter 'email' "
        } else {
            // First try to find user with the email as the username
            User user = User.find("from User where lower(username) = :email", [email: params.email?.toLowerCase()])

            // find user by user's contact email
            if (!user) {
                Contact contact = Contact.find("from Contact where lower(email) = :email", [email: params.email?.toLowerCase()])
                user = User.findByContact(contact)
            }

            if( user ){
                String newPassword = PasswordUtil.generateRandom();
                log.info("Resetting password for user[${user.username}] to [$newPassword]...")
                user.password = newPassword;
                User.withTransaction {
                    user.save(failOnError: true);
                }
                log.debug("Sending confirmation email to ${params.email}...")

                TrustmarkMailClientImpl emailClient = new TrustmarkMailClientImpl()

                emailClient
                        .setUser(TBRProperties.getProperties().getProperty(TrustmarkMailClientImpl.SMTP_USER))
                        .setPswd(TBRProperties.getProperties().getProperty(TrustmarkMailClientImpl.SMTP_PSWD))
                        .setSmtpHost(TBRProperties.getProperties().getProperty(TrustmarkMailClientImpl.SMTP_HOST))
                        .setSmtpPort(TBRProperties.getProperties().getProperty(TrustmarkMailClientImpl.SMTP_PORT))
                        .setFromAddress(TBRProperties.getProperties().getProperty(TrustmarkMailClientImpl.FROM_ADDRESS))
                        .setSmtpAuthorization(Boolean.parseBoolean(TBRProperties.getProperties().getProperty(TrustmarkMailClientImpl.SMTP_AUTH)))
                        .setSubject(g.message(code: 'reset.password.subject').toString())
                        .addRecipient(params.email)
                        .setContent(g.render(template: "/templates/passwordReset", model: [email: params.email, username: user.username, password: newPassword]).toString(), "text/html")
                        .sendMail()

                result.status = "SUCCESS"
                result.message = "Your password has been successfully reset, please check your email for the new password."
            }else{
                log.warn("No such user ${params.email}")
                result.status = "FAILURE"
                result.message = "The system does not contain any user with email '${params.email}'"
            }
        }

        withFormat {
            html { throw new UnsupportedOperationException("NOT SUPPORTED"); }
            xml { throw new UnsupportedOperationException("NOT SUPPORTED"); }
            json {
                render result as JSON
            }
        }

    }//end resetPassword()


}
