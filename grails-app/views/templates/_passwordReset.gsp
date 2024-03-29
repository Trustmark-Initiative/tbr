<%@ page contentType="text/html"%>


<div>
    <h3>Your Password has been Reset Successfully!</h3>
    <div>
        The password for user account with email[${email}] has been successfully reset.
        <br/>
        You may change your password here: <a href="${resetUrl}">Change Password</a>
        <br/>
    </div>
    <br/>
    <div style="margin-top: 1em;">
        If you did not generate this password reset request, please notify support.
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