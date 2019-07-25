package com.finartz.security.app.repository;

import com.finartz.security.app.domain.AdminUser;
import com.finartz.security.app.domain.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonRepository implements BaseLdapNameAware {

    @Autowired
    private LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public void create(AdminUser p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public void create(Guest p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public List<AdminUser> findAll() {
        EqualsFilter filter = new EqualsFilter("objectclass", "person");
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), new PersonContextMapper());
    }

    public AdminUser findOne(String uid) {
        Name dn = LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", uid)
                .build();
        return ldapTemplate.lookup(dn, new PersonContextMapper());
    }

    public List<AdminUser> findByName(String name) {
        LdapQuery q = query()
                .where("objectclass").is("person")
                .and("cn").whitespaceWildcardsLike(name);
        return ldapTemplate.search(q, new PersonContextMapper());
    }

    public void update(AdminUser p) {
        ldapTemplate.rebind(buildDn(p), null, buildAttributes(p));
    }

    public void updateLastName(AdminUser p) {
        Attribute attr = new BasicAttribute("sn", p.getLastName());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(p), new ModificationItem[]{item});
    }

    public void delete(AdminUser p) {
        ldapTemplate.unbind(buildDn(p));
    }

    public void delete(Guest p) {
        ldapTemplate.unbind(buildDn(p));
    }

    private Name buildDn(AdminUser p) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", p.getId())
                .build();
    }

    private Name buildDn(Guest p) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", p.getId())
                .build();
    }

    private Attributes buildAttributes(AdminUser p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add("person");
        ocAttr.add("organizationalPerson");
        ocAttr.add("inetOrgPerson");
        attrs.put(ocAttr);
        attrs.put("ou", "people");
        attrs.put("uid", p.getId());
        attrs.put("cn", p.getFirstName());
        attrs.put("sn", p.getLastName());
        attrs.put("userPassword", p.getUserPassword());
        return attrs;
    }

    private Attributes buildAttributes(Guest p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("inetOrgPerson");
        attrs.put(ocAttr);
        attrs.put("ou", "people");
        attrs.put("uid", p.getId());
        attrs.put("cn", p.getFirstName());
        attrs.put("sn", p.getLastName());
        attrs.put("userPassword", p.getPassword());
        return attrs;
    }


    private static class PersonContextMapper extends AbstractContextMapper<AdminUser> {
        public AdminUser doMapFromContext(DirContextOperations context) {
            AdminUser person = new AdminUser();
            person.setFirstName(context.getStringAttribute("cn"));
            person.setLastName(context.getStringAttribute("sn"));
            return person;
        }
    }
}