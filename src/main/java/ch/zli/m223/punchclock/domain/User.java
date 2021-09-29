package ch.zli.m223.punchclock.domain;

import javax.persistence.*;

import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;

import io.quarkus.elytron.security.common.BcryptUtil;

@Entity
public class User {

    // NOTE(cvl): The classdiagram falsely depicts this as type User instead of Long.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountName;

    @Column(nullable = false)
    private String passwdHash;

    @Column(nullable = false)
    private String role;

    public User() { }

    public User(Long id, String accountName, String passwd, String role) {
        this.id = id;
        this.accountName = accountName;
        this.passwdHash = getPasswdHash(passwd);
        this.role = role;
    }

    public User(String accountName, String passwd, String role) {
        this.accountName = accountName;
        this.passwdHash = getPasswdHash(passwd);
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setPasswdHash(String passwd) {
        this.passwdHash = getPasswdHash(passwd);
    }

    public String getPasswordHash() {
        return passwdHash;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public boolean checkPassword(String passwd) throws Exception {
        // convert encrypted password string to a password key
        Password rawPassword = ModularCrypt.decode(passwdHash);

        // create the password factory based on the bcrypt algorithm
        PasswordFactory factory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT);

        // create encrypted password based on stored string
        BCryptPassword restored = (BCryptPassword) factory.translate(rawPassword);

        // verify restored password against original
        return factory.verify(restored, passwd.toCharArray());
    }

    private String getPasswdHash(String passwd) {
        return BcryptUtil.bcryptHash(passwd);
    }
}
