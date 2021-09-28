package ch.zli.m223.punchclock.domain;

import javax.persistence.*;

//import io.quarkus.security.common.BcryptUtil;

@Entity
public class User {

    // NOTE(cvl): The classdiagram falsely depicts this as type User instead of Long.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String passwdHash;

    @Column(nullable = false)
    private String role;

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
        //this.passwdHash = BcryptUtil.bcryptHash(passwd);
        this.passwdHash = passwd; // TODO: Hash
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

    public boolean checkPassword(String passwd) {
        //return passwdHash.equals(BcryptUtil.bcryptHash(passwd));
        return passwdHash.equals(passwd); // TODO: Hash
    }
}
