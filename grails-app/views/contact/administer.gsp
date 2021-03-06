<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Contacts</title>
    <script type="text/javascript">
        var ORG_VIEW_BASE_URL = "${createLink(controller:'organization', action: 'view')}/";

        $(document).ready(function(){
            listContacts([]);
        });

        let selectOrganizations = function(id)  {
            list("${createLink(controller:'organization', action: 'list')}"
                , curriedSelectOrganizations('select-organization')(id)
                , {name: 'ALL'});
        }

        let selectContactTypes = function(id)  {
            list("${createLink(controller:'contact', action: 'types')}"
                , curriedContactTypes('select-contact-types')(id)
                , {name: 'ALL'});
        }

        let listContacts = function(data)  {
            list("${createLink(controller:'contact', action: 'list')}"
                , renderResults
                , {id: 0});
        }

        let populateContactForm = function(contact) {
            if(contact.id === 0)  {
                selectOrganizations(0);
                selectContactTypes('0');
            } else {
                selectContactTypes(contact.type.name)
                selectOrganizations(contact.organization.id);
                document.getElementById('lastName').value = contact.lastName;
                document.getElementById('firstName').value = contact.firstName;
                document.getElementById('emailAddr').value = contact.email;
                document.getElementById('phoneNbr').value = contact.phone;
            }
            document.getElementById('lastName').focus();
        }

        let getDetails = function(id)  {
            get("${createLink(controller:'contact', action: 'get')}"
                , contactDetail('contact-details')(populateContactForm)
                (function(){updateContact(id, document.getElementById('lastName').value
                    , document.getElementById('firstName').value
                    , document.getElementById('emailAddr').value
                    , document.getElementById('phoneNbr').value
                    , document.getElementById('ctypes').options[document.getElementById('ctypes').selectedIndex].value
                    , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);})
                , { id: id }
            );
        }

        let renderResults = function(results)  {
            renderContactOffset = curriedContact('contacts-table')
            ({
                editable: true
                , fnAdd: function(){renderContactForm('contact-details', populateContactForm
                        , function(){updateContact(0, document.getElementById('lastName').value
                            , document.getElementById('firstName').value
                            , document.getElementById('emailAddr').value
                            , document.getElementById('phoneNbr').value
                            , document.getElementById('ctypes').options[document.getElementById('ctypes').selectedIndex].value
                            , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);}
                        , {id:0});}
                , fnRemove: removeContact
                , fnDraw: drawContacts
                , title: 'Points of Contact'
                , hRef: 'javascript:getDetails'
                , includeOrganizationColumn: true
            })
            (results);
            renderContactOffset(0);
        }

        let removeContact = function()  {
            if (confirm("The selected contact(s) may be in use by current systems. Do you wish to proceed?")) {
                getCheckedIds('edit-contacts', function (list) {
                    update("${createLink(controller:'contact', action: 'delete')}"
                        , listContacts
                        , {ids: list}
                    );
                });
            }
        }

        let updateContact = function(id, lname, fname, email, phone, type, orgId)  {
            if(checkContact(lname, fname, email, phone, type, orgId))  {
                if(id === 0)  {
                    add("${createLink(controller:'contact', action: 'add')}"
                        , listContacts
                        , { lname: lname
                            , fname: fname
                            , email: email
                            , phone: phone
                            , organizationId: orgId
                            , type: type
                        });
                }  else {
                    update("${createLink(controller:'contact', action: 'update')}"
                        , listContacts
                        , {
                            id: id
                            , lname: lname
                            , fname: fname
                            , email: email
                            , phone: phone
                            , organizationId: orgId
                            , type: type
                        });
                }
                clearForm();
            } else {
                scroll(0,0);
            }
        }

        let clearForm = function()  {
            setSuccessStatus("<b>Successfully saved contact.</b>");
            hideIt('contact-details');
            scroll(0,0);
        }
    </script>
</head>

<body>
<div id="status-header"></div>
<div id="contacts-table"></div>
<div id="contact-details"></div>
</body>
</html>