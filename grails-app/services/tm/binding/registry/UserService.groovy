package tm.binding.registry

import grails.gorm.transactions.Transactional

@Transactional
class UserService {

    final String ROLE_USER = 'ROLE_USER'

    def serviceMethod() {

    }

    /**
     * add a user
     * @param args
     * name
     * pswd
     * email
     * contact
     * @return
     */
    def add(String name, String pswd, String email, Contact contact)  {

        User user = new User(
                username: email,
                password: pswd,
                name: name,
                enabled: false,
                accountExpired: false,
                accountLocked: true,
                passwordExpired: false,
                contact: contact
        )
        user.save(true)

        Role role = Role.findByAuthority(ROLE_USER)
        UserRole.create(user, role, true)

        return user
    }

    def unlock(User user)  {
        log.debug("unlock -> ${user.username}")
        user.enabled = true
        user.accountLocked = false
        user.accountExpired = false
        user.save(true)
    }

    def lock(User user)  {
        log.debug("lock -> ${user.username}")
        user.enabled = false
        user.accountLocked = true
        user.accountExpired = false
        user.save(true)
    }
}
