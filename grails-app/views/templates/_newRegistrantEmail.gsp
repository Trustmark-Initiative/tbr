<%@ page contentType="text/html"%>


<div>
    <h3>Your account has been created successfully!</h3>
    <div>
        An account has been provisioned for you within the Trustmark Binding Registry (TBR) hosted by the ${organizationName}.  To finish setting up your account, you must choose an initial password by following this link:  <a href="${resetUrl}">Create Password</a>
        <br/>
        In the future you can login to the TBR by going to the tools main page: <a href="${tbrUrl}">Trustmark Binding Registry Tool</a>
        <br/>
    </div>
    <br/>
    <div style="margin-top: 1em;">
        If you have any questions about this account, feel free to respond to this email or e-mail the administrator of the TBR: <a href="mailto:${adminEmail}">TBR Administrator Email</a>.
    </div>
    <br />
    <div>
        Please <a href="${tbrUrl}">Click Here</a> to return to the Trustmark Binding Registry Tool.
    </div>
    <div style="margin-top: 2em;font-size: 80%; font-family: courier, monospace;">
        This email was auto-generated by the GTRI Trustmark Binding Registry Tool.  For more information, please visit:
        <a href="https://trustmark.gtri.gatech.edu">https://trustmark.gtri.gatech.edu</a>.
    </div>
</div>